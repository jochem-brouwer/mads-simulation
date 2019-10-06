package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;

public class Simulation {
	public static final LocalTime FIRST_TRAM_LEAVE_TIME = LocalTime.of(7, 0); // TODO: Adapt
	public static final Duration TRAM_LEAVE_FREQUENCY = Duration.ofMinutes(5); // TODO: Adapt

	public static void main(final String[] args) {

		// TODO: Schedule initial Event

		while (!EventScheduler.get().getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
		}
	}
}
