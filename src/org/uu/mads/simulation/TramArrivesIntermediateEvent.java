package org.uu.mads.simulation;

public class TramArrivesIntermediateEvent extends Event {
	private IntPlatform intPlatform;
	private Tram tram;

  public TramArrivesIntermediateEvent(IntPlatform intPlatform, Tram tram) {
    this.intPlatform = intPlatform;
    this.tram = tram;
  }

  public void fire(EventScheduler currentEventScheduler) {
    Passenger.calculatePassengers(this.intPlatform);
    Duration dwellTime = Tram.loadPassengers(this.tram);

    intPlatform.setOccupied();

    TramLeavesIntermediateEvent tramLeavesIntermediateEvent = new TramLeavesIntermediateEvent(this.intPlatform, this.tram);

    currentEventScheduler.scheduleEventAhead(tramLeavesIntermediateEvent, dwellTime);
  }
}