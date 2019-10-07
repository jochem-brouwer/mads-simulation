package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.List;

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

		// When a tram arrives, we calculate the passengers and load them into the tram.
		int passengersOut = this.intPlatform.dumpPassengers(this.tram);
		this.intPlatform.calculatePassengers();
		int passengersIn = this.intPlatform.loadPassengers(this.intPlatform, this.tram);

		final Duration dwellTime = this.tram.calculateDwellTime(
				this.intPlatform, passengersIn, passengersOut);

		this.intPlatform.setOccupied();

		final TramLeavesIntStationEvent tramLeavesIntermediateEvent = new TramLeavesIntStationEvent(
				this.intPlatform, this.tram);

		System.out.println("Tram " + this.tram.getId() + " is arriving at platform " + this.intPlatform.getName() +
				", dumps " + passengersOut + " passengers and loads " + passengersIn + " passengers.");
		System.out.println("The dwell time is " + dwellTime + " seconds." );

		EventScheduler.get().scheduleEventAhead(tramLeavesIntermediateEvent, dwellTime);
	}

	@Override
	public String toString() {
		return "TramArrivesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}