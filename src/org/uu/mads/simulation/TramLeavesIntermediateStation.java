package org.uu.mads.simulation;

import java.time.Duration;

public class TramLeavesIntermediateStation extends Event {
	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramLeavesIntermediateStation(final IntPlatform intPlatform, final Tram tram) {
		super();
		this.intPlatform = intPlatform;
		this.tram = tram;
	}

	public IntPlatform getIntPlatform() {
		return this.intPlatform;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
  public void fire(final EventScheduler currentEventScheduler) {
    final Duration platformFreeTime = Duration.ofSeconds(40);
    final Duration travelTime = this.intPlatform.getTravelTime();
    final ArriveWaitingPoint tramLeavesIntermediateEvent = new TramLeavesIntermediateEvent(this.intPlatform, this.tram);

    if ( (this.intPlatform.getNextWaitingPoint()) instanceof WaitingPointInt) {
      final ArriveWaitingPointIntermediateStationEvent arrivalEvent = new ArriveWaitingPointIntermediateStationEvent(this.intPlatform.getNextWp(), this.tram);
      currentEventScheduler.scheduleEventAhead(arrivalEvent, travelTime)
    } else {
      // it is a WaitingPointJunction
      final ArriveWaitingPointEndStationEvent arrivalEvent = new ArriveWaitingPointEndStationEvent(this.intPlatform.getNextWp(), this.tram);
      currentEventScheduler.scheduleEventAhead(arrivalEvent, travelTime);
    }

    final PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(this.intPlatform)

    currentEventScheduler.scheduleEventAhead(platformFreeEvent, platformFreeTime);
  }
}