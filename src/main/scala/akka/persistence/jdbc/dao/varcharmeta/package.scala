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

package akka.persistence.jdbc.dao

import akka.persistence.PersistentRepr
import akka.persistence.jdbc.serialization.{ SerializationResult, Serialized }
import akka.serialization.Serializer

import scala.util.Try

package object varcharmeta {
  final val `UTF-8` = "UTF-8"

  def toByteArray(message: String): Array[Byte] = message.getBytes(`UTF-8`)

  def byteArrayToString(bytes: Array[Byte]): String = new String(bytes, `UTF-8`)

  final val CollectSerializedPF: PartialFunction[SerializationResult, Serialized] = {
    case e: Serialized ⇒ e
  }

  def serialize(bytes: Array[Byte], serializer: Serializer): Array[Byte] = serializer.toBinary(bytes)

  def serialize(xs: Iterable[SerializationResult], serializer: Serializer): Try[Iterable[SerializationResult]] = {
    def serializePayload(payload: Array[Byte]): Any = serializer.toBinary(payload)
    def serializeReprPayload(repr: PersistentRepr): PersistentRepr =
      PersistentRepr(serializePayload(repr.payload.asInstanceOf[Array[Byte]]), repr.sequenceNr, repr.persistenceId, repr.manifest, repr.deleted, repr.sender, repr.writerUuid)
    def mapSerialized(ser: Serialized): Serialized = ser.copy(persistentRepr = serializeReprPayload(ser.persistentRepr))
    Try(xs.collect { case e: Serialized ⇒ e }.map(mapSerialized))
  }

  def deserialize(message: String, serializer: Serializer): Array[Byte] = serializer.fromBinary(message.getBytes(`UTF-8`)) match {
    case xs: Array[Byte] ⇒ xs
    case other           ⇒ throw new IllegalArgumentException("VarcharSerialization only accepts byte arrays, not [" + other + "]")
  }
}
