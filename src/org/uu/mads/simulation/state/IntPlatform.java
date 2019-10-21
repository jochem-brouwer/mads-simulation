package org.uu.mads.simulation.state;

import java.time.Duration;
import java.util.Objects;

public class IntPlatform extends Platform {
	private Tram tram;
	private boolean occupied = false;

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

	public void arriveTram(final Tram tram) {
		Objects.requireNonNull(tram, "Given tram must not be null.");
		if (this.tram != null) {
			throw new IllegalStateException("There is already a tram on platform " + getName() + ".");
		}
		this.tram = tram;
		this.occupied = true;
	}

	public Tram getTram() {
		return this.tram;
	}

	public boolean isOccupied() {
		return this.occupied;
	}

	public void setUnoccupied() {
		this.occupied = false;
	}

	@Override
	public String toString() {
		return "IntPlatform [tram=" + this.tram + ", isOccupied=" + this.occupied + "]";
	}
}