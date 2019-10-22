package org.uu.mads.simulation.state;

import java.time.LocalTime;
import java.util.Objects;

public class Tram {
	public static final int CAPACITY = 420; // Is fixed.

	private final int id;
	private int numOfPassengers;

	private LocalTime JunctionArrivalTime;
	private LocalTime JunctionEnteringTime;

	public LocalTime getJunctionArrivalTime() { return this.JunctionArrivalTime; }
	public LocalTime getJunctionEnteringTime() { return this.JunctionEnteringTime; }

	public void setJunctionArrivalTime(LocalTime time) { this.JunctionArrivalTime = time;}
	public void setJunctionEnteringTime(LocalTime time) { this.JunctionEnteringTime = time; }

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