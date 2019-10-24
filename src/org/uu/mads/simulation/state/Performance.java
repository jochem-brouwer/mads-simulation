package org.uu.mads.simulation.state;

import java.time.Duration;
import java.io.Serializable;

public class Performance implements Serializable{
	private final long waiting_passengers_x_minutes; // TODO: split passengers up in categories?

	private final long total_passengers;
	private final Duration total_waiting_time;
	private final Duration average_waiting_time;
	private final Duration max_waiting_time;

	private final long cs_total_delays;
	private final long cs_total_departures;

	private final long pr_total_delays;
	private final long pr_total_departures;

	private final Duration cs_total_delay_time;
	private final Duration cs_average_delay;
	private final Duration cs_maximum_delay;

	private final Duration pr_total_delay_time;
	private final Duration pr_average_delay;
	private final Duration pr_maximum_delay;

	private final float cs_percentage_of_delays;
	private final float pr_percentage_of_delays;

	private final Duration cs_total_junction_waitingTime;
	private final Duration pr_total_junction_waitingTime;
	private final Duration cs_average_junction_waitingTime;
	private final Duration pr_average_junction_waitingTime;
	private final Duration cs_maximum_junction_waitingTime;
	private final Duration pr_maximum_junction_waitingTime;
	private final long cs_junction_arrivals;
	private final long pr_junction_arrivals;

	public Performance(final long waiting_passengers_x_minutes, final long total_passengers,
			final Duration total_waiting_time, final Duration average_waiting_time, final Duration max_waiting_time,
			final long cs_total_delays, final long cs_total_departures, final long pr_total_delays,
			final long pr_total_departures, final Duration cs_total_delay_time, final Duration cs_average_delay,
			final Duration cs_maximum_delay, final Duration pr_total_delay_time, final Duration pr_average_delay,
			final Duration pr_maximum_delay, final float cs_percentage_of_delays, final float pr_percentage_of_delays,
			final Duration cs_total_junction_waitingTime, final Duration pr_total_junction_waitingTime,
			final Duration cs_average_junction_waitingTime, final Duration pr_average_junction_waitingTime,
			final Duration cs_maximum_junction_waitingTime, final Duration pr_maximum_junction_waitingTime,
			final long cs_junction_arrivals, final long pr_junction_arrivals) {
		super();
		this.waiting_passengers_x_minutes = waiting_passengers_x_minutes;
		this.total_passengers = total_passengers;
		this.total_waiting_time = total_waiting_time;
		this.average_waiting_time = average_waiting_time;
		this.max_waiting_time = max_waiting_time;
		this.cs_total_delays = cs_total_delays;
		this.cs_total_departures = cs_total_departures;
		this.pr_total_delays = pr_total_delays;
		this.pr_total_departures = pr_total_departures;
		this.cs_total_delay_time = cs_total_delay_time;
		this.cs_average_delay = cs_average_delay;
		this.cs_maximum_delay = cs_maximum_delay;
		this.pr_total_delay_time = pr_total_delay_time;
		this.pr_average_delay = pr_average_delay;
		this.pr_maximum_delay = pr_maximum_delay;
		this.cs_percentage_of_delays = cs_percentage_of_delays;
		this.pr_percentage_of_delays = pr_percentage_of_delays;
		this.cs_total_junction_waitingTime = cs_total_junction_waitingTime;
		this.pr_total_junction_waitingTime = pr_total_junction_waitingTime;
		this.cs_average_junction_waitingTime = cs_average_junction_waitingTime;
		this.pr_average_junction_waitingTime = pr_average_junction_waitingTime;
		this.cs_maximum_junction_waitingTime = cs_maximum_junction_waitingTime;
		this.pr_maximum_junction_waitingTime = pr_maximum_junction_waitingTime;
		this.cs_junction_arrivals = cs_junction_arrivals;
		this.pr_junction_arrivals = pr_junction_arrivals;
	}

	public long getWaiting_passengers_x_minutes() {
		return this.waiting_passengers_x_minutes;
	}

	public long getTotal_passengers() {
		return this.total_passengers;
	}

	public Duration getTotal_waiting_time() {
		return this.total_waiting_time;
	}

	public Duration getAverage_waiting_time() {
		return this.average_waiting_time;
	}

	public Duration getMax_waiting_time() {
		return this.max_waiting_time;
	}

	public long getCs_total_delays() {
		return this.cs_total_delays;
	}

	public long getCs_total_departures() {
		return this.cs_total_departures;
	}

	public long getPr_total_delays() {
		return this.pr_total_delays;
	}

	public long getPr_total_departures() {
		return this.pr_total_departures;
	}

	public Duration getCs_total_delay_time() {
		return this.cs_total_delay_time;
	}

	public Duration getCs_average_delay() {
		return this.cs_average_delay;
	}

	public Duration getCs_maximum_delay() {
		return this.cs_maximum_delay;
	}

	public Duration getPr_total_delay_time() {
		return this.pr_total_delay_time;
	}

	public Duration getPr_average_delay() {
		return this.pr_average_delay;
	}

	public Duration getPr_maximum_delay() {
		return this.pr_maximum_delay;
	}

	public float getCs_percentage_of_delays() {
		return this.cs_percentage_of_delays;
	}

	public float getPr_percentage_of_delays() {
		return this.pr_percentage_of_delays;
	}

	public Duration getCs_total_junction_waitingTime() {
		return this.cs_total_junction_waitingTime;
	}

	public Duration getPr_total_junction_waitingTime() {
		return this.pr_total_junction_waitingTime;
	}

	public Duration getCs_average_junction_waitingTime() {
		return this.cs_average_junction_waitingTime;
	}

	public Duration getPr_average_junction_waitingTime() {
		return this.pr_average_junction_waitingTime;
	}

	public Duration getCs_maximum_junction_waitingTime() {
		return this.cs_maximum_junction_waitingTime;
	}

	public Duration getPr_maximum_junction_waitingTime() {
		return this.pr_maximum_junction_waitingTime;
	}

	public long getCs_junction_arrivals() {
		return this.cs_junction_arrivals;
	}

	public long getPr_junction_arrivals() {
		return this.pr_junction_arrivals;
	}

	@Override
	public String toString() {
		return "Performance [waiting_passengers_x_minutes=" + this.waiting_passengers_x_minutes + ", total_passengers="
				+ this.total_passengers + ", total_waiting_time=" + this.total_waiting_time + ", average_waiting_time="
				+ this.average_waiting_time + ", max_waiting_time=" + this.max_waiting_time + ", cs_total_delays="
				+ this.cs_total_delays + ", cs_total_departures=" + this.cs_total_departures + ", pr_total_delays="
				+ this.pr_total_delays + ", pr_total_departures=" + this.pr_total_departures + ", cs_total_delay_time="
				+ this.cs_total_delay_time + ", cs_average_delay=" + this.cs_average_delay + ", cs_maximum_delay="
				+ this.cs_maximum_delay + ", pr_total_delay_time=" + this.pr_total_delay_time + ", pr_average_delay="
				+ this.pr_average_delay + ", pr_maximum_delay=" + this.pr_maximum_delay + ", cs_percentage_of_delays="
				+ this.cs_percentage_of_delays + ", pr_percentage_of_delays=" + this.pr_percentage_of_delays
				+ ", cs_total_junction_waitingTime=" + this.cs_total_junction_waitingTime
				+ ", pr_total_junction_waitingTime=" + this.pr_total_junction_waitingTime
				+ ", cs_average_junction_waitingTime=" + this.cs_average_junction_waitingTime
				+ ", pr_average_junction_waitingTime=" + this.pr_average_junction_waitingTime
				+ ", cs_maximum_junction_waitingTime=" + this.cs_maximum_junction_waitingTime
				+ ", pr_maximum_junction_waitingTime=" + this.pr_maximum_junction_waitingTime
				+ ", cs_junction_arrivals=" + this.cs_junction_arrivals + ", pr_junction_arrivals="
				+ this.pr_junction_arrivals + "]";
	}

}
