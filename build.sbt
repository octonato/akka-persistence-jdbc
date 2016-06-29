/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import de.heikoseeberger.sbtheader.license.Apache2_0
import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform

version := "2.5.1-SNAPSHOT"

name := "akka-persistence-jdbc"

organization := "com.github.dnvriend"

scalaVersion := "2.11.8"

fork in Test := true

parallelExecution in Test := false

lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  resolvers += Resolver.typesafeRepo("releases"),
  resolvers += Resolver.jcenterRepo,
  resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
  fork in Test := true,
  parallelExecution in Test := false,
  licenses +=("Apache-2.0", url("http://opensource.org/licenses/apache2.0.php")),
  SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(RewriteArrowSymbols, true),
  scalacOptions ++= Seq("-feature", "-language:higherKinds", "-language:implicitConversions", "-deprecation", "-Ybackend:GenBCode", "-Ydelambdafy:method", "-target:jvm-1.8"),
  headers := Map(
    "scala" -> Apache2_0("2016", "Dennis Vriend"),
    "conf" -> Apache2_0("2016", "Dennis Vriend", "#")
  )
)

lazy val root = (project in file("."))
  .aggregate(common, journal, query, snapshot)
  .dependsOn(common, journal, query, snapshot)
  .enablePlugins(AutomateHeaderPlugin)

lazy val common = (project in file("jdbc-common"))
  .settings(commonSettings:_*)

lazy val journal = (project in file("jdbc-journal"))
  .settings(commonSettings:_*)
  .dependsOn(common % "compile->compile;test->test")

lazy val query = (project in file("jdbc-query"))
  .settings(commonSettings:_*)
  .dependsOn(common % "compile->compile;test->test", journal)

lazy val snapshot = (project in file("jdbc-snapshot"))
  .settings(commonSettings:_*)
  .dependsOn(common % "compile->compile;test->test")

//lazy val inmemory = (project in file("inmemory"))
//  .settings(commonSettings:_*)
//  .dependsOn(common)
