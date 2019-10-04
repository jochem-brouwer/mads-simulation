package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Passenger;
import org.uu.mads.simulation.state.Tram;

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
	public void fire() {
		Passenger.calculatePassengers(this.intPlatform);
		final Duration dwellTime = this.tram.loadPassengers(this.intPlatform);

		this.intPlatform.setOccupied();

		final TramLeavesIntermediateStationEvent tramLeavesIntermediateEvent = new TramLeavesIntermediateStationEvent(
				this.intPlatform, this.tram);

		EventScheduler.get().scheduleEventAhead(tramLeavesIntermediateEvent, dwellTime);
	}

	@Override
	public String toString() {
		return "TramArrivesIntermediateEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}