package org.uu.mads.simulation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TryOccupyJunctionEvent extends Event {
	private final EndStation endStation;
	private final WaitingPointJunction waitingPointJunction;
	private final Junction junction;
	private Tram tram;

	public TryOccupyJunctionEvent(final EndStation endStation, final WaitingPointJunction waitingPointJunction,
			final Junction junction) {
		super();
		this.endStation = endStation;
		this.waitingPointJunction = waitingPointJunction;
		this.junction = junction;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	public WaitingPointJunction getWaitingPointJunction() {
		return this.waitingPointJunction;
	}

	public Junction getJunction() {
		return this.junction;
	}

	@Override
	public void fire(final EventScheduler scheduler) {
		if (this.junction.isJunctionUsed() == false) {

			// If the platform B is free, we send the tram from the junction to platform B.
			if (this.endStation.getTramOnPlatformB() == null) {

				this.junction.setTramOnLaneInB(this.tram);

				final LocalTime time = scheduler.getCurrentTime();
				time.plus(1, ChronoUnit.MINUTES);

				// We schedule a new event to free the junction again.
				final FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent(this.junction);
				scheduler.scheduleEvent(freeJunctionEvent, time);

				// If the platform A is free, we send the tram from the junction to platform A.
			} else if (this.endStation.getTramOnPlatformA() == null) {

				this.junction.setTramOnLaneInA(this.tram);
				final LocalTime time = scheduler.getCurrentTime();
				time.plus(1, ChronoUnit.MINUTES);

				// We schedule a new event to free the junction again.
				final FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent(this.junction);
				scheduler.scheduleEvent(freeJunctionEvent, time);
			}

		}
	}
}