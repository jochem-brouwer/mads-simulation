package org.uu.mads.simulation.state;

public class DayAndPeakPerformances {

    private Performance dailyPerformance;
    private Performance peakPerformance;

    public DayAndPeakPerformances(Performance dailyPerformance, Performance peakPerformance){

        this.dailyPerformance = dailyPerformance;
        this.peakPerformance = peakPerformance;
    }


}