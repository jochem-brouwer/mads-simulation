package org.uu.mads.simulation;

import java.util.Queue;

public class IntPlatform {

	private LocalTime lastPassengersCalc;
	private Queue<Passenger> waitingPassengers;
	private Tram tram;
	private boolean isOccupied;
	private WaitingPoint nextWp;
	private WaitingPoint lastWp;
  private Duration AverageTravelTime;


  public IntPlatform(Duration AVGTravelTime) {
    AverageTravelTime = AVGTravelTime;
  }

	// Getters and setters are here

  public void setOccupied() {
    this.isOccupied = true;
  }

  public void setUnoccupied() {
    this.isOccupied = false;
  }

  public boolean platformIsOccupied() {
    return this.isOccupied;
  }

  // returns travel duration of this platform to the next platform
  public Duration getTravelTime() {
    // todo logic to calculate "random" travel time
    return AverageTravelTime;
  }

  public WaitingPoint getNextWp() {
    return this.nextWp;
  }

  public WaitingPoint getLastWp() {
    return this.lastWp;
  }
}