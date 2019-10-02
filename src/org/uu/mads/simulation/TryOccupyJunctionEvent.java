package org.uu.mads.simulation;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TryOccupyJunctionEvent extends Event {
	private EndStation endStation;
	private WaitingPointJunction waitingPointJunction;
	private Junction junction;
	private Tram tram;
	private Boolean waitingPoint;

	public TryOccupyJunctionEvent(Junction nextJunction, EndStation endStation, Tram tram, Boolean waitingPoint) {
		junction = nextJunction;
		this.endStation = endStation;
		this.tram = tram;
		this.waitingPoint = waitingPoint;
	}

	public void fire(EventScheduler scheduler) {

		// If the event is called by an incoming train at the waiting point, we use this method:
		if (waitingPoint == true) {
			fireFromWaitingPoint(scheduler);

		// Else if the event is called by an outgoing train at the station, we use this method:
		} else {
			fireFromEndStation(scheduler);
		}
	}

	private void fireFromWaitingPoint(EventScheduler scheduler) {

		// If the junction crossing is not used, we can send our tram into the crossing.
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
		// Else if platform A is free, we are in mode 3 and can send two trams at once
		} else if (entStation.tramOnPlatformA == null){

			junction.tramOnLaneInA = tram;
			LocalTime time = scheduler.getCurrentTime();
			time.plus(1, ChronoUnit.MINUTES);

			// We schedule a new event to free the junction again.
			FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent();
			scheduler.scheduleEvent(freeJunctionEvent, time);
		}
	}

	private void fireFromEndStation(EventScheduler scheduler) {

		if (junction.junctionUsed == false) {

			if (endStation.tramOnPlatformA != null) {

				junction.tramOnLaneAOut = tram;
				endStation.tramOnPlatformA == null;

				LocalTime time = scheduler.getCurrentTime();
				time.plus(1, ChronoUnit.MINUTES);
				FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent();
				scheduler.scheduleEvent(freeJunctionEvent, time);

			} else if (endStation.tramOnPlatformB != null) {
				
				junction.tramOnLaneBOut = tram;
				endStation.tramOnPlatformB = null;

				LocalTime time = scheduler.getCurrentTime();
				time.plus(1, ChronoUnit.MINUTES);
				FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent();
				scheduler.scheduleEvent(freeJunctionEvent, time);
			}
		} else if (endStation.tramOnPlatformB != null) {

			junction.tramOnLaneBOut = tram;
			endStation.tramOnPlatformB = null;

			LocalTime time = scheduler.getCurrentTime();
			time.plus(1, ChronoUnit.MINUTES);
			FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent();
			scheduler.scheduleEvent(freeJunctionEvent, time);
		}
	}
}