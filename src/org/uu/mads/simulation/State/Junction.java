package org.uu.mads.simulation.State;

public class Junction {
	private Tram tramOnLaneInA;
	private Tram tramOnLaneInB;
	private Tram tramOnLaneAOut;
	private Tram tramOnLaneBOut;
	private Boolean isJunctionUsed = false;

	public Junction() {
		super();
	}

	public Tram getTramOnLaneInA() {
		return this.tramOnLaneInA;
	}

	public void setTramOnLaneInA(final Tram tramOnLaneInA) {
		this.tramOnLaneInA = tramOnLaneInA;
	}

	public Tram getTramOnLaneInB() {
		return this.tramOnLaneInB;
	}

	public void setTramOnLaneInB(final Tram tramOnLaneInB) {
		this.tramOnLaneInB = tramOnLaneInB;
	}

	public Tram getTramOnLaneAOut() {
		return this.tramOnLaneAOut;
	}

	public void setTramOnLaneAOut(final Tram tramOnLaneAOut) {
		this.tramOnLaneAOut = tramOnLaneAOut;
	}

	public Tram getTramOnLaneBOut() {
		return this.tramOnLaneBOut;
	}

	public void setTramOnLaneBOut(final Tram tramOnLaneBOut) {
		this.tramOnLaneBOut = tramOnLaneBOut;
	}

	public Boolean isJunctionUsed() {
		return this.isJunctionUsed;
	}

	public void setJunctionUsed(final Boolean isJunctionUsed) {
		this.isJunctionUsed = isJunctionUsed;
	}

}