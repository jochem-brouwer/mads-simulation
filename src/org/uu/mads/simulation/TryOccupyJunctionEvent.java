package org.uu.mads.simulation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TryOccupyJunctionEvent extends Event {
	private EndStation endStation;
	private WaitingPointJunction waitingPointJunction;
	private Junction junction;
	private Tram tram;

	public TryOccupyJunctionEvent(Junction nextJunction, EndStation x, Tram y) {
		junction = nextJunction;
		endStation = x;
		tram = y;
	}

	public static void fire(EventScheduler scheduler) {
		if (junction.junctionUsed == false) {


			// If the platform B is free, we send the tram from the junction to platform B.
			if (endStation.tramOnPlatformB == null) {

				junction.tramOnLaneInB = tram;

				LocalTime time = scheduler.getCurrentTime();
				time.plus(1, ChronoUnit.MINUTES);

				// We schedule a new event to free the junction again.
				FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent();
				scheduler.scheduleEvent(freeJunctionEvent, time);

			// If the platform A is free, we send the tram from the junction to platform A.
			} else if (entStation.tramOnPlatformA == null) {

				junction.tramOnLaneInA = tram;
				LocalTime time = scheduler.getCurrentTime();
				time.plus(1, ChronoUnit.MINUTES);

				// We schedule a new event to free the junction again.
				FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent();
				scheduler.scheduleEvent(freeJunctionEvent, time);
			}


		}
	}
}