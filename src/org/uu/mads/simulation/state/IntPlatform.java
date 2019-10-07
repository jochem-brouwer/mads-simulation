package org.uu.mads.simulation.state;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import static java.time.temporal.ChronoUnit.SECONDS;

public class IntPlatform extends Platform {
	private Tram tram;
	private boolean isOccupied;

	private Duration averageTravelTime;


	private Random random;

	public IntPlatform(final Duration averageTravelTime, final WaitingPoint nextWaitingPoint,
			final WaitingPoint lastWaitingPoint) {
		super(averageTravelTime, nextWaitingPoint, lastWaitingPoint);
	}

	public boolean isOccupied() {
		return this.isOccupied;
	}

	public void setOccupied() {
		this.isOccupied = true;
	}

	public void setUnoccupied() {
		this.isOccupied = false;
	}

    public Duration getTravelTime() {
        // TODO logic to calculate "random" travel time
        return this.averageTravelTime;
    }

	@Override
	// TODO: Update with inherited values?
	public String toString() {
		return "IntPlatform [tram=" + this.tram + ", isOccupied=" + this.isOccupied + "]";
	}
	public boolean isPlatformOccupied() {
		return this.isOccupied;
	}
}