package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;

public class ScheduledLeaveEndStationEvent extends Event {
	private static final int PRIORITY = 1;

	private final EndStation endStation;
	private final Duration tramLeaveFrequency;

	public ScheduledLeaveEndStationEvent(final EndStation endStation, final Duration tramLeaveFrequency) {
		super(PRIORITY);
		this.endStation = endStation;
		this.tramLeaveFrequency = tramLeaveFrequency;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	@Override
	public void fire() {
		if (this.endStation.getNextScheduledLeave().equals(EventScheduler.get().getCurrentTime())
				|| this.endStation.getNextScheduledLeave().isBefore(EventScheduler.get().getCurrentTime())) {
			System.out.println(
					"Tram is ready at end station " + this.endStation + " and the schedule demands a tram to leave.");
			System.out.println("Requesting to use junction.");
			final TryOccupyJunctionEvent tryOccupyJunctionEvent = new TryOccupyJunctionEvent(this.endStation);
			EventScheduler.get().scheduleEventAhead(tryOccupyJunctionEvent, Duration.ZERO);
		} else {
			System.out.println("Tram is ready at end station " + this.endStation
					+ " but the next scheduled leave is in the future.");
			final ScheduledLeaveEndStationEvent scheduledLeaveEndStationEvent = new ScheduledLeaveEndStationEvent(
					this.endStation, this.tramLeaveFrequency);
			EventScheduler.get().scheduleEvent(scheduledLeaveEndStationEvent, this.endStation.getNextScheduledLeave());
		}

	}

	@Override
	public String toString() {
		return "ScheduledLeaveEndStationEvent [endStation=" + this.endStation + "]";
	}

}