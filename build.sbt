import com.typesafe.sbt.packager.SettingsHelper._

name := """hue-controller"""
version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

packageName in Universal := "hue-controller"
makeDeploymentSettings(Universal, packageBin in Universal, "zip")
