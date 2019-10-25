package org.uu.mads.simulation;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.uu.mads.simulation.events.ArriveWaitingPointEvent;
import org.uu.mads.simulation.events.ScheduledLeaveEndStationEvent;
import org.uu.mads.simulation.state.DayAndPeakPerformances;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Performance;
import org.uu.mads.simulation.state.Platform;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPoint;

public class Simulation {
	public static final int NUMBER_OF_RUNS = 1000;

	public static final Duration TURN_AROUND_DURATION = Duration.ofMinutes(4); // Turn around time is 4 min.
	public static final double PASSENGER_IN_MULTIPLICATOR = 1; // 1 -> 100%
	public static final int NUMBER_OF_TRAMS = 16; // number of trams we want to deploy

	public static final boolean ARTIFICIAL_DATA = false;
	public static final boolean ARTIFICIAL_DRIVING_TIME = false;
	public static final String CSV_PATH_POISS_PASS_IN_ART1 = "data/artificial-input-data-passengers-02.csv";

	public static final double TRAM_PASSG_DWELL_TIME_FACTOR = 0; // adds a relationship between the passengers in the
																	// tram and the dwell time.

	public static final boolean LOG = false; // flag to enable/disable logging
	public static final boolean LOG_VERBOSE = false; // flag to enable/disable verbose logging
	public static final boolean LOG_TRAM_POSITIONS = false; // flag to enable/disable tram position overview logging

	public static final boolean SERIALIZE_PERFORMANCES = false; // flag to enable/disable performance serialization
	public static final boolean PERSIST_PERFORMANCE_TABLE = true; // flag to enable/disable performance table generation

	public static final LocalTime FIRST_SCHEDULED_LEAVE_TIME_PR = LocalTime.of(6, 0);
	public static final LocalTime FIRST_PASSENGER_CALC = LocalTime.of(6, 0);
	public static final int INITIAL_NUMBER_OF_TRAMS_CS = (NUMBER_OF_TRAMS % 2) == 0 ? (NUMBER_OF_TRAMS / 2) - 1
			: NUMBER_OF_TRAMS / 2;
	public static final Duration TRAM_LEAVE_FREQUENCY = Duration.ofSeconds(3600 / NUMBER_OF_TRAMS);
	public static final Duration AVG_ONE_WAY_DRIVING_TIME = Duration.ofMinutes(17);
	public static final Duration JUNCTION_DURATION = Duration.ofMinutes(1);
	public static final LocalTime SIMULATION_START_TIME = FIRST_SCHEDULED_LEAVE_TIME_PR
			.minus(TURN_AROUND_DURATION.plus(JUNCTION_DURATION)); // time where we start to deploy trams
	public static final LocalTime SIMULATION_END_TIME = LocalTime.of(19, 00); // time where we end the simulation;
	public static final String CSV_PATH_POISS_PASS_IN = "data/PassengersInPoisson.csv";
	public static final String CSV_PATH_POISS_PASS_OUT = "data/PassengersOutPoisson.csv";

	private static EndStation centraalEndStation;
	private static EndStation uithofEndStation;
	private static LocalTime firstScheduledLeaveTimeCS;

	public static void main(final String[] args) throws IOException {
		final List<DayAndPeakPerformances> dayAndPeakPerformances = new ArrayList<>();

		for (int i = 1; i <= NUMBER_OF_RUNS; i++) {
			final DayAndPeakPerformances performanceOfRun = runSimulation(i);
			dayAndPeakPerformances.add(performanceOfRun);
			EventScheduler.reset();
			PerformanceTracker.reset();
		}

		final List<Performance> dayPerformances = new ArrayList<>();
		final List<Performance> peakPerformances = new ArrayList<>();
		for (final DayAndPeakPerformances dayAndPeakPerformance : dayAndPeakPerformances) {
			dayPerformances.add(dayAndPeakPerformance.getDailyPerformance());
			peakPerformances.add(dayAndPeakPerformance.getPeakPerformance());
		}

		PerformanceTracker.printPerformanceReport(peakPerformances, "Peak");
		PerformanceTracker.printPerformanceReport(dayPerformances, "Day");

		final LocalTime simEndTime = LocalTime.now();
		if (SERIALIZE_PERFORMANCES) {
			PerformanceTracker.serializePerformances(dayPerformances, simEndTime, "Day");
			PerformanceTracker.serializePerformances(peakPerformances, simEndTime, "Peak");
		}
		if (PERSIST_PERFORMANCE_TABLE) {
			PerformanceTracker.persistPerformanceTable(dayPerformances, simEndTime, "Day");
			PerformanceTracker.persistPerformanceTable(peakPerformances, simEndTime, "Peak");
		}
	}

	private static DayAndPeakPerformances runSimulation(final int run) throws IOException {
		System.out.println("Simulation run " + run);

		calculateCSLeave();

		initializeState();

		final ScheduledLeaveEndStationEvent scheduledLeaveEndStationCentraalEvent = new ScheduledLeaveEndStationEvent(
				centraalEndStation);
		final ScheduledLeaveEndStationEvent scheduledLeaveEndStationUithofEvent = new ScheduledLeaveEndStationEvent(
				uithofEndStation);

		EventScheduler.getInstance().scheduleEvent(scheduledLeaveEndStationCentraalEvent, firstScheduledLeaveTimeCS);
		EventScheduler.getInstance().scheduleEvent(scheduledLeaveEndStationUithofEvent, FIRST_SCHEDULED_LEAVE_TIME_PR);

		while (EventScheduler.getInstance().fireNextEvent()) {
			// Next event is fired
		}

		return PerformanceTracker.getDayAndPeakPerformances();
	}

	/**
	 * Calculates the first schedules leave time for central station.
	 */
	private static void calculateCSLeave() {
		LocalTime firstRound = FIRST_SCHEDULED_LEAVE_TIME_PR.plus(AVG_ONE_WAY_DRIVING_TIME.plus(TURN_AROUND_DURATION));
		while (firstRound.compareTo(FIRST_SCHEDULED_LEAVE_TIME_PR.plus(TRAM_LEAVE_FREQUENCY)) == 1) {
			firstRound = firstRound.minus(TRAM_LEAVE_FREQUENCY);
		}
		firstScheduledLeaveTimeCS = firstRound;
	}

	private static void initializeState() {
		// Junctions
		final Junction centraalJunction = new Junction();
		final Junction uithofJunction = new Junction();

		// Platforms Direction A -> Uithof
		centraalEndStation = new EndStation("Centraal Station", centraalJunction, firstScheduledLeaveTimeCS,
				Duration.ofSeconds(134), NUMBER_OF_TRAMS);
		final IntPlatform vrPlatformA = new IntPlatform("Vaartsche-Rijn-A", Duration.ofSeconds(243));
		final IntPlatform gwPlatformA = new IntPlatform("Galgenwaard-A", Duration.ofSeconds(59));
		final IntPlatform krPlatformA = new IntPlatform("Kromme-Rijn-A", Duration.ofSeconds(101));
		final IntPlatform plPlatformA = new IntPlatform("Padualaan-A", Duration.ofSeconds(60));
		final IntPlatform hlPlatformA = new IntPlatform("Heidelberglaan-A", Duration.ofSeconds(86));
		final IntPlatform umcPlatformA = new IntPlatform("UMC-A", Duration.ofSeconds(78));
		final IntPlatform wkzPlatformA = new IntPlatform("WKZ-A", Duration.ofSeconds(113));

		// Platforms Direction B -> Centraal
		uithofEndStation = new EndStation("P+R De Uithof", uithofJunction, FIRST_SCHEDULED_LEAVE_TIME_PR,
				Duration.ofSeconds(110), INITIAL_NUMBER_OF_TRAMS_CS);
		final IntPlatform wkzPlatformB = new IntPlatform("WKZ-B", Duration.ofSeconds(78));
		final IntPlatform umcPlatformB = new IntPlatform("UMC-B", Duration.ofSeconds(82));
		final IntPlatform hlPlatformB = new IntPlatform("Heidelberglaan-B", Duration.ofSeconds(60));
		final IntPlatform plPlatformB = new IntPlatform("Padualaan-B", Duration.ofSeconds(100));
		final IntPlatform krPlatformB = new IntPlatform("Kromme-Rijn-B", Duration.ofSeconds(59));
		final IntPlatform gwPlatformB = new IntPlatform("Galgenwaard-B", Duration.ofSeconds(243));
		final IntPlatform vrPlatformB = new IntPlatform("Vaartsche-Rijn-B", Duration.ofSeconds(135));

		// Waiting Points Direction A -> Uithof
		final WaitingPoint vrWaitingPointPlA = new WaitingPoint(vrPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint gwWaitingPointPlA = new WaitingPoint(gwPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint krWaitingPointPlA = new WaitingPoint(krPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint plWaitingPointPlA = new WaitingPoint(plPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint hlWaitingPointPlA = new WaitingPoint(hlPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint umcWaitingPointPlA = new WaitingPoint(umcPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint wkzWaitingPointPlA = new WaitingPoint(wkzPlatformA, NUMBER_OF_TRAMS);
		final WaitingPoint uithofWaitingPoint = new WaitingPoint(uithofEndStation, INITIAL_NUMBER_OF_TRAMS_CS);

		// Waiting Points Direction B -> Centraal
		final WaitingPoint wkzWaitingPointPlB = new WaitingPoint(wkzPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint umcWaitingPointPlB = new WaitingPoint(umcPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint hlWaitingPointPlB = new WaitingPoint(hlPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint plWaitingPointPlB = new WaitingPoint(plPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint krWaitingPointPlB = new WaitingPoint(krPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint gwWaitingPointPlB = new WaitingPoint(gwPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint vrWaitingPointPlB = new WaitingPoint(vrPlatformB, INITIAL_NUMBER_OF_TRAMS_CS);
		final WaitingPoint centraalWaitingPoint = new WaitingPoint(centraalEndStation, NUMBER_OF_TRAMS);

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

		tramFactory(centraalWaitingPoint, uithofWaitingPoint);
	}

	// this function creates NUMBER_OF_TRAMS trams and dumps them all into the
	// waiting point at Uithof before the junction at the given
	// SIMULATION_START_TIME
	private static void tramFactory(final WaitingPoint cs, final WaitingPoint uit) {
		int tramId = 1;

		for (int i = 0; i < INITIAL_NUMBER_OF_TRAMS_CS; i++) {
			final Tram newTram = new Tram(tramId, 0);
			final ArriveWaitingPointEvent arriveWaitingPointEvent = new ArriveWaitingPointEvent(cs, newTram);
			EventScheduler.getInstance().scheduleEventAhead(arriveWaitingPointEvent, Duration.ZERO);
			tramId += 1;
		}
		for (int i = INITIAL_NUMBER_OF_TRAMS_CS; i < NUMBER_OF_TRAMS; i++) {
			final Tram newTram = new Tram(tramId, 0);
			final ArriveWaitingPointEvent arriveWaitingPointEvent = new ArriveWaitingPointEvent(uit, newTram);
			EventScheduler.getInstance().scheduleEventAhead(arriveWaitingPointEvent, Duration.ZERO);
			tramId += 1;
		}
	}

	public static void log(final String log) {
		if (LOG) {
			System.out.println(EventScheduler.getInstance().getCurrentTime() + ": " + log);
		}
	}

	public static void logVerbose(final String log) {
		if (LOG_VERBOSE) {
			log(log);
		}
	}

	public static void logTramPositions() {
		if (LOG_TRAM_POSITIONS) {
			Platform platform = uithofEndStation;
			do {
				if (platform instanceof EndStation) {
					final EndStation endStation = (EndStation) platform;
					final Junction junction = endStation.getJunction();
					log("Trams on junction for end station " + endStation.getName() + ": " + junction.getTramOnLaneInA()
							+ " (In-A), " + junction.getTramOnLaneInB() + " (In-B), " + junction.getTramOnLaneOutA()
							+ " (Out-A), " + junction.getTramOnLaneOutB() + " (Out-B).");
					log("Trams on end station " + endStation.getName() + ": " + endStation.getTramOnPlatformA()
							+ " (Platform A), " + endStation.getTramOnPlatformB() + " (Platform B).");
				} else {
					final IntPlatform intPlatform = (IntPlatform) platform;
					log("Tram on platform " + intPlatform.getName() + ": " + intPlatform.getTram());
				}
				final WaitingPoint nextWaitingPoint = platform.getNextWaitingPoint();
				log("Trams on waiting point for platform " + nextWaitingPoint.getNextPlatform().getName() + ": "
						+ nextWaitingPoint.getWaitingTrams());
				platform = nextWaitingPoint.getNextPlatform();
			} while (!platform.equals(uithofEndStation)); // until the circle is complete
		}
	}

}
