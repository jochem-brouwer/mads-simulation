package org.uu.mads.simulation.state;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.SECONDS;

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
		this.lastPassengersCalc = Simulation.FIRST_PASSENGER_CALC;
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

	public void calculatePassengers() {
		double rate = EventScheduler.get().getPassengerRate();
		long passedTime = (SECONDS.between(EventScheduler.get().getCurrentTime(), this.lastPassengersCalc));
		long numberOfPassengers = (int)(passedTime * rate);
		System.out.println("Number of passengers on Platform :" + numberOfPassengers);

		Random random = new Random();

		// We generate a new passenger with its own arrival time. The passengers are stored in a list that can be
		// accessed when a tram leaves a station.
		for (int i = 0; i < passedTime; i++) {
			int randomInt = random.nextInt(100);
			if (rate > (randomInt / 100)) { // TODO depends on our poisson rate
				Passenger passenger = new Passenger(this.lastPassengersCalc.plus(i, SECONDS), this);
				addWaitingPassenger(passenger);
			}
		}
		this.lastPassengersCalc = EventScheduler.get().getCurrentTime();
	}

	public int loadPassengers(Platform platform, Tram tram) {

		int remainingCapacity = tram.getRemainingCapacity();
		int numOfPassengers = tram.getNumOfPassengers();
		int passengersIn = 0;

		// We iterate through the list of passengers waiting at the platform
		for (int i = 0; i < this.waitingPassengers.size(); i++) {

			// If we still have capacity, we add the passenger to the tram and delete it from the platformList.
			// Else, we break the loop.
			if (remainingCapacity != 0) {
				this.waitingPassengers.remove();
				remainingCapacity--;
				numOfPassengers++;
				passengersIn++;
			} else { break; }
		}
		// We set the number of passengers to the tram.
		tram.setNumOfPassengers(numOfPassengers);
		return passengersIn;
	}

	public int dumpPassengers(Tram tram) {
		// This functions calculates the number of passengers leaving the tram and removes them from the tram number.
		double dumpingPercentage = 0.5; // TODO: This percentage is calculated using the input analysis?
		int numOfPassengers = tram.getNumOfPassengers();
		int passengersOut = (int)(numOfPassengers * dumpingPercentage);
		tram.setNumOfPassengers(numOfPassengers - passengersOut);
		return passengersOut;
	}

	@Override
	public String toString() {
		return "Platform [averageTravelTime=" + this.averageTravelTime + ", nextWaitingPoint=" + this.nextWaitingPoint
				+ ", lastPassengersCalc=" + this.lastPassengersCalc + ", waitingPassengers=" + this.waitingPassengers
				+ "]";
	}

}