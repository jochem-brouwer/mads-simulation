package org.uu.mads.simulation;

import java.time.Duration;

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
	public void fire(final EventScheduler currentEventScheduler) {
		Passenger.calculatePassengers(this.intPlatform);
		final Duration dwellTime = this.tram.loadPassengers(this.intPlatform);

		this.intPlatform.setOccupied();

		final TramLeavesIntermediateEvent tramLeavesIntermediateEvent = new TramLeavesIntermediateStation(
				this.intPlatform, this.tram);

		currentEventScheduler.scheduleEventAhead(tramLeavesIntermediateEvent, dwellTime);
	}
}