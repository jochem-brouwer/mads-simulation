package org.uu.mads.simulation.state;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.PerformanceTracker;
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

		Objects.requireNonNull(junction, "Given junction must not be null!");
		Objects.requireNonNull(nextScheduledLeave, "Given nextScheduledLeave must not be null!");
		Objects.requireNonNull(lastTramLeftId, "Given lastTramleftId must not be null!");

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

	public void arriveOnPlatformA(final Tram tram) {
		if (this.tramOnPlatformA != null) {
			throw new IllegalStateException(
					"There is already a tram on platform A while trying to let a new one arrive from the junction.");
		}
		this.tramOnPlatformA = tram;
		this.arrivalTimePlatformA = EventScheduler.getInstance().getCurrentTime();

		// When a tram arrives, we calculate the passengers and load them into the tram.
		final int passengersOut = tram.dumpPassengers(this);
		calculatePassengers();
		final int passengersIn = tram.loadPassengers(this);

		Simulation.log("Tram " + tram.getId() + " is arriving at platform " + getName() + " at "
				+ EventScheduler.getInstance().getCurrentTime() + ", dumps " + passengersOut + " passengers and loads "
				+ passengersIn + " passengers.");

		final ScheduledLeaveEndStationEvent scheduledLeave = new ScheduledLeaveEndStationEvent(this);
		EventScheduler.getInstance().scheduleEventAhead(scheduledLeave, Simulation.TURN_AROUND_DURATION);
	}

	public Tram getTramOnPlatformB() {
		return this.tramOnPlatformB;
	}

	public void arriveOnPlatformB(final Tram tram) {
		if (this.tramOnPlatformB != null) {
			throw new IllegalStateException(
					"There is already a tram on platform A while trying to let a new one arrive from the junction.");
		}
		this.tramOnPlatformB = tram;
		this.arrivalTimePlatformB = EventScheduler.getInstance().getCurrentTime();

		// When a tram arrives, we calculate the passengers and load them into the tram.
		final int passengersOut = tram.dumpPassengers(this);
		calculatePassengers();
		final int passengersIn = tram.loadPassengers(this);

		Simulation.log("Tram " + tram.getId() + " is arriving at platform " + getName() + " at "
				+ EventScheduler.getInstance().getCurrentTime() + ", dumps " + passengersOut + " passengers and loads "
				+ passengersIn + " passengers.");

		final ScheduledLeaveEndStationEvent scheduledLeave = new ScheduledLeaveEndStationEvent(this);
		EventScheduler.getInstance().scheduleEventAhead(scheduledLeave, Simulation.TURN_AROUND_DURATION);
	}

	public LocalTime getArrivalTimePlatformA() {
		return this.arrivalTimePlatformA;
	}

	public LocalTime getArrivalTimePlatformB() {
		return this.arrivalTimePlatformB;
	}

	public boolean isTramReadyOnPlatformA() {
		LocalTime lastPossibleArrivalTime = null;
		lastPossibleArrivalTime = EventScheduler.getInstance().getCurrentTime()
				.minus(Simulation.TURN_AROUND_DURATION);

		if ((this.tramOnPlatformA != null) && (this.arrivalTimePlatformA.equals(lastPossibleArrivalTime)
				|| this.arrivalTimePlatformA.isBefore(lastPossibleArrivalTime))) {
			return true;
		}
		return false;
	}

	public boolean isTramReadyOnPlatformB() {
		final LocalTime lastPossibleArrivalTime = EventScheduler.getInstance().getCurrentTime()
				.minus(Simulation.TURN_AROUND_DURATION);

		if ((this.tramOnPlatformB != null) && (this.arrivalTimePlatformB.equals(lastPossibleArrivalTime)
				|| this.arrivalTimePlatformB.isBefore(lastPossibleArrivalTime))) {
			return true;
		}
		return false;
	}

	public boolean isNextScheduledLeaveDue() {
		return (this.nextScheduledLeave.equals(EventScheduler.getInstance().getCurrentTime())
				|| this.nextScheduledLeave.isBefore(EventScheduler.getInstance().getCurrentTime()));
	}

	public boolean checkForOrder(final Tram tram) {
		return tram.getId() == ((this.lastTramLeftId % Simulation.NUMBER_OF_TRAMS) + 1);
	}

	public void departFromPlatformA() {
		this.lastTramLeftId = this.tramOnPlatformA.getId();
		this.tramOnPlatformA = null;
		final Duration delay = Duration.between(this.nextScheduledLeave, EventScheduler.getInstance().getCurrentTime());

		Simulation.log("Tram " + this.lastTramLeftId + " has left " + getName() + " - platform A at "
				+ EventScheduler.getInstance().getCurrentTime() + " with a delay of " + delay.toSeconds()
				+ " seconds.");

		Simulation.log("Next scheduled leave: " + this.nextScheduledLeave.toString());
		Simulation.log("We leave at current time: " + EventScheduler.getInstance().getCurrentTime());
		Simulation.logVerbose("Delay: " + delay.toSeconds());

		this.nextScheduledLeave = this.nextScheduledLeave.plus(Simulation.TRAM_LEAVE_FREQUENCY);

		if (getName() == "Centraal Station") {
			PerformanceTracker.addDelay(delay, 0);
		} else {
			PerformanceTracker.addDelay(delay, 1);
		}
	}

	public void departFromPlatformB() {
		this.lastTramLeftId = this.tramOnPlatformB.getId();
		this.tramOnPlatformB = null;
		final Duration delay = Duration.between(this.nextScheduledLeave, EventScheduler.getInstance().getCurrentTime());

		Simulation.log("Tram " + this.lastTramLeftId + " has left " + getName() + " - platform B at "
				+ EventScheduler.getInstance().getCurrentTime() + " with a delay of " + delay.toSeconds()
				+ " seconds.");

		Simulation.logVerbose("Next scheduled leave: " + this.nextScheduledLeave.toString());
		Simulation.logVerbose("We leave at current time: " + EventScheduler.getInstance().getCurrentTime());
		Simulation.logVerbose("Delay: " + delay.toSeconds());

		this.nextScheduledLeave = this.nextScheduledLeave.plus(Simulation.TRAM_LEAVE_FREQUENCY);

		if (getName() == "Centraal Station") {
			PerformanceTracker.addDelay(delay, 0);
		} else {
			PerformanceTracker.addDelay(delay, 1);
		}
	}

	@Override
	public String toString() {
		return "EndStation [junction=" + this.junction + ", nextScheduledLeave=" + this.nextScheduledLeave
				+ ", tramOnPlatformA=" + this.tramOnPlatformA + ", tramOnPlatformB=" + this.tramOnPlatformB
				+ ", arrivalTimePlatformA=" + this.arrivalTimePlatformA + ", arrivalTimePlatformB="
				+ this.arrivalTimePlatformB + "]";
	}
}
