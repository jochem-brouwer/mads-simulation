package org.uu.mads.simulation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.uu.mads.simulation.state.DayAndPeakPerformances;
import org.uu.mads.simulation.state.Performance;

public class PerformanceTracker {
	private static final LocalTime DAY_START_TIME = LocalTime.of(7, 0);
	private static final LocalTime DAY_END_TIME = LocalTime.of(19, 0);
	private static final LocalTime PEAK_START_TIME = LocalTime.of(7, 30);
	private static final LocalTime PEAK_END_TIME = LocalTime.of(9, 30);
	private static final String CSV_DELIMITER = ",";

	private static PerformanceTracker dayPerformanceTracker = new PerformanceTracker(DAY_START_TIME, DAY_END_TIME);
	private static PerformanceTracker peakPerformanceTracker = new PerformanceTracker(PEAK_START_TIME, PEAK_END_TIME);

	private final LocalTime startTime;
	private final LocalTime endTime;

	private long totalPassengers = 0;
	private Duration totalWaitingTime = Duration.ZERO;
	private Duration averageWaitingTime = Duration.ZERO;
	private Duration maxWaitingTime = Duration.ZERO;

	private long csTotalDelays = 0;
	private long csTotalDepartures = 0;

	private long prTotalDelays = 0;
	private long prTotalDepartures = 0;

	private Duration csTotalDelayTime = Duration.ZERO;
	private Duration csAverageDelay = Duration.ZERO;
	private Duration csMaximumDelay = Duration.ZERO;

	private Duration prTotalDelayTime = Duration.ZERO;
	private Duration prAverageDelay = Duration.ZERO;
	private Duration prMaximumDelay = Duration.ZERO;

	private float csPercentageOfDelays;
	private float prPercentageOfDelays;

	private Duration csTotalJunctionWaitingTime = Duration.ZERO;
	private Duration prTotalJunctionWaitingTime = Duration.ZERO;
	private Duration csAverageJunctionWaitingTime = Duration.ZERO;
	private Duration prAverageJunctionWaitingTime = Duration.ZERO;
	private Duration csMaximumJunctionWaitingTime = Duration.ZERO;
	private Duration prMaximumJunctionWaitingTime = Duration.ZERO;
	private long csJunctionArrivals = 0;
	private long prJunctionArrivals = 0;

	private PerformanceTracker(final LocalTime starTime, final LocalTime endTime) {
		// private constructor because this is a singleton
		this.startTime = starTime;
		this.endTime = endTime;
	}

	public static void reset() {
		dayPerformanceTracker = new PerformanceTracker(DAY_START_TIME, DAY_END_TIME);
		peakPerformanceTracker = new PerformanceTracker(PEAK_START_TIME, PEAK_END_TIME);
	}

	public static void addPassenger(final Duration waitingTime) {
		dayPerformanceTracker.addPassengerToInstance(waitingTime);
		peakPerformanceTracker.addPassengerToInstance(waitingTime);
	}

	private void addPassengerToInstance(final Duration waitingTime) {
		if (isWithinTimeFrame()) {
			this.totalPassengers += 1;
			this.totalWaitingTime = this.totalWaitingTime.plus(waitingTime);
			if (this.maxWaitingTime.toSeconds() < waitingTime.toSeconds()) {
				this.maxWaitingTime = waitingTime;
			}
		}
	}

	public static void addJunctionWaitingTime(final Duration waitingTime, final int junction) {
		dayPerformanceTracker.addJunctionWaitingTimeToInstance(waitingTime, junction);
		peakPerformanceTracker.addJunctionWaitingTimeToInstance(waitingTime, junction);
	}

	private void addJunctionWaitingTimeToInstance(final Duration waitingTime, final int junction) {

		if (isWithinTimeFrame()) {

			// junction denotes the junction. 0 for CS, 1 for P+R.
			if (junction == 0) {
				this.csJunctionArrivals += 1;
				this.csTotalJunctionWaitingTime = this.csTotalJunctionWaitingTime.plus(waitingTime);

				if (this.csMaximumJunctionWaitingTime.toSeconds() < waitingTime.toSeconds()) {
					this.csMaximumJunctionWaitingTime = waitingTime;
				}
			} else {
				this.prJunctionArrivals += 1;
				this.prTotalJunctionWaitingTime = this.prTotalJunctionWaitingTime.plus(waitingTime);

				if (this.prMaximumJunctionWaitingTime.toSeconds() < waitingTime.toSeconds()) {
					this.prMaximumJunctionWaitingTime = waitingTime;
				}
			}
		}
	}

	public static void addDelay(final Duration delay, final int endStation) {
		dayPerformanceTracker.addDelayToInstance(delay, endStation);
		peakPerformanceTracker.addDelayToInstance(delay, endStation);
	}

	private void addDelayToInstance(final Duration delay, final int endStation) {
		// We add one departure to total departures.
		// We check if the delay is bigger than 1 minute. If so, we add one delay to
		// total_delays and add the amount
		// to total_delay_time.

		// endStation denotes the end station. 0 for CS, 1 for P+R.

		if (isWithinTimeFrame()) {
			if (endStation == 0) {
				this.csTotalDepartures += 1;

				if ((delay.compareTo(Duration.ofMinutes(1)) >= 0) && (delay.isNegative() == false)) {
					this.csTotalDelayTime = this.csTotalDelayTime.plus(delay);
					if (delay.compareTo(this.csMaximumDelay) >= 0) {
						this.csMaximumDelay = delay;
					}
					this.csTotalDelays += 1;
				}

			} else {
				this.prTotalDepartures += 1;
				if ((delay.compareTo(Duration.ofMinutes(1)) >= 0) && (delay.isNegative() == false)) {
					this.prTotalDelayTime = this.prTotalDelayTime.plus(delay);
					if (delay.compareTo(this.prMaximumDelay) >= 0) {
						this.prMaximumDelay = delay;
					}
					this.prTotalDelays += 1;
				}
			}

		}
	}

	private boolean isWithinTimeFrame() {
		final LocalTime currentTime = EventScheduler.getInstance().getCurrentTime();
		return (currentTime.isAfter(this.startTime) || currentTime.equals(this.startTime))
				&& (currentTime.isBefore(this.endTime) || currentTime.equals(this.endTime));
	}

	private Performance getPerformance() {
		calculateAverageWaitingTime();
		calculateJunctionWaitingTime();
		calculateAveragePunctuality();

		return new Performance(this.totalPassengers, this.totalWaitingTime, this.averageWaitingTime,
				this.maxWaitingTime, this.csTotalDelays, this.csTotalDepartures, this.prTotalDelays,
				this.prTotalDepartures, this.csTotalDelayTime, this.csAverageDelay, this.csMaximumDelay,
				this.prTotalDelayTime, this.prAverageDelay, this.prMaximumDelay, this.csPercentageOfDelays,
				this.prPercentageOfDelays, this.csTotalJunctionWaitingTime, this.prTotalJunctionWaitingTime,
				this.csAverageJunctionWaitingTime, this.prAverageJunctionWaitingTime, this.csMaximumJunctionWaitingTime,
				this.prMaximumJunctionWaitingTime, this.csJunctionArrivals, this.prJunctionArrivals);
	}

	private void calculateAverageWaitingTime() {
		if (this.totalPassengers != 0) {
			this.averageWaitingTime = this.totalWaitingTime.dividedBy(this.totalPassengers);
		}
	}

	private void calculateJunctionWaitingTime() {
		this.csAverageJunctionWaitingTime = this.csTotalJunctionWaitingTime.dividedBy(this.csJunctionArrivals);
		this.prAverageJunctionWaitingTime = this.prTotalJunctionWaitingTime.dividedBy(this.prJunctionArrivals);
	}

	public static DayAndPeakPerformances getDayAndPeakPerformances() {
		return new DayAndPeakPerformances(dayPerformanceTracker.getPerformance(),
				peakPerformanceTracker.getPerformance());
	}

	private void calculateAveragePunctuality() {

		// Calculate average punctuality for CS:
		if (this.csTotalDelays != 0) {
			this.csAverageDelay = this.csTotalDelayTime.dividedBy(this.csTotalDelays);
		} else {
			this.csAverageDelay = Duration.ZERO;
		}
		this.csPercentageOfDelays = ((float) this.csTotalDelays / (float) this.csTotalDepartures) * 100;

		// We do the same for P+R:
		if (this.prTotalDelays != 0) {
			this.prAverageDelay = this.prTotalDelayTime.dividedBy(this.prTotalDelays);
		} else {
			this.prAverageDelay = Duration.ZERO;
		}
		this.prPercentageOfDelays = ((float) this.prTotalDelays / (float) this.prTotalDepartures) * 100;
	}

	public static void printPerformanceReport(final List<Performance> performances, final String type) {
		// Passengers
		long totalPassengers = 0;
		Duration totalWaitingTime = Duration.ZERO;
		Duration totalMaxWaitingTime = Duration.ZERO;

		// CS Junction
		Duration csTotalJunctionWaitingTime = Duration.ZERO;
		long totalCsJunctionArrivals = 0;
		Duration csAverageJunctionWaitingTime;
		Duration totalCsJunctionMaxWaitingTime = Duration.ZERO;

		// PR Junction
		Duration prTotalJunctionWaitingTime = Duration.ZERO;
		long totalPrJunctionArrivals = 0;
		Duration prAverageJunctionWaitingTime;
		Duration totalPrJunctionMaxWaitingTime = Duration.ZERO;

		// Performance Measures CS
		long csTotalDelays = 0;
		Duration csTotalDelayTime = Duration.ZERO;
		long csTotalDepartures = 0;
		double totalCsPercentageOfDelays = 0;
		Duration totalCsMaxDelays = Duration.ZERO;

		// Performance Measures PR
		long prTotalDelays = 0;
		Duration prTotalDelayTime = Duration.ZERO;
		long prTotalDepartures = 0;
		double totalPrPercentageOfDelays = 0;
		Duration totalPrMaxDelays = Duration.ZERO;

		for (final Performance performance : performances) {
			// Passengers
			totalPassengers += performance.getTotalPassengers();
			totalWaitingTime = totalWaitingTime.plus(performance.getTotalWaitingTime());
			totalMaxWaitingTime = totalMaxWaitingTime.plus(performance.getMaxWaitingTime());

			// CS Junction
			csTotalJunctionWaitingTime = csTotalJunctionWaitingTime.plus(performance.getCsTotalJunctionWaitingTime());
			totalCsJunctionArrivals += performance.getCsJunctionArrivals();
			totalCsJunctionMaxWaitingTime = totalCsJunctionMaxWaitingTime
					.plus(performance.getCsMaximumJunctionWaitingTime());

			// PR Junction
			prTotalJunctionWaitingTime = prTotalJunctionWaitingTime.plus(performance.getPrTotalJunctionWaitingTime());
			totalPrJunctionArrivals += performance.getPrJunctionArrivals();
			totalPrJunctionMaxWaitingTime = totalPrJunctionMaxWaitingTime
					.plus(performance.getPrMaximumJunctionWaitingTime());

			// Performance Measures CS
			csTotalDelays += performance.getCsTotalDelays();
			csTotalDelayTime = csTotalDelayTime.plus(performance.getCsTotalDelayTime());
			csTotalDepartures += performance.getCsTotalDepartures();
			totalCsPercentageOfDelays += performance.getCsPercentageOfDelays();
			totalCsMaxDelays = totalCsMaxDelays.plus(performance.getCsMaximumDelay());

			// Performance Measures PR
			prTotalDelays += performance.getPrTotalDelays();
			prTotalDelayTime = prTotalDelayTime.plus(performance.getPrTotalDelayTime());
			prTotalDepartures += performance.getPrTotalDepartures();
			totalPrPercentageOfDelays += performance.getPrPercentageOfDelays();
			totalPrMaxDelays = totalPrMaxDelays.plus(performance.getPrMaximumDelay());
		}

		// Passengers averages
		final int averageTotalPassengers = (int) (totalPassengers / performances.size());
		final Duration averageTotalWaitingTime = totalWaitingTime.dividedBy(performances.size());
		final Duration averageMaxWaitingTime = totalMaxWaitingTime.dividedBy(performances.size());
		final Duration averageWaitingTime = totalPassengers == 0 ? Duration.ZERO
				: totalWaitingTime.dividedBy(totalPassengers);

		// CS Junction averages
		final Duration averageCsTotalJunctionWaitingTime = csTotalJunctionWaitingTime.dividedBy(performances.size());
		final long averageTotalCsJunctionArrivals = totalCsJunctionArrivals / performances.size();
		final Duration averageCsMaxJunctionWaitingTime = totalCsJunctionMaxWaitingTime.dividedBy(performances.size());
		csAverageJunctionWaitingTime = totalCsJunctionArrivals == 0 ? Duration.ZERO
				: csTotalJunctionWaitingTime.dividedBy(totalCsJunctionArrivals);

		// PR Junction averages
		final Duration averagePrTotalJunctionWaitingTime = prTotalJunctionWaitingTime.dividedBy(performances.size());
		final long averageTotalPrJunctionArrivals = totalPrJunctionArrivals / performances.size();
		final Duration averagePrMaxJunctionWaitingTime = totalPrJunctionMaxWaitingTime.dividedBy(performances.size());
		prAverageJunctionWaitingTime = totalPrJunctionArrivals == 0 ? Duration.ZERO
				: prTotalJunctionWaitingTime.dividedBy(totalPrJunctionArrivals);

		// Performance Measures CS averages
		final Duration csAverageDelay = csTotalDelays == 0 ? Duration.ZERO : csTotalDelayTime.dividedBy(csTotalDelays);
		final Duration averageCsMaximumDelay = totalCsMaxDelays.dividedBy(performances.size());
		final int averageCsTotalDepartures = (int) (csTotalDepartures / performances.size());
		final int averageCsTotalDelays = (int) (csTotalDelays / performances.size());
		final Duration averageCsTotalDelayTime = csTotalDelayTime.dividedBy(performances.size());
		final float averageCsPercentageOfDelays = (float) (totalCsPercentageOfDelays / performances.size());

		// Performance Measures PR averages
		final Duration prAverageDelay = prTotalDelays == 0 ? Duration.ZERO : prTotalDelayTime.dividedBy(prTotalDelays);
		final Duration averagePrMaximumDelay = totalPrMaxDelays.dividedBy(performances.size());
		final int averagePrTotalDepartures = (int) (prTotalDepartures / performances.size());
		final int averagePrTotalDelays = (int) (prTotalDelays / performances.size());
		final Duration averagePrTotalDelayTime = prTotalDelayTime.dividedBy(performances.size());
		final float averagePrPercentageOfDelays = (float) (totalPrPercentageOfDelays / performances.size());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println(type + " Performance Report");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("Average total passengers: " + averageTotalPassengers);
		System.out.println("Average total waiting time in seconds: " + averageTotalWaitingTime.toSeconds());

		System.out.println("Average maximum waiting time of a passenger: " + averageMaxWaitingTime.toSeconds());

		System.out.println("Average waiting time: (passengers / waiting time) " + averageWaitingTime.toSeconds());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("Average total waiting time for junction at Centraal Station: "
				+ averageCsTotalJunctionWaitingTime.toSeconds());
		System.out.println("Average total junction arrivals: " + averageTotalCsJunctionArrivals);
		System.out.println("Average maximum waiting time: " + averageCsMaxJunctionWaitingTime.toSeconds());
		System.out.println(
				"Average waiting time at junction Centraal Station: " + csAverageJunctionWaitingTime.toSeconds());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("Average total waiting time for junction at P+R Uithof: "
				+ averagePrTotalJunctionWaitingTime.toSeconds());
		System.out.println("Average total junction arrivals: " + averageTotalPrJunctionArrivals);
		System.out.println("Average maximum waiting time: " + averagePrMaxJunctionWaitingTime.toSeconds());
		System.out.println("Average waiting time at junction P+R Uithof: " + prAverageJunctionWaitingTime.toSeconds());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("PERFORMANCE MEASURES FOR CENTRAAL STATION:");
		System.out.println("Average total departures: " + averageCsTotalDepartures);
		System.out.println("Average total departure delays: " + averageCsTotalDelays);
		System.out.println("Average total delay amount: " + averageCsTotalDelayTime.toSeconds());

		System.out.println("Average delay time: (total delay amount / total delays): " + csAverageDelay.toSeconds());

		System.out.println("Average maximum delay: " + averageCsMaximumDelay.toSeconds());

		System.out
				.println("Average delay percentage: (delays / total departures): " + averageCsPercentageOfDelays + "%");

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("PERFORMANCE MEASURES FOR P+R UITHOF:");
		System.out.println("Average total departures: " + averagePrTotalDepartures);
		System.out.println("Average total departure delays: " + averagePrTotalDelays);
		System.out.println("Average total delay amount: " + averagePrTotalDelayTime.toSeconds());

		System.out.println("Average delay time: (total delay amount / total delays): " + prAverageDelay.toSeconds());

		System.out.println("Average maximum delay: " + averagePrMaximumDelay.toSeconds());

		System.out
				.println("Average delay percentage: (delays / total departures): " + averagePrPercentageOfDelays + "%");

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");
	}

	public static void serializePerformances(final List<Performance> performances, final LocalTime simEndTime,
			final String type) {
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
		final File directory = new File(
				System.getProperty("user.dir") + "/output/output-data-" + simEndTime.format(dtf));
		directory.mkdirs();

		for (int i = 0; i < performances.size(); i++) {
			final String fileName = (type + "PerformanceNr" + (i + 1));
			final Performance performance = performances.get(i);

			try {
				final FileOutputStream fileStream = new FileOutputStream(directory + "/" + fileName);
				final ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
				objectStream.writeObject(performance);

				objectStream.close();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void persistPerformanceTable(final List<Performance> performances, final LocalTime simEndTime,
			final String type) throws IOException {

		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");
		final String directory = System.getProperty("user.dir") + "/output/output-data-" + simEndTime.format(dtf);
		new File(directory).mkdirs();

		final List<String> columns = Arrays.asList("run", //
				"totalPassengers", "totalWaitingTime", "averageWaitingTime", "maxWaitingTime", //
				"csTotalDepartures", "csTotalDelays", "csTotalDelayTime", "csMaximumDelay", //
				"csAverageDelay", "csPercentageOfDelays", "csTotalJunctionWaitingTime", //
				"csAverageJunctionWaitingTime", "csMaximumJunctionWaitingTime", "csJunctionArrivals", //
				"prTotalDepartures", "prTotalDelays", "prTotalDelayTime", "prMaximumDelay", //
				"prAverageDelay", "prPercentageOfDelays", "prTotalJunctionWaitingTime", //
				"prAverageJunctionWaitingTime", "prMaximumJunctionWaitingTime", "prJunctionArrivals" //
		);

		final String fileName = type + "PerformanceTable.csv";
		final FileWriter csvWriter = new FileWriter(directory + "/" + fileName);
		csvWriter.append(String.join(CSV_DELIMITER, columns));
		csvWriter.append("\n");

		for (int i = 1; i <= performances.size(); i++) {
			final Performance performance = performances.get(i - 1);
			csvWriter.append(i + CSV_DELIMITER);
			csvWriter.append(performance.getTotalPassengers() + CSV_DELIMITER);
			csvWriter.append(performance.getTotalWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getAverageWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getMaxWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsTotalDepartures() + CSV_DELIMITER);
			csvWriter.append(performance.getCsTotalDelays() + CSV_DELIMITER);
			csvWriter.append(performance.getCsTotalDelayTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsMaximumDelay().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsAverageDelay().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsPercentageOfDelays() + CSV_DELIMITER);
			csvWriter.append(performance.getCsTotalJunctionWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsAverageJunctionWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsMaximumJunctionWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getCsJunctionArrivals() + CSV_DELIMITER);
			csvWriter.append(performance.getPrTotalDepartures() + CSV_DELIMITER);
			csvWriter.append(performance.getPrTotalDelays() + CSV_DELIMITER);
			csvWriter.append(performance.getPrTotalDelayTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getPrMaximumDelay().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getPrAverageDelay().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getPrPercentageOfDelays() + CSV_DELIMITER);
			csvWriter.append(performance.getPrTotalJunctionWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getPrAverageJunctionWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getPrMaximumJunctionWaitingTime().toSeconds() + CSV_DELIMITER);
			csvWriter.append(performance.getPrJunctionArrivals() + CSV_DELIMITER);
			csvWriter.append("\n");
		}

		csvWriter.flush();
		csvWriter.close();
	}

}
