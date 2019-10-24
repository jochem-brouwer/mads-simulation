package org.uu.mads.simulation.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

abstract class PoissonReader {
	private static final String DELIMITER = ",";

	protected LinkedHashMap<String, Map<LocalTime, Double>> ratesByTimeByPlatform = null;

	public abstract Map<LocalTime, Double> getRatesByTimeForPlatform(final String platformName, final String csvPath);

	protected Map<LocalTime, Double> getRatesByTimeForPlatform(final String platformName) {
		if (!this.ratesByTimeByPlatform.containsKey(platformName)) {
			throw new IllegalArgumentException("Invalid platform provided. Platform " + platformName + " not found.");
		}

		return Collections.unmodifiableMap(this.ratesByTimeByPlatform.get(platformName)); // We return a copy, not the
																							// // original map
	}

	protected void readData(final String csvPath) throws IOException {
		final BufferedReader csvReader = new BufferedReader(new FileReader(csvPath));
		final String[] columns = csvReader.readLine().split(DELIMITER);
		final LinkedHashMap<String, Map<LocalTime, Double>> ratesByTimeByPlatform = new LinkedHashMap<>();
		for (int i = 1; i < columns.length; i++) {
			ratesByTimeByPlatform.put(columns[i], new TreeMap<>());
		}

		String row;
		while ((row = csvReader.readLine()) != null) {
			final String[] data = row.split(DELIMITER);
			final LocalTime time = LocalTime.parse(data[0]);
			for (int i = 1; i < data.length; i++) {
				final Map<LocalTime, Double> ratesByTime = ratesByTimeByPlatform.get(columns[i]);
				ratesByTime.put(time, Double.parseDouble(data[i]) / 60); // / 60 because the unit here is
																			// minites but we want seconds
			}
		}

		csvReader.close();

		this.ratesByTimeByPlatform = ratesByTimeByPlatform;
	}
}
