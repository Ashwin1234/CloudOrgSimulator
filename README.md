# Homework 1 - Ashwin Bhaskar Srivatsa
## Description
As part of this homework we are required to create different cloud simulations on different datacenters which are mixes of Iaas, Paas and Saas model implementation.

## Overview
To start with this assignment contains implementation of various services and policies in cloud datacenters using the [Cloudsim plus](https://cloudsimplus.org/) framework. There are different simulations that are executed using different configurations with pricing being calculated. The simulations also show the effect of different configuarations on VM and cloudlet allocations by the brokers.

## Instructions to run
Using intellij
Install Intellij, sbt <br/>
clone the repo - ```https://github.com/Ashwin1234/CloudOrgSimulator``` and navigate to the folder. <br/>
click on the Simulation.scala file in src and click run. <br/>
choose the simulation by entering 1,2 or 3, you will get the output of that particular simulation. <br/>
Using sbt
Navigate to project folder and type in the following commend ```sbt clean compile run``` <br/>
choose the simulation by entering 1,2 or 3, you will get the output of that particular simulation. <br/>
To run the test ```sbt test```

## project structure
1) resources/provider.conf - It is the config file which contains different configuration parameters for Datacenters, Hosts, VMs, cloudlets etc
2) BasicCloudSimPLusExample.scala - Basic simulation provided by the repo
3) Simulations/Simulation1.scala - It is the implementation of Iaas configuration and produces the simulation results.
4) Simulations/Simulation2.scala - It is the implementation of Paas configuration and produces the simulation results.
5) Simulations/Simulation3.scala - It is the implementation of Saas configuration and produces the simulation results.
6) Simulations/Simulation.scala - The main file or starting point of execution.
7) HelperUtils - HelperUtils
8) Test/test.scala - contains various tests to be performed on the simulations.

Iaas - Infrastructure as a service where a user can scale up/down the datacenters and can set various policies for VM, cloudlet allocation, scheduling and utilization models.<br />
Paas - Platform as a service where a user does not have the authority to manage the datacenters and hosts, but can set various policies for VMs and hosts. <br/>
Saas - Software as a service is the most common one where the brokers have limited control and can only set the scheduling algorithm for the cloudlets.  <br/>

## Simulations
### Simulation 1
In Iaas the number of data centers, VMs and the policies are set by the user
```datacenter{
os = "Windows 10"
CostPerSecond=0.01
CostPerMem=0.02
CostPerStorage=0.001
CostPerBw=0.005
Number=2
}
```
```
utilizationratio = 0.80
Host RAMInMBs = 10000
Number of VMs = 10
Number of cloudlets = 15
cloudlets size = 10,000
VM mipsCapacity = 1000
VmScheduling= "TimeShared"
VmAllocationPolicy=["RoundRobin","BestFit"]
CloudletScheduling= "SpaceShared"

================== Simulation finished at time 201.83 ==================



                                              SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Cost|Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds| CPU|Total
---------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 3|   0|        3| 0|        3|      10000|          3|        0|        13|      12|$0.13|$0.14
       1|SUCCESS| 3|   1|        3| 1|        3|      10000|          3|       13|        25|      13|$0.13|$0.14
       2|SUCCESS| 3|   2|        3| 2|        3|      10000|          3|       25|        38|      13|$0.13|$0.14
       3|SUCCESS| 3|   3|        3| 3|        3|      10000|          3|       38|        50|      13|$0.13|$0.14
       4|SUCCESS| 3|   0|        3| 4|        3|      10000|          3|       50|        63|      13|$0.13|$0.14
       5|SUCCESS| 3|   1|        3| 5|        3|      10000|          3|       63|        76|      13|$0.13|$0.14
       6|SUCCESS| 3|   2|        3| 6|        3|      10000|          3|       76|        88|      13|$0.13|$0.14
       7|SUCCESS| 3|   3|        3| 7|        3|      10000|          3|       88|       101|      13|$0.13|$0.14
       8|SUCCESS| 3|   0|        3| 8|        3|      10000|          3|      101|       113|      13|$0.13|$0.14
       9|SUCCESS| 3|   1|        3| 9|        3|      10000|          3|      113|       126|      13|$0.13|$0.14
      10|SUCCESS| 3|   0|        3| 0|        3|      10000|          3|      126|       139|      13|$0.13|$0.14
      11|SUCCESS| 3|   1|        3| 1|        3|      10000|          3|      139|       151|      13|$0.13|$0.14
      12|SUCCESS| 3|   2|        3| 2|        3|      10000|          3|      151|       164|      13|$0.13|$0.14
      13|SUCCESS| 3|   3|        3| 3|        3|      10000|          3|      164|       176|      13|$0.13|$0.14
      14|SUCCESS| 3|   0|        3| 4|        3|      10000|          3|      177|       189|      13|$0.13|$0.14
      15|SUCCESS| 3|   1|        3| 5|        3|      10000|          3|      189|       202|      13|$0.13|$0.14
---------------------------------------------------------------------------------------------------------------
20:45:21.898 [run-main-0] INFO  java.lang.Class -  total processing cost for VMs: 3.0274503 total storageCost for VMs: 10.0 total memory cost for VMs: 200.0 total bwcost for VMs: 50.0 total cost for VMs: 263.02747
20:45:21.904 [run-main-0] INFO  java.lang.Class -  total cost per bw for all the cloudlets is: 0.08 total cost for executing all the cloudlets are: 2.1762
[success] Total time: 22 s, completed Sep 24, 2021 8:45:21 PM
```
In this example there are 10 VMs being assigned to 4 hosts containing 3 Processing Elements each. The scheduling policy used for VMs are TimeShared and the allocation of VMs are done using RoundRobin in the first data center and Best fit for the second data center.
The VMs are allocated to the hosts using the RoundRobin allocation policy here the VMs are assigned to the hosts in a cyclic manner.
The cloudlets are allocated to VMs using space shared scheduling policy. One cloudlet is executed in one VM and others are in the waiting list.
The total cost for executing each cloudlet is $0.14 and the overall cost is $2.1762


## Simulation 2
In Paas user can manage various policies for VMs and cloudlets
```
datacenter{
os = "MacOS"
CostPerSecond=0.01
CostPerMem=0.02
CostPerStorage=0.001
CostPerBw=0.005
Number=2
}

```
```
VmAllocationPolicy= ["FirstFit","Simple"]
CloudletScheduling= "SpaceShared"
Number of cloudlets = 25
size of cloudlets = 50000
```
```
================== Simulation finished at time 2602.77 ==================



                                              SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Cost|Cost
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds| CPU|Total
---------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 3|   0|        3| 0|        3|      50000|          2|        0|       100|     100|$1.00|$1.01
       1|SUCCESS| 3|   1|        3| 1|        3|      50000|          2|      100|       200|     100|$1.00|$1.01
       2|SUCCESS| 3|   1|        3| 2|        3|      50000|          2|      200|       300|     100|$1.00|$1.01
       3|SUCCESS| 3|   1|        3| 3|        3|      50000|          2|      300|       400|     100|$1.00|$1.01
       4|SUCCESS| 3|   1|        3| 4|        3|      50000|          2|      400|       500|     100|$1.00|$1.01
       5|SUCCESS| 3|   1|        3| 5|        3|      50000|          2|      501|       600|     100|$1.00|$1.01
       6|SUCCESS| 3|   1|        3| 6|        3|      50000|          2|      601|       700|     100|$1.00|$1.01
       7|SUCCESS| 3|   1|        3| 7|        3|      50000|          2|      701|       800|     100|$1.00|$1.01
       8|SUCCESS| 3|   1|        3| 8|        3|      50000|          2|      801|       900|     100|$1.00|$1.01
       9|SUCCESS| 3|   1|        3| 9|        3|      50000|          2|      901|      1000|     100|$1.00|$1.01
      10|SUCCESS| 3|   1|        3|10|        3|      50000|          2|     1001|      1101|     100|$1.00|$1.01
      11|SUCCESS| 3|   0|        3| 0|        3|      50000|          2|     1101|      1201|     100|$1.00|$1.01
      12|SUCCESS| 3|   1|        3| 1|        3|      50000|          2|     1201|      1301|     100|$1.00|$1.01
      13|SUCCESS| 3|   1|        3| 2|        3|      50000|          2|     1301|      1401|     100|$1.00|$1.01
      14|SUCCESS| 3|   1|        3| 3|        3|      50000|          2|     1401|      1501|     100|$1.00|$1.01
      15|SUCCESS| 3|   1|        3| 4|        3|      50000|          2|     1502|      1601|     100|$1.00|$1.01
      16|SUCCESS| 3|   1|        3| 5|        3|      50000|          2|     1602|      1701|     100|$1.00|$1.01
      17|SUCCESS| 3|   1|        3| 6|        3|      50000|          2|     1702|      1801|     100|$1.00|$1.01
      18|SUCCESS| 3|   1|        3| 7|        3|      50000|          2|     1802|      1901|     100|$1.00|$1.01
      19|SUCCESS| 3|   1|        3| 8|        3|      50000|          2|     1902|      2001|     100|$1.00|$1.01
      20|SUCCESS| 3|   1|        3| 9|        3|      50000|          2|     2002|      2102|     100|$1.00|$1.01
      21|SUCCESS| 3|   1|        3|10|        3|      50000|          2|     2102|      2202|     100|$1.00|$1.01
      22|SUCCESS| 3|   0|        3| 0|        3|      50000|          2|     2202|      2302|     100|$1.00|$1.01
      23|SUCCESS| 3|   1|        3| 1|        3|      50000|          2|     2302|      2402|     100|$1.00|$1.01
      24|SUCCESS| 3|   1|        3| 2|        3|      50000|          2|     2402|      2502|     100|$1.00|$1.01
      25|SUCCESS| 3|   1|        3| 3|        3|      50000|          2|     2503|      2602|     100|$1.00|$1.01
---------------------------------------------------------------------------------------------------------------
20:06:10.075 [run-main-0] INFO  java.lang.Class -  total processing cost for VMs: 42.94571 total storageCost for VMs: 11.264001 total memory cost for VMs: 220.0 total bwcost for VMs: 55.0 total cost for VMs: 329.20975
20:06:10.082 [run-main-0] INFO  java.lang.Class -  total cost per bw for all the cloudlets is: 0.13000000000000003 total cost for executing all the cloudlets are: 26.2856
[success] Total time: 20 s, completed Sep 24, 2021 8:06:10 PM
```
In this example there are 11 VMs being assigned to 4 hosts containing 3 Processing Elements each. The scheduling policy used for VMs are TimeShared and the allocation of VMs are done using FirstFit in the first data center and SimpleAllocation for the second data center.
There are 25 cloudlets that are allocated to VMs using space shared scheduling policy. The VMs are allocated to the hosts using the Simple allocation policy here the VMs are assigned to the hosts having the least amount PEs in use.
The total cost for executing each cloudlet is $1.01 and the overall cost is $26.2856. The cost is high because the size of cloudlets executed on VMs are more (50,000). The utilization ratio is set to 0.5 

## Simulation 3
In Saas the user has no control over the hosts or VMs, but can access the scheduler algorithm for the cloudlets.
```
================== Simulation finished at time 842.22 ==================
datacenter {
Number = 2
}
cloudlet {
Size = 20000
RAMInMBs = 10000
PEs = 3
Number = 20
CloudletScheduling="SpaceShared"
}


                                              SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime|Cost|Cost
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds| CPU|Total
---------------------------------------------------------------------------------------------------------------
       0|SUCCESS| 3|   0|        3| 0|        3|      20000|          3|        0|        40|      40|$0.40|$0.41
       1|SUCCESS| 3|   0|        3| 1|        3|      20000|          3|       40|        80|      40|$0.40|$0.41
       2|SUCCESS| 3|   0|        3| 2|        3|      20000|          3|       80|       120|      40|$0.40|$0.41
       3|SUCCESS| 3|   0|        3| 3|        3|      20000|          3|      120|       160|      40|$0.40|$0.41
       4|SUCCESS| 3|   0|        3| 4|        3|      20000|          3|      160|       200|      40|$0.40|$0.41
       5|SUCCESS| 3|   0|        3| 5|        3|      20000|          3|      201|       240|      40|$0.40|$0.41
       6|SUCCESS| 3|   0|        3| 6|        3|      20000|          3|      241|       280|      40|$0.40|$0.41
       7|SUCCESS| 3|   0|        3| 7|        3|      20000|          3|      281|       320|      40|$0.40|$0.41
       8|SUCCESS| 3|   0|        3| 8|        3|      20000|          3|      321|       360|      40|$0.40|$0.41
       9|SUCCESS| 3|   0|        3| 9|        3|      20000|          3|      361|       400|      40|$0.40|$0.41
      10|SUCCESS| 3|   0|        3|10|        3|      20000|          3|      401|       441|      40|$0.40|$0.41
      11|SUCCESS| 3|   0|        3| 0|        3|      20000|          3|      441|       481|      40|$0.40|$0.41
      12|SUCCESS| 3|   0|        3| 1|        3|      20000|          3|      481|       521|      40|$0.40|$0.41
      13|SUCCESS| 3|   0|        3| 2|        3|      20000|          3|      521|       561|      40|$0.40|$0.41
      14|SUCCESS| 3|   0|        3| 3|        3|      20000|          3|      561|       601|      40|$0.40|$0.41
      15|SUCCESS| 3|   0|        3| 4|        3|      20000|          3|      602|       641|      40|$0.40|$0.41
      16|SUCCESS| 3|   0|        3| 5|        3|      20000|          3|      642|       681|      40|$0.40|$0.41
      17|SUCCESS| 3|   0|        3| 6|        3|      20000|          3|      682|       721|      40|$0.40|$0.41
      18|SUCCESS| 3|   0|        3| 7|        3|      20000|          3|      722|       761|      40|$0.40|$0.41
      19|SUCCESS| 3|   0|        3| 8|        3|      20000|          3|      762|       801|      40|$0.40|$0.41
      20|SUCCESS| 3|   0|        3| 9|        3|      20000|          3|      802|       842|      40|$0.40|$0.41
---------------------------------------------------------------------------------------------------------------
20:29:59.567 [run-main-0] INFO  java.lang.Class -  total processing cost for VMs: 13.896631 total storageCost for VMs: 11.0 total memory cost for VMs: 220.0 total bwcost for VMs: 55.0 total cost for VMs: 299.89667
20:29:59.574 [run-main-0] INFO  java.lang.Class -  total cost per bw for all the cloudlets is: 0.10500000000000002 total cost for executing all the cloudlets are: 8.6301
[success] Total time: 25 s, completed Sep 24, 2021 8:29:59 PM
```

In this example there are 11 VMs being assigned to 4 hosts containing 3 Processing Elements each. The scheduling policy used for VMs are TimeShared and the allocation of VMs are done using BestFit in the first data center and RoundRobin for the second data center.
There are 20 cloudlets that are allocated to VMs using space shared scheduling policy. The VMs are allocated to the hosts using the BestFit allocation policy here the VMs are assigned to the hosts which have the least power consumption.
The total cost for executing each cloudlet is $0.41 and the overall cost is $8.631. the cost is slightly high because the size of cloudlets executed on VMs are 20000. The utilization ratio is set to 0.5.

## Test
6 tests added 
1) Utilization ratio is Iaas 
2) mips capcity of VMs in Iaas
3) Size of cloudlets in Paas
4) Number of cloudlets in Paas
5) Size of cloudlets  in Saas
6) Scheduling algorithm in Saas


## FutureWork
Analysing which configuration is cost-effective to the customer.
Use Multiple brokers
Scheduling efficient algorithms to allocate VMs to hosts.




