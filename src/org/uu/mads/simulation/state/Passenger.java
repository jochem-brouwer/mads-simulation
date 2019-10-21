package org.uu.mads.simulation.state;

import java.time.LocalTime;
import java.util.Objects;

public class Passenger {

	private final LocalTime arrivalTimePlatform;
	private LocalTime leaveTimePlatform;
	private final Platform platform;

	public Passenger(final LocalTime arrivalTimePlatform, final Platform platform) {
		super();

		Objects.requireNonNull(arrivalTimePlatform, "Given arrivalTimePlatform must not be null!");
		Objects.requireNonNull(platform, "Given platform must not be null!");

		this.arrivalTimePlatform = arrivalTimePlatform;
		this.platform = platform;
	}

	@Override
	public String toString() {
		return "Passenger [arrivalTimePlatform=" + this.arrivalTimePlatform + "]";
	}
}