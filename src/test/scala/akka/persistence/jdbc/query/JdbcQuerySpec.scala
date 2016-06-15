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

package akka.persistence.jdbc.query

import akka.persistence.CapabilityFlag
import akka.persistence.jdbc.config.JournalConfig
import akka.persistence.jdbc.util.Schema.Postgres
import akka.persistence.jdbc.util.{ ClasspathResources, DropCreate, SlickDatabase }
import akka.persistence.query._
import com.typesafe.config.{ Config, ConfigFactory }

abstract class JdbcQuerySpec(config: Config) extends QuerySpec(config)
    with CurrentPersistenceIdsQuerySpec
    with AllPersistenceIdsQuerySpec
    with CurrentEventsByPersistenceIdQuerySpec
    with EventsByPersistenceIdQuerySpec
    with CurrentEventsByTagQuerySpec
    with ClasspathResources
    with DropCreate {

  override val readJournalPluginId: String = "jdbc-read-journal"

  val cfg = system.settings.config.getConfig(readJournalPluginId)

  val journalConfig = new JournalConfig(cfg)

  val db = SlickDatabase.forConfig(cfg, journalConfig.slickConfiguration)

  protected override def afterAll(): Unit = {
    db.close()
    super.afterAll()
  }
}

class PostgresQuerySpec extends JdbcQuerySpec(ConfigFactory.load("postgres-application.conf")) {
  dropCreate(Postgres())

  override protected def supportsOrderingByDateIndependentlyOfPersistenceId: CapabilityFlag = true

  override protected def supportsOrderingByPersistenceIdAndSequenceNr: CapabilityFlag = false
}
