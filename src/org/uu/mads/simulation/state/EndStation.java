package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Performance;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.events.ScheduledLeaveEndStationEvent;

public class EndStation extends Platform {
	private final Junction junction;
	private LocalTime nextScheduledLeave;
	private Tram tramOnPlatformA;
	private Tram tramOnPlatformB;
	private LocalTime arrivalTimePlatformA;
	private LocalTime arrivalTimePlatformB;
	private int lastTramLeftId;

	public EndStation(final String name, final Junction junction, final LocalTime nextScheduledLeave,
			final Duration averageTravelTime, final int lastTramLeftId) {
		super(name, averageTravelTime);
		this.junction = junction;
		this.nextScheduledLeave = nextScheduledLeave;
		this.lastTramLeftId = lastTramLeftId;
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

	public int getLastTramLeft() {
		return this.lastTramLeftId;
	}

	public Tram getTramOnPlatformA() {
		return this.tramOnPlatformA;
	}

	public void setTramOnPlatformA(final Tram tramOnPlatformA) {
		// System.out.println("OCCUPY A");
		this.tramOnPlatformA = tramOnPlatformA;
		this.arrivalTimePlatformA = EventScheduler.get().getCurrentTime();
		final ScheduledLeaveEndStationEvent scheduledLeave = new ScheduledLeaveEndStationEvent(this);
		EventScheduler.get().scheduleEventAhead(scheduledLeave, Simulation.TURN_AROUND_DURATION);
		Simulation.log("Tram " + tramOnPlatformA.getId() + " arrived at " + getName() + " platform A at "
				+ EventScheduler.get().getCurrentTime());
	}

	public Tram getTramOnPlatformB() {
		return this.tramOnPlatformB;
	}

	public void setTramOnPlatformB(final Tram tramOnPlatformB) {
		// System.out.println("OCCUPY B");
		this.tramOnPlatformB = tramOnPlatformB;
		this.arrivalTimePlatformB = EventScheduler.get().getCurrentTime();
		final ScheduledLeaveEndStationEvent scheduledLeave = new ScheduledLeaveEndStationEvent(this);
		EventScheduler.get().scheduleEventAhead(scheduledLeave, Simulation.TURN_AROUND_DURATION);
		Simulation.log("Tram " + tramOnPlatformB.getId() + " arrived at " + getName() + " platform B at "
				+ EventScheduler.get().getCurrentTime());
	}

	public LocalTime getArrivalTimePlatformA() {
		return this.arrivalTimePlatformA;
	}

	public LocalTime getArrivalTimePlatformB() {
		return this.arrivalTimePlatformB;
	}

	public boolean isTramReadyOnPlatformA() {
		final LocalTime lastPossibleArrivalTime = EventScheduler.get().getCurrentTime()
				.minus(Simulation.TURN_AROUND_DURATION);

		if ((this.tramOnPlatformA != null) && (this.arrivalTimePlatformA.equals(lastPossibleArrivalTime)
				|| this.arrivalTimePlatformA.isBefore(lastPossibleArrivalTime))) {
			return true;
		}
		return false;
	}

	public boolean isTramReadyOnPlatformB() {
		final LocalTime lastPossibleArrivalTime = EventScheduler.get().getCurrentTime()
				.minus(Simulation.TURN_AROUND_DURATION);

		if ((this.tramOnPlatformB != null) && (this.arrivalTimePlatformB.equals(lastPossibleArrivalTime)
				|| this.arrivalTimePlatformB.isBefore(lastPossibleArrivalTime))) {
			return true;
		}
		return false;
	}

	public boolean isNextScheduledLeaveDue() {
		return (this.nextScheduledLeave.equals(EventScheduler.get().getCurrentTime())
				|| this.nextScheduledLeave.isBefore(EventScheduler.get().getCurrentTime()));
	}

	public boolean checkForOrder(final Tram tram) {
		if (tram.getId() == ((getLastTramLeft() % Simulation.NUMBER_OF_TRAMS) + 1)) {
			return true;
		} else if (getLastTramLeft() == -1) {
			return true;
		} else {
			return false;
		}
	}

	public void departFromPlatformA() {
		Simulation.log("Tram " + this.tramOnPlatformA.getId() + " leaves " + getName() + " platform A at "
				+ EventScheduler.get().getCurrentTime());
		this.lastTramLeftId = this.tramOnPlatformA.getId();
		this.tramOnPlatformA = null;
		final Duration delay = Duration.between(this.nextScheduledLeave, EventScheduler.get().getCurrentTime());

		System.out.println("Next scheduled leave: " + this.nextScheduledLeave.toString());
		System.out.println("We leave at current time: " + EventScheduler.get().getCurrentTime());
		System.out.println("Delay: " + delay.toSeconds());

		this.nextScheduledLeave = this.nextScheduledLeave.plus(Simulation.TRAM_LEAVE_FREQUENCY);

		// System.out.println("Next scheduled leave: " +
		// this.nextScheduledLeave.toString());

		Performance.get().addDelay(delay);
	}

	public void departFromPlatformB() {
		Simulation.log("Tram " + this.tramOnPlatformB.getId() + " leaves " + getName() + " platform B at "
				+ EventScheduler.get().getCurrentTime());
		this.lastTramLeftId = this.tramOnPlatformB.getId();
		this.tramOnPlatformB = null;
		final Duration delay = Duration.between(this.nextScheduledLeave, EventScheduler.get().getCurrentTime());

		System.out.println("Next scheduled leave: " + this.nextScheduledLeave.toString());
		System.out.println("We leave at current time: " + EventScheduler.get().getCurrentTime());
		System.out.println("Delay: " + delay.toSeconds());

		this.nextScheduledLeave = this.nextScheduledLeave.plus(Simulation.TRAM_LEAVE_FREQUENCY);

		// System.out.println("Next scheduled leave: " +
		// this.nextScheduledLeave.toString());

		Performance.get().addDelay(delay);
	}

	@Override
	public String toString() {
		return "EndStation [junction=" + this.junction + ", nextScheduledLeave=" + this.nextScheduledLeave
				+ ", tramOnPlatformA=" + this.tramOnPlatformA + ", tramOnPlatformB=" + this.tramOnPlatformB
				+ ", arrivalTimePlatformA=" + this.arrivalTimePlatformA + ", arrivalTimePlatformB="
				+ this.arrivalTimePlatformB + "]";
	}
}
