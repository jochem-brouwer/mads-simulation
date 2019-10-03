package org.uu.mads.simulation;

public class ScheduledLeaveEndStationEvent extends Event {
	private static final int PRIORITY = 1;
	private final EndStation endStation;

	public ScheduledLeaveEndStationEvent(final EndStation endStation) {
		super(PRIORITY);
		this.endStation = endStation;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	@Override
	public void fire(final EventScheduler scheduler) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "ScheduledLeaveEndStationEvent [endStation=" + this.endStation + "]";
	}

}