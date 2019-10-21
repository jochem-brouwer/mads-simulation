package org.uu.mads.simulation.state;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Performance;
import org.uu.mads.simulation.Simulation;

public class Platform {
	private static final double TRAVEL_TIME_SD_LOG = 0.3588221;
	private static final double DWELL_TIME__DIRST_SHAPE = 2.0;

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
		final Duration travelTimeToNextPlatf = Duration.ofSeconds((long) this.travelTimeToNexcPlatfDist.sample());
		Simulation.log("Platform " + this.name + ": Travel time to next platform is "
				+ travelTimeToNextPlatf.toSeconds() + " seconds.");
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
		Simulation.log("Number of passengers on platform " + this.name + ":" + numberOfPassengers);

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

	public static Duration calculateDwellTime(final int passengersIn, final int passengersOut) {
		final double mean = 12.5 + (0.22 * passengersIn) + (0.13 * passengersOut);
		final double scale = Math.pow(mean / Math.sqrt(DWELL_TIME__DIRST_SHAPE), 2) / mean;
		final double minimum = 0.8 * mean;

		final GammaDistribution dwellTimeDist = new GammaDistribution(DWELL_TIME__DIRST_SHAPE, scale);

		return Duration.ofSeconds(Math.round(Math.max(minimum, dwellTimeDist.sample())));
	}

	// TODO: Include in Tram class?
	public int loadPassengers(final Tram tram) {

		int remainingCapacity = tram.getRemainingCapacity();
		int numOfPassengers = tram.getNumOfPassengers();
		int passengersIn = 0;

		while (remainingCapacity > 0) {
			if (this.waitingPassengers.poll() != null) {
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

	// TODO: Include in Tram class?
	/**
	 * This functions calculates the number of passengers leaving the tram and
	 * removes them from the tram number.
	 */
	public int dumpPassengers(final Tram tram) {
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