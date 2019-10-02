package org.uu.mads.simulation;

public class FreeJunctionEvent extends Event {
	private final Junction junction;
	private final Tram tram;

	public FreeJunctionEvent(final Junction junction, final Tram tram) {
		super();
		this.junction = junction;
		this.tram = tram;
	}

	public Junction getJunction() {
		return this.junction;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		// TODO Auto-generated method stub

	}

}