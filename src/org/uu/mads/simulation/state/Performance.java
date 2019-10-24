package org.uu.mads.simulation.state;

import java.time.Duration;
import java.io.Serializable;

public class Performance implements Serializable{
    private final long totalPassengers;
    private final Duration totalWaitingTime;
    private final Duration averageWaitingTime;
    private final Duration maxWaitingTime;

	private final long csTotalDelays;
	private final long csTotalDepartures;

	private final long prTotalDelays;
	private final long prTotalDepartures;

	private final Duration csTotalDelayTime;
	private final Duration csAverageDelay;
	private final Duration csMaximumDelay;

	private final Duration prTotalDelayTime;
	private final Duration prAverageDelay;
	private final Duration prMaximumDelay;

	private final float csPercentageOfDelays;
	private final float prPercentageOfDelays;

	private final Duration csTotalJunctionWaitingTime;
	private final Duration prTotalJunctionWaitingTime;
	private final Duration csAverageJunctionWaitingTime;
	private final Duration prAverageJunctionWaitingTime;
	private final Duration csMaximumJunctionWaitingTime;
	private final Duration prMaximumJunctionWaitingTime;
	private final long csJunctionArrivals;
	private final long prJunctionArrivals;

	public Performance(final long total_passengers, final Duration total_waiting_time,
			final Duration average_waiting_time, final Duration max_waiting_time, final long cs_total_delays,
			final long cs_total_departures, final long pr_total_delays, final long pr_total_departures,
			final Duration cs_total_delay_time, final Duration cs_average_delay, final Duration cs_maximum_delay,
			final Duration pr_total_delay_time, final Duration pr_average_delay, final Duration pr_maximum_delay,
			final float cs_percentage_of_delays, final float pr_percentage_of_delays,
			final Duration cs_total_junction_waitingTime, final Duration pr_total_junction_waitingTime,
			final Duration cs_average_junction_waitingTime, final Duration pr_average_junction_waitingTime,
			final Duration cs_maximum_junction_waitingTime, final Duration pr_maximum_junction_waitingTime,
			final long cs_junction_arrivals, final long pr_junction_arrivals) {
		super();
		this.totalPassengers = total_passengers;
		this.totalWaitingTime = total_waiting_time;
		this.averageWaitingTime = average_waiting_time;
		this.maxWaitingTime = max_waiting_time;
		this.csTotalDelays = cs_total_delays;
		this.csTotalDepartures = cs_total_departures;
		this.prTotalDelays = pr_total_delays;
		this.prTotalDepartures = pr_total_departures;
		this.csTotalDelayTime = cs_total_delay_time;
		this.csAverageDelay = cs_average_delay;
		this.csMaximumDelay = cs_maximum_delay;
		this.prTotalDelayTime = pr_total_delay_time;
		this.prAverageDelay = pr_average_delay;
		this.prMaximumDelay = pr_maximum_delay;
		this.csPercentageOfDelays = cs_percentage_of_delays;
		this.prPercentageOfDelays = pr_percentage_of_delays;
		this.csTotalJunctionWaitingTime = cs_total_junction_waitingTime;
		this.prTotalJunctionWaitingTime = pr_total_junction_waitingTime;
		this.csAverageJunctionWaitingTime = cs_average_junction_waitingTime;
		this.prAverageJunctionWaitingTime = pr_average_junction_waitingTime;
		this.csMaximumJunctionWaitingTime = cs_maximum_junction_waitingTime;
		this.prMaximumJunctionWaitingTime = pr_maximum_junction_waitingTime;
		this.csJunctionArrivals = cs_junction_arrivals;
		this.prJunctionArrivals = pr_junction_arrivals;
	}

	public long getTotalPassengers() {
		return this.totalPassengers;
	}

	public Duration getTotalWaitingTime() {
		return this.totalWaitingTime;
	}

	public Duration getAverageWaitingTime() {
		return this.averageWaitingTime;
	}

	public Duration getMaxWaitingTime() {
		return this.maxWaitingTime;
	}

	public long getCsTotalDelays() {
		return this.csTotalDelays;
	}

	public long getCsTotalDepartures() {
		return this.csTotalDepartures;
	}

	public long getPrTotalDelays() {
		return this.prTotalDelays;
	}

	public long getPrTotalDepartures() {
		return this.prTotalDepartures;
	}

	public Duration getCsTotalDelayTime() {
		return this.csTotalDelayTime;
	}

	public Duration getCsAverageDelay() {
		return this.csAverageDelay;
	}

	public Duration getCsMaximumDelay() {
		return this.csMaximumDelay;
	}

	public Duration getPrTotalDelayTime() {
		return this.prTotalDelayTime;
	}

	public Duration getPrAverageDelay() {
		return this.prAverageDelay;
	}

	public Duration getPrMaximumDelay() {
		return this.prMaximumDelay;
	}

	public float getCsPercentageOfDelays() {
		return this.csPercentageOfDelays;
	}

	public float getPrPercentageOfDelays() {
		return this.prPercentageOfDelays;
	}

	public Duration getCsTotalJunctionWaitingTime() {
		return this.csTotalJunctionWaitingTime;
	}

	public Duration getPrTotalJunctionWaitingTime() {
		return this.prTotalJunctionWaitingTime;
	}

	public Duration getCsAverageJunctionWaitingTime() {
		return this.csAverageJunctionWaitingTime;
	}

	public Duration getPrAverageJunctionWaitingTime() {
		return this.prAverageJunctionWaitingTime;
	}

	public Duration getCsMaximumJunctionWaitingTime() {
		return this.csMaximumJunctionWaitingTime;
	}

	public Duration getPrMaximumJunctionWaitingTime() {
		return this.prMaximumJunctionWaitingTime;
	}

	public long getCsJunctionArrivals() {
		return this.csJunctionArrivals;
	}

	public long getPrJunctionArrivals() {
		return this.prJunctionArrivals;
	}

	@Override
	public String toString() {
		return "Performance [total_passengers=" + this.totalPassengers + ", total_waiting_time="
				+ this.totalWaitingTime + ", average_waiting_time=" + this.averageWaitingTime
				+ ", max_waiting_time=" + this.maxWaitingTime + ", cs_total_delays=" + this.csTotalDelays
				+ ", cs_total_departures=" + this.csTotalDepartures + ", pr_total_delays=" + this.prTotalDelays
				+ ", pr_total_departures=" + this.prTotalDepartures + ", cs_total_delay_time="
				+ this.csTotalDelayTime + ", cs_average_delay=" + this.csAverageDelay + ", cs_maximum_delay="
				+ this.csMaximumDelay + ", pr_total_delay_time=" + this.prTotalDelayTime + ", pr_average_delay="
				+ this.prAverageDelay + ", pr_maximum_delay=" + this.prMaximumDelay + ", cs_percentage_of_delays="
				+ this.csPercentageOfDelays + ", pr_percentage_of_delays=" + this.prPercentageOfDelays
				+ ", cs_total_junction_waitingTime=" + this.csTotalJunctionWaitingTime
				+ ", pr_total_junction_waitingTime=" + this.prTotalJunctionWaitingTime
				+ ", cs_average_junction_waitingTime=" + this.csAverageJunctionWaitingTime
				+ ", pr_average_junction_waitingTime=" + this.prAverageJunctionWaitingTime
				+ ", cs_maximum_junction_waitingTime=" + this.csMaximumJunctionWaitingTime
				+ ", pr_maximum_junction_waitingTime=" + this.prMaximumJunctionWaitingTime
				+ ", cs_junction_arrivals=" + this.csJunctionArrivals + ", pr_junction_arrivals="
				+ this.prJunctionArrivals + "]";
	}

}
