package controllers

import javax.inject._

import play.api.mvc._
import services.HueService

@Singleton
class HueController @Inject()(hueService: HueService) extends Controller {

  def listAllLights = Action {
    hueService.listAllLights()
    Ok
  }

  def energizeAllLights = Action {
    hueService.listAllLights()
    Ok
  }

}
