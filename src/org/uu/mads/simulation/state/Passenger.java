package org.uu.mads.simulation.state;

public class Passenger {
	private double arrivalTimePlatform;

	public static int calculatePassengers(final EndStation endStation) {
		return 0; // TODO change
	}

  // I put this function into the IntPlatform class.
  /*public static int calculatePassengers(final IntPlatform platform) {
	  return 0; // TODO change
  }*/

    @Override
    public String toString() {
        return "Passenger [arrivalTimePlatform=" + this.arrivalTimePlatform + "]";
    }
}