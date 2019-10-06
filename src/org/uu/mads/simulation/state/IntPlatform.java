package org.uu.mads.simulation.state;

import org.uu.mads.simulation.EventScheduler;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Queue;
import static java.time.temporal.ChronoUnit.SECONDS;

public class IntPlatform extends Platform {
	private Tram tram;
	private boolean isOccupied;

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

    public long calculatePassengers() {
        EventScheduler scheduler = EventScheduler.get();
        long passedTime = (SECONDS.between(scheduler.getCurrentTime(), lastPassengersCalc));
        long numberOfPassengers = (int)(passedTime * scheduler.getPassengerRate());
        System.out.println("Number of passengers on Platform :" + numberOfPassengers);
        return numberOfPassengers;
    }

	@Override
	// TODO: Update with inherited values?
	public String toString() {
		return "IntPlatform [tram=" + this.tram + ", isOccupied=" + this.isOccupied + "]";
	public boolean isPlatformOccupied() {
		return this.isOccupied;
	}
}