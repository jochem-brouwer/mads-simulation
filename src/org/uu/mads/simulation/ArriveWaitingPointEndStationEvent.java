package org.uu.mads.simulation;

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
	public void fire() {
		// TODO Auto-generated method stub

	}
}