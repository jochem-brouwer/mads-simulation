package org.uu.mads.simulation.state;

import org.uu.mads.simulation.EventScheduler;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Queue;
import static java.time.temporal.ChronoUnit.SECONDS;

public class IntPlatform {

	private LocalTime lastPassengersCalc;
	private Queue<Passenger> waitingPassengers;
	private Tram tram;
	private boolean isOccupied;
	private WaitingPoint nextWp;
	private WaitingPointInt lastWp;
	private final Duration averageTravelTime;

	public IntPlatform(final Duration aVGTravelTime) {
		this.averageTravelTime = aVGTravelTime;
	}

	public void setOccupied() {
		this.isOccupied = true;
	}

	public void setUnoccupied() {
		this.isOccupied = false;
	}

	public boolean isPlatformOccupied() {
		return this.isOccupied;
	}

	// returns travel duration of this platform to the next platform
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

	public WaitingPoint getNextWp() {
		return this.nextWp;
	}

	public WaitingPointInt getLastWp() {
		return this.lastWp;
	}


}