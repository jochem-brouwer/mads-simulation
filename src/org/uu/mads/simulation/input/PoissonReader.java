package org.uu.mads.simulation.input;

import static java.time.temporal.ChronoUnit.HOURS;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.uu.mads.simulation.Simulation;

abstract class PoissonReader {
	private static final String DELIMITER = ",";
	private static final String DELIMITER_ART = ";";

	private LinkedHashMap<String, Map<LocalTime, Double>> ratesByTimeByPlatform = null;

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

		if (!this.ratesByTimeByPlatform.containsKey(platformName)) {
			throw new IllegalArgumentException("Invalid platform provided. Platform " + platformName + " not found.");
		}

		return Collections.unmodifiableMap(this.ratesByTimeByPlatform.get(platformName)); // We return a copy, not the
																							// original map
	}

	private void readData(final String csvPath) throws IOException {
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

	private void readDataArtificial(final String csvPath) throws IOException {
		final BufferedReader csvReader = new BufferedReader(new FileReader(csvPath));
		final LinkedHashMap<String, Map<LocalTime, Double>> ratesByTimeByPlatform = new LinkedHashMap<>();

		String row;
		csvReader.readLine();
		
		System.out.println(csvPath);
		
		String[] names = new String[] {"WKZ", "UMC", "Heidelberglaan", "Padualaan", "Kromme-Rijn", "Galgenwaard", "Vaartsche-Rijn", "Centraal Station", "P+R De Uithof"};

		while ((row = csvReader.readLine()) != null) {
			final String[] data = row.split(DELIMITER_ART);

			String name = data[0];

			final int direction = Integer.parseInt(data[1]);


			if ((name.equals("P+R De Uithof") && (direction == 1))
					|| (name.equals("Centraal Station") && (direction == 0))) {
				continue;
			}

			double timeStart = (double) Float.parseFloat(data[2]);
            double timeEnd = (double) Float.parseFloat(data[3]);
            
            int hourStart  = (int) Math.floor(timeStart);
            int minStart = (int) ((timeStart - hourStart) * 60);

            final LocalTime time1 = LocalTime.of(hourStart, minStart);
            
            int hourEnd =  (int) Math.floor(timeEnd);
            int minEnd  =  (int) ((timeEnd - hourEnd) * 60);
            
            final LocalTime time2 = LocalTime.of(hourEnd, minEnd);

			final float passIn = Float.parseFloat(data[4]);
			final float passOut = Float.parseFloat(data[5]);

			boolean got = false;
			
			for (int i = 0; i < names.length; i++) {
				if (names[i].equals(name)) {
					got = true;
					break;
				}
			}
			
			if (!got) {
				throw(new Error("Incorrect station name: " + name));
			}
			
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

	}

}
