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

package akka.persistence.jdbc.serialization

import akka.actor.ExtendedActorSystem
import akka.persistence.PersistentRepr
import akka.serialization.{ BaseSerializer, SerializationExtension, Serializer }

class JsonSerializer(val system: ExtendedActorSystem) extends BaseSerializer {

  val persistentReprSerializer: Serializer = SerializationExtension(system).serializerFor(classOf[PersistentRepr])

  override def includeManifest: Boolean = true

  override def fromBinary(bytes: Array[Byte], manifest: Option[Class[_]]): AnyRef = ???

  //  override def toBinary(obj: AnyRef): Array[Byte] = obj match {
  //    case xs: Array[Byte] ⇒ persistentReprSerializer.fromBinary(xs) match {
  //      case repr: PersistentRepr ⇒ repr.payload
  //      case obj                  ⇒
  //    }
  //    case other ⇒ throw new IllegalArgumentException("JsonSerializer only serializes byte arrays not [" + other + "]")
  //  }
  override def toBinary(o: AnyRef): Array[Byte] = ???
}
