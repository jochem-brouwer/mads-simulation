package org.uu.mads.simulation.state;

import java.util.Queue;

public class EndStation {
	private double lastPassengersCalc;
	private double nextScheduledLeave;
	private Queue<Passenger> waitingPassengers;

	private Tram tramOnPlatformA;
	private Tram tramOnPlatformB;

	private double platformAArrivalTime;
	private double platformBArrivalTime;

	public double getLastPassengersCalc() {
		return this.lastPassengersCalc;
	}

	public double getNextScheduledLeave() {
		return this.nextScheduledLeave;
	}

	public Queue<Passenger> getWaitingPassengers() {
		return this.waitingPassengers;
	}

	public Tram getTramOnPlatformA() {
		return this.tramOnPlatformA;
	}

	public Tram getTramOnPlatformB() {
		return this.tramOnPlatformB;
	}

	public double getPlatformAArrivalTime() {
		return this.platformAArrivalTime;
	}

	public double getPlatformBArrivalTime() {
		return this.platformBArrivalTime;
	}

	public void freePlatformA() {
		this.tramOnPlatformA = null;
	}

	public void freePlatformB() {
		this.tramOnPlatformB = null;
	}
}
