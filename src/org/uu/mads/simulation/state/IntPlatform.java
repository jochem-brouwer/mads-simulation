package org.uu.mads.simulation.state;

import java.time.Duration;

public class IntPlatform extends Platform {
	private Tram tram;
	private boolean isOccupied = false;

	public IntPlatform(final String name, final Duration averageTravelTime) {
		super(name, averageTravelTime);
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

	public boolean isPlatformOccupied() {
		return this.isOccupied;
	}
}