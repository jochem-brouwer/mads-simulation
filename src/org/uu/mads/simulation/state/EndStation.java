package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;

public class EndStation extends Platform {
	public static final Duration TURN_AROUND_DURATION = Simulation.TURN_AROUND;

	private final Junction junction;
	private LocalTime nextScheduledLeave;
	private Tram tramOnPlatformA;
	private Tram tramOnPlatformB;
	private LocalTime arrivalTimePlatformA;
	private LocalTime arrivalTimePlatformB;

	public EndStation(final String name, final Junction junction, final LocalTime nextScheduledLeave,
			final Duration averageTravelTime) {
		super(name, averageTravelTime);
		this.junction = junction;
		this.nextScheduledLeave = nextScheduledLeave;
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
		// TODO: Collect delays for punctuality performance measure
	}

	public void departFromPlatformB() {
		this.tramOnPlatformB = null;
		this.nextScheduledLeave = EventScheduler.get().getCurrentTime().plus(Simulation.TRAM_LEAVE_FREQUENCY);
		// TODO: Collect delays for punctuality performance measure
	}

	@Override
	public String toString() {
		return "EndStation [junction=" + this.junction + ", nextScheduledLeave=" + this.nextScheduledLeave
				+ ", tramOnPlatformA=" + this.tramOnPlatformA + ", tramOnPlatformB=" + this.tramOnPlatformB
				+ ", arrivalTimePlatformA=" + this.arrivalTimePlatformA + ", arrivalTimePlatformB="
				+ this.arrivalTimePlatformB + "]";
	}
}
