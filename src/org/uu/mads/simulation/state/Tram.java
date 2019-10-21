package org.uu.mads.simulation.state;

import java.time.Duration;
import java.util.Objects;

public class Tram {
	public static final int CAPACITY = 420; // Is fixed.

	private final int id;
	private int numOfPassengers;

	public Tram(final int id, final int numOfPassengers) {
		super();
		this.id = id;
		this.numOfPassengers = numOfPassengers;
	}

	public void setNumOfPassengers(final int passengers) {
		this.numOfPassengers = passengers;
	}

	public int getId() {
		return this.id;
	}

	public int getRemainingCapacity() {
		return (CAPACITY - this.numOfPassengers);
	}

	public int getNumOfPassengers() {
		return this.numOfPassengers;
	}

	// loads/unloads passengers on a platform and returns the dwell time of the tram
	public static Duration calculateDwellTime(final int passengersIn, final int passengersOut) {
		final Duration dwellTime = Duration.ofSeconds((long) (12.5 + (0.22 * passengersIn) + (0.13 * passengersOut)));
		return dwellTime; // TODO add a stochastic distribution.
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.numOfPassengers);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tram)) {
			return false;
		}
		final Tram other = (Tram) obj;
		return (this.id == other.id) && (this.numOfPassengers == other.numOfPassengers);
	}

	@Override
	public String toString() {
		return "Tram [id=" + this.id + ", numOfPassengers=" + this.numOfPassengers + "]";
	}

}