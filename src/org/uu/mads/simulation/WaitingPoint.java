package org.uu.mads.simulation;

public class WaintingPoint {
	private List<Tram> waitingTrams;


	// Getters and setters are here
}

public class WaitingPointInt extends WaitingPoint {
	private IntPlatfrom nextPlatform;
}



public class WaitingPointJunction extends WaitingPoint {
	private Junction nextJunction;
}

