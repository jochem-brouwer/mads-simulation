package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;

public class ScheduledLeaveEndStationEvent extends Event {
	private static final int LOW_PRIORITY = 1;

	private final EndStation endStation;

	public ScheduledLeaveEndStationEvent(final EndStation endStation) {
		super(LOW_PRIORITY);

		Objects.requireNonNull(endStation, "Given endStation must not be null!");

		this.endStation = endStation;
	}

	@Override
	public void fire() {
		if (this.endStation.isNextScheduledLeaveDue()) {
			final TryOccupyJunctionEvent tryOccupyJunctionEvent = new TryOccupyJunctionEvent(this.endStation);
			EventScheduler.getInstance().scheduleEventAhead(tryOccupyJunctionEvent, Duration.ZERO);
		} else {
			final ScheduledLeaveEndStationEvent scheduledLeaveEndStationEvent = new ScheduledLeaveEndStationEvent(
					this.endStation);
			EventScheduler.getInstance().scheduleEvent(scheduledLeaveEndStationEvent,
					this.endStation.getNextScheduledLeave());
		}

	}

	@Override
	public String toString() {
		return "ScheduledLeaveEndStationEvent [endStation=" + this.endStation + "]";
	}

}