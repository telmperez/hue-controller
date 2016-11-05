package actors

import javax.inject.Inject

import actors.HueActor.HueLightsUpdate
import akka.actor.Actor
import com.philips.lighting.model.{PHLight, PHLightState}
import play.api.Logger
import services.{HueLightModes, HueService}


object HueActor {

  final val actorName = "hue-actor"

  case class HueLightsUpdate(lights: List[PHLight])

}

class HueActor @Inject()(hueService: HueService) extends Actor with HueLightModes {

  var lights: Map[String, (PHLight, PHLightState)] = Map()

  override def receive: Receive = {
    case HueLightsUpdate(l) => l.foreach { ls =>

      // Update lights map
      lights.get(ls.getUniqueId) match {
        case Some(x) => {
          lights + (ls.getUniqueId -> (ls, x._1.getLastKnownLightState))
        }
        case None => lights + (ls.getUniqueId -> (ls, ls.getLastKnownLightState))
      }

      // Check for default mode
      if (isLightInDefaultMode(ls)) {
        val lightId = ls.getUniqueId
        Logger.info(s"Setting Light [$lightId] to Energize Light State")
        hueService.updateLightState(ls, energizeLightState)
      }

    }
    case _ => Logger.warn("Unknown Message Received")
  }

}

