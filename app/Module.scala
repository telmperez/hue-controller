import com.google.inject.AbstractModule
import java.time.Clock

import actors.HueActor
import play.api.Logger
import play.api.libs.concurrent.AkkaGuiceSupport

class Module extends AbstractModule with AkkaGuiceSupport {

  override def configure() = {
    Logger.info("Init Play Module")
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    bindActor[HueActor](HueActor.actorName)
  }

}
