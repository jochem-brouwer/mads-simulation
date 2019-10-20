package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.IntPlatform;
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
		final int passengersOut = this.intPlatform.dumpPassengers(this.tram);
		this.intPlatform.calculatePassengers();
		final int passengersIn = this.intPlatform.loadPassengers(this.intPlatform, this.tram);

		Simulation.logVerbose("Tram " + this.tram.getId() + " is arriving at platform " + this.intPlatform.getName()
				+ " at " + EventScheduler.get().getCurrentTime() + ", dumps " + passengersOut + " passengers and loads "
				+ passengersIn + " passengers.");

		final Duration dwellTime = this.tram.calculateDwellTime(this.intPlatform, passengersIn, passengersOut);

		Simulation.logVerbose("The dwell time is " + (int) (dwellTime.getSeconds()) + " seconds.");

		this.intPlatform.setOccupied();

		final TramLeavesIntStationEvent tramLeavesIntStationEvent = new TramLeavesIntStationEvent(this.intPlatform,
				this.tram);

		EventScheduler.get().scheduleEventAhead(tramLeavesIntStationEvent, dwellTime);
	}

	@Override
	public String toString() {
		return "TramArrivesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}