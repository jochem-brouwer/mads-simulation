package org.uu.mads.simulation;

public class TryOccupyJunctionEvent extends Event {
	private final EndStation endStation;
	private final WaitingPointJunction waitingPointJunction;
	private final Junction junction;

	public TryOccupyJunctionEvent(final EndStation endStation, final WaitingPointJunction waitingPointJunction,
			final Junction junction) {
		super();
		this.endStation = endStation;
		this.waitingPointJunction = waitingPointJunction;
		this.junction = junction;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	public WaitingPointJunction getWaitingPointJunction() {
		return this.waitingPointJunction;
	}

	public Junction getJunction() {
		return this.junction;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}

}