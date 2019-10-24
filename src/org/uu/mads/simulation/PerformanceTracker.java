package org.uu.mads.simulation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.uu.mads.simulation.state.Performance;

public class PerformanceTracker {
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

	private static PerformanceTracker instance = null;

	private PerformanceTracker() {
		// private constructor because this is a singleton
	}

	public static PerformanceTracker getInstance() {
		if (instance == null) {
			instance = new PerformanceTracker();
		}
		return instance;
	}

	public static void reset() {
		instance = null;
	}

	public static void printPerformanceReport(final List<Performance> performances) {
		// Passengers
		long totalPassengers = 0;
		Duration totalWaitingTime = Duration.ZERO;
		Duration maxWaitingTime = Duration.ZERO;
		Duration averageWaitingTime;

		// CS Junction
		Duration csTotalJunctionWaitingTime = Duration.ZERO;
		long totalCsJunctionArrivals = 0;
		Duration csMaximumJunctionWaitingTime = Duration.ZERO;
		Duration csAverageJunctionWaitingTime;

		// PR Junction
		Duration prTotalJunctionWaitingTime = Duration.ZERO;
		long totalPrJunctionArrivals = 0;
		Duration prMaximumJunctionWaitingTime = Duration.ZERO;
		Duration prAverageJunctionWaitingTime;

		// Performance Measures CS
		long csTotalDelays = 0;
		Duration csTotalDelayTime = Duration.ZERO;
		Duration csAverageDelay;
		Duration csMaximumDelay = Duration.ZERO;
		long csTotalDepartures = 0;
		float csPercentageOfDelays = 0;

		// Performance Measures PR
		long prTotalDelays = 0;
		Duration prTotalDelayTime = Duration.ZERO;
		Duration prAverageDelay;
		Duration prMaximumDelay = Duration.ZERO;
		long prTotalDepartures = 0;
		float prPercentageOfDelays = 0;

		for (final Performance performance : performances) {
			// Passengers
			totalPassengers += performance.getTotalPassengers();
			totalWaitingTime = totalWaitingTime.plus(performance.getTotalWaitingTime());
			maxWaitingTime = maxWaitingTime.compareTo(performance.getMaxWaitingTime()) < 0
					? performance.getMaxWaitingTime()
					: maxWaitingTime;

			// CS Junction
			csTotalJunctionWaitingTime = csTotalJunctionWaitingTime.plus(performance.getCsTotalJunctionWaitingTime());
			totalCsJunctionArrivals += performance.getCsJunctionArrivals();
			csMaximumJunctionWaitingTime = csMaximumJunctionWaitingTime
					.compareTo(performance.getCsMaximumJunctionWaitingTime()) < 0
							? performance.getCsMaximumJunctionWaitingTime()
							: csMaximumJunctionWaitingTime;

			// PR Junction
			prTotalJunctionWaitingTime = prTotalJunctionWaitingTime.plus(performance.getPrTotalJunctionWaitingTime());
			totalPrJunctionArrivals += performance.getPrJunctionArrivals();
			prMaximumJunctionWaitingTime = prMaximumJunctionWaitingTime
					.compareTo(performance.getPrMaximumJunctionWaitingTime()) < 0
							? performance.getPrMaximumJunctionWaitingTime()
							: prMaximumJunctionWaitingTime;

			// Performance Measures CS
			csTotalDelays += performance.getCsTotalDelays();
			csTotalDelayTime = csTotalDelayTime.plus(performance.getCsTotalDelayTime());
			csMaximumDelay = csMaximumDelay.compareTo(performance.getCsMaximumDelay()) < 0
					? performance.getCsMaximumDelay()
					: csMaximumDelay;
			csTotalDepartures += performance.getCsTotalDepartures();
			csPercentageOfDelays = ((float) csTotalDelays / (float) csTotalDepartures) * 100;

			// Performance Measures PR
			prTotalDelays += performance.getPrTotalDelays();
			prTotalDelayTime = prTotalDelayTime.plus(performance.getPrTotalDelayTime());
			prMaximumDelay = prMaximumDelay.compareTo(performance.getPrMaximumDelay()) < 0
					? performance.getPrMaximumDelay()
					: prMaximumDelay;
			prTotalDepartures += performance.getPrTotalDepartures();
			prPercentageOfDelays = ((float) prTotalDelays / (float) prTotalDepartures) * 100;
		}
		averageWaitingTime = totalPassengers == 0 ? Duration.ZERO : totalWaitingTime.dividedBy(totalPassengers);
		csAverageJunctionWaitingTime = totalCsJunctionArrivals == 0 ? Duration.ZERO
				: csTotalJunctionWaitingTime.dividedBy(totalCsJunctionArrivals);
		prAverageJunctionWaitingTime = totalPrJunctionArrivals == 0 ? Duration.ZERO
				: prTotalJunctionWaitingTime.dividedBy(totalPrJunctionArrivals);
		csAverageDelay = csTotalDelays == 0 ? Duration.ZERO : csTotalDelayTime.dividedBy(csTotalDelays);
		prAverageDelay = prTotalDelays == 0 ? Duration.ZERO : prTotalDelayTime.dividedBy(prTotalDelays);

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("Total passengers: " + totalPassengers);
		System.out.println("Total waiting time in seconds: " + totalWaitingTime.getSeconds());

		System.out.println("Maximum waiting time of a passenger: " + maxWaitingTime.getSeconds());

		System.out.println("Average waiting time: (passengers / waiting time) " + averageWaitingTime.getSeconds());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println(
				"Total waiting time for junction at Centraal Station: " + csTotalJunctionWaitingTime.getSeconds());
		System.out.println("Total junction arrivals: " + totalCsJunctionArrivals);
		System.out.println("Maximum waiting time: " + csMaximumJunctionWaitingTime.getSeconds());
		System.out.println(
				"Average waiting time at junction Centraal Station: " + csAverageJunctionWaitingTime.getSeconds());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("Total waiting time for junction at P+R Uithof: " + prTotalJunctionWaitingTime.getSeconds());
		System.out.println("Total junction arrivals: " + totalPrJunctionArrivals);
		System.out.println("Maximum waiting time: " + prMaximumJunctionWaitingTime.getSeconds());
		System.out.println("Average waiting time at junction P+R Uithof: " + prAverageJunctionWaitingTime.getSeconds());

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("PERFORMANCE MEASURES FOR CENTRAAL STATION:");
		System.out.println("Total departure delays: " + csTotalDelays);
		System.out.println("Total delay amount: " + csTotalDelayTime.getSeconds());

		System.out.println("Average delay time: (total delay amount / total delays): " + csAverageDelay.getSeconds());

		System.out.println("Maximum delay: " + csMaximumDelay.toSeconds());

		System.out.println("Total departure delays: " + csTotalDelays);
		System.out.println("Total departures: " + csTotalDepartures);

		System.out.println("Delay percentage: (delays / total departures): " + csPercentageOfDelays + "%");

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");

		System.out.println("PERFORMANCE MEASURES FOR P+R UITHOF:");
		System.out.println("Total departure delays: " + prTotalDelays);
		System.out.println("Total delay amount: " + prTotalDelayTime.getSeconds());

		System.out.println("Average delay time: (total delay amount / total delays): " + prAverageDelay.getSeconds());

		System.out.println("Maximum delay: " + prMaximumDelay.toSeconds());

		System.out.println("Total departure delays: " + prTotalDelays);
		System.out.println("Total departures: " + prTotalDepartures);

		System.out.println("Delay percentage: (delays / total departures): " + prPercentageOfDelays + "%");

		System.out.println("");
		System.out.println("==================================================================");
		System.out.println("");
	}

	public void addPassenger(final Duration waitingTime) {
		if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7,0))
		&& EventScheduler.getInstance().getCurrentTime().isBefore(LocalTime.of(19,0))) {
			this.totalPassengers += 1;
			this.totalWaitingTime = this.totalWaitingTime.plus(waitingTime);
			if (this.maxWaitingTime.getSeconds() < waitingTime.getSeconds()) {
				this.maxWaitingTime = waitingTime;
			}
		}
	}

	public void addJunctionWaitingTime(final Duration waitingTime, final int junction) {

		if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7,0))
				&& EventScheduler.getInstance().getCurrentTime().isBefore(LocalTime.of(19,0))) {

			// junction denotes the junction. 0 for CS, 1 for P+R.
			if (junction == 0) {
				this.csJunctionArrivals += 1;
				this.csTotalJunctionWaitingTime = this.csTotalJunctionWaitingTime.plus(waitingTime);

				if (this.csMaximumJunctionWaitingTime.getSeconds() < waitingTime.getSeconds()) {
					this.csMaximumJunctionWaitingTime = waitingTime;
				}
			} else {
				this.prJunctionArrivals += 1;
				this.prTotalJunctionWaitingTime = this.prTotalJunctionWaitingTime.plus(waitingTime);

				if (this.prMaximumJunctionWaitingTime.getSeconds() < waitingTime.getSeconds()) {
					this.prMaximumJunctionWaitingTime = waitingTime;
				}
			}
		}
	}

	public void addDelay(final Duration delay, final int endStation) {
		// We add one departure to total departures.
		// We check if the delay is bigger than 1 minute. If so, we add one delay to
		// total_delays and add the amount
		// to total_delay_time.

		// endStation denotes the end station. 0 for CS, 1 for P+R.

		if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7,0))
				&& EventScheduler.getInstance().getCurrentTime().isBefore(LocalTime.of(19,0))) {
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

	public Performance getPerformance() {
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

	public static void serializePerformances(final List<Performance> performanceList) {
		// save the object to file
		FileOutputStream fileStream = null;
		ObjectOutputStream objectStream = null;
		final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH-mm-ss");

		for (int i = 0; i < performanceList.size(); i++) {
			final String fileName = ("PerformanceNr" + (i + 1));
			final Performance performance = performanceList.get(i);
			final File directory = new File(
					System.getProperty("user.dir") + "/output/output-data-" + LocalTime.now().format(dtf));
			directory.mkdirs();
			try {
				fileStream = new FileOutputStream(directory + "/" + fileName);
				objectStream = new ObjectOutputStream(fileStream);
				objectStream.writeObject(performance);

				objectStream.close();
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
