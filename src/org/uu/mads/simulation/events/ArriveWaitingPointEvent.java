package org.uu.mads.simulation.events;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.Simulation;
import org.uu.mads.simulation.state.EndStation;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPoint;

public class ArriveWaitingPointEvent extends Event {
	private final static int HIGH_PRIORITY = 2;
	private final WaitingPoint waitingPoint;
	private final Tram tram;

	public ArriveWaitingPointEvent(final WaitingPoint waitingPoint, final Tram tram) {
		super(HIGH_PRIORITY);

		Objects.requireNonNull(tram, "Given tram must not be null!");
		Objects.requireNonNull(waitingPoint, "Given waitingPoint must not be null!");

		this.waitingPoint = waitingPoint;
		this.tram = tram;
	}

	@Override
	public void fire() {
		Simulation.log("Tram " + this.tram.getId() + " arrived at Waiting point of platform "
				+ this.waitingPoint.getNextPlatform().getName() + " at " + EventScheduler.getInstance().getCurrentTime());
		this.waitingPoint.addTram(this.tram);

		Simulation.logTramPositions();

		if (EventScheduler.getInstance().getCurrentTime().isAfter(LocalTime.of(7,30))
		&& this.waitingPoint.getNextPlatform() instanceof EndStation) {
			this.tram.setJunctionArrivalTime(EventScheduler.getInstance().getCurrentTime());
		}

		if (this.waitingPoint.isTramWaitingInCorrectOrder()) {
			Simulation.logVerbose("Trams at " + this.waitingPoint + " are in correct order");

			// The tram with the correct id to leave next has already arrived
			if (this.waitingPoint.getNextPlatform() instanceof EndStation) {
				final TryOccupyJunctionEvent tryOccupyJunctionEvent = new TryOccupyJunctionEvent(
						(EndStation) this.waitingPoint.getNextPlatform());
				EventScheduler.getInstance().scheduleEventAhead(tryOccupyJunctionEvent, Duration.ZERO);
			} else if (!((IntPlatform) this.waitingPoint.getNextPlatform()).isOccupied()) {
				final TramArrivesIntPlatformEvent tramArrivesIntermediateEvent = new TramArrivesIntPlatformEvent(
						(IntPlatform) this.waitingPoint.getNextPlatform(), this.waitingPoint.popNextTramWaiting());
				EventScheduler.getInstance().scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
			}
		} else {
			Simulation.logVerbose("Trams at " + this.waitingPoint + " are not in correct order");
		}
	}

	@Override
	public String toString() {
		return "ArriveWaitingPointEvent [waitingPoint=" + this.waitingPoint + ", tram=" + this.tram + "]";
	}

}