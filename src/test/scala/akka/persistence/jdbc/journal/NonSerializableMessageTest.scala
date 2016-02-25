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

package akka.persistence.jdbc.journal

import akka.actor.{ Props, ActorRef }
import akka.event.LoggingReceive
import akka.persistence.{ RecoveryCompleted, PersistentActor }
import akka.persistence.jdbc.TestSpec
import akka.testkit.TestProbe
import scala.concurrent.duration._

class NonSerializableMessageTest extends TestSpec("in-memory-application.conf") {

  class TestActor(id: String, received: ActorRef, recovered: ActorRef) extends PersistentActor {
    override def persistenceId: String = id

    def handleEvent(any: Any): Unit =
      received ! any

    override def receiveRecover: Receive = LoggingReceive {
      case msg: RecoveryCompleted ⇒
      case msg                    ⇒ recovered ! msg
    }

    override def receiveCommand: Receive = LoggingReceive {
      case msg ⇒ persist(msg)(handleEvent)
    }
  }

  def withPersistentActor(f: (ActorRef, TestProbe, TestProbe) ⇒ Unit): Unit = {
    val receiveProbe = TestProbe()
    val recoverProbe = TestProbe()
    val persistentActor = system.actorOf(Props(new TestActor("1", receiveProbe.ref, recoverProbe.ref)))
    try { f(persistentActor, receiveProbe, recoverProbe) } finally cleanup(persistentActor)
  }

  class NonSerializable extends Serializable

  it should "not persistent a non serializable message" in {
    withPersistentActor { (actor, received, recovered) ⇒
      actor ! new NonSerializable()
      recovered.expectNoMsg(100.millis)
      received.expectNoMsg(100.millis)
    }

    withPersistentActor((actor, received, recovered) ⇒
      recovered.expectNoMsg(100.millis)
    )
  }
}
