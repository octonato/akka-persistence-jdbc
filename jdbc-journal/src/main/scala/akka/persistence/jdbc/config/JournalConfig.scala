package akka.persistence.jdbc.config

import akka.persistence.jdbc.util.ConfigOps._
import com.typesafe.config.Config

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

class DeletedToTableColumnNames(config: Config) {
  private val cfg = config.asConfig("tables.deletedTo.columnNames")
  val persistenceId: String = cfg.as[String]("persistenceId", "persistence_id")
  val deletedTo: String = cfg.as[String]("deletedTo", "deleted_to")
  override def toString: String = s"DeletedToTableColumnNames($persistenceId,$deletedTo)"
}

class DeletedToTableConfiguration(config: Config) {
  private val cfg = config.asConfig("tables.deletedTo")
  val tableName: String = cfg.as[String]("tableName", "deleted_to")
  val schemaName: Option[String] = cfg.as[String]("schemaName").trim
  val columnNames: DeletedToTableColumnNames = new DeletedToTableColumnNames(config)
  override def toString: String = s"DeletedToTableConfiguration($tableName,$schemaName,$columnNames)"
}

class JournalPluginConfig(config: Config) {
  val tagSeparator: String = config.as[String]("tagSeparator", ",")
  val dao: String = config.as[String]("dao", "akka.persistence.jdbc.dao.bytea.journal.ByteArrayJournalDao")
  override def toString: String = s"JournalPluginConfig($tagSeparator,$dao)"
}

class JournalConfig(config: Config) {
  val slickConfiguration = new SlickConfiguration(config)
  val journalTableConfiguration = new JournalTableConfiguration(config)
  val deletedToTableConfiguration = new DeletedToTableConfiguration(config)
  val pluginConfig = new JournalPluginConfig(config)
  override def toString: String = s"JournalConfig($slickConfiguration,$journalTableConfiguration,$deletedToTableConfiguration,$pluginConfig)"
}