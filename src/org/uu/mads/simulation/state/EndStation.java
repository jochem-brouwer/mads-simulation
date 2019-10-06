package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Set;

import org.uu.mads.simulation.EventScheduler;

public class EndStation {
	private static final Duration TURN_AROUND_DURATION = Duration.ofMinutes(3);

	private final String name;
	private LocalTime lastPassengersCalc;
	private LocalTime nextScheduledLeave;
	private final Queue<Passenger> waitingPassengers = new ArrayDeque<>();
	private Tram tramOnPlatformA;
	private Tram tramOnPlatformB;
	private LocalTime arrivalTimePlatformA;
	private LocalTime arrivalTimePlatformB;

	public EndStation(final String name, final LocalTime nextScheduledLeave) {
		super();
		this.name = name;
		this.nextScheduledLeave = nextScheduledLeave;
	}

	public String getName() {
		return this.name;
	}

	public LocalTime getLastPassengersCalc() {
		return this.lastPassengersCalc;
	}

	public void setLastPassengersCalc(final LocalTime lastPassengersCalc) {
		this.lastPassengersCalc = lastPassengersCalc;
	}

	public LocalTime getNextScheduledLeave() {
		return this.nextScheduledLeave;
	}

	public void setNextScheduledLeave(final LocalTime nextScheduledLeave) {
		this.nextScheduledLeave = nextScheduledLeave;
	}

	public Queue<Passenger> getWaitingPassengers() {
		return this.waitingPassengers;
	}

	public Queue<Passenger> addWaitingPassenger(final Passenger waitingPassenger) {
		this.waitingPassengers.add(waitingPassenger);
		return this.waitingPassengers;
	}

	public Queue<Passenger> addWaitingPassengers(final Set<Passenger> waitingPassengers) {
		for (final Passenger waitingPassenger : waitingPassengers) {
			addWaitingPassenger(waitingPassenger);
		}
		return this.waitingPassengers;
	}

	public Tram getTramOnPlatformA() {
		return this.tramOnPlatformA;
	}

	public void setTramOnPlatformA(final Tram tramOnPlatformA) {
		this.tramOnPlatformA = tramOnPlatformA;
		this.arrivalTimePlatformA = EventScheduler.get().getCurrentTime();
	}

	public Tram getTramOnPlatformB() {
		return this.tramOnPlatformB;
	}

	public void setTramOnPlatformB(final Tram tramOnPlatformB) {
		this.tramOnPlatformB = tramOnPlatformB;
		this.arrivalTimePlatformB = EventScheduler.get().getCurrentTime();
	}

	public LocalTime getArrivalTimePlatformA() {
		return this.arrivalTimePlatformA;
	}

	public LocalTime getArrivalTimePlatformB() {
		return this.arrivalTimePlatformB;
	}

	public boolean isTramReadyOnPlatformA() {
		if ((this.tramOnPlatformA != null) && this.arrivalTimePlatformA
				.isBefore(EventScheduler.get().getCurrentTime().minus(TURN_AROUND_DURATION))) {
			return true;
		}
		return false;
	}

	public boolean isTramReadyOnPlatformB() {
		if ((this.tramOnPlatformB != null) && this.arrivalTimePlatformB
				.isBefore(EventScheduler.get().getCurrentTime().minus(TURN_AROUND_DURATION))) {
			return true;
		}
		return false;
	}

	public void freePlatformA() {
		this.tramOnPlatformA = null;
	}

	public void freePlatformB() {
		this.tramOnPlatformB = null;
	}
}
