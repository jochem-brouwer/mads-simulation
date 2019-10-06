package org.uu.mads.simulation.state;

import java.time.LocalTime;
import java.util.List;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.events.TryOccupyJunctionEvent;

public class WaitingPointJunction extends WaitingPoint {

	private Junction nextJunction;
	private List<Tram> tramList;
	private EndStation endStation;
	private Tram previousTram;

	// TODO: No constructor, no getters, no setters?!

	private void schedule(final EventScheduler scheduler) {

		// TODO: This method is dead code as it is private and never called locally
		for (int i = 0; i < this.tramList.size(); i++) {

			final Tram currentTram = this.tramList.get(i);

			if (currentTram.previousTram.id == this.previousTram.id) {
				final LocalTime time = scheduler.getCurrentTime();
				final TryOccupyJunctionEvent junctionEvent = new TryOccupyJunctionEvent(this.nextJunction,
						this.endStation, currentTram);
				scheduler.ScheduleEvent(junctionEvent, time);
				this.previousTram = currentTram;

				// Delete the tram from the waiting list.
				this.tramList.remove(i);
				i = 0; // TODO: Doesn't look right
			}
		}
	}
}