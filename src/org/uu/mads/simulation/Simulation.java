package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;

public class Simulation {
	public static final LocalTime FIRST_SCHEDULED_LEAVE_TIME_PR = LocalTime.of(6, 0); // TODO: Adapt
	public static final LocalTime FIRST_PASSENGER_CALC = LocalTime.of(6, 0); // TODO: Adapt
	public static final LocalTime FIRST_SCHEDULED_LEAVE_TIME_CS = LocalTime.of(6, 0); // TODO: Adapt
	public static final Duration TRAM_LEAVE_FREQUENCY = Duration.ofMinutes(5); // TODO: Adapt

	public static void main(final String[] args) {

		// TODO: Schedule initial Event

		while (!EventScheduler.get().getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
		}
	}
}
