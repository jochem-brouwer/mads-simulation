package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class EventScheduler {
	private final SortedMap<LocalTime, List<Event>> events = new TreeMap<>(); // needs to be sorted by time and priority
	private final LocalTime currentTime;

	public EventScheduler(final LocalTime currentTime) {
		this.currentTime = currentTime;
	}


	public LocalTime getCurrentTime() {
		return this.currentTime;
	}

	public void scheduleEvent(final Event event, final LocalTime time) {
		// TODO: implement

	}

	public void scheduleEventAhead(final Event event, final Duration duration) {
		// TODO: implement
	}
}
