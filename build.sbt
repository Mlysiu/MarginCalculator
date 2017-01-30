name := "MarginCalculator"

version := "1.0"

scalaVersion := "2.12.1"

organization := "com.mlysiu"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val sprayV = "1.3.3"
  Seq(
    "io.spray" %% "spray-json" % sprayV,
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalaz" %% "scalaz-core" % "7.2.8"
  )
}