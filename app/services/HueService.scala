package services

import java.util

import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import javax.inject.{Inject, Named, Singleton}

import actors.HueActor
import actors.HueActor.HueLightsUpdate
import akka.actor.ActorRef
import com.philips.lighting.hue.sdk.{PHAccessPoint, PHHueSDK, PHSDKListener}
import com.philips.lighting.model.{PHLight, _}
import play.Configuration
import play.api.Logger
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

@Singleton
class HueService @Inject()(config: Configuration, lifecycle: ApplicationLifecycle, @Named(HueActor.actorName) hueActor: ActorRef) extends HueLightModes {

  val host = config.getString("hue.host")
  val userId = config.getString("hue.user.id")
  val heartbeatMs = config.getLong("hue.hearbeat.ms")

  Logger.info(s"Connecting to the Hue Bridge - host: [$host] userId: [$userId]")
  val bridge = PHHueSDK.getInstance()
  bridge.connect(new PHAccessPoint(host, userId, null))

  Logger.info("Register SDK Listener")
  bridge.getNotificationManager.registerSDKListener(new HueListener)

  Logger.info("Registering Shutdown Hook")
  lifecycle.addStopHook(() => Future.successful(bridge.disableAllHeartbeat()))

  def updateLightState(light: PHLight, lightState: PHLightState) = {
    bridge.getSelectedBridge.updateLightState(light, lightState)
  }

  def energizeAllLights() = {
    for (l <- bridge.getSelectedBridge.getResourceCache.getAllLights) {
      updateLightState(l, energizeLightState)
    }
  }

  def listAllLights() = {
    for (l <- bridge.getSelectedBridge.getResourceCache.getAllLights) {
      val ls: PHLightState = l.getLastKnownLightState
      Logger.info("#################################################################")
      Logger.info(s"ID: [${l.getUniqueId}]")
      Logger.info(s"Model: [${l.getModelNumber}]")
      Logger.info(s"Brightness: [${ls.getBrightness}]")
      Logger.info(s"Hue: [${ls.getHue}]")
      Logger.info(s"Colour Temperature: [${ls.getCt}]")
      Logger.info(s"Saturation: [${ls.getSaturation}]")
      Logger.info(s"x: [${ls.getX}] y: [${ls.getY}]")
      Logger.info(s"Is Reachable? [${ls.isReachable}]")
      Logger.info(s"Is On? [${ls.isOn}]")
      Logger.info("#################################################################")
    }
  }

  class HueListener private[HueService] extends PHSDKListener {

    override def onError(i: Int, s: String): Unit = {
      Logger.error(s"Error -  $i $s")
    }

    override def onConnectionLost(phAccessPoint: PHAccessPoint): Unit = {
      Logger.warn("Connection Lost")
    }

    override def onConnectionResumed(phBridge: PHBridge): Unit = {
      Logger.trace("Connection Resumed")
    }

    override def onAuthenticationRequired(phAccessPoint: PHAccessPoint): Unit = {
      Logger.warn("Authentication Required")
    }

    override def onCacheUpdated(list: util.List[Integer], phBridge: PHBridge): Unit = {
      Logger.trace("Cache Updated")
      hueActor ! HueLightsUpdate(phBridge.getResourceCache.getAllLights.asScala.toList)
    }

    override def onAccessPointsFound(list: util.List[PHAccessPoint]): Unit = {
      Logger.info("Found Access Points")
    }

    override def onBridgeConnected(phBridge: PHBridge, s: String): Unit = {
      Logger.info(s"Bridge Connected -- Setting Heartbeat to [$heartbeatMs]ms")
      bridge.setSelectedBridge(phBridge)
      bridge.enableHeartbeat(phBridge, heartbeatMs)
    }

    override def onParsingErrors(list: util.List[PHHueParsingError]): Unit = {
      Logger.error("Parsing Error")
      for (x <- list) {
        Logger.error(x.getJSONContext.toString(2))
      }
    }
  }

}
