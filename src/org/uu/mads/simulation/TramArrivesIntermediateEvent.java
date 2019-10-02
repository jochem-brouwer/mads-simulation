package org.uu.mads.simulation;

public class TramArrivesIntermediateEvent extends Event {
	private static final int PRIORITY = 2;
	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramArrivesIntermediateEvent(final IntPlatform intPlatform, final Tram tram) {
		super(PRIORITY);
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
    Passenger.calculatePassengers(this.intPlatform);
    Duration dwellTime = Tram.loadPassengers(this.tram);

    intPlatform.setOccupied();

    TramLeavesIntermediateEvent tramLeavesIntermediateEvent = new TramLeavesIntermediateEvent(this.intPlatform, this.tram);

    currentEventScheduler.scheduleEventAhead(tramLeavesIntermediateEvent, dwellTime);
  }
}