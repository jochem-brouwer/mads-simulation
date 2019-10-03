package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class EventScheduler {
	private final SortedMap<LocalTime, List<Event>> scheduledEventsByTime = new TreeMap<>(); // needs to be sorted by
																								// time and
	// priority
	private final LocalTime currentTime;

	public EventScheduler(final LocalTime currentTime) {
		this.currentTime = currentTime;
	}

	public LocalTime getCurrentTime() {
		return this.currentTime;
	}

	public void scheduleEvent(final Event event, final LocalTime eventTime) {
		final List<Event> scheduledEvents;

		if (this.scheduledEventsByTime.containsKey(eventTime)) {
			scheduledEvents = this.scheduledEventsByTime.get(eventTime);
			for (int i = 0; i < scheduledEvents.size(); i++) {
				final Event scheduledEvent = scheduledEvents.get(i);
				if (scheduledEvent.getPriority() < event.getPriority()) {
					scheduledEvents.add(i, event);
				}
			}
			if (!scheduledEvents.contains(event)) {
				scheduledEvents.add(event);
			}
		} else {
			scheduledEvents = new LinkedList<>();
			scheduledEvents.add(event);
		}
		this.scheduledEventsByTime.put(eventTime, scheduledEvents);
	}

	public void scheduleEventAhead(final Event event, final Duration duration) {
		final LocalTime eventTime = this.currentTime.plus(duration);
		scheduleEvent(event, eventTime);
	}

	public static void main(final String[] args) {
		System.out.println("hi");
	}
}
