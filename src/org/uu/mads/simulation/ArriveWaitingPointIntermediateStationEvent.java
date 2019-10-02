package org.uu.mads.simulation;

public class ArriveWaitingPointIntermediateStationEvent extends Event {
	private final WaitingPointInt waitingPointInt;
	private final Tram tram;

	public ArriveWaitingPointIntermediateStationEvent(final WaitingPointInt waitingPointInt, final Tram tram) {
		super();
		this.waitingPointInt = waitingPointInt;
		this.tram = tram;
	}

	public WaitingPointInt getWaitingPointInt() {
		return this.waitingPointInt;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}
}