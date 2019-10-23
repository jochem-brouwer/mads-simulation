package org.uu.mads.simulation.input;

public class PassengersOutReader extends PoissonReader {
	private static PassengersOutReader instance = null;

	private PassengersOutReader() {
		// Singleton, we only want to read the data from disk once
	}

	public static PassengersOutReader getInstance() {
		if (instance == null) {
			instance = new PassengersOutReader();
		}
		return instance;
	}
}
