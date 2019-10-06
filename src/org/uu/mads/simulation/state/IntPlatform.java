package org.uu.mads.simulation.state;

import java.time.Duration;

public class IntPlatform extends Platform {
	private Tram tram;
	private boolean isOccupied;

	public IntPlatform(final Duration averageTravelTime, final WaitingPoint nextWaitingPoint,
			final WaitingPoint lastWaitingPoint) {
		super(averageTravelTime, nextWaitingPoint, lastWaitingPoint);
	}

	public boolean isOccupied() {
		return this.isOccupied;
	}

	public void setOccupied() {
		this.isOccupied = true;
	}

	public void setUnoccupied() {
		this.isOccupied = false;
	}

	@Override
	// TODO: Update with inherited values?
	public String toString() {
		return "IntPlatform [tram=" + this.tram + ", isOccupied=" + this.isOccupied + "]";
	}
}