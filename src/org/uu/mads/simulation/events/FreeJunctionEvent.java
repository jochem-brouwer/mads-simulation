package org.uu.mads.simulation.events;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Performance;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;

public class FreeJunctionEvent extends Event {
	private final EndStation endStation;
	private final Tram tram;

	public FreeJunctionEvent(final EndStation endStation, final Tram tram) {
		super();

		Objects.requireNonNull(tram, "Given tram must not be null!");
		Objects.requireNonNull(endStation, "Given endStation must not be null!");

		this.endStation = endStation;
		this.tram = tram;
	}

	@Override
	public void fire() {
		final Junction junction = this.endStation.getJunction();

		if (EventScheduler.get().getCurrentTime().isAfter(LocalTime.of(7,30))
			&& this.tram.getJunctionArrivalTime() != null) {
			this.tram.setJunctionEnteringTime(EventScheduler.get().getCurrentTime().minus(Duration.ofMinutes(1)));
			Duration waitingTime = Duration.between(
					this.tram.getJunctionArrivalTime(), this.tram.getJunctionEnteringTime());

			/*System.out.println(this.tram.getId());
			System.out.println(this.tram.getJunctionArrivalTime());
			System.out.println(this.tram.getJunctionEnteringTime());
			System.out.println(waitingTime.getSeconds());*/

			if (this.endStation.getName() == "Centraal Station") {
				Performance.get().addJunctionWaitingTime(waitingTime, 0);
			} else {
				Performance.get().addJunctionWaitingTime(waitingTime, 1);
			}
		}

		if (this.tram.equals(junction.getTramOnLaneInA())) {
			junction.removeTramOnLaneInA();
			if (this.endStation.getTramOnPlatformA() != null) {
				throw new IllegalStateException(
						"There is already a tram on platform A while trying to let a new one arrive from the junction.");
			}
			this.endStation.setTramOnPlatformA(this.tram);
			scheduleScheduledLeaveEndStationEvent();
			Simulation.logTramPositions();
		} else if (this.tram.equals(junction.getTramOnLaneInB())) {
			junction.removeTramOnLaneInB();
			if (this.endStation.getTramOnPlatformB() != null) {
				throw new IllegalStateException(
						"There is already a tram on platform B while trying to let a new one arrive from the junction.");
			}
			this.endStation.setTramOnPlatformB(this.tram);
			scheduleScheduledLeaveEndStationEvent();
			Simulation.logTramPositions();
		} else if (this.tram.equals(junction.getTramOnLaneOutA())) {
			junction.removeTramOnLaneOutA();
			scheduleArriveWaitingPointEvent();
			Simulation.logTramPositions();
		} else if (this.tram.equals(junction.getTramOnLaneOutB())) {
			junction.removeTramOnLaneOutB();
			scheduleArriveWaitingPointEvent();
			Simulation.logTramPositions();
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