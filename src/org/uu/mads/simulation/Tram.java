package org.uu.mads.simulation;

import java.time.Duration;

public class Tram {
	public static final int CAPACITY = 400; // TODO: change

	private final int id;
	private final int numOfPassengers;
	private final Tram previousTram;

	public Tram(final int id, final int numOfPassengers, final Tram previousTram) {
		super();
		this.id = id;
		this.numOfPassengers = numOfPassengers;
		this.previousTram = previousTram;
	}

	public int getId() {
		return this.id;
	}

	public int getNumOfPassengers() {
		return this.numOfPassengers;
	}

	public Tram getPreviousTram() {
		return this.previousTram;
	}

	// loads/unloads passengers on a platform and returns the dwell time of the tram
	public Duration loadPassengers(final IntPlatform platform) {
		this.numOfPassengers = this.numOfPassengers; // TODO: WTF?

		return 12.5; // todo change this to actual dwell time (random)
	}

}