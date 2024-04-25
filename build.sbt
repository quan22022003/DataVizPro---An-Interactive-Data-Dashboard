ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.0"


lazy val root = (project in file("."))
  .settings(
    name := "CS-C2120 Quan's Data Dashboard"
  )

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies ++= Seq(
  "org.scalafx" % "scalafx_3" % "19.0.0-R30",
  "org.scalatest" %% "scalatest" % "3.2.17" % Test
)

