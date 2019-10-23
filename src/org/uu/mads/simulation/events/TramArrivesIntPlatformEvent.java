package org.uu.mads.simulation.events;

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

		final TramLeavesIntPlatformEvent tramLeavesIntStationEvent = new TramLeavesIntPlatformEvent(this.intPlatform);
		EventScheduler.getInstance().scheduleEventAhead(tramLeavesIntStationEvent, this.intPlatform.getDwellTime());

		Simulation.logTramPositions();
	}

	@Override
	public String toString() {
		return "TramArrivesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}