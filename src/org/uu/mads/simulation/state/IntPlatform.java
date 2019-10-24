package org.uu.mads.simulation.state;

import java.time.Duration;
import java.util.Objects;

import org.apache.commons.math3.distribution.GammaDistribution;
import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;

public class IntPlatform extends Platform {
	private static final double DWELL_TIME__DIRST_SHAPE = 2.0;

	private Tram tram;
	private boolean occupied = false;
	Duration dwellTime;

	public IntPlatform(final String name, final Duration averageTravelTime) {
		super(name, averageTravelTime);
	}

	public Tram getTram() {
		return this.tram;
	}

	public boolean isOccupied() {
		return this.occupied;
	}

	public void setUnoccupied() {
		this.occupied = false;
	}

	public Duration getDwellTime() {
		return this.dwellTime;
	}

	public void arriveTram(final Tram tram) {
		Objects.requireNonNull(tram, "Given tram must not be null.");
		if (this.tram != null) {
			throw new IllegalStateException("There is already a tram on platform " + getName() + ".");
		}

		this.tram = tram;
		this.occupied = true;

		// When a tram arrives, we calculate the passengers and load them into the tram.
		final int passengersOut = this.tram.dumpPassengers(this);
		calculatePassengers();
		final int passengersIn = this.tram.loadPassengers(this);

		Simulation.log("Tram " + this.tram.getId() + " is arriving at platform " + getName() + " at "
				+ EventScheduler.getInstance().getCurrentTime() + ", dumps " + passengersOut + " passengers and loads "
				+ passengersIn + " passengers.");

		calculateDwellTime(passengersIn, passengersOut);
	}

	public Tram departTram() {
		if (this.tram == null) {
			throw new IllegalStateException("There is no tram on platform " + getName() + " that could depart.");
		}
		final Tram departingTram = this.tram;
		this.tram = null;
		return departingTram;
	}

	public void calculateDwellTime(final int passengersIn, final int passengersOut) {
		final double mean = 12.5 + (0.22 * passengersIn) + (0.13 * passengersOut)
				+ (Simulation.TRAM_PASSG_DWELL_TIME_FACTOR * this.tram.getNumOfPassengers());
		final double scale = Math.pow(mean / Math.sqrt(DWELL_TIME__DIRST_SHAPE), 2) / mean;
		final double minimum = 0.8 * mean;

		final GammaDistribution dwellTimeDist = new GammaDistribution(DWELL_TIME__DIRST_SHAPE, scale);

		this.dwellTime = Duration.ofSeconds(Math.round(Math.max(minimum, dwellTimeDist.sample())));

		Simulation.log("The dwell time is " + this.dwellTime.toSeconds() + " seconds.");
	}

	@Override
	public String toString() {
		return "IntPlatform [tram=" + this.tram + ", isOccupied=" + this.occupied + "]";
	}
}