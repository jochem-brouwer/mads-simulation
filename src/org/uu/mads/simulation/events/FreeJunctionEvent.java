package org.uu.mads.simulation.events;

import org.uu.mads.simulation.state.Junction;

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
	public void fire() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "FreeJunctionEvent [junction=" + this.junction + "]";
	}

}