package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;

public class Simulation {

	public static final int TRAMS_PER_HOUR = 16; // This is fixed during our simulation run.
	public static final Duration TURN_AROUND = Duration.ofMinutes(4); // Turn around time is 4 min.
	public static final LocalTime FIRST_SCHEDULED_LEAVE_TIME_PR = LocalTime.of(6, 0); // TODO: Adapt
	public static final LocalTime FIRST_PASSENGER_CALC = LocalTime.of(6, 0); // TODO: Adapt
	public static LocalTime FIRST_SCHEDULED_LEAVE_TIME_CS = LocalTime.of(6, 0); // TODO: Adapt
	public static final Duration TRAM_LEAVE_FREQUENCY = Duration.ofSeconds(225); // This is 3.75 minutes with 16 trams


	private static void calculateCSLeave(Duration frequency, LocalTime startTime) {
		// Calculates the first schedules leave time for central station.

		LocalTime firstRound = FIRST_SCHEDULED_LEAVE_TIME_PR.plus(Duration.ofMinutes(17) .plus(TURN_AROUND));
		while (firstRound.compareTo(startTime.plus(frequency)) == 1) {
			firstRound.minus(frequency);
		}

		FIRST_SCHEDULED_LEAVE_TIME_CS = firstRound;
		System.out.println("First scheduled leave at PR: " + FIRST_SCHEDULED_LEAVE_TIME_PR);
		System.out.println("First scheduled leave at CS: " + FIRST_SCHEDULED_LEAVE_TIME_CS);
	}

	public static void main(final String[] args) {

		// TODO: Schedule initial Event
		calculateCSLeave(TRAM_LEAVE_FREQUENCY, FIRST_SCHEDULED_LEAVE_TIME_PR);

		while (!EventScheduler.get().getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
		}
	}
}
