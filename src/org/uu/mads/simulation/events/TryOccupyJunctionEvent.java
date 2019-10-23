package org.uu.mads.simulation.events;

import java.time.LocalTime;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;

public class TryOccupyJunctionEvent extends Event {
	private final EndStation endStation;

	public TryOccupyJunctionEvent(final EndStation endStation) {
		super();

		Objects.requireNonNull(endStation, "Given endStation must not be null!");

		this.endStation = endStation;
	}

	@Override
	public void fire() {
		if (this.endStation.getLastWaitingPoint().isTramWaitingInCorrectOrder()) {
			Simulation.logVerbose("There is a tram at the waiting point for the end station" + this.endStation.getName()
					+ " that is requesting to use the junction.");
			useJunctionForArrival();
			useJunctionForDeparture();
		} else {
			Simulation.logVerbose("There is a tram ready at end station " + this.endStation.getName()
					+ " that is requesting to use the junction.");
			useJunctionForDeparture();
		}
	}

	private void useJunctionForArrival() {
		final Junction junction = this.endStation.getJunction();
		if (!junction.isJunctionUsed()) {
			if (this.endStation.getTramOnPlatformB() == null) {
				// We send the tram from the junction to platform B
				final Tram nextTramWaiting = this.endStation.getLastWaitingPoint().popNextTramWaiting();
				junction.setTramOnLaneInB(nextTramWaiting);
				scheduleFreeJunctionEvent(nextTramWaiting);
				Simulation.logTramPositions();
			} else if (this.endStation.getTramOnPlatformA() == null) {
				// We send the tram from the junction to platform A
				final Tram nextTramWaiting = this.endStation.getLastWaitingPoint().popNextTramWaiting();
				junction.setTramOnLaneInA(nextTramWaiting);
				scheduleFreeJunctionEvent(nextTramWaiting);
				Simulation.logTramPositions();
			}
		} else if ((this.endStation.getTramOnPlatformA() == null) && junction.canUseLaneInA()) {
			// We are in mode 3 and can send two trams at once
			// We send the tram from the junction to platform A
			final Tram nextTramWaiting = this.endStation.getLastWaitingPoint().popNextTramWaiting();
			junction.setTramOnLaneInA(nextTramWaiting);
			scheduleFreeJunctionEvent(nextTramWaiting);
			Simulation.logTramPositions();
		}
	}

	private void useJunctionForDeparture() {
		final Junction junction = this.endStation.getJunction();
		if (!junction.isJunctionUsed() && this.endStation.isNextScheduledLeaveDue()) {
			if (this.endStation.isTramReadyOnPlatformA()
					&& this.endStation.checkForOrder(this.endStation.getTramOnPlatformA())) {
				// We send the tram from platform A to the junction
				final Tram tramOnPlatformA = this.endStation.getTramOnPlatformA();

				if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7, 30))) {
					tramOnPlatformA.setJunctionArrivalTime(EventScheduler.getInstance().getCurrentTime());
				}

				junction.setTramOnLaneOutA(tramOnPlatformA);
				this.endStation.departFromPlatformA();
				scheduleFreeJunctionEvent(tramOnPlatformA);
				Simulation.logTramPositions();
			} else if (this.endStation.isTramReadyOnPlatformB()
					&& this.endStation.checkForOrder(this.endStation.getTramOnPlatformB())) {
				// We send the tram from platform B to the junction
				final Tram tramOnPlatformB = this.endStation.getTramOnPlatformB();

				if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7, 30))) {
					tramOnPlatformB.setJunctionArrivalTime(EventScheduler.getInstance().getCurrentTime());
				}

				junction.setTramOnLaneOutB(tramOnPlatformB);
				this.endStation.departFromPlatformB();
				scheduleFreeJunctionEvent(tramOnPlatformB);
				Simulation.logTramPositions();
			}
		} else if (this.endStation.isTramReadyOnPlatformB() && this.endStation.isNextScheduledLeaveDue()
				&& junction.canUseLaneOutB() && this.endStation.checkForOrder(this.endStation.getTramOnPlatformB())) {
			// We are in mode 3 and can send two trams at once
			// We send the tram from platform B to the junction
			final Tram tramOnPlatformB = this.endStation.getTramOnPlatformB();

			if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7, 30))) {
				tramOnPlatformB.setJunctionArrivalTime(EventScheduler.getInstance().getCurrentTime());
			}

			junction.setTramOnLaneOutB(tramOnPlatformB);
			this.endStation.departFromPlatformB();
			scheduleFreeJunctionEvent(tramOnPlatformB);
			Simulation.logTramPositions();
		}
	}

	private void scheduleFreeJunctionEvent(final Tram tram) {
		final FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent(this.endStation, tram);
		EventScheduler.getInstance().scheduleEventAhead(freeJunctionEvent, Simulation.JUNCTION_DURATION);

	}

	@Override
	public String toString() {
		return "TryOccupyJunctionEvent [endStation=" + this.endStation + "]";
	}
}