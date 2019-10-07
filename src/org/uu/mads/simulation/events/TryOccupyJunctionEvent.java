package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
import org.uu.mads.simulation.state.Tram;

public class TryOccupyJunctionEvent extends Event {
	private static final Duration JUNCTION_DURATION = Duration.ofMinutes(1);

	private final EndStation endStation;

	public TryOccupyJunctionEvent(final EndStation endStation) {
		super();
		this.endStation = endStation;
	}

	public EndStation getEndStation() {
		return this.endStation;
	}

	@Override
	public void fire() {
		if (this.endStation.getLastWaitingPoint().isTramWaiting()) {
			System.out.println("There is a tram at the waiting point for the end station " + this.endStation.getName()
					+ " that is requesting to use the junction.");
			useJunctionForArrival();
			useJunctionForDeparture();
		} else {
			System.out.println("There is a tram ready at end station " + this.endStation.getName()
					+ " that is requesting to use the junction.");
			useJunctionForDeparture();
		}
	}

	private void useJunctionForArrival() {
		final Junction junction = this.endStation.getJunction();
		if (!junction.isJunctionUsed()) {
			System.out.println("The junction of the end station " + this.endStation.getName() + " is currently free.");
			// We can send our tram into the crossing
			if (this.endStation.getTramOnPlatformB() == null) {
				// We send the tram from the junction to platform B
				final Tram nextTramWaiting = this.endStation.getLastWaitingPoint().popNextTramWaiting();
				junction.setTramOnLaneInB(nextTramWaiting);
				System.out.println(
						"Tram +  " + nextTramWaiting.getId() + " has moved from the waiting point for the end station "
								+ this.endStation.getName() + " to the lane In-B of its junction.");
				scheduleFreeJunctionEvent(nextTramWaiting);
			} else if (this.endStation.getTramOnPlatformA() == null) {
				// We send the tram from the junction to platform A
				final Tram nextTramWaiting = this.endStation.getLastWaitingPoint().popNextTramWaiting();
				junction.setTramOnLaneInA(nextTramWaiting);
				System.out.println(
						"Tram +  " + nextTramWaiting.getId() + " has moved from the waiting point for the end station "
								+ this.endStation.getName() + " to the lane In-A of its junction.");
				scheduleFreeJunctionEvent(nextTramWaiting);
			}
		} else if ((this.endStation.getTramOnPlatformA() == null) && junction.canUseLaneInA()) {
			System.out.println(
					"The junction of the end station " + this.endStation.getName() + "  is currently being used.");
			// We are in mode 3 and can send two trams at once
			// We send the tram from the junction to platform A
			final Tram nextTramWaiting = this.endStation.getLastWaitingPoint().popNextTramWaiting();
			junction.setTramOnLaneInA(nextTramWaiting);
			System.out.println(
					"Tram +  " + nextTramWaiting.getId() + " has moved from the waiting point for the end station "
							+ this.endStation.getName() + " to the lane In-A of its junction.");
			scheduleFreeJunctionEvent(nextTramWaiting);
		} else {
			System.out.println("Cannot use junction for arrival");
		}
	}

	private void useJunctionForDeparture() {
		final Junction junction = this.endStation.getJunction();
		if (!junction.isJunctionUsed()) {
			System.out.println("The junction of the end station " + this.endStation.getName() + " is currently free.");
			if (this.endStation.isTramReadyOnPlatformA()) {
				// We send the tram from platform A to the junction
				final Tram tramOnPlatformA = this.endStation.getTramOnPlatformA();
				junction.setTramOnLaneOutA(tramOnPlatformA);
				this.endStation.departFromPlatformA();
				System.out
						.println("Tram +  " + tramOnPlatformA.getId() + " has moved from platform A of the end station "
								+ this.endStation.getName() + " to the lane Out-A of its junction.");
				scheduleFreeJunctionEvent(tramOnPlatformA);
			} else if (this.endStation.isTramReadyOnPlatformB()) {
				// We send the tram from platform B to the junction
				final Tram tramOnPlatformB = this.endStation.getTramOnPlatformB();
				junction.setTramOnLaneOutB(tramOnPlatformB);
				this.endStation.departFromPlatformB();
				System.out
						.println("Tram +  " + tramOnPlatformB.getId() + " has moved from platform B of the end station "
								+ this.endStation.getName() + " to the lane Out-B of its junction.");
				scheduleFreeJunctionEvent(tramOnPlatformB);
			}
		} else if (this.endStation.isTramReadyOnPlatformB() && junction.canUseLaneOutB()) {
			// We are in mode 3 and can send two trams at once
			// We send the tram from platform B to the junction
			System.out.println(
					"The junction of the end station " + this.endStation.getName() + "  is currently being used.");
			final Tram tramOnPlatformB = this.endStation.getTramOnPlatformB();
			junction.setTramOnLaneOutB(tramOnPlatformB);
			this.endStation.departFromPlatformB();
			System.out.println("Tram +  " + tramOnPlatformB.getId() + " has moved from platform B of the end station "
					+ this.endStation.getName() + " to the lane Out-B of its junction.");
			scheduleFreeJunctionEvent(tramOnPlatformB);
		}
	}

	private void scheduleFreeJunctionEvent(final Tram tram) {
		final FreeJunctionEvent freeJunctionEvent = new FreeJunctionEvent(this.endStation, tram);
		EventScheduler.get().scheduleEventAhead(freeJunctionEvent, JUNCTION_DURATION);
	}

	@Override
	public String toString() {
		return "TryOccupyJunctionEvent [endStation=" + this.endStation + "]";
	}
}