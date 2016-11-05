package services

import com.philips.lighting.model.{PHLight, PHLightState}
import play.api.Logger

trait HueLightModes {

  val ltw004DefaultLightMode = new PHLightState()
  ltw004DefaultLightMode.setBrightness(254)
  ltw004DefaultLightMode.setCt(366)

  val lct007DefaultLightMode = new PHLightState()
  lct007DefaultLightMode.setHue(14956)
  lct007DefaultLightMode.setBrightness(254)
  lct007DefaultLightMode.setCt(366)
  lct007DefaultLightMode.setSaturation(140)
  lct007DefaultLightMode.setX(0.4571F)
  lct007DefaultLightMode.setY(0.4097F)

  val lct001DefaultLightMode = new PHLightState()
  lct001DefaultLightMode.setHue(14910)
  lct001DefaultLightMode.setBrightness(254)
  lct001DefaultLightMode.setCt(369)
  lct001DefaultLightMode.setSaturation(144)
  lct001DefaultLightMode.setX(0.4596F)
  lct001DefaultLightMode.setY(0.4105F)


  val whiteLightState = new PHLightState()
  whiteLightState.setBrightness(254)
  whiteLightState.setHue(34076)
  whiteLightState.setCt(153)
  whiteLightState.setSaturation(0)
  whiteLightState.setX(0.3227F)
  whiteLightState.setY(0.329F)

  val energizeLightState = new PHLightState()
  energizeLightState.setBrightness(254)
  energizeLightState.setHue(34076)
  energizeLightState.setCt(153)
  energizeLightState.setSaturation(251)
  energizeLightState.setX(0.3144F)
  energizeLightState.setY(0.3301F)


  def isLightInDefaultMode(pHLight: PHLight): Boolean = {
    pHLight.getModelNumber match {
      case "LTW004" => areLightModesEqual(pHLight.getLastKnownLightState, ltw004DefaultLightMode)
      case "LCT001" => areLightModesEqual(pHLight.getLastKnownLightState, lct001DefaultLightMode)
      case "LCT007" => areLightModesEqual(pHLight.getLastKnownLightState, lct007DefaultLightMode)
      case _ => Logger.warn("Found Unsupported Light"); false
    }
  }

  def areLightModesEqual(s1: PHLightState, s2: PHLightState): Boolean = {
    if (s1.getBrightness != null && s2.getBrightness != null && s1.getBrightness != s2.getBrightness) {
      return false
    }
    if (s1.getHue != null && s2.getHue != null && s1.getHue != s2.getHue) {
      return false
    }
    if (s1.getCt != null && s2.getCt != null && s1.getCt != s2.getCt) {
      return false
    }
    if (s1.getSaturation != null && s2.getSaturation != null && s1.getSaturation != s2.getSaturation) {
      return false
    }
    if (s1.getX != null && s2.getX != null && s1.getX != s2.getX) {
      return false
    }
    if (s1.getY != null && s2.getY != null && s1.getY != s2.getY) {
      return false
    }
    true
  }

}
