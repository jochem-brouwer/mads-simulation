package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;

public class ScheduledLeaveEndStationEvent extends Event {
	private static final int LOW_PRIORITY = 1;

	private final EndStation endStation;

	public ScheduledLeaveEndStationEvent(final EndStation endStation) {
		super(LOW_PRIORITY);
		this.endStation = endStation;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	@Override
	public void fire() {
		if (this.endStation.getNextScheduledLeave().equals(EventScheduler.get().getCurrentTime())
				|| this.endStation.getNextScheduledLeave().isBefore(EventScheduler.get().getCurrentTime())) {
			//System.out.println("The schedule demands a tram to leave from end station " + this.endStation + ".");
			//System.out.println("Requesting to use junction.");
			final TryOccupyJunctionEvent tryOccupyJunctionEvent = new TryOccupyJunctionEvent(this.endStation);
			EventScheduler.get().scheduleEventAhead(tryOccupyJunctionEvent, Duration.ZERO);
		} else {
			//System.out.println("Next scheduled leave is in the future at end station " + this.endStation + ".");
			//System.out.println("Rescheduling scheduled leave event.");
			final ScheduledLeaveEndStationEvent scheduledLeaveEndStationEvent = new ScheduledLeaveEndStationEvent(
					this.endStation);
			EventScheduler.get().scheduleEvent(scheduledLeaveEndStationEvent, this.endStation.getNextScheduledLeave());
		}

	}

	@Override
	public String toString() {
		return "ScheduledLeaveEndStationEvent [endStation=" + this.endStation + "]";
	}

}