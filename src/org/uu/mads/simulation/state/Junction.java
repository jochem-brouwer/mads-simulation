package org.uu.mads.simulation.state;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;

import java.time.LocalTime;

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
		Simulation.log("Tram " + tramOnLaneInA.getId() + " occupies junction track In A at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneInA = tramOnLaneInA;
	}

	public void removeTramOnLaneInA() {
		Simulation.log("Tram " + this.tramOnLaneInA.getId() + " leaves junction track In A at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneInA = null;
	}

	public Tram getTramOnLaneInB() {
		return this.tramOnLaneInB;
	}

	public void setTramOnLaneInB(final Tram tramOnLaneInB) {
		Simulation.log("Tram " + tramOnLaneInB.getId() + " occupies junction track In B at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneInB = tramOnLaneInB;
	}

	public void removeTramOnLaneInB() {
		Simulation.log("Tram " + this.tramOnLaneInB.getId() + " leaves junction track In B at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneInB = null;
	}

	public Tram getTramOnLaneOutA() {
		return this.tramOnLaneOutA;
	}

	public void setTramOnLaneOutA(final Tram tramOnLaneOutA) {
		Simulation.log("Tram " + tramOnLaneOutA.getId() + " occupies junction track Out A at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneOutA = tramOnLaneOutA;
	}

	public void removeTramOnLaneOutA() {
		Simulation.log("Tram " + this.tramOnLaneOutA.getId() + " leaves junction track Out A at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneOutA = null;
	}

	public Tram getTramOnLaneOutB() {
		return this.tramOnLaneOutB;
	}

	public void setTramOnLaneOutB(final Tram tramOnLaneOutB) {
		Simulation.log("Tram " + tramOnLaneOutB.getId() + " occupies junction track Out B at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneOutB = tramOnLaneOutB;
	}

	public void removeTramOnLaneOutB() {
		Simulation.log("Tram " + this.tramOnLaneOutB.getId() + " leaves junction track Out B at " + EventScheduler.getInstance().getCurrentTime());
		this.tramOnLaneOutB = null;
	}

	public Boolean isJunctionUsed() {
		return (this.tramOnLaneInA != null) || (this.tramOnLaneInB != null) || (this.tramOnLaneOutA != null)
				|| (this.tramOnLaneOutB != null);
	}

	/**
	 * @return true if lane A In can be used. This means the following lanes are
	 *         unoccupied: Lane A In Lane A Out Lane B In
	 */
	public Boolean canUseLaneInA() {
		return ((this.tramOnLaneInA == null) && (this.tramOnLaneInB == null) && (this.tramOnLaneOutA == null));
	}

	/**
	 * @return true if lane B Out can be used. This means the following lanes are
	 *         unoccupied: Lane A Out Lane B In Lane B Out
	 */
	public Boolean canUseLaneOutB() {
		return ((this.tramOnLaneInB == null) && (this.tramOnLaneOutA == null) && (this.tramOnLaneOutB == null));
	}

	@Override
	public String toString() {
		return "Junction [tramOnLaneInA=" + this.tramOnLaneInA + ", tramOnLaneInB=" + this.tramOnLaneInB
				+ ", tramOnLaneOutA=" + this.tramOnLaneOutA + ", tramOnLaneOutB=" + this.tramOnLaneOutB
				+ ", isJunctionUsed=" + "]";
	}

}