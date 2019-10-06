package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;

public class EndStation extends Platform {
	private static final Duration TURN_AROUND_DURATION = Duration.ofMinutes(3);

	private final String name;
	private final Junction junction;
	private LocalTime nextScheduledLeave;
	private Tram tramOnPlatformA;
	private Tram tramOnPlatformB;
	private LocalTime arrivalTimePlatformA;
	private LocalTime arrivalTimePlatformB;

	public EndStation(final String name, final Junction junction, final LocalTime nextScheduledLeave,
			final Duration averageTravelTime, final WaitingPoint nextWaitingPoint,
			final WaitingPoint lastWaitingPoint) {
		super(averageTravelTime, nextWaitingPoint, lastWaitingPoint);
		this.name = name;
		this.junction = junction;
		this.nextScheduledLeave = nextScheduledLeave;
	}

	public String getName() {
		return this.name;
	}

	public LocalTime getNextScheduledLeave() {
		return this.nextScheduledLeave;
	}

	public Junction getJunction() {
		return this.junction;
	}

	public void setNextScheduledLeave(final LocalTime nextScheduledLeave) {
		this.nextScheduledLeave = nextScheduledLeave;
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

	public void departFromPlatformA() {
		this.tramOnPlatformA = null;
		this.nextScheduledLeave = EventScheduler.get().getCurrentTime().plus(Simulation.TRAM_LEAVE_FREQUENCY);
	}

	public void departFromPlatformB() {
		this.tramOnPlatformB = null;
		this.nextScheduledLeave = EventScheduler.get().getCurrentTime().plus(Simulation.TRAM_LEAVE_FREQUENCY);
	}

	@Override
	// TODO: Update with inherited attributes?
	public String toString() {
		return "EndStation [name=" + this.name + ", nextScheduledLeave=" + this.nextScheduledLeave
				+ ", tramOnPlatformA=" + this.tramOnPlatformA + ", tramOnPlatformB=" + this.tramOnPlatformB
				+ ", arrivalTimePlatformA=" + this.arrivalTimePlatformA + ", arrivalTimePlatformB="
				+ this.arrivalTimePlatformB + "]";
	}
}
