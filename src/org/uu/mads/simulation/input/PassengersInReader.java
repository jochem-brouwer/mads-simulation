package org.uu.mads.simulation.input;

public class PassengersInReader extends PoissonReader {
	private static PassengersInReader instance = null;

	private PassengersInReader() {
		// Singleton, we only want to read the data from disk once
	}

	public static PassengersInReader getInstance() {
		if (instance == null) {
			instance = new PassengersInReader();
		}
		return instance;
	}
}
