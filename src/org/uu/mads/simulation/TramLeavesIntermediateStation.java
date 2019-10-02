package org.uu.mads.simulation;

public class TramLeavesIntermediateStationEvent extends Event {
  private IntPlatform intPlatform;
  private Tram tram;



  public void fire(EventScheduler currentEventScheduler) {
    Duration platformFreeTime = Duration.ofSeconds(40);
    Duration travelTime = this.intPlatform.getTravelTime();
    ArriveWaitingPoint tramLeavesIntermediateEvent = new TramLeavesIntermediateEvent(this.intPlatform, this.tram);

    if ( (intPlatform.getNextWaitingPoint()) instanceof WaitingPointInt) {
      ArriveWaitingPointIntermediateStationEvent arrivalEvent = new ArriveWaitingPointIntermediateStationEvent(intPlatform.getNextWp(), this.tram);
      currentEventScheduler.scheduleEventAhead(arrivalEvent, travelTime)
    } else {
      // it is a WaitingPointJunction
      ArriveWaitingPointEndStationEvent arrivalEvent = new ArriveWaitingPointEndStationEvent(intPlatform.getNextWp(), this.tram);
      currentEventScheduler.scheduleEventAhead(arrivalEvent, travelTime);
    }

    PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(intPlatform)

    currentEventScheduler.scheduleEventAhead(platformFreeEvent, platformFreeTime);
  }
}