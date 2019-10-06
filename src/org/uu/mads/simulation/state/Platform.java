package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Platform {
	private final Duration averageTravelTime;
	private final WaitingPoint nextWaitingPoint;
	private final WaitingPoint lastWaitingPoint;
	private LocalTime lastPassengersCalc;
	private final Queue<Passenger> waitingPassengers = new ArrayDeque<>();

	public Platform(final Duration averageTravelTime, final WaitingPoint nextWaitingPoint,
			final WaitingPoint lastWaitingPoint) {
		this.averageTravelTime = averageTravelTime;
		this.nextWaitingPoint = nextWaitingPoint;
		this.lastWaitingPoint = lastWaitingPoint;
	}

	// returns travel duration of this platform to the next platform
	public Duration getTravelTime() {
		// TODO logic to calculate "random" travel time
		return this.averageTravelTime;
	}

	public WaitingPoint getNextWaitingPoint() {
		return this.nextWaitingPoint;
	}

	public WaitingPoint getLastWaitingPoint() {
		return this.lastWaitingPoint;
	}

	public LocalTime getLastPassengersCalc() {
		return this.lastPassengersCalc;
	}

	public void setLastPassengersCalc(final LocalTime lastPassengersCalc) {
		this.lastPassengersCalc = lastPassengersCalc;
	}

	public Passenger popFirstWaitingPassenger() {
		return this.waitingPassengers.poll();
	}

	public List<Passenger> popFirstWaitingPassengers(final int numberOfWaitingPassengers) {
		final List<Passenger> passengers = new ArrayList<>();
		for (int i = 0; i < numberOfWaitingPassengers; i++) {
			passengers.add(this.waitingPassengers.poll());
		}
		return passengers;
	}

	public Queue<Passenger> addWaitingPassenger(final Passenger waitingPassenger) {
		this.waitingPassengers.add(waitingPassenger);
		return this.waitingPassengers;
	}

	public Queue<Passenger> addWaitingPassengers(final Set<Passenger> waitingPassengers) {
		for (final Passenger waitingPassenger : waitingPassengers) {
			addWaitingPassenger(waitingPassenger);
		}
		return this.waitingPassengers;
	}

	@Override
	public String toString() {
		return "Platform [averageTravelTime=" + this.averageTravelTime + ", nextWaitingPoint=" + this.nextWaitingPoint
				+ ", lastPassengersCalc=" + this.lastPassengersCalc + ", waitingPassengers=" + this.waitingPassengers
				+ "]";
	}

}