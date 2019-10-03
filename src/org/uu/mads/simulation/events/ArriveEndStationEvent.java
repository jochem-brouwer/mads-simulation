package org.uu.mads.simulation.events;

import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Tram;

public class ArriveEndStationEvent extends Event {
	private final EndStation endStation;
	private final Tram tram;

	public ArriveEndStationEvent(final EndStation endStation, final Tram tram) {
		super();
		this.endStation = endStation;
		this.tram = tram;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "ArriveEndStationEvent [endStation=" + this.endStation + ", tram=" + this.tram + "]";
	}

}