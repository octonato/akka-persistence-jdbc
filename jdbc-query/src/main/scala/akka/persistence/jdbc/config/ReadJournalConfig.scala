package akka.persistence.jdbc.config

import akka.persistence.jdbc.util.ConfigOps._
import com.typesafe.config.Config

import scala.concurrent.duration.{ FiniteDuration, _ }

class ReadJournalPluginConfig(config: Config) {
  val tagSeparator: String = config.as[String]("tagSeparator", ",")
  val dao: String = config.as[String]("dao", "akka.persistence.jdbc.dao.bytea.readjournal.ByteArrayReadJournalDao")
  override def toString: String = s"ReadJournalPluginConfig($tagSeparator,$dao)"
}

class JournalTableColumnNames(config: Config) {
  private val cfg = config.asConfig("tables.journal.columnNames")
  val ordering: String = cfg.as[String]("ordering", "ordering")
  val persistenceId: String = cfg.as[String]("persistenceId", "persistence_id")
  val sequenceNumber: String = cfg.as[String]("sequenceNumber", "sequence_number")
  val created: String = cfg.as[String]("created", "created")
  val tags: String = cfg.as[String]("tags", "tags")
  val message: String = cfg.as[String]("message", "message")
  override def toString: String = s"JournalTableColumnNames($persistenceId,$sequenceNumber,$created,$tags,$message)"
}

class JournalTableConfiguration(config: Config) {
  private val cfg = config.asConfig("tables.journal")
  val tableName: String = cfg.as[String]("tableName", "journal")
  val schemaName: Option[String] = cfg.as[String]("schemaName").trim
  val columnNames: JournalTableColumnNames = new JournalTableColumnNames(config)
  override def toString: String = s"JournalTableConfiguration($tableName,$schemaName,$columnNames)"
}

class ReadJournalConfig(config: Config) {
  val slickConfiguration = new SlickConfiguration(config)
  val journalTableConfiguration = new JournalTableConfiguration(config)
  val pluginConfig = new ReadJournalPluginConfig(config)
  val refreshInterval: FiniteDuration = config.asFiniteDuration("refresh-interval", 1.second)
  val maxBufferSize: Int = config.as[String]("max-buffer-size", "500").toInt
  override def toString: String = s"ReadJournalConfig($slickConfiguration,$journalTableConfiguration,$pluginConfig,$refreshInterval,$maxBufferSize)"
}

