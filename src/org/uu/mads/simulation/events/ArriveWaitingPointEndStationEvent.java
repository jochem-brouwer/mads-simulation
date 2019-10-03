package org.uu.mads.simulation.events;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPointJunction;

public class ArriveWaitingPointEndStationEvent extends Event {
	private final WaitingPointJunction waitingPointJunction;
	private final Tram tram;

	public ArriveWaitingPointEndStationEvent(final WaitingPointJunction waitingPointJunction, final Tram tram) {
		super();
		this.waitingPointJunction = waitingPointJunction;
		this.tram = tram;
	}

	public WaitingPointJunction getWaitingPointJunction() {
		return this.waitingPointJunction;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire(final EventScheduler scheduler) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		return "ArriveWaitingPointEndStationEvent [waitingPointJunction=" + this.waitingPointJunction + ", tram="
				+ this.tram + "]";
	}
}