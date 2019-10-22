package org.uu.mads.simulation.input;

import java.time.LocalTime;

public class InputStationData {

    private final String name;
    private final int direction;
    private final LocalTime time1;
    private final LocalTime time2;
    private final float passIn;
    private final float passOut;

    public String getName() {
        return this.name;
    }

    public int getDirection() {
        return this.direction;
    }

    public LocalTime getTime1() {
        return time1;
    }

    public LocalTime getTime2() {
        return time2;
    }

    public float getPassIn() {
        return passIn;
    }

    public float getPassOut() {
        return passOut;
    }

    @Override
    public String toString() {
        return "InputStationData{" +
                "name='" + name + '\'' +
                ", direction=" + direction +
                ", time1=" + time1 +
                ", time2=" + time2 +
                ", passIn=" + passIn +
                ", passOut=" + passOut +
                '}';
    }

    public InputStationData(String name, int direction, LocalTime time1, LocalTime time2, float passOut, float passIn) {
        this.name = name;
        this.direction = direction;
        this.time1 = time1;
        this.time2 = time2;
        this.passIn = passIn;
        this.passOut = passOut;
    }
}
