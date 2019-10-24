package org.uu.mads.simulation.state;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.PerformanceTracker;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.input.PassengersInReader;

public class Platform {
	private static final double TRAVEL_TIME_SD_LOG = 0.3588221;
	private static final int POISSON_RATE_INTERVAL_MIN = 15;
	private static final int POISSON_RATE_INTERVAL_SEC = POISSON_RATE_INTERVAL_MIN * 60;

	private final String name;
	private final Duration avgTravelTimeToNextPlatf;
	private final LogNormalDistribution travelTimeToNexcPlatfDist;
	private LocalTime lastPassengersCalc;

	private WaitingPoint nextWaitingPoint;
	private WaitingPoint lastWaitingPoint;
	private final Queue<Passenger> waitingPassengers = new ArrayDeque<>();

	public Platform(final String name, final Duration averageTravelTime) {
		super();

		Objects.requireNonNull(name, "Given name must not be null!");
		Objects.requireNonNull(averageTravelTime, "Given averageTravelTime must not be null!");

		this.name = name;
		this.avgTravelTimeToNextPlatf = averageTravelTime;
		this.travelTimeToNexcPlatfDist = new LogNormalDistribution(Math.log(averageTravelTime.toSeconds()),
				TRAVEL_TIME_SD_LOG);
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
		final Duration travelTimeToNextPlatform;
		if (Simulation.ARTIFICIAL_DRIVING_TIME) {
			final Random rand = new Random();
			final int rng = rand.nextInt(10);

			if (rng < 4) {
				travelTimeToNextPlatform = Duration.ofSeconds((long) (this.avgTravelTimeToNextPlatf.toSeconds() * 0.8));
			} else if (rng < 7) {
				travelTimeToNextPlatform = Duration.ofSeconds(this.avgTravelTimeToNextPlatf.toSeconds() * 1);
			} else if (rng < 9) {
				travelTimeToNextPlatform = Duration.ofSeconds((long) (this.avgTravelTimeToNextPlatf.toSeconds() * 1.2));
			} else {
				travelTimeToNextPlatform = Duration.ofSeconds((long) (this.avgTravelTimeToNextPlatf.toSeconds() * 1.4));
			}
		} else {
			travelTimeToNextPlatform = Duration.ofSeconds((long) this.travelTimeToNexcPlatfDist.sample());
		}
		Simulation.log("Platform " + this.name + ": Travel time to next platform is "
				+ travelTimeToNextPlatform.toSeconds() + " seconds.");
		return travelTimeToNextPlatform;
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

	public int getNoOfWaitingPassengers() {
		return this.waitingPassengers.size();
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

	private void addWaitingPassenger(final Passenger waitingPassenger) {
		this.waitingPassengers.add(waitingPassenger);
	}

	public void calculatePassengers() {
		String path;
		if (Simulation.ARTIFICIAL_DATA) {
			path = Simulation.CSV_PATH_POISS_PASS_IN_ART1;
		} else {
			path = Simulation.CSV_PATH_POISS_PASS_IN;
		}

		final Map<LocalTime, Double> passengerInRatesByTime = PassengersInReader.getInstance()
				.getRatesByTimeForPlatform(this.name, path);

		final LocalTime currentTime = EventScheduler.getInstance().getCurrentTime();

		int passArrSinceLastCalc = 0;
		if (currentTime.isAfter(this.lastPassengersCalc)) {
			LocalTime currentInterval = LocalTime.ofSecondOfDay(this.lastPassengersCalc.toSecondOfDay()
					- (this.lastPassengersCalc.toSecondOfDay() % POISSON_RATE_INTERVAL_SEC));
			int totalSecondsPassed = 0;
			LocalTime startTime = this.lastPassengersCalc;
			do {
				final LocalTime nextInterval = currentInterval.plusSeconds(POISSON_RATE_INTERVAL_SEC);
				// End time is either the start of the next interval or the current time (if
				// calulation ends in this interval)
				final LocalTime endTime = currentTime.isAfter(nextInterval) ? nextInterval : currentTime;
				final long secondsInInterval = SECONDS.between(startTime, endTime);
				final double rate = passengerInRatesByTime.get(currentInterval) * Simulation.PASSENGER_IN_MULTIPLICATOR;

				if (rate > 0) { // rate <= 0 means no passengers arrive
					final PoissonDistribution poissonDistribution = new PoissonDistribution(rate);
					// Find out how many passengers arrive in every second of this interval
					for (int i = 0; i < secondsInInterval; i++) {
						totalSecondsPassed++;
						final int numOfPassengersForSecond = poissonDistribution.sample();
						// Add passengers for this second
						for (int j = 0; j < numOfPassengersForSecond; j++) {
							passArrSinceLastCalc++;
							final Passenger passenger = new Passenger(
									this.lastPassengersCalc.plusSeconds(totalSecondsPassed), this);
							addWaitingPassenger(passenger);
						}
					}
				}
				startTime = endTime;
				currentInterval = nextInterval;
			} while (startTime != currentTime);

			this.lastPassengersCalc = currentTime;
		}

		Simulation.log("Number of passengers that arrived on platform " + this.name
				+ " since last passenger calculation: " + passArrSinceLastCalc);
		Simulation.log("New number of passengers on platform " + this.name + ": " + this.waitingPassengers.size());
	}

	@Override
	public String toString() {
		return "Platform [name=" + this.name + ", averageTravelTime=" + this.avgTravelTimeToNextPlatf
				+ ", nextWaitingPoint=" + this.nextWaitingPoint + ", lastWaitingPoint=" + this.lastWaitingPoint
				+ ", lastPassengersCalc=" + this.lastPassengersCalc + ", waitingPassengers=" + this.waitingPassengers
				+ "]";
	}

}