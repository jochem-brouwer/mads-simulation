package org.uu.mads.simulation;

public class Junction {

	private Tram tramOnLaneInA;
	private Tram tramOnLaneInB;
	private Tram tramOnLaneAOut;
	private Tram tramOnLaneBOut;
	private Boolean junctionUsed;

	// Getters and setters are here

	public Junction() {
		tramOnLaneInA = null;
		tramOnLaneInB = null;
		tramOnLaneAOut = null;
		tramOnLaneBOut = null;
		junctionUsed = false;
	}
}