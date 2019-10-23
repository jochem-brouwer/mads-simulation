package org.uu.mads.simulation.state;

import java.time.LocalTime;
import java.util.Objects;

public class Tram {
	public static final int CAPACITY = 420; // Is fixed.

	private final int id;
	private int numOfPassengers;

	private LocalTime junctionArrivalTime;
	private LocalTime junctionEnteringTime;

	public LocalTime getJunctionArrivalTime() {
		return this.junctionArrivalTime;
	}

	public LocalTime getJunctionEnteringTime() {
		return this.junctionEnteringTime;
	}

	public void setJunctionArrivalTime(final LocalTime time) {
		this.junctionArrivalTime = time;
	}

	public void setJunctionEnteringTime(final LocalTime time) {
		this.junctionEnteringTime = time;
	}

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

	/**
	 * Calculates the number of passengers entering the tram and adds them to the
	 * tram.
	 */
	public int loadPassengers(final Platform platform) {
		final int remainingCapacity = getRemainingCapacity();
		final int noOfWaitingPassengers = platform.getNoOfWaitingPassengers();

		int passengersIn;
		if (remainingCapacity >= noOfWaitingPassengers) {
			platform.popFirstWaitingPassengers(noOfWaitingPassengers);
			passengersIn = noOfWaitingPassengers;
		} else {
			platform.popFirstWaitingPassengers(remainingCapacity);
			passengersIn = remainingCapacity;
		}

		this.numOfPassengers += passengersIn;

		return passengersIn;
	}

	/**
	 * Calculates the number of passengers leaving the tram and removes them from
	 * the tram.
	 */
	public int dumpPassengers() {
		final double dumpingPercentage = 0.5; // TODO: This percentage is calculated using the input analysis?
		final int passengersOut = (int) (Math.round(this.numOfPassengers * dumpingPercentage));
		this.numOfPassengers = this.numOfPassengers - passengersOut;

		return passengersOut;
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