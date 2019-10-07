package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.List;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Passenger;
import org.uu.mads.simulation.state.Platform;
import org.uu.mads.simulation.state.Tram;

public class TramLeavesIntStationEvent extends Event {
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

	// This function is used to load all of the passengers from the platform onto the tram as long as we have enough
	// capacity.

	@Override
	public void fire() {

		final Duration platformFreeTime = Duration.ofSeconds(40);
		final Duration travelTime = this.intPlatform.getTravelTime();

		final ArriveWaitingPointEvent arrivalEvent = new ArriveWaitingPointEvent(this.intPlatform.getNextWaitingPoint(),
				this.tram);
		EventScheduler.get().scheduleEventAhead(arrivalEvent, travelTime);

		final PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(this.intPlatform);
		EventScheduler.get().scheduleEventAhead(platformFreeEvent, platformFreeTime);

		//System.out.println("Tram " + this.tram.getId() + " leaves the platform " + this.intPlatform +
		//		" and schedules a new arrival event at platform " +
		//		this.intPlatform.getNextWaitingPoint().getNextPlatform().getName() + " after travel time " +
		//		travelTime + ".");

	}

	@Override
	public String toString() {
		return "TramLeavesIntStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}