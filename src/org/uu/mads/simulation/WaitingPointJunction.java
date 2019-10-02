package org.uu.mads.simulation;

import java.time.LocalTime;
import java.util.List;

public class WaitingPointJunction extends WaitingPoint {

	private Junction nextJunction;
	private List<Tram> tramList;
	private EndStation endStation;
	private Tram previousTram;

	private void schedule(EventScheduler scheduler) {

		for (int i = 0; i < tramList.size(); i++) {

			Tram currentTram = tramList.get(i);

			if (currentTram.previousTram.id == previousTram.id) {
				LocalTime time = scheduler.getCurrentTime();
				TryOccupyJunctionEvent junctionEvent = new TryOccupyJunctionEvent(nextJunction, endStation, currentTram);
				scheduler.ScheduleEvent(junctionEvent, time);
				previousTram = currentTram;

				// Delete the tram from the waiting list.
				tramList.remove(i);
				i = 0;
			}
		}
	}
}