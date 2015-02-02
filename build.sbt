import play.twirl.sbt.Import.TwirlKeys

name := """agilenet"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

TwirlKeys.templateImports += "play.data._"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4"
)
