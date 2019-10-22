package org.uu.mads.simulation.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class PassengerInReader {
	private static final String PATH_TO_CSV_STRING = "data/PassengerList - outputPoisson.csv";
	private static final String DELIMITER = ",";

	private static PassengerInReader instance = null;

	private LinkedHashMap<String, Map<LocalTime, Double>> passengerInRatesByTimeByPlatform = null;

	private PassengerInReader() {
		// Singleton
	}

	public static PassengerInReader getInstance() {
		if (instance == null) {
			instance = new PassengerInReader();
		}
		return instance;
	}

	public Map<LocalTime, Double> getPassengerInRatesByTimeForPlatform(final String platformName) {
		if (this.passengerInRatesByTimeByPlatform == null) {
			try {
				readData();
			} catch (final IOException exception) {
				throw new RuntimeException(exception);
			}
		}

		if (!this.passengerInRatesByTimeByPlatform.containsKey(platformName)) {
			throw new IllegalArgumentException("Invalid platform provided.");
		}

		return Collections.unmodifiableMap(this.passengerInRatesByTimeByPlatform.get(platformName));
	}

	private void readData() throws IOException {
		final BufferedReader csvReader = new BufferedReader(new FileReader(PATH_TO_CSV_STRING));
		final String[] columns = csvReader.readLine().split(DELIMITER);
		final LinkedHashMap<String, Map<LocalTime, Double>> passengerInRatesByTimeByPlatform = new LinkedHashMap<>();
		for (int i = 1; i < columns.length; i++) {
			passengerInRatesByTimeByPlatform.put(columns[i], new TreeMap<>());
		}

		String row;
		while ((row = csvReader.readLine()) != null) {
			final String[] data = row.split(DELIMITER);
			final LocalTime time = LocalTime.parse(data[0]);
			for (int i = 1; i < data.length; i++) {
				final Map<LocalTime, Double> passengerInRatesByTime = passengerInRatesByTimeByPlatform.get(columns[i]);
				passengerInRatesByTime.put(time, Double.parseDouble(data[i]) / 60); // / 60 because the unit here is
																					// minites but we want seconds
			}
		}

		csvReader.close();

		this.passengerInRatesByTimeByPlatform = passengerInRatesByTimeByPlatform;
	}

}
