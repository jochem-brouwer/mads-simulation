package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.Junction;
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
			useJunctionForArrival();
		} else {
			useJunctionForDeparture();
		}
	}

	private void useJunctionForArrival() {
		if (!this.junction.isJunctionUsed()) {
			// We can send our tram into the crossing
			if (this.endStation.getTramOnPlatformB() == null) {
				// We send the tram from the junction to platform B
				this.junction.setTramOnLaneInB(this.waitingPointJunction.popNextTramWaiting());
				scheduleFreeJunctionEvent();
			} else if (this.endStation.getTramOnPlatformA() == null) {
				// We send the tram from the junction to platform A
				this.junction.setTramOnLaneInA(this.waitingPointJunction.popNextTramWaiting());
				scheduleFreeJunctionEvent();
			}
		} else if (this.endStation.getTramOnPlatformA() == null) {
			// We are in mode 3 and can send two trams at once
			// We send the tram from the junction to platform A
			this.junction.setTramOnLaneInA(this.waitingPointJunction.popNextTramWaiting());
			scheduleFreeJunctionEvent();
		}
	}

	private void useJunctionForDeparture() {
		if (!this.junction.isJunctionUsed()) {
			if (this.endStation.isTramReadyOnPlatformA()) {
				// We send the tram from platform A to the junction
				this.junction.setTramOnLaneOutA(this.endStation.getTramOnPlatformA());
				this.endStation.freePlatformA();
				scheduleFreeJunctionEvent();
			} else if (this.endStation.isTramReadyOnPlatformB()) {
				// We send the tram from platform B to the junction
				this.junction.setTramOnLaneOutB(this.endStation.getTramOnPlatformB());
				this.endStation.freePlatformB();
				scheduleFreeJunctionEvent();
			}
		} else if (this.endStation.isTramReadyOnPlatformB()) {
			// We are in mode 3 and can send two trams at once
			// We send the tram from platform B to the junction
			this.junction.setTramOnLaneOutB(this.endStation.getTramOnPlatformB());
			this.endStation.freePlatformB();
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