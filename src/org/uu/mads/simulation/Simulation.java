package org.uu.mads.simulation;

public class Simulation {

	public static void main(final String[] args) {
		final EventScheduler eventScheduler = EventScheduler.get();
		while (!eventScheduler.getScheduledEventsByTime().isEmpty()) { // TODO: Yeah I know - we need to talk about end
																		// conditions
			// TODO
		}
	}
}
