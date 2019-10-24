package org.uu.mads.simulation.state;

public class DayAndPeakPerformances {

    public Performance dailyPerformance;
    public Performance peakPerformance;

    public DayAndPeakPerformances(Performance dailyPerformance, Performance peakPerformance){
        this.dailyPerformance = dailyPerformance;
        this.peakPerformance = peakPerformance;
    }


}