/*package Simulations

import Simulations.BasicCloudSimPlusExample.{config, logger}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.distributions.{ContinuousDistribution, UniformDistr}
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.PeSimple
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudsimplus.listeners.CloudletVmEventInfo

import collection.JavaConverters.*

class RandomSimulation


object RandomSimulation:

  val random: ContinuousDistribution = new UniformDistr();

  val cloudsim = new CloudSim();
  val broker0 = new DatacenterBrokerSimple(cloudsim);

  val hostPes = List(new PeSimple(config.getLong("cloudSimulator.host.mipsCapacity")))
  logger.info(s"Created one processing element: $hostPes")

  val hostList = List(new HostSimple(config.getLong("cloudSimulator.host.RAMInMBs"),
    config.getLong("cloudSimulator.host.StorageInMBs"),
    config.getLong("cloudSimulator.host.BandwidthInMBps"),
    hostPes.asJava))

  logger.info(s"Created one host: $hostList")

  val vmAllocation : VmAllocationPolicySimple = new VmAllocationPolicySimple();

  vmAllocation.setFindHostForVmFunction(randomAllocationPolicy)

  val dc0 = new DatacenterSimple(cloudsim, hostList.asJava);

  val vmList = List(
    new VmSimple(config.getLong("cloudSimulator.vm.mipsCapacity"), hostPes.length)
      .setRam(config.getLong("cloudSimulator.vm.RAMInMBs"))
      .setBw(config.getLong("cloudSimulator.vm.BandwidthInMBps"))
      .setSize(config.getLong("cloudSimulator.vm.StorageInMBs")),new VmSimple(config.getLong("cloudSimulator.vm.mipsCapacity"), hostPes.length)
      .setRam(config.getLong("cloudSimulator.vm.RAMInMBs"))
      .setBw(config.getLong("cloudSimulator.vm.BandwidthInMBps"))
      .setSize(config.getLong("cloudSimulator.vm.StorageInMBs"))
  )
  logger.info(s"Created one virtual machine: $vmList")

  val utilizationModel = new UtilizationModelDynamic(config.getDouble("cloudSimulator.utilizationRatio"));
  val cloudletList = new CloudletSimple(config.getLong("cloudSimulator.cloudlet.size"), config.getInt("cloudSimulator.cloudlet.PEs"), utilizationModel) ::
    (new CloudletSimple(config.getLong("cloudSimulator.cloudlet.size"), config.getInt("cloudSimulator.cloudlet.PEs"), utilizationModel) :: Nil)

  logger.info(s"Created a list of cloudlets: $cloudletList")



  broker0.submitVmList(vmList.asJava);
  broker0.submitCloudletList(cloudletList.asJava);

  logger.info("Starting cloud simulation...")
  cloudsim.start();

  new CloudletsTableBuilder(broker0.getCloudletFinishedList()).build();

  def randomAllocationPolicy(vmAllocationPolicy: VmAllocationPolicy, vm: Vm): Host = {
    val hostList: List[Any] = vmAllocationPolicy.getHostList();
    hostList.foreach(c=>
      val randomIndex: Int = (random.sample() * hostList.length).toInt;
      val host: Host = hostList(randomIndex)
      if (host.isSuitableForVm(vm)){
        return host
    }
    )
    return null

  }*/




