package Simulations

import HelperUtils.{CreateLogger, CustomCloudletsTable, ObtainConfigReference}
import Simulations.BasicCloudSimPlusExample.{config, logger}

import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyBestFit, VmAllocationPolicyFirstFit, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.distributions.{ContinuousDistribution, UniformDistr}
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple
import org.cloudbus.cloudsim.resources.PeSimple
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.{Vm, VmCost, VmSimple}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudsimplus.listeners.VmHostEventInfo

import collection.JavaConverters.*
import scala.collection.mutable.ListBuffer

/* This class is an implementation of Paas datacenters
where the user cannot specify hosts, Datacenters parameters
 but can set schedulers and policies*/
class Simulation2

object Simulation2:
  /*val config = ObtainConfigReference("cloudSimulator") match {
    case Some(value) => value
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }*/

  val logger = CreateLogger(classOf[BasicCloudSimPlusExample])

  val random: ContinuousDistribution = new UniformDistr();
  val config: Config = ConfigFactory.load("provider.conf").getConfig("Cloud")
  val paasconfig: Config = config.getConfig("Paas")

  def start() = {




    val vmAllocation = Map("FirstFit"->new VmAllocationPolicyFirstFit(),"RoundRobin"->new VmAllocationPolicyRoundRobin(),"Simple"->new VmAllocationPolicySimple(),"BestFit"->new VmAllocationPolicyBestFit());

    val cloudletScheduling = Map("SpaceShared"->new CloudletSchedulerSpaceShared(), "TimeShared" -> new CloudletSchedulerTimeShared());


    val cloudsim = new CloudSim();
    val broker0 = new DatacenterBrokerSimple(cloudsim);

    val hostPes = List(new PeSimple(config.getLong("host.mipsCapacity")),new PeSimple(config.getLong("host.mipsCapacity")),new PeSimple(config.getLong("host.mipsCapacity")))
    logger.info(s"Created one processing element: $hostPes")

    /* Method to create hosts */
    def createHosts(): ListBuffer[Host] = {

      val hostList = new ListBuffer[Host]

      (1 to config.getInt("host.Number")) foreach (i =>

        hostList +=
          (new HostSimple(config.getLong("host.RAMInMBs"),
            config.getLong("host.StorageInMBs"),
            config.getLong("host.BandwidthInMBps"),
            hostPes.asJava)
            .setRamProvisioner(new ResourceProvisionerSimple())
            .setBwProvisioner(new ResourceProvisionerSimple())
            .setVmScheduler(new VmSchedulerTimeShared()))
        )
      return hostList

    }

    /* Method to create VMs*/
    def createVM(): ListBuffer[Vm] = {

      val vmList  = new ListBuffer[Vm]
      (1 to config.getInt("vm.Number")) foreach(i =>

        vmList+=
          (new VmSimple(config.getLong("vm.mipsCapacity"), config.getInt("vm.PEs"))
            .setRam(config.getLong("vm.RAMInMBs"))
            .setBw(config.getLong("vm.BandwidthInMBps"))
            .setCloudletScheduler(cloudletScheduling(paasconfig.getString("cloudlet.CloudletScheduling"))))
        )

      return vmList
    }

    /* Method to destroy Vms if they take more than 70% of processing power*/
    def vmDestruction(info: VmHostEventInfo): Unit = {
      val vm = info.getVm
      //Destroys VM 1 when its CPU usage reaches 90%
      if (vm.getCpuPercentUtilization > 0.70 && vm.isCreated) {
        logger.info(s"Destroyed $vm  Cpu utilization ${vm.getCpuPercentUtilization*100} ");
        vm.getHost.destroyVm(vm)
      }
    }



    val hostList = createHosts()

    logger.info(s"Created hosts: $hostList")

    /* Method to create Datacenters*/
    def createDataCenter(): Unit = {
      val allocationlist: List[String] = paasconfig.getStringList("vm.VmAllocationPolicy").asScala.toList

      (1 to paasconfig.getInt("datacenter.Number")) foreach(i =>

        new DatacenterSimple(cloudsim, hostList.asJava, vmAllocation(allocationlist(i-1)))
          .getCharacteristics()
          .setCostPerSecond(paasconfig.getDouble("datacenter.CostPerSecond"))
          .setCostPerMem(paasconfig.getDouble("datacenter.CostPerMem"))
          .setCostPerStorage(paasconfig.getDouble("datacenter.CostPerStorage"))
          .setCostPerBw(paasconfig.getDouble("datacenter.CostPerBw"))
          .setOs(paasconfig.getString("datacenter.os"))
        )
    }

    //val vmAllocation: VmAllocationPolicySimple = new VmAllocationPolicySimple();

    createDataCenter()



    val vmList = createVM()
    logger.info(s"Created one virtual machine: $vmList")

    /*(1 to config.getInt("vm.Number")) foreach(i =>
      vmList(i-1).addOnUpdateProcessingListener(vmDestruction)
      )*/

    val utilizationModel = new UtilizationModelDynamic(config.getDouble("utilizationRatio"));

    /* Method to create cloudlets*/
    def createCloudlets(): ListBuffer[Cloudlet] = {
      val cloudletList = new  ListBuffer[Cloudlet]
      (1 to paasconfig.getInt("cloudlet.Number")) foreach(i =>
        cloudletList+= (new CloudletSimple(paasconfig.getLong("cloudlet.Size"), paasconfig.getInt("cloudlet.PEs"), utilizationModel))
        )
      return cloudletList
    }

    val cloudletList = createCloudlets()

    logger.info(s"Created a list of cloudlets: $cloudletList")


    broker0.submitVmList(vmList.asJava);
    broker0.submitCloudletList(cloudletList.asJava);

    logger.info("Starting cloud simulation...")
    cloudsim.start();


    new CustomCloudletsTable(broker0.getCloudletCreatedList()).build();

    vmCost();
    cloudletCost();

    /* Method to calculate VM costs*/
    def vmCost(): Unit = {
      val processingCost = new ListBuffer[Float];
      val memoryCost = new ListBuffer[Float]
      val storageCost = new ListBuffer[Float]
      val bwCost = new ListBuffer[Float]
      val totalCost = new ListBuffer[Float]
      vmList.foreach(i =>
        val cost: VmCost = new VmCost(i)

          processingCost += (cost.getProcessingCost().toFloat);
        memoryCost += (cost.getMemoryCost().toFloat);
        storageCost +=  (cost.getStorageCost().toFloat);
        bwCost += (cost.getBwCost().toFloat);
        totalCost += (cost.getTotalCost().toFloat);

      )


      logger.info(s" total processing cost for VMs: ${processingCost.sum} total storageCost for VMs: ${storageCost.sum} total memory cost for VMs: ${memoryCost.sum} total bwcost for VMs: ${bwCost.sum} total cost for VMs: ${totalCost.sum}");

    }

    /* Method to calculate cloudlet costs */
    def cloudletCost(): Unit = {
      val bwCost = new ListBuffer[Double];
      val totalCost = new ListBuffer[Double];
      cloudletList.foreach(i =>
        bwCost += (i.getCostPerBw());
        totalCost += (i.getTotalCost());
      )

      logger.info(s" total cost per bw for all the cloudlets is: ${bwCost.sum} total cost for executing all the cloudlets are: ${totalCost.sum}");
    }



  }
