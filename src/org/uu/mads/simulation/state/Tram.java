package org.uu.mads.simulation.state;

import java.time.Duration;

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

}