package org.uu.mads.simulation.events;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class ScheduledLeaveEndStation extends Event {
    // Initially schedule this event once

    private List<LocalTime> departures;
    private EndStation endStation;


    public ScheduledLeaveEndStation(EndStation endStation) {
        this.endStation = endStation;
    }

    public void fire() {

        EventScheduler scheduler = EventScheduler.get();
        for (int i = 0; i < departures.size(); i++) {
            LocalTime nextLeave = departures.get(i);
            // If the current time is smaller than the nextLeave time, schedule this again
            if (scheduler.getCurrentTime().compareTo(nextLeave) == 0) {
                scheduler.scheduleEvent(this, nextLeave);
            } else {
                TryOccupyJunctionEvent occupy = new TryOccupyJunctionEvent(this.endStation);
                scheduler.scheduleEventAhead(occupy, Duration.ZERO);
            }
        }
    }
}
