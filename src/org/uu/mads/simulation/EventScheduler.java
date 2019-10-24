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
	private LocalTime currentTime = Simulation.SIMULATION_START_TIME;

	private final double passengerRate = 0.1;

	private EventScheduler() {
		// private constructor because this is a singleton
	}

	public static EventScheduler getInstance() {
		if (instance == null) {
			instance = new EventScheduler();
		}
		return instance;
	}

	public static void reset() {
		instance = null;
	}

	public double getPassengerRate() {
		return this.passengerRate;
	}

	public LocalTime getCurrentTime() {
		return this.currentTime;
	}

	public SortedMap<LocalTime, List<Event>> getScheduledEventsByTime() {
		return this.scheduledEventsByTime;
	}

	/**
	 * @return true if next event time is smaller than simulation end time and will
	 *         be executed, false otherwise
	 */
	public boolean fireNextEvent() {
		this.currentTime = this.scheduledEventsByTime.firstKey();

		if (!this.currentTime.isAfter(Simulation.SIMULATION_END_TIME)) {
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
			Simulation.logVerbose("Event " + nextEvent + " scheduled at time " + this.currentTime + " has been fired.");
			return true;
		} else {
			Simulation.log("Next event would be after simulation end time. Stopping simualion.");
			return false;
		}
	}

	public void scheduleEvent(final Event event, final LocalTime eventTime) {
		Simulation.logVerbose("Event " + event + " is to be scheduled at " + eventTime + ".");

		final List<Event> scheduledEvents;
		if (this.scheduledEventsByTime.containsKey(eventTime)) {
			Simulation.logVerbose("There are already events scheduled at " + eventTime + ".");
			scheduledEvents = this.scheduledEventsByTime.get(eventTime);
			for (int i = 0; i < scheduledEvents.size(); i++) {
				final Event scheduledEvent = scheduledEvents.get(i);
				if (scheduledEvent.getPriority() < event.getPriority()) {
					scheduledEvents.add(i, event);
					Simulation.logVerbose("Event " + event + " has been scheduled at " + eventTime + " at position " + i
							+ " in the event list for that time.");
					break;
				}
			}
			if (!scheduledEvents.contains(event)) {
				scheduledEvents.add(event);
				Simulation.logVerbose("Event " + event + " has been scheduled at " + eventTime
						+ " at the end of the event list for that time.");
			}
		} else {
			Simulation.logVerbose("There are no events scheduled at " + eventTime + " yet.");
			scheduledEvents = new LinkedList<>();
			scheduledEvents.add(event);
			Simulation.logVerbose(
					"Event " + event + " has been scheduled at " + eventTime + " in a new event list for that time.");
		}
		this.scheduledEventsByTime.put(eventTime, scheduledEvents);
	}

	public void scheduleEventAhead(final Event event, final Duration duration) {
		final LocalTime eventTime = this.currentTime.plus(duration);
		scheduleEvent(event, eventTime);
	}

}
