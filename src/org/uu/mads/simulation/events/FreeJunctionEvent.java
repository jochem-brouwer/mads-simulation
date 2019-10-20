package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;

public class FreeJunctionEvent extends Event {
	private final EndStation endStation;
	private final Tram tram;

	public FreeJunctionEvent(final EndStation endStation, final Tram tram) {
		super();
		this.endStation = endStation;
		this.tram = tram;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		Objects.requireNonNull(this.tram, "Given tram must not be null!");

		final Junction junction = this.endStation.getJunction();

		if (this.tram.equals(junction.getTramOnLaneInA())) {
			junction.removeTramOnLaneInA();
			if (this.endStation.getTramOnPlatformA() != null) {
				throw new IllegalStateException(
						"There is already a tram on platform A while trying to let a new one arrive from the junction.");
			}
			this.endStation.setTramOnPlatformA(this.tram);
			scheduleScheduledLeaveEndStationEvent();
			// System.out.println("Tram from lane In-A of junction for end station " +
			// this.endStation.getName()
			// + " has been moved to platform A.");
		} else if (this.tram.equals(junction.getTramOnLaneInB())) {
			junction.removeTramOnLaneInB();
			if (this.endStation.getTramOnPlatformB() != null) {
				throw new IllegalStateException(
						"There is already a tram on platform B while trying to let a new one arrive from the junction.");
			}
			this.endStation.setTramOnPlatformB(this.tram);
			scheduleScheduledLeaveEndStationEvent();
			// System.out.println("Tram from lane In-B of junction for end station " +
			// this.endStation.getName()
			// + " has been moved to platform B.");
		} else if (this.tram.equals(junction.getTramOnLaneOutA())) {
			junction.removeTramOnLaneOutA();
			scheduleArriveWaitingPointEvent();
			// System.out.println("Tram from lane Out-A of junction for end station " +
			// this.endStation.getName()
			// + " has been moved to the next platform's waiting point.");

		} else if (this.tram.equals(junction.getTramOnLaneOutB())) {
			junction.removeTramOnLaneOutB();
			scheduleArriveWaitingPointEvent();
			// System.out.println("Tram from lane Out-B of junction for end station " +
			// this.endStation.getName()
			// + " has been moved to the next platform's waiting point.");
		} else {
			throw new IllegalArgumentException("The given tram is not in the junction.");
		}

		// Schedule TryOccupyJunction again
		final TryOccupyJunctionEvent tryOccupyJunctionEvent = new TryOccupyJunctionEvent(this.endStation);
		EventScheduler.get().scheduleEventAhead(tryOccupyJunctionEvent, Duration.ZERO);
	}

	private void scheduleScheduledLeaveEndStationEvent() {
		final ScheduledLeaveEndStationEvent scheduledLeaveEndStationEvent = new ScheduledLeaveEndStationEvent(
				this.endStation);
		EventScheduler.get().scheduleEventAhead(scheduledLeaveEndStationEvent, Duration.ZERO);
	}

	private void scheduleArriveWaitingPointEvent() {
		final ArriveWaitingPointEvent arriveWaitingPointEvent = new ArriveWaitingPointEvent(
				this.endStation.getNextWaitingPoint(), this.tram);
		EventScheduler.get().scheduleEventAhead(arriveWaitingPointEvent, this.endStation.getTravelTimeToNextPlatform());
	}

	@Override
	public String toString() {
		return "FreeJunctionEvent [endStation=" + this.endStation + ", tram=" + this.tram + "]";
	}

}