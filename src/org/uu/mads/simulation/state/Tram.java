package org.uu.mads.simulation.state;

import java.time.Duration;
import java.util.Objects;

public class Tram {
	public static final int CAPACITY = 400; // TODO: change

	private final int id;
	private final int numOfPassengers;

	public Tram(final int id, final int numOfPassengers) {
		super();
		this.id = id;
		this.numOfPassengers = numOfPassengers;
	}

	public int getId() {
		return this.id;
	}

	public int getNumOfPassengers() {
		return this.numOfPassengers;
	}

	// loads/unloads passengers on a platform and returns the dwell time of the tram
	public Duration loadPassengers(final IntPlatform platform) {
		// this.numOfPassengers = this.numOfPassengers; // TODO: WTF?

		return Duration.ZERO; // TODO change this to actual dwell time (random)
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