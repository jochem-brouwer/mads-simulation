package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.uu.mads.simulation.events.Event;

public class EventScheduler {
	private static EventScheduler instance = null;

	private final SortedMap<LocalTime, List<Event>> scheduledEventsByTime = new TreeMap<>();
	private LocalTime currentTime = null;

	private EventScheduler() {
		// private constructor because this is a singleton
	}

	public static EventScheduler get() {
		if (instance == null) {
			instance = new EventScheduler();
		}
		return instance;
	}

	public LocalTime getCurrentTime() {
		return this.currentTime;
	}

	public SortedMap<LocalTime, List<Event>> getScheduledEventsByTime() {
		return this.scheduledEventsByTime;
	}

	public void fireNextEvent() {
		this.currentTime = this.scheduledEventsByTime.firstKey();
		final List<Event> eventsForCurrentTime = this.scheduledEventsByTime.get(this.currentTime);
		final Event nextEvent = eventsForCurrentTime.get(0);
		eventsForCurrentTime.remove(0);

		if (eventsForCurrentTime.isEmpty()) {
			// No more events for current time
			this.scheduledEventsByTime.remove(this.currentTime);
		} else {
			// Additional events at current time (multiple events at the same time)
			this.scheduledEventsByTime.put(this.currentTime, eventsForCurrentTime);
		}

		nextEvent.fire();
		System.out.println("Event " + nextEvent + " scheduled at time " + this.currentTime + " has been fired.");
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

}
