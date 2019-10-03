package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.State.IntPlatform;
import org.uu.mads.simulation.State.Tram;
import org.uu.mads.simulation.State.WaitingPointInt;
import org.uu.mads.simulation.State.WaitingPointJunction;

public class TramLeavesIntermediateStationEvent extends Event {
	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramLeavesIntermediateStationEvent(final IntPlatform intPlatform, final Tram tram) {
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
	public void fire(final EventScheduler currentEventScheduler) {
		final Duration platformFreeTime = Duration.ofSeconds(40);
		final Duration travelTime = this.intPlatform.getTravelTime();

		if ((this.intPlatform.getNextWp()) instanceof WaitingPointInt) {
			final ArriveWaitingPointIntermediateStationEvent arrivalEvent = new ArriveWaitingPointIntermediateStationEvent(
					(WaitingPointInt) this.intPlatform.getNextWp(), this.tram);
			currentEventScheduler.scheduleEventAhead(arrivalEvent, travelTime);
		} else {
			// it is a WaitingPointJunction
			final ArriveWaitingPointEndStationEvent arrivalEvent = new ArriveWaitingPointEndStationEvent(
					(WaitingPointJunction) this.intPlatform.getNextWp(), this.tram);
			currentEventScheduler.scheduleEventAhead(arrivalEvent, travelTime);
		}

		final PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(this.intPlatform);

		currentEventScheduler.scheduleEventAhead(platformFreeEvent, platformFreeTime);
	}

	@Override
	public String toString() {
		return "TramLeavesIntermediateStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}