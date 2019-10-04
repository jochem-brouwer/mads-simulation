package org.uu.mads.simulation;

public class Simulation {

	public static void main(final String[] args) {
		while (!EventScheduler.get().getScheduledEventsByTime().isEmpty()) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
		}
	}
}
