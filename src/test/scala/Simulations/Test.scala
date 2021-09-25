package Simulations

import Simulations.Simulation1.iaasconfig
import Simulations.Simulation2.paasconfig
import Simulations.Simulation3.saasconfig
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Test extends AnyFlatSpec with Matchers {
  behavior of "configuration parameters module"

  it should "obtain the utilization ratio" in {
    iaasconfig.getDouble("utilizationRatio") shouldBe 0.8E0
  }

  it should "obtain the MIPS capacity" in {
    iaasconfig.getLong("vm.mipsCapacity") shouldBe 1000
  }

  it should "cloudlet size in paas" in {
    paasconfig.getLong("cloudlet.Size") shouldBe 50000
  }

  it should "cloudlet numbers" in {
    paasconfig.getLong("cloudlet.Number") shouldBe 26
  }
  it should "cloudlet size in saas" in {
    saasconfig.getLong("cloudlet.Size") shouldBe 20000
  }

  it should "cloudlet scheduling" in {
    saasconfig.getString("cloudlet.CloudletScheduling") shouldBe "SpaceShared"
  }


}
