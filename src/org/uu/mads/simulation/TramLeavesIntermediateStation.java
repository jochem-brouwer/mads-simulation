package org.uu.mads.simulation;

public class TramLeavesIntermediateStation extends Event {
	private final IntPlatform intPlatform;
	private final Tram tram;

	public TramLeavesIntermediateStation(final IntPlatform intPlatform, final Tram tram) {
		super();
		this.intPlatform = intPlatform;
		this.tram = tram;
	}

	public IntPlatform getIntPlatform() {
		return this.intPlatform;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}

}