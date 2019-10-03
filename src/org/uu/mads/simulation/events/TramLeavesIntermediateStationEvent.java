package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventSchedule;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPointInt;
import org.uu.mads.simulation.state.WaitingPointJunction;

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
	public void fire() {
		final Duration platformFreeTime = Duration.ofSeconds(40);
		final Duration travelTime = this.intPlatform.getTravelTime();

		if ((this.intPlatform.getNextWp()) instanceof WaitingPointInt) {
			final ArriveWaitingPointIntermediateStationEvent arrivalEvent = new ArriveWaitingPointIntermediateStationEvent(
					(WaitingPointInt) this.intPlatform.getNextWp(), this.tram);
			EventSchedule.get().scheduleEventAhead(arrivalEvent, travelTime);
		} else {
			// it is a WaitingPointJunction
			final ArriveWaitingPointEndStationEvent arrivalEvent = new ArriveWaitingPointEndStationEvent(
					(WaitingPointJunction) this.intPlatform.getNextWp(), this.tram);
			EventSchedule.get().scheduleEventAhead(arrivalEvent, travelTime);
		}

		final PlatformFreeEvent platformFreeEvent = new PlatformFreeEvent(this.intPlatform);

		EventSchedule.get().scheduleEventAhead(platformFreeEvent, platformFreeTime);
	}

	@Override
	public String toString() {
		return "TramLeavesIntermediateStationEvent [intPlatform=" + this.intPlatform + ", tram=" + this.tram + "]";
	}
}