package org.uu.mads.simulation;

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