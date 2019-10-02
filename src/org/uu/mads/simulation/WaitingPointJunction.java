package org.uu.mads.simulation;

import java.time.LocalTime;
import java.util.List;

public class WaitingPointJunction extends WaitingPoint {

	private Junction nextJunction;
	private List<Tram> tramList;
	private EndStation endStation;
	private Tram previousTram;

	private void schedule(EventScheduler scheduler) {
		while (!tramList.isEmpty()) {
			Tram currentTram = tramList.get(0);
			if (currentTram.previousTram.id == previousTram.id) {
				LocalTime time = scheduler.getCurrentTime();
				TryOccupyJunctionEvent junctionEvent = new TryOccupyJunctionEvent(nextJunction, endStation, currentTram);
				scheduler.ScheduleEvent(junctionEvent, time);
				previousTram = currentTram;

				// Delete the tram from the waiting list.
				tramList.remove(0);
			}
		}
	}
}