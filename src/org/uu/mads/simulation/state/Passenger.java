package org.uu.mads.simulation.state;

import java.time.LocalTime;

public class Passenger {

	private LocalTime arrivalTimePlatform;
	private LocalTime leaveTimePlatform;
	private Platform platform;

	public Passenger(LocalTime arrivalTimePlatform, Platform platform) {
	    this.arrivalTimePlatform = arrivalTimePlatform;
	    this.platform = platform;
    }

    @Override
    public String toString() {
        return "Passenger [arrivalTimePlatform=" + this.arrivalTimePlatform + "]";
    }
}