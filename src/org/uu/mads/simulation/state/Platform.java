package org.uu.mads.simulation.state;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Performance;
import org.uu.mads.simulation.Simulation;

public class Platform {
	private static final double STANDARD_DEVIATION_LOG = 0.3588221;

	private final String name;
	private final Duration avgTravelTimeToNextPlatf;
	private final LogNormalDistribution travelTimeToNexcPlatfDist;
	private LocalTime lastPassengersCalc;

	private WaitingPoint nextWaitingPoint;
	private WaitingPoint lastWaitingPoint;
	private final Queue<Passenger> waitingPassengers = new ArrayDeque<>();

	public Platform(final String name, final Duration averageTravelTime) {
		this.name = name;
		this.avgTravelTimeToNextPlatf = averageTravelTime;
		this.travelTimeToNexcPlatfDist = new LogNormalDistribution(Math.log(averageTravelTime.toSeconds()),
				STANDARD_DEVIATION_LOG);
		this.lastPassengersCalc = Simulation.FIRST_PASSENGER_CALC;
	}

	public String getName() {
		return this.name;
	}

	public Duration getAverageTravelTime() {
		return this.avgTravelTimeToNextPlatf;
	}

	/**
	 * @return travel duration of this platform to the next platform
	 */
	public Duration getTravelTimeToNextPlatform() {
		final Duration travelTimeToNextPlatf = Duration.ofSeconds((long) this.travelTimeToNexcPlatfDist.sample());
		if (Simulation.LOG_VERBOSE) {
			System.out.println("Platform " + this.name + ": Travel time to next platform is "
					+ travelTimeToNextPlatf.toSeconds() + " seconds.");
		}
		return travelTimeToNextPlatf;
	}

	public WaitingPoint getNextWaitingPoint() {
		return this.nextWaitingPoint;
	}

	public void setNextWaitingPoint(final WaitingPoint nextWaitingPoint) {
		this.nextWaitingPoint = nextWaitingPoint;
	}

	public WaitingPoint getLastWaitingPoint() {
		return this.lastWaitingPoint;
	}

	public void setLastWaitingPoint(final WaitingPoint lastWaitingPoint) {
		this.lastWaitingPoint = lastWaitingPoint;
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

	public void addWaitingPassenger(final Passenger waitingPassenger) {
		this.waitingPassengers.add(waitingPassenger);
	}

	public void addWaitingPassengers(final Set<Passenger> waitingPassengers) {
		for (final Passenger waitingPassenger : waitingPassengers) {
			addWaitingPassenger(waitingPassenger);
		}
	}

	public void calculatePassengers() {
		final double rate = EventScheduler.get().getPassengerRate();
		final long passedTime = (SECONDS.between(this.lastPassengersCalc, EventScheduler.get().getCurrentTime()));
		final long numberOfPassengers = (int) (passedTime * rate);
		System.out.println("Number of passengers on Platform :" + numberOfPassengers);

		final Random random = new Random();

		// We generate a new passenger with its own arrival time. The passengers are
		// stored in a list that can be
		// accessed when a tram leaves a station.
		for (int i = 0; i < passedTime; i++) {
			final int randomInt = random.nextInt(100);
			if (rate > (randomInt / 100)) { // TODO depends on our poisson rate
				final Passenger passenger = new Passenger(this.lastPassengersCalc.plus(i, SECONDS), this);
				addWaitingPassenger(passenger);
				final Duration waitingTime = Duration.between(this.lastPassengersCalc.plus(i, SECONDS),
						EventScheduler.get().getCurrentTime());
				Performance.get().addPassenger(waitingTime);
			}
		}
		this.lastPassengersCalc = EventScheduler.get().getCurrentTime();
	}

	public int loadPassengers(final Platform platform, final Tram tram) {

		int remainingCapacity = tram.getRemainingCapacity();
		int numOfPassengers = tram.getNumOfPassengers();
		int passengersIn = 0;

		// We iterate through the list of passengers waiting at the platform
		for (int i = 0; i < this.waitingPassengers.size(); i++) {

			// If we still have capacity, we add the passenger to the tram and delete it
			// from the platformList.
			// Else, we break the loop.
			if (remainingCapacity != 0) {
				this.waitingPassengers.remove();
				remainingCapacity--;
				numOfPassengers++;
				passengersIn++;
			} else {
				break;
			}
		}
		// We set the number of passengers to the tram.
		tram.setNumOfPassengers(numOfPassengers);
		return passengersIn;
	}

	public int dumpPassengers(final Tram tram) {
		// This functions calculates the number of passengers leaving the tram and
		// removes them from the tram number.
		final double dumpingPercentage = 0.5; // TODO: This percentage is calculated using the input analysis?
		final int numOfPassengers = tram.getNumOfPassengers();
		final int passengersOut = (int) (numOfPassengers * dumpingPercentage);
		tram.setNumOfPassengers(numOfPassengers - passengersOut);

		return passengersOut;
	}

	@Override
	public String toString() {
		return "Platform [name=" + this.name + ", averageTravelTime=" + this.avgTravelTimeToNextPlatf
				+ ", nextWaitingPoint=" + this.nextWaitingPoint + ", lastWaitingPoint=" + this.lastWaitingPoint
				+ ", lastPassengersCalc=" + this.lastPassengersCalc + ", waitingPassengers=" + this.waitingPassengers
				+ "]";
	}

}