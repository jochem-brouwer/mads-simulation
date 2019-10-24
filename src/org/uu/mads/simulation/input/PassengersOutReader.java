package org.uu.mads.simulation.input;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Map;

import org.uu.mads.simulation.Simulation;

public class PassengersOutReader extends PoissonReader {
	private static final String DELIMITER_ART = ";";

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

	@Override
	public Map<LocalTime, Double> getRatesByTimeForPlatform(final String platformName, final String csvPath) {
		if (this.ratesByTimeByPlatform == null) {
			try {
				if (Simulation.ARTIFICIAL_DATA) {
					readDataArtificial(csvPath);
				} else {
					readData(csvPath);
				}
			} catch (final IOException exception) {
				throw new RuntimeException(exception);
			}
		}
		return getRatesByTimeForPlatform(platformName);
	}

	private void readDataArtificial(final String csvPath) throws IOException {
		// TODO
	}
}
