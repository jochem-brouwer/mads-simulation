package org.uu.mads.simulation.events;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.State.Junction;

public class FreeJunctionEvent extends Event {
	private final Junction junction;

	public FreeJunctionEvent(final Junction junction) {
		super();
		this.junction = junction;
	}

	public Junction getJunction() {
		return this.junction;
	}

	@Override
	public void fire(final EventScheduler scheduler) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "FreeJunctionEvent [junction=" + this.junction + "]";
	}

}