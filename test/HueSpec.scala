import org.scalatestplus.play.{OneAppPerSuite, OneAppPerTest, PlaySpec}
import services.HueService

class HueSpec extends PlaySpec with OneAppPerSuite {

  "Hue" should {

    "list all lights" in {
      val hueService = app.injector.instanceOf(classOf[HueService])
      hueService.listAllLights()
    }

  }

}
