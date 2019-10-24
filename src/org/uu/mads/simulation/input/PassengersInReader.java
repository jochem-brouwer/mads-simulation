package org.uu.mads.simulation.input;

import static java.time.temporal.ChronoUnit.HOURS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.uu.mads.simulation.Simulation;

public class PassengersInReader extends PoissonReader {
	private static final String DELIMITER_ART = ";";

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
		final BufferedReader csvReader = new BufferedReader(new FileReader(csvPath));
		final LinkedHashMap<String, Map<LocalTime, Double>> ratesByTimeByPlatform = new LinkedHashMap<>();

		String row;
		csvReader.readLine();

		while ((row = csvReader.readLine()) != null) {
			final String[] data = row.split(DELIMITER_ART);

			String name = data[0];

			final int direction = Integer.parseInt(data[1]);

			if ((name.equals("P+R De Uithof") && (direction == 1))
					|| (name.equals("Centraal Station") && (direction == 0))) {
				continue;
			}

			final double timeStart = Float.parseFloat(data[2]);
			final double timeEnd = Float.parseFloat(data[3]);

			final int hourStart = (int) Math.floor(timeStart);
			final int minStart = (int) ((timeStart - hourStart) * 60);

			final LocalTime time1 = LocalTime.of(hourStart, minStart);

			final int hourEnd = (int) Math.floor(timeEnd);
			final int minEnd = (int) ((timeEnd - hourEnd) * 60);

			final LocalTime time2 = LocalTime.of(hourEnd, minEnd);

			final float passIn = Float.parseFloat(data[4]);
			final float passOut = Float.parseFloat(data[5]);

			if (direction == 0) {
				if (!name.equals("P+R De Uithof") && !name.equals("Centraal Station")) {
					name = name + "-A";
				}
			} else if (!name.equals("P+R De Uithof") && !name.equals("Centraal Station")) {
				name = name + "-B";
			}

			if (!ratesByTimeByPlatform.containsKey(name)) {
				ratesByTimeByPlatform.put(name, new TreeMap<>());
			}

			final float timeInterval = (time1.until(time2, HOURS));
			final double passengerInput = (((passIn / timeInterval) / 60) / 60);
			LocalTime timeStamp = time1;
			final Map<LocalTime, Double> ratesByTime = ratesByTimeByPlatform.get(name);

			while (timeStamp.isBefore(time2)) {
				ratesByTime.put(timeStamp, passengerInput);
				timeStamp = timeStamp.plusMinutes(15);
			}

			this.ratesByTimeByPlatform = ratesByTimeByPlatform;
		}
		csvReader.close();
	}
}
