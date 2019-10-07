package org.uu.mads.simulation.state;

public class Junction {
	private Tram tramOnLaneInA;
	private Tram tramOnLaneInB;
	private Tram tramOnLaneOutA;
	private Tram tramOnLaneOutB;

	public Junction() {
		super();
	}

	public Tram getTramOnLaneInA() {
		return this.tramOnLaneInA;
	}

	public void setTramOnLaneInA(final Tram tramOnLaneInA) {
		this.tramOnLaneInA = tramOnLaneInA;
	}

	public void removeTramOnLaneInA() {
		this.tramOnLaneInA = null;
	}

	public Tram getTramOnLaneInB() {
		return this.tramOnLaneInB;
	}

	public void setTramOnLaneInB(final Tram tramOnLaneInB) {
		this.tramOnLaneInB = tramOnLaneInB;
	}

	public void removeTramOnLaneInB() {
		this.tramOnLaneInB = null;
	}

	public Tram getTramOnLaneOutA() {
		return this.tramOnLaneOutA;
	}

	public void setTramOnLaneOutA(final Tram tramOnLaneOutA) {
		this.tramOnLaneOutA = tramOnLaneOutA;
	}

	public void removeTramOnLaneOutA() {
		this.tramOnLaneOutA = null;
	}

	public Tram getTramOnLaneOutB() {
		return this.tramOnLaneOutB;
	}

	public void setTramOnLaneOutB(final Tram tramOnLaneOutB) {
		this.tramOnLaneOutB = tramOnLaneOutB;
	}

	public void removeTramOnLaneOutB() {
		this.tramOnLaneOutB = null;
	}

	public Boolean isJunctionUsed() {
		return (this.tramOnLaneInA != null) || (this.tramOnLaneInB != null) || (this.tramOnLaneOutA != null)
				|| (this.tramOnLaneOutB != null);
	}
	
	/**
	 * @return returns True if lane A In can be used. This means the following lanes are unoccupied: Lane A In Lane A Out Lane B In
	 */
	public Boolean canUseLaneInA() {
		return (this.tramOnLaneInA == null && this.tramOnLaneInB == null && this.tramOnLaneOutA == null);
	}
	
	/**
	 * @return returns True if lane B Out can be used. This means the following lanes are unoccupied: Lane A Out Lane B In Lane B Out
	 */
	public Boolean canUseLaneOutB() {
		return (this.tramOnLaneInB == null && this.tramOnLaneOutA == null && this.tramOnLaneOutB == null);
	}

	@Override
	public String toString() {
		return "Junction [tramOnLaneInA=" + this.tramOnLaneInA + ", tramOnLaneInB=" + this.tramOnLaneInB
				+ ", tramOnLaneOutA=" + this.tramOnLaneOutA + ", tramOnLaneOutB=" + this.tramOnLaneOutB
				+ ", isJunctionUsed=" + "]";
	}

}