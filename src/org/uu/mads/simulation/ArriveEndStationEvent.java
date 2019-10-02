package org.uu.mads.simulation;

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

}