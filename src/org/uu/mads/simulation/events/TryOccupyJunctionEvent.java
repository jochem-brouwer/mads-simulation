package org.uu.mads.simulation.events;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPointJunction;

public class TryOccupyJunctionEvent extends Event {
	private final EndStation endStation;
	private final WaitingPointJunction waitingPointJunction;
	private final Junction junction;
	private final Tram tram;

	public TryOccupyJunctionEvent(final EndStation endStation, final WaitingPointJunction waitingPointJunction,
			final Junction junction, final Tram tram) {
		super();
		this.endStation = endStation;
		this.waitingPointJunction = waitingPointJunction;
		this.junction = junction;
		this.tram = tram;
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

	@Override
	public String toString() {
		return "TryOccupyJunctionEvent [endStation=" + this.endStation + ", waitingPointJunction="
				+ this.waitingPointJunction + ", junction=" + this.junction + ", tram=" + this.tram + "]";
	}
}