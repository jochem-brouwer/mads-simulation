package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Tram;

public class TramLeavesIntStationEvent extends Event {
	private static final Duration PLATFORM_FREE_DURATION = Duration.ofSeconds(40);

	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramLeavesIntStationEvent(final IntPlatform intPlatform, final Tram tram) {
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

	// This function is used to load all of the passengers from the platform onto
	// the tram as long as we have enough
	// capacity.

	@Override
	public void fire() {
		final Duration travelTime = this.intPlatform.getTravelTimeToNextPlatform();

		final ArriveWaitingPointEvent arrivalEvent = new ArriveWaitingPointEvent(this.intPlatform.getNextWaitingPoint(),
				this.tram);
		EventScheduler.get().scheduleEventAhead(arrivalEvent, travelTime);

		final PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(this.intPlatform);
		EventScheduler.get().scheduleEventAhead(platformFreeEvent, PLATFORM_FREE_DURATION);

		// System.out.println("Tram " + this.tram.getId() + " leaves the platform " +
		// this.intPlatform +
		// " and schedules a new arrival event at platform " +
		// this.intPlatform.getNextWaitingPoint().getNextPlatform().getName() + " after
		// travel time " +
		// travelTime + ".");
		Simulation.log("Tram " + this.tram.getId() + " leaves " + this.intPlatform.getName() + " at "
				+ EventScheduler.get().getCurrentTime());
	}

	@Override
	public String toString() {
		return "TramLeavesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}