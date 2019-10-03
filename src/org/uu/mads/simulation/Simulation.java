package org.uu.mads.simulation;

import java.time.LocalTime;

public class Simulation {
	private static LocalTime startTime = LocalTime.of(9, 0); // TODO: change

	public static void main(final String[] args) {
		final EventScheduler eventScheduler = new EventScheduler(startTime);
		while (!eventScheduler.getScheduledEventsByTime().isEmpty()) { // TODO: Yeah I know - we need to talk about end
																		// conditions
			// TODO
		}
	}
}
