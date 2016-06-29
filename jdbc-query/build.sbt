import build.Version._

name := "akka-persistence-jdbc-query"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-persistence-query-experimental" % akkaVersion
)
    