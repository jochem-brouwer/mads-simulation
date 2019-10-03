package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class EventScheduler {
	private final SortedMap<LocalTime, List<Event>> scheduledEventsByTime = new TreeMap<>();
	private final LocalTime currentTime;

	public EventScheduler(final LocalTime currentTime) {
		this.currentTime = currentTime;
	}

	public LocalTime getCurrentTime() {
		return this.currentTime;
	}

	public void scheduleEvent(final Event event, final LocalTime eventTime) {
		System.out.println("Event " + event + " is to be scheduled at " + eventTime + ".");

		final List<Event> scheduledEvents;
		if (this.scheduledEventsByTime.containsKey(eventTime)) {
			System.out.println("There are already events scheduled at " + eventTime + ".");
			scheduledEvents = this.scheduledEventsByTime.get(eventTime);
			for (int i = 0; i < scheduledEvents.size(); i++) {
				final Event scheduledEvent = scheduledEvents.get(i);
				if (scheduledEvent.getPriority() < event.getPriority()) {
					scheduledEvents.add(i, event);
					System.out.println("Event " + event + " has been scheduled at " + eventTime + " at position " + i
							+ " in the event list for that time.");
				}
			}
			if (!scheduledEvents.contains(event)) {
				scheduledEvents.add(event);
				System.out.println("Event " + event + " has been scheduled at " + eventTime
						+ " at the end of the event list for that time.");
			}
		} else {
			System.out.println("There no events yet scheduled at " + eventTime + ".");
			scheduledEvents = new LinkedList<>();
			scheduledEvents.add(event);
			System.out.println(
					"Event " + event + " has been scheduled at " + eventTime + " in a new event list for that time.");
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
