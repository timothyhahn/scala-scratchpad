import scala.concurrent.stm._
import akka.actor._
import akka.transactor._

abstract class Component
class Position(var x: Int, var y: Int) extends Component
class Velocity(var x: Int, var y: Int) extends Component
class World(val velocity: Velocity, val position: Ref[Position])

case class Increment
case object GetCount


class MovementSystem(world: World) extends Actor {

  def process(p: Position):Position = {
    p.x += world.velocity.x
    p.y += world.velocity.y
    p
  }

  def receive = {
    case coordinated @ Coordinated(Increment) => {
      coordinated atomic { implicit t =>
        world.position.getAndTransform(x => process(x))
      }
    }

    case GetCount => 
      sender ! world.position.single.get
  }
}

object Refs extends App {

  import scala.concurrent.Await
  import scala.concurrent.duration._
  import akka.util.Timeout
  import akka.pattern.ask

  val system = ActorSystem("app")

  val world = new World(new Velocity(1,1), Ref(new Position(0,0)))

  val system1 = system.actorOf(Props(new MovementSystem(world)), name="system1")
  val system2 = system.actorOf(Props(new MovementSystem(world)), name="system2")

  implicit val timeout = Timeout(5 seconds)

  system1 ! Coordinated(Increment)
  system2 ! Coordinated(Increment)

  val position = Await.result(system1 ? GetCount, timeout.duration).asInstanceOf[Position]
  println(position.x)
  println(position.y)
  
}
