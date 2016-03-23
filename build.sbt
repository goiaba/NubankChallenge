name := "nubank-challenge"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "2.4.2",
  "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.2",
  "com.typesafe.akka" % "akka-http-xml-experimental_2.11" % "2.4.2",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.2",
  "com.typesafe.akka" % "akka-http-testkit-experimental_2.11" % "2.4.2-RC3",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)