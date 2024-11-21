package utils

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class ObservableSpec extends AnyWordSpec with Matchers {

    "An Observable" should {
        "add an Observer" in {
            val observable = new Observable
            val observer = new Observer {
                def update(): Unit = {}
            }
            observable.add(observer)
            observable.subscribers should contain(observer)
        }

        "remove an Observer" in {
            val observable = new Observable
            val observer = new Observer {
                def update(): Unit = {}
            }
            observable.add(observer)
            observable.remove(observer)
            observable.subscribers should not contain observer
        }

        "notify all Observers" in {
            var updated = false
            val observable = new Observable
            val observer = new Observer {
                def update(): Unit = {
                    updated = true
                }
            }
            observable.add(observer)
            observable.notifyObservers
            updated should be(true)
        }

        "not notify removed Observers" in {
            var updated = false
            val observable = new Observable
            val observer = new Observer {
                def update(): Unit = {
                    updated = true
                }
            }
            observable.add(observer)
            observable.remove(observer)
            observable.notifyObservers
            updated should be(false)
        }
    }
}