package Simulations

import HelperUtils.{CreateLogger, CustomCloudletsTable, ObtainConfigReference}
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
import org.cloudbus.cloudsim.schedulers.cloudlet
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.{Vm, VmCost, VmSimple}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudsimplus.listeners.VmHostEventInfo

import collection.JavaConverters.*
import scala.collection.mutable.ListBuffer

/* This class is an implementation of Saas
datacenters where the user only has control over the cloudlets
 and has no access over VMs and hosts*/

class Simulation3

object Simulation3:

  val logger = CreateLogger(classOf[BasicCloudSimPlusExample])

  val random: ContinuousDistribution = new UniformDistr();

  val config: Config = ConfigFactory.load("provider.conf").getConfig("Cloud")

  val saasconfig:Config = config.getConfig("Saas")

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



    /* Method to create VMs */
    def createVM(): ListBuffer[Vm] = {

      val vmList  = new ListBuffer[Vm]
      (1 to config.getInt("vm.Number")) foreach(i =>



        vmList+=
          (new VmSimple(config.getLong("vm.mipsCapacity"), config.getInt("vm.PEs"))
            .setRam(config.getLong("vm.RAMInMBs"))
            .setBw(config.getLong("vm.BandwidthInMBps"))
            .setSize(config.getLong("vm.Size"))
            .setCloudletScheduler(cloudletScheduling(saasconfig.getString("cloudlet.CloudletScheduling"))))
        )

      return vmList
    }






    val hostList = createHosts()

    logger.info(s"Created hosts: $hostList")

    /* Method to create Datacenters */
    def createDataCenter(): Unit = {
      val allocationlist: List[String] = config.getStringList("vm.VmAllocationPolicy").asScala.toList

      (1 to saasconfig.getInt("datacenter.Number")) foreach(i =>

        new DatacenterSimple(cloudsim, hostList.asJava, vmAllocation(allocationlist(i-1)))
          .getCharacteristics()
          .setCostPerSecond(config.getDouble("datacenter.CostPerSecond"))
          .setCostPerMem(config.getDouble("datacenter.CostPerMem"))
          .setCostPerStorage(config.getDouble("datacenter.CostPerStorage"))
          .setCostPerBw(config.getDouble("datacenter.CostPerBw"))
          .setOs(config.getString("datacenter.os"))
        )
    }

    //val vmAllocation: VmAllocationPolicySimple = new VmAllocationPolicySimple();

    val dc0 = createDataCenter()



    val vmList = createVM()
    logger.info(s"Created virtual machines: $vmList")

    val utilizationModel = new UtilizationModelDynamic(config.getDouble("utilizationRatio"));

    /* Method to create cloudlets */
    def createCloudlets(): ListBuffer[Cloudlet] = {
      val cloudletList = new  ListBuffer[Cloudlet]
      (0 to saasconfig.getInt("cloudlet.Number")) foreach(i =>

        cloudletList+= new CloudletSimple(saasconfig.getLong("cloudlet.Size"), saasconfig.getInt("cloudlet.PEs"), utilizationModel)
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

    /* Method to calculate costs of VMs */
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

    /* Method to calculate the costs of cloudlets */
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
