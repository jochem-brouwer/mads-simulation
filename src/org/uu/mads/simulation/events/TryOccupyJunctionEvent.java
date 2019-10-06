package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPointJunction;

public class TryOccupyJunctionEvent extends Event {
	private static final Duration JUNCTION_DURATION = Duration.ofMinutes(1);

	private final EndStation endStation;
	private final WaitingPointJunction waitingPointJunction;
	private final Junction junction;

	public TryOccupyJunctionEvent(final EndStation endStation, final WaitingPointJunction waitingPointJunction,
			final Junction junction) {
		super();
		this.endStation = endStation;
		this.waitingPointJunction = waitingPointJunction;
		this.junction = junction;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	public WaitingPointJunction getWaitingPointJunction() {
		return this.waitingPointJunction;
	}

	public Junction getJunction() {
		return this.junction;
	}

	@Override
	public void fire() {
		if (this.waitingPointJunction.isTramWaiting()) {
			System.out.println("There is a tram at the waiting point for the end station " + this.endStation
					+ " that is requesting to use the junction.");
			useJunctionForArrival();
		} else {
			System.out.println("There is a tram ready at end station " + this.endStation
					+ " that is requesting to use the junction.");
			useJunctionForDeparture();
		}
	}

	private void useJunctionForArrival() {
		if (!this.junction.isJunctionUsed()) {
			System.out.println("The junction " + this.waitingPointJunction + " is currently free.");
			// We can send our tram into the crossing
			if (this.endStation.getTramOnPlatformB() == null) {
				// We send the tram from the junction to platform B
				final Tram nextTramWaiting = this.waitingPointJunction.popNextTramWaiting();
				this.junction.setTramOnLaneInB(nextTramWaiting);
				System.out.println(
						"Tram +  " + nextTramWaiting.getId() + " has moved from the waiting point for the end station "
								+ this.endStation.getName() + " to the lane In-B of its junction.");
				scheduleFreeJunctionEvent();
			} else if (this.endStation.getTramOnPlatformA() == null) {
				// We send the tram from the junction to platform A
				final Tram nextTramWaiting = this.waitingPointJunction.popNextTramWaiting();
				this.junction.setTramOnLaneInA(nextTramWaiting);
				System.out.println(
						"Tram +  " + nextTramWaiting.getId() + " has moved from the waiting point for the end station "
								+ this.endStation.getName() + " to the lane In-A of its junction.");
				scheduleFreeJunctionEvent();
			}
		} else if (this.endStation.getTramOnPlatformA() == null) {
			System.out.println("The junction " + this.waitingPointJunction + " is currently being used.");
			// We are in mode 3 and can send two trams at once
			// We send the tram from the junction to platform A
			final Tram nextTramWaiting = this.waitingPointJunction.popNextTramWaiting();
			this.junction.setTramOnLaneInA(nextTramWaiting);
			System.out.println(
					"Tram +  " + nextTramWaiting.getId() + " has moved from the waiting point for the end station "
							+ this.endStation.getName() + " to the lane In-A of its junction.");
			scheduleFreeJunctionEvent();
		}
	}

	private void useJunctionForDeparture() {
		if (!this.junction.isJunctionUsed()) {
			System.out.println("The junction " + this.waitingPointJunction + " is currently free.");
			if (this.endStation.isTramReadyOnPlatformA()) {
				// We send the tram from platform A to the junction
				final Tram tramOnPlatformA = this.endStation.getTramOnPlatformA();
				this.junction.setTramOnLaneOutA(tramOnPlatformA);
				this.endStation.freePlatformA();
				System.out
						.println("Tram +  " + tramOnPlatformA.getId() + " has moved from platform A of the end station "
								+ this.endStation.getName() + " to the lane Out-A of its junction.");
				scheduleFreeJunctionEvent();
			} else if (this.endStation.isTramReadyOnPlatformB()) {
				// We send the tram from platform B to the junction
				final Tram tramOnPlatformB = this.endStation.getTramOnPlatformB();
				this.junction.setTramOnLaneOutB(tramOnPlatformB);
				this.endStation.freePlatformB();
				System.out
						.println("Tram +  " + tramOnPlatformB.getId() + " has moved from platform B of the end station "
								+ this.endStation.getName() + " to the lane Out-B of its junction.");
				scheduleFreeJunctionEvent();
			}
		} else if (this.endStation.isTramReadyOnPlatformB()) {
			// We are in mode 3 and can send two trams at once
			// We send the tram from platform B to the junction
			System.out.println("The junction " + this.waitingPointJunction + " is currently being used.");
			final Tram tramOnPlatformB = this.endStation.getTramOnPlatformB();
			this.junction.setTramOnLaneOutB(tramOnPlatformB);
			this.endStation.freePlatformB();
			System.out.println("Tram +  " + tramOnPlatformB.getId() + " has moved from platform B of the end station "
					+ this.endStation.getName() + " to the lane Out-B of its junction.");
			scheduleFreeJunctionEvent();
		}
	}

	private void scheduleFreeJunctionEvent() {
		final FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent(this.junction);
		EventScheduler.get().scheduleEventAhead(freeJunctionEvent, JUNCTION_DURATION);
	}

	@Override
	public String toString() {
		return "TryOccupyJunctionEvent [endStation=" + this.endStation + ", waitingPointJunction="
				+ this.waitingPointJunction + ", junction=" + this.junction + "]";
	}
}