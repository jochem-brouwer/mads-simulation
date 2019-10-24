package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.math3.distribution.PoissonDistribution;
import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.PerformanceTracker;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.input.PassengersOutReader;

public class Tram {
	public static final int CAPACITY = 420; // Blaze it!
	private static final int POISSON_RATE_INTERVAL_MIN = 15;
	private static final int POISSON_RATE_INTERVAL_SEC = POISSON_RATE_INTERVAL_MIN * 60;

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

		List<Passenger> passengers;

		if (remainingCapacity >= noOfWaitingPassengers) {
			passengers = platform.popFirstWaitingPassengers(noOfWaitingPassengers);
			passengersIn = noOfWaitingPassengers;
		} else {
			passengers = platform.popFirstWaitingPassengers(remainingCapacity);
			passengersIn = remainingCapacity;
		}

		for (Passenger passenger:passengers) {
			passenger.setLeaveTimePlatform(EventScheduler.getInstance().getCurrentTime());
			PerformanceTracker.addPassenger(passenger.getWaitingTime());
		}

		this.numOfPassengers += passengersIn;

		return passengersIn;
	}

	/**
	 * Calculates the number of passengers leaving the tram and removes them from
	 * the tram.
	 */
	public int dumpPassengers(final Platform platform) {
		final double rate;
		if ((platform instanceof EndStation)) {
			rate = 1;
		} else {
			final Map<LocalTime, Double> ratesByTime;
			
			if (Simulation.ARTIFICIAL_DATA) {
				ratesByTime	= PassengersOutReader.getInstance()
						.getRatesByTimeForPlatform(platform.getName(), Simulation.CSV_PATH_POISS_PASS_IN_ART1);				
			} else {
				ratesByTime	= PassengersOutReader.getInstance()
						.getRatesByTimeForPlatform(platform.getName(), Simulation.CSV_PATH_POISS_PASS_OUT);
			}

			final LocalTime currentTime = EventScheduler.getInstance().getCurrentTime();
			final LocalTime currentInterval = LocalTime.ofSecondOfDay(
					currentTime.toSecondOfDay() - (currentTime.toSecondOfDay() % POISSON_RATE_INTERVAL_SEC));

			rate = ratesByTime.get(currentInterval);
		}

		final int dumpPercentage;
		if (rate <= 0) {
			dumpPercentage = 0;
		} else {
			final PoissonDistribution poissonDistribution = new PoissonDistribution(rate);
			// should always be between and 1
			dumpPercentage = Math.min(Math.max(poissonDistribution.sample(), 0), 1);
		}

		final int passengersOut = (Math.round(this.numOfPassengers * dumpPercentage));
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