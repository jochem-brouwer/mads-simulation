package org.uu.mads.simulation.state;

public class Junction {
	private Tram tramOnLaneInA;
	private Tram tramOnLaneInB;
	private Tram tramOnLaneOutA;
	private Tram tramOnLaneOutB;
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

	public Tram getTramOnLaneOutA() {
		return this.tramOnLaneOutA;
	}

	public void setTramOnLaneOutA(final Tram tramOnLaneOutA) {
		this.tramOnLaneOutA = tramOnLaneOutA;
	}

	public Tram getTramOnLaneOutB() {
		return this.tramOnLaneOutB;
	}

	public void setTramOnLaneOutB(final Tram tramOnLaneOutB) {
		this.tramOnLaneOutB = tramOnLaneOutB;
	}

	public Boolean isJunctionUsed() {
		return this.isJunctionUsed;
	}

	public void setJunctionUsed(final Boolean isJunctionUsed) {
		this.isJunctionUsed = isJunctionUsed;
	}

}