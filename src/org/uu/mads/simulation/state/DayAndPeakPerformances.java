package org.uu.mads.simulation.state;

public class DayAndPeakPerformances {

	public Performance dailyPerformance;
	public Performance peakPerformance;

	public DayAndPeakPerformances(final Performance dailyPerformance, final Performance peakPerformance) {
		this.dailyPerformance = dailyPerformance;
		this.peakPerformance = peakPerformance;
	}

	public Performance getDailyPerformance() {
		return this.dailyPerformance;
	}

	public Performance getPeakPerformance() {
		return this.peakPerformance;
	}

}