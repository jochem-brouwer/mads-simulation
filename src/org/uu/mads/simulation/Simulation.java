package org.uu.mads.simulation;

public class Simulation {

	public static void main(final String[] args) {

		// TODO: Schedule initial Event

		while (!EventScheduler.get().getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
		}
	}
}
