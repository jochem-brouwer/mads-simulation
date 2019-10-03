package org.uu.mads.simulation;

import java.time.LocalTime;

public class Simulation {
	private static final LocalTime START_TIME = LocalTime.of(9, 0); // TODO: adjust

	public static void main(final String[] args) {
		final EventSchedule eventSchedule = EventSchedule.get();
		while (!eventSchedule.getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			// TODO
		}
	}
}
