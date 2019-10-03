package org.uu.mads.simulation.state;

import java.time.LocalTime;
import java.util.List;

import org.uu.mads.simulation.EventSchedule;
import org.uu.mads.simulation.events.TryOccupyJunctionEvent;

public class WaitingPointJunction extends WaitingPoint {
	private Junction nextJunction;
	private List<Tram> tramList;
	private EndStation endStation;
	private Tram previousTram;

	private void schedule(final EventSchedule scheduler) {
		while (!this.tramList.isEmpty()) {
			final Tram currentTram = this.tramList.get(0);
			if (currentTram.getId() == this.previousTram.getId()) {
				final LocalTime time = scheduler.getCurrentTime();
				final TryOccupyJunctionEvent junctionEvent = new TryOccupyJunctionEvent(this.endStation, this, this.nextJunction, currentTram);
				scheduler.scheduleEvent(junctionEvent, time);
				this.previousTram = currentTram;

				// Delete the tram from the waiting list.
				this.tramList.remove(0);
			}
		}
	}
}