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
	private final Tram tram;

	public TryOccupyJunctionEvent(final EndStation endStation, final WaitingPointJunction waitingPointJunction,
			final Junction junction, final Tram tram) {
		super();
		this.endStation = endStation;
		this.waitingPointJunction = waitingPointJunction;
		this.junction = junction;
		this.tram = tram;
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
		if (!this.junction.isJunctionUsed()) {
			if (this.waitingPointJunction.getNextTramWaiting() != null) {
				useJunctionForArrival();
			} else {
				useJunctionForDeparture();
			}
		}
	}

	private void useJunctionForArrival() {
		if (this.junction.isJunctionUsed() == false) {
			// We can send our tram into the crossing.
			if (this.endStation.getTramOnPlatformB() == null) {
				// We send the tram from the junction to platform B.
				this.junction.setTramOnLaneInB(this.tram);
				scheduleFreeJunctionEvent();
			} else if (this.endStation.getTramOnPlatformA() == null) {
				// We send the tram from the junction to platform A.
				this.junction.setTramOnLaneInA(this.tram);
				scheduleFreeJunctionEvent();
			}
		} else if (this.endStation.getTramOnPlatformA() == null) {
			// We are in mode 3 and can send two trams at once
			this.junction.setTramOnLaneInA(this.tram);
			scheduleFreeJunctionEvent();
		}
	}

	private void useJunctionForDeparture() {
		if (this.junction.isJunctionUsed() == false) {
			if (this.endStation.getTramOnPlatformB() == null) {
				// We send the tram from the junction to platform B.
				this.junction.setTramOnLaneOutB(this.tram);
				this.endStation.freePlatformB();
				scheduleFreeJunctionEvent();
			} else if (this.endStation.getTramOnPlatformA() == null) {
				// We send the tram from the junction to platform A.
				this.junction.setTramOnLaneOutA(this.tram);
				this.endStation.freePlatformA();
				scheduleFreeJunctionEvent();
			}
		} else if (this.endStation.getTramOnPlatformB() == null) {
			// We are in mode 3 and can send two trams at once
			this.junction.setTramOnLaneOutB(this.tram);
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
				+ this.waitingPointJunction + ", junction=" + this.junction + ", tram=" + this.tram + "]";
	}
}