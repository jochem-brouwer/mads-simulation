package org.uu.mads.simulation;

import java.time.Duration;
import java.time.LocalTime;

import org.uu.mads.simulation.events.ScheduledLeaveEndStationEvent;
import org.uu.mads.simulation.events.TryOccupyJunctionEvent;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPoint;

public class Simulation {

	public static final int TRAMS_PER_HOUR = 16; // This is fixed during our simulation run.
	public static final Duration TURN_AROUND_DURATION = Duration.ofMinutes(4); // Turn around time is 4 min.
	public static final LocalTime FIRST_SCHEDULED_LEAVE_TIME_PR = LocalTime.of(6, 0); // TODO: Adapt
	public static final LocalTime FIRST_PASSENGER_CALC = LocalTime.of(6, 0); // TODO: Adapt
	public static final Duration TRAM_LEAVE_FREQUENCY = Duration.ofSeconds(225); // This is 3.75 minutes with 16 trams
	public static final int NUMBER_OF_TRAMS = 3;

	private static EndStation centraalEndStation;
	private static EndStation uithofEndStation;
	private static LocalTime firstScheduledLeaveTimeCS;

	/**
	 * Calculates the first schedules leave time for central station.
	 */
	private static void calculateCSLeave() {
		LocalTime firstRound = FIRST_SCHEDULED_LEAVE_TIME_PR.plus(Duration.ofMinutes(17).plus(TURN_AROUND_DURATION));
		while (firstRound.compareTo(FIRST_SCHEDULED_LEAVE_TIME_PR.plus(TRAM_LEAVE_FREQUENCY)) == 1) {
			firstRound = firstRound.minus(TRAM_LEAVE_FREQUENCY);
		}

		System.out.println("First scheduled leave at PR: " + FIRST_SCHEDULED_LEAVE_TIME_PR);
		System.out.println("First scheduled leave at CS: " + firstRound);
		firstScheduledLeaveTimeCS = firstRound;
	}

	public static void main(final String[] args) {
		calculateCSLeave();

		initializeState();

		final ScheduledLeaveEndStationEvent scheduledLeaveEndStationCentraalEvent = new ScheduledLeaveEndStationEvent(
				centraalEndStation);
		final ScheduledLeaveEndStationEvent scheduledLeaveEndStationUithofEvent = new ScheduledLeaveEndStationEvent(
				uithofEndStation);

		EventScheduler.get().scheduleEvent(scheduledLeaveEndStationCentraalEvent, firstScheduledLeaveTimeCS);
		EventScheduler.get().scheduleEvent(scheduledLeaveEndStationUithofEvent, FIRST_SCHEDULED_LEAVE_TIME_PR);
		// TODO: Schedule initial Event

		while (LocalTime.of(12, 0).isAfter(EventScheduler.get().getCurrentTime())) { // TODO: We need better end conditions
			EventScheduler.get().fireNextEvent();
			//System.out.println(EventScheduler.get().getScheduledEventsByTime());
		}
	}

	private static void initializeState() {
		// Junctions
		final Junction centraalJunction = new Junction();
		final Junction uithofJunction = new Junction();

		// Platforms Direction A -> Uithof
		centraalEndStation = new EndStation("Centraal Station", centraalJunction, firstScheduledLeaveTimeCS,
				Duration.ofSeconds(134));
		final IntPlatform vrPlatformA = new IntPlatform("Vaartsche-Rijn-A", Duration.ofSeconds(243));
		final IntPlatform gwPlatformA = new IntPlatform("Galgenwaard-A", Duration.ofSeconds(59));
		final IntPlatform krPlatformA = new IntPlatform("Kromme-Rijn-A", Duration.ofSeconds(101));
		final IntPlatform plPlatformA = new IntPlatform("Padualaan-A", Duration.ofSeconds(60));
		final IntPlatform hlPlatformA = new IntPlatform("Heidelberglaan-A", Duration.ofSeconds(86));
		final IntPlatform umcPlatformA = new IntPlatform("UMC-A", Duration.ofSeconds(78));
		final IntPlatform wkzPlatformA = new IntPlatform("WKZ-A", Duration.ofSeconds(113));

		// Platforms Direction B -> Centraal
		uithofEndStation = new EndStation("P+R De Uithof", uithofJunction, FIRST_SCHEDULED_LEAVE_TIME_PR,
				Duration.ofSeconds(110));
		final IntPlatform wkzPlatformB = new IntPlatform("WKZ-B", Duration.ofSeconds(78));
		final IntPlatform umcPlatformB = new IntPlatform("UMC-B", Duration.ofSeconds(82));
		final IntPlatform hlPlatformB = new IntPlatform("Heidelberglaan-B", Duration.ofSeconds(60));
		final IntPlatform plPlatformB = new IntPlatform("Padualaan-B", Duration.ofSeconds(100));
		final IntPlatform krPlatformB = new IntPlatform("Kromme-Rijn-B", Duration.ofSeconds(59));
		final IntPlatform gwPlatformB = new IntPlatform("Galgenwaard-B", Duration.ofSeconds(243));
		final IntPlatform vrPlatformB = new IntPlatform("Vaartsche-Rijn-B", Duration.ofSeconds(135));

		// Waiting Points Direction A -> Uithof
		final WaitingPoint vrWaitingPointPlA = new WaitingPoint(vrPlatformA);
		final WaitingPoint gwWaitingPointPlA = new WaitingPoint(gwPlatformA);
		final WaitingPoint krWaitingPointPlA = new WaitingPoint(krPlatformA);
		final WaitingPoint plWaitingPointPlA = new WaitingPoint(plPlatformA);
		final WaitingPoint hlWaitingPointPlA = new WaitingPoint(hlPlatformA);
		final WaitingPoint umcWaitingPointPlA = new WaitingPoint(umcPlatformA);
		final WaitingPoint wkzWaitingPointPlA = new WaitingPoint(wkzPlatformA);
		final WaitingPoint uithofWaitingPoint = new WaitingPoint(uithofEndStation);

		// Waiting Points Direction B -> Centraal
		final WaitingPoint wkzWaitingPointPlB = new WaitingPoint(wkzPlatformB);
		final WaitingPoint umcWaitingPointPlB = new WaitingPoint(umcPlatformB);
		final WaitingPoint hlWaitingPointPlB = new WaitingPoint(hlPlatformB);
		final WaitingPoint plWaitingPointPlB = new WaitingPoint(plPlatformB);
		final WaitingPoint krWaitingPointPlB = new WaitingPoint(krPlatformB);
		final WaitingPoint gwWaitingPointPlB = new WaitingPoint(gwPlatformB);
		final WaitingPoint vrWaitingPointPlB = new WaitingPoint(vrPlatformB);
		final WaitingPoint centraalWaitingPoint = new WaitingPoint(centraalEndStation);

		// Waiting Point Chain at Platforms Direction A -> Uithof
		centraalEndStation.setLastWaitingPoint(centraalWaitingPoint);
		centraalEndStation.setNextWaitingPoint(vrWaitingPointPlA);
		vrPlatformA.setLastWaitingPoint(vrWaitingPointPlA);
		vrPlatformA.setNextWaitingPoint(gwWaitingPointPlA);
		gwPlatformA.setLastWaitingPoint(gwWaitingPointPlA);
		gwPlatformA.setNextWaitingPoint(krWaitingPointPlA);
		krPlatformA.setLastWaitingPoint(krWaitingPointPlA);
		krPlatformA.setNextWaitingPoint(plWaitingPointPlA);
		plPlatformA.setLastWaitingPoint(plWaitingPointPlA);
		plPlatformA.setNextWaitingPoint(hlWaitingPointPlA);
		hlPlatformA.setLastWaitingPoint(hlWaitingPointPlA);
		hlPlatformA.setNextWaitingPoint(umcWaitingPointPlA);
		umcPlatformA.setLastWaitingPoint(umcWaitingPointPlA);
		umcPlatformA.setNextWaitingPoint(wkzWaitingPointPlA);
		wkzPlatformA.setLastWaitingPoint(wkzWaitingPointPlA);
		wkzPlatformA.setNextWaitingPoint(uithofWaitingPoint);

		// Waiting Point Chain at Platforms Direction B -> Centraal
		uithofEndStation.setLastWaitingPoint(uithofWaitingPoint);
		uithofEndStation.setNextWaitingPoint(wkzWaitingPointPlB);
		wkzPlatformB.setLastWaitingPoint(wkzWaitingPointPlB);
		wkzPlatformB.setNextWaitingPoint(umcWaitingPointPlB);
		umcPlatformB.setLastWaitingPoint(umcWaitingPointPlB);
		umcPlatformB.setNextWaitingPoint(hlWaitingPointPlB);
		hlPlatformB.setLastWaitingPoint(hlWaitingPointPlB);
		hlPlatformB.setNextWaitingPoint(plWaitingPointPlB);
		plPlatformB.setLastWaitingPoint(plWaitingPointPlB);
		plPlatformB.setNextWaitingPoint(krWaitingPointPlB);
		krPlatformB.setLastWaitingPoint(krWaitingPointPlB);
		krPlatformB.setNextWaitingPoint(gwWaitingPointPlB);
		gwPlatformB.setLastWaitingPoint(gwWaitingPointPlB);
		gwPlatformB.setNextWaitingPoint(vrWaitingPointPlB);
		vrPlatformB.setLastWaitingPoint(vrWaitingPointPlB);
		vrPlatformB.setNextWaitingPoint(centraalWaitingPoint);

		// Add trams
		int tramId = 1;
		uithofEndStation.setTramOnPlatformA(new Tram(tramId++, 0));
		uithofEndStation.setTramOnPlatformB(new Tram(tramId++, 0));
		centraalEndStation.setTramOnPlatformB(new Tram(tramId++, 0));
	}
}
