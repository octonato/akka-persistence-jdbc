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

package akka.persistence.jdbc.config

import akka.persistence.jdbc.util.ConfigOps._
import com.typesafe.config.Config

class SlickConfiguration(config: Config) {
  private val cfg = config.asConfig("slick")
  val slickDriver: String = cfg.as[String]("driver", "slick.driver.PostgresDriver$")
  val jndiName: Option[String] = cfg.as[String]("jndiName").trim
  val jndiDbName: Option[String] = cfg.as[String]("jndiDbName")
  override def toString: String = s"SlickConfiguration($slickDriver,$jndiName,$jndiDbName)"
}