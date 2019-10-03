package org.uu.mads.simulation.events;

import org.uu.mads.simulation.EventScheduler;

public abstract class Event {
	private final int priority;

	public Event() {
		this.priority = 0;
	}

	public Event(final int priority) {
		this.priority = priority;
	}

	public abstract void fire(EventScheduler scheduler);

	public int getPriority() {
		return this.priority;
	}

}