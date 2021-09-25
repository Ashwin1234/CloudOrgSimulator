import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.{BasicCloudSimPlusExample, Simulation1, Simulation2, Simulation3}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object Simulation:
  val logger = CreateLogger(classOf[Simulation])

  @main def runSimulation =
    /*logger.info("Constructing a cloud model...")
    //BasicCloudSimPlusExample.Start()
    RoundRobin.start()
    //SimpleAllocation.start()
    logger.info("Finished cloud simulation...")*/
    println("choose a simulation to run")
    val a = scala.io.StdIn.readInt();
    a match {
      case 1 => Simulation1.start();
      case 2 => Simulation2.start();
      case 3 => Simulation3.start();
    }

class Simulation