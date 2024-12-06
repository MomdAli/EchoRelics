package model.events

import scala.collection.mutable
import scala.concurrent.{Future, ExecutionContext}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Try, Success, Failure}

object EventManager {

  /** A private variable that holds a list of EventListener instances. This list
    * is used to manage and notify listeners about events.
    */
  private var listeners: List[EventListener] = List()

  /** A private queue to manage the sequence of game events. This queue holds
    * instances of `GameEvent` and is used to store and process events in the
    * order they are added.
    */
  private val eventQueue: mutable.Queue[GameEvent] = mutable.Queue()

  /** Subscribes a new listener to the EventManager.
    *
    * @param listener
    *   The EventListener to be added to the list of listeners.
    */
  def subscribe(listener: EventListener): Unit = {
    listeners = listener :: listeners
  }

  /** Unsubscribes the given listener from the event manager.
    *
    * @param listener
    *   The EventListener to be removed from the list of listeners.
    */
  def unsubscribe(listener: EventListener): Unit = {
    listeners = listeners.filter(_ != listener)
  }

  /** Adds a new event to the event queue.
    *
    * @param event
    *   The GameEvent to be added to the queue.
    */
  def notify(event: GameEvent): Unit = {
    eventQueue.enqueue(event)
  }

  /** Instantly notifies all relevant listeners about the occurrence of a game
    * event.
    *
    * @param event
    *   The game event that has occurred and needs to be notified to listeners.
    */
  def instantNotify(event: GameEvent): Unit = {
    listeners.foreach(_.handleEvent(event))
  }

  /** Processes all events in the event queue asynchronously.
    *
    * This method continuously dequeues events from the `eventQueue` and
    * notifies all registered listeners by calling their `handleEvent` method
    * with the dequeued event.
    *
    * The method runs until the `eventQueue` is empty.
    */
  private def processNextEvent(): Unit = {
    if (eventQueue.nonEmpty) {
      val nextEvent = eventQueue.dequeue()

      // Process each listener for the given event asynchronously
      Future {
        listeners.foreach { listener =>
          Try {
            listener.handleEvent(nextEvent)
          } match {
            case Success(_) => // Do nothing if successful
            case Failure(ex) =>
              println(
                s"Error handling event ${nextEvent}: ${ex.getMessage}"
              )
          }
        }
      }.onComplete { _ =>
        processNextEvent()
      }
    }
  }

  /** Processes all events in the event queue asynchronously.
    *
    * This method continuously dequeues events from the `eventQueue` and
    * notifies all registered listeners by calling their `handleEvent` method
    * with the dequeued event.
    *
    * The method runs until the `eventQueue` is empty.
    */
  def processEvents(): Unit = {
    Future {
      processNextEvent()
    }
  }

  /** Checks if the given listener is subscribed to the event manager.
    *
    * @param listener
    *   The EventListener to check for subscription.
    * @return
    *   true if the listener is subscribed, false otherwise.
    */
  def isSubscribed(listener: EventListener): Boolean = {
    listeners.contains(listener)
  }
}

trait EventListener {

  /** Handles the given game event.
    *
    * @param event
    *   The game event to be handled.
    */
  def handleEvent(event: GameEvent): Unit
}
