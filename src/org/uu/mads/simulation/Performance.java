package org.uu.mads.simulation;

import org.uu.mads.simulation.state.EndStation;

import java.time.Duration;

public class Performance {

	private static Performance instance = null;

	private long waiting_passengers_x_minutes; // TODO: split passengers up in categories?

	private long total_passengers = 0;
	private Duration total_waiting_time = Duration.ZERO;
	private Duration average_waiting_time = Duration.ZERO;
	private Duration max_waiting_time = Duration.ZERO;

	private long cs_total_delays = 0;
	private long cs_total_departures = 0;

	private long pr_total_delays = 0;
	private long pr_total_departures = 0;

	private Duration cs_total_delay_time = Duration.ZERO;
	private Duration cs_average_delay = Duration.ZERO;
	private Duration cs_maximum_delay = Duration.ZERO;

	private Duration pr_total_delay_time = Duration.ZERO;
	private Duration pr_average_delay = Duration.ZERO;
	private Duration pr_maximum_delay = Duration.ZERO;

	private float cs_percentage_of_delays;
	private float pr_percentage_of_delays;

	private Duration cs_total_junction_waitingTime = Duration.ZERO;
	private Duration pr_total_junction_waitingTime = Duration.ZERO;
	private Duration cs_average_junction_waitingTime = Duration.ZERO;
	private Duration pr_average_junction_waitingTime = Duration.ZERO;
	private Duration cs_maximum_junction_waitingTime = Duration.ZERO;
	private Duration pr_maximum_junction_waitingTime = Duration.ZERO;
	private long cs_junction_arrivals = 0;
	private long pr_junction_arrivals = 0;

	private Performance() {
		// private constructor because this is a singleton
	}

	public static Performance getInstance() {
		if (instance == null) {
			instance = new Performance();
		}
		return instance;
	}

	public void addPassenger(final Duration waitingTime) {
		this.total_passengers += 1;
		this.total_waiting_time = this.total_waiting_time.plus(waitingTime);
		if (this.max_waiting_time.getSeconds() < waitingTime.getSeconds()) {
			this.max_waiting_time = waitingTime;
		}
	}

	public void addJunctionWaitingTime(Duration waitingTime, int junction) {

		// junction denotes the junction. 0 for CS, 1 for P+R.
		if (junction == 0) {
			this.cs_junction_arrivals += 1;
			this.cs_total_junction_waitingTime = this.cs_total_junction_waitingTime.plus(waitingTime);

			if (this.cs_maximum_junction_waitingTime.getSeconds() < waitingTime.getSeconds()) {
				this.cs_maximum_junction_waitingTime = waitingTime;
			}
		} else {
			this.pr_junction_arrivals += 1;
			this.pr_total_junction_waitingTime = this.pr_total_junction_waitingTime.plus(waitingTime);

			if (this.pr_maximum_junction_waitingTime.getSeconds() < waitingTime.getSeconds()) {
				this.pr_maximum_junction_waitingTime = waitingTime;
			}
		}
	}

	public void addDelay(Duration delay, int endStation) {
		// We add one departure to total departures.
		// We check if the delay is bigger than 1 minute. If so, we add one delay to
		// total_delays and add the amount
		// to total_delay_time.

		// endStation denotes the end station. 0 for CS, 1 for P+R.

		if (endStation == 0){
			this.cs_total_departures += 1;

			if ((delay.compareTo(Duration.ofMinutes(1)) >= 0) && (delay.isNegative() == false)) {
				this.cs_total_delay_time = this.cs_total_delay_time.plus(delay);
				if (delay.compareTo(this.cs_maximum_delay) >= 0) {
					this.cs_maximum_delay = delay;
				}
				this.cs_total_delays += 1;
			}

		}else {
			this.pr_total_departures += 1;
			if ((delay.compareTo(Duration.ofMinutes(1)) >= 0) && (delay.isNegative() == false)) {
				this.pr_total_delay_time = this.pr_total_delay_time.plus(delay);
				if (delay.compareTo(this.pr_maximum_delay) >= 0) {
					this.pr_maximum_delay = delay;
				}
				this.pr_total_delays += 1;
			}
		}
	}

	public Duration calculateAverageWaitingTime() {

		if (this.total_passengers != 0) {
			this.average_waiting_time = this.total_waiting_time.dividedBy(this.total_passengers);
		}

		System.out.println("");
		System.out.println("Total passengers: " + this.total_passengers);
		System.out.println("Total waiting time in seconds: " + this.total_waiting_time.getSeconds());

		System.out.println("Maximum waiting time of a passenger: " + this.max_waiting_time.getSeconds());

		System.out.println(
				"Average waiting time: (passengers / waiting time) " + this.average_waiting_time.getSeconds());

		return this.average_waiting_time;
	}

	public Duration calculateJunctionWaitingTime() {

		this.cs_average_junction_waitingTime = this.cs_total_junction_waitingTime.dividedBy(this.cs_junction_arrivals);
		this.pr_average_junction_waitingTime = this.pr_total_junction_waitingTime.dividedBy(this.pr_junction_arrivals);

		System.out.println("");
		System.out.println("Total waiting time for junction at Centraal Station: "
				+ this.cs_total_junction_waitingTime.getSeconds());
		System.out.println("Total junction arrivals: " + this.cs_junction_arrivals);
		System.out.println("Maximum waiting time: " + this.cs_maximum_junction_waitingTime.getSeconds());
		System.out.println("Average waiting time at junction Centraal Station: "
				+ this.cs_average_junction_waitingTime.getSeconds());

		System.out.println("");
		System.out.println("Total waiting time for junction at P+R Uithof: "
				+ this.pr_total_junction_waitingTime.getSeconds());
		System.out.println("Total junction arrivals: " + this.pr_junction_arrivals);
		System.out.println("Maximum waiting time: " + this.pr_maximum_junction_waitingTime.getSeconds());
		System.out.println("Average waiting time at junction P+R Uithof: "
				+ this.pr_average_junction_waitingTime.getSeconds());

		return this.pr_average_junction_waitingTime;
	}

	public Duration calculateAveragePunctuality() {

		// Calculate average punctuality for CS:
		if (this.cs_total_delays != 0) {
			this.cs_average_delay = this.cs_total_delay_time.dividedBy(this.cs_total_delays);
		} else {
			this.cs_average_delay = Duration.ZERO;
		}
		this.cs_percentage_of_delays = ((float) this.cs_total_delays / (float) this.cs_total_departures) * 100;

		System.out.println("");
		System.out.println("PERFORMANCE MEASURES FOR CENTRAAL STATION:");
		System.out.println("Total departure delays: " + this.cs_total_delays);
		System.out.println("Total delay amount: " + this.cs_total_delay_time.getSeconds());

		System.out
				.println("Average delay time: (total delay amount / total delays): " + this.cs_average_delay.getSeconds());

		System.out.println("Maximum delay: " + this.cs_maximum_delay.toSeconds());

		System.out.println("Total departure delays: " + this.cs_total_delays);
		System.out.println("Total departures: " + this.cs_total_departures);

		System.out.println("Delay percentage: (delays / total departures): " + this.cs_percentage_of_delays+ "%");
		System.out.println("==================================================================");
		System.out.println("");

		// We do the same for P+R:
		if (this.pr_total_delays != 0) {
			this.pr_average_delay = this.pr_total_delay_time.dividedBy(this.pr_total_delays);
		} else {
			this.pr_average_delay = Duration.ZERO;
		}
		this.pr_percentage_of_delays = ((float) this.pr_total_delays / (float) this.pr_total_departures) * 100;

		System.out.println("");
		System.out.println("PERFORMANCE MEASURES FOR P+R UITHOF:");
		System.out.println("Total departure delays: " + this.pr_total_delays);
		System.out.println("Total delay amount: " + this.pr_total_delay_time.getSeconds());

		System.out
				.println("Average delay time: (total delay amount / total delays): " + this.pr_average_delay.getSeconds());

		System.out.println("Maximum delay: " + this.pr_maximum_delay.toSeconds());

		System.out.println("Total departure delays: " + this.pr_total_delays);
		System.out.println("Total departures: " + this.pr_total_departures);

		System.out.println("Delay percentage: (delays / total departures): " + this.pr_percentage_of_delays + "%");
		System.out.println("==================================================================");
		System.out.println("");

		return this.cs_average_delay;
	}
}
