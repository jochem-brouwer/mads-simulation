package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Passenger;
import org.uu.mads.simulation.state.Tram;

public class TramArrivesIntStationEvent extends Event {
	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramArrivesIntStationEvent(final IntPlatform intPlatform, final Tram tram) {
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
	public void fire() {
		this.intPlatform.calculatePassengers();
		final Duration dwellTime = this.tram.loadPassengers(this.intPlatform);

		this.intPlatform.setOccupied();

		final TramLeavesIntStationEvent tramLeavesIntermediateEvent = new TramLeavesIntStationEvent(
				this.intPlatform, this.tram);

		EventScheduler.get().scheduleEventAhead(tramLeavesIntermediateEvent, dwellTime);
	}

	@Override
	public String toString() {
		return "TramArrivesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}