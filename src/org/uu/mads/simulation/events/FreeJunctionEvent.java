package org.uu.mads.simulation.events;

import java.util.Objects;

import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;

public class FreeJunctionEvent extends Event {
	private final EndStation endStation;
	private final Tram tram;

	public FreeJunctionEvent(final EndStation endStation, final Tram tram) {
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
		Objects.requireNonNull(this.tram, "Given tram must not be null!");

		final Junction junction = this.endStation.getJunction();

		if (this.tram.equals(junction.getTramOnLaneInA())) {
			junction.removeTramOnLaneInA();
			if (this.endStation.getTramOnPlatformA() != null) {
				throw new IllegalStateException(
						"There is already a tram on platform A while trying to let a new one arrive from the junction.");
			}
			this.endStation.setTramOnPlatformA(this.tram);
		} else if (this.tram.equals(junction.getTramOnLaneInB())) {
			junction.removeTramOnLaneInB();
			if (this.endStation.getTramOnPlatformB() != null) {
				throw new IllegalStateException(
						"There is already a tram on platform B while trying to let a new one arrive from the junction.");
			}
			this.endStation.setTramOnPlatformB(this.tram);
		} else if (this.tram.equals(junction.getTramOnLaneOutA())) {
			junction.removeTramOnLaneOutA();

		} else if (this.tram.equals(junction.getTramOnLaneOutB())) {
			junction.removeTramOnLaneOutB();
			new ArriveWaitingPointEvent(this.endStation.getNextWaitingPoint(), this.tram);
		} else {
			throw new IllegalArgumentException("The given tram is not in the junction.");
		}
	}

	@Override
	public String toString() {
		return "FreeJunctionEvent [endStation=" + this.endStation + ", tram=" + this.tram + "]";
	}

}