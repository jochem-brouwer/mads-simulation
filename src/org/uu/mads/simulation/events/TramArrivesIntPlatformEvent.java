package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Tram;

public class TramArrivesIntPlatformEvent extends Event {
	private static final int SUPER_HIGH_PRIORITY = 3;
	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramArrivesIntPlatformEvent(final IntPlatform intPlatform, final Tram tram) {
		super(SUPER_HIGH_PRIORITY);

		Objects.requireNonNull(tram, "Given tram must not be null!");
		Objects.requireNonNull(intPlatform, "Given intPlatform must not be null!");

		this.intPlatform = intPlatform;
		this.tram = tram;
	}

	@Override
	public void fire() {
		this.intPlatform.arriveTram(this.tram);

		// When a tram arrives, we calculate the passengers and load them into the tram.
		final int passengersOut = this.intPlatform.dumpPassengers(this.tram);
		this.intPlatform.calculatePassengers();
		final int passengersIn = this.intPlatform.loadPassengers(this.tram);

		Simulation.log("Tram " + this.tram.getId() + " is arriving at platform " + this.intPlatform.getName() + " at "
				+ EventScheduler.get().getCurrentTime() + ", dumps " + passengersOut + " passengers and loads "
				+ passengersIn + " passengers.");

		final Duration dwellTime = IntPlatform.calculateDwellTime(passengersIn, passengersOut);

		Simulation.log("The dwell time is " + dwellTime.getSeconds() + " seconds.");

		final TramLeavesIntPlatformEvent tramLeavesIntStationEvent = new TramLeavesIntPlatformEvent(this.intPlatform);

		EventScheduler.get().scheduleEventAhead(tramLeavesIntStationEvent, dwellTime);

		Simulation.logTramPositions();
	}

	@Override
	public String toString() {
		return "TramArrivesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}