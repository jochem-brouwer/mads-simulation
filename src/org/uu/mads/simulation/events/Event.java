package org.uu.mads.simulation.events;

public abstract class Event {
	private final int priority;

	public Event() {
		this.priority = 0;
	}

	public Event(final int priority) {
		this.priority = priority;
	}

	public abstract void fire();

	public int getPriority() {
		return this.priority;
	}

}