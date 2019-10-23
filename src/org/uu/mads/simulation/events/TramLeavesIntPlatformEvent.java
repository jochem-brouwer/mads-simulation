package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Tram;

public class TramLeavesIntPlatformEvent extends Event {
	private static final Duration PLATFORM_FREE_DURATION = Duration.ofSeconds(40);

	private final IntPlatform intPlatform;

	public TramLeavesIntPlatformEvent(final IntPlatform intPlatform) {
		super();

		Objects.requireNonNull(intPlatform, "Given intPlatform must not be null!");

		this.intPlatform = intPlatform;
	}

	@Override
	public void fire() {
		final Tram tram = this.intPlatform.departTram();

		final Duration travelTime = this.intPlatform.getTravelTimeToNextPlatform();
		final ArriveWaitingPointEvent arrivalEvent = new ArriveWaitingPointEvent(this.intPlatform.getNextWaitingPoint(),
				tram);
		EventScheduler.getInstance().scheduleEventAhead(arrivalEvent, travelTime);

		final PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(this.intPlatform);
		EventScheduler.getInstance().scheduleEventAhead(platformFreeEvent, PLATFORM_FREE_DURATION);

		Simulation.log("Tram " + tram.getId() + " left platform " + this.intPlatform.getName() + ".");

		Simulation.logTramPositions();
	}

	@Override
	public String toString() {
		return "TramLeavesIntStationEvent [intPlatform=" + this.intPlatform + "]";
	}
}