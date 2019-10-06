package org.uu.mads.simulation;

import java.time.Duration;

public class Simulation {
	public static final Duration TRAM_LEAVE_FREQUENCY = Duration.ofMinutes(5); // TODO: Adapt

	public static void main(final String[] args) {

		// TODO: Schedule initial Event

		while (!EventScheduler.get().getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
		}
	}
}
