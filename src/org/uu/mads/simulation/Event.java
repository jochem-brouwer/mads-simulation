package org.uu.mads.simulation;

public abstract class Event {
	private int priority;

	 public void fire(EventScheduler eventScheduler);

}