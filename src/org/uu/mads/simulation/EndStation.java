package org.uu.mads.simulation;

import java.util.Queue;

public class EndStation {
	private double lastPassengersCalc;
	private double nextScheduledLeave;
	private Queue<Passenger> waitingPassengers;
	private Tram tramOnPlatformA;
	private Tram tramOnPlatformB;

	private double platformAArrivalTime;
	private double platformBArrivalTime;
	// Getters and setters are here
}
