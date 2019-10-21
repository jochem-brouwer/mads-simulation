package org.uu.mads.simulation.state;

import java.time.Duration;

public class IntPlatform extends Platform {
	private Tram tram;
	private boolean isOccupied = false;

	public IntPlatform(final String name, final Duration averageTravelTime) {
		super(name, averageTravelTime);
	}

	public Tram departTram() {
		if (this.tram == null) {
			throw new IllegalStateException("There is no tram on platform " + getName() + " that could depart.");
		}
		final Tram departingTram = this.tram;
		this.tram = null;
		return departingTram;
	}

	public Tram getTram() {
		return this.tram;
	}

	public void setTram(final Tram tram) {
		this.tram = tram;
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
	public String toString() {
		return "IntPlatform [tram=" + this.tram + ", isOccupied=" + this.isOccupied + "]";
	}
}