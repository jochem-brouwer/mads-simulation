package org.uu.mads.simulation;

import java.time.Duration;

public class Performance {

    private static Performance instance = null;

    private long waiting_passengers_x_minutes; // TODO: split passengers up in categories?

    private long total_passengers = 0;
    private Duration total_waiting_time = Duration.ofSeconds(0);
    private Duration average_waiting_time = Duration.ofSeconds(0);

    private long total_delays = 0;
    private long total_departures = 0;
    private Duration total_delay_time = Duration.ofSeconds(0);
    private Duration average_delay = Duration.ofSeconds(0);
    private Duration maximum_delay = Duration.ofSeconds(0);
    private float percentage_of_delays;

    private Performance() {
        // private constructor because this is a singleton
    }

    public static Performance get() {
        if (instance == null) {
            instance = new Performance();
        }
        return instance;
    }

    public void addPassenger(Duration waitingTime) {

        this.total_passengers += 1;
        this.total_waiting_time = this.total_waiting_time.plus(waitingTime);
    }

    public void addDelay(Duration delay) {
        // We add one departure to total departures.
        this.total_departures += 1;

        // We check if the delay is bigger than 1 minute. If so, we add one delay to total_delays and add the amount
        // to total_delay_time.
        if (delay.compareTo(Duration.ofMinutes(1)) >= 0 && (delay.isNegative() == false)) {
            this.total_delay_time = this.total_delay_time.plus(delay);
            if (delay.compareTo(this.maximum_delay) >= 0) {
                this.maximum_delay = delay;
            }
            this.total_delays += 1;
        }
    }

    public Duration calculateAverageWaitingTime() {

        this.average_waiting_time = this.total_waiting_time.dividedBy(this.total_passengers);

        System.out.println("Total passengers: " + this.total_passengers);
        System.out.println("Total waiting time in seconds: " + this.total_waiting_time.getSeconds());

        System.out.println("Average waiting time: (passengers / waiting time) " + this.average_waiting_time.getSeconds());

        return average_waiting_time;
    }

    public Duration calculateAveragePunctuality() {

        if(total_delays != 0) {
            this.average_delay = this.total_delay_time.dividedBy(total_delays);
        } else { this.average_delay = Duration.ZERO; }

        this.percentage_of_delays = ((float)this.total_delays / (float)this.total_departures) * 100;

        System.out.println("Total departure delays: " + this.total_delays);
        System.out.println("Total delay amount: " + this.total_delay_time.getSeconds());

        System.out.println("Average delay time: (total delay amount / total delays): " + this.average_delay.getSeconds());

        System.out.println("Maximum delay: " + this.maximum_delay.toSeconds());

        System.out.println("Total departure delays: " + this.total_delays);
        System.out.println("Total departures: " + this.total_departures);

        System.out.println("Delay percentage: (delays / total departures): " + this.percentage_of_delays);

        return average_delay;
    }
}
