package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
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
		this.waitingPoint = waitingPoint;
		this.tram = tram;
	}

	public WaitingPoint getWaitingPoint() {
		return this.waitingPoint;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		this.waitingPoint.addTram(this.tram);

		final Tram nextTram = this.waitingPoint.getNextTramWaiting();

		if (nextTram != null) {

			// The tram with the correct id to leave next has already arrived
			if (this.waitingPoint.getNextPlatform() instanceof EndStation) {
				// Next station is an end station -> Schedule TryOccupyJunction
				//System.out.println("Tram " + tram.getId() + " arrived at the waiting point for the endstation "
				//		+ this.waitingPoint.getNextPlatform().getName() + ".");

				final TryOccupyJunctionEvent tryOccupyJunctionEvent = new TryOccupyJunctionEvent(
						(EndStation) this.waitingPoint.getNextPlatform());
				EventScheduler.get().scheduleEventAhead(tryOccupyJunctionEvent, Duration.ZERO);
			} else {

				// Next station is an int. station -> Schedule TramArrivesIntermediate
				//System.out.println("Tram " + tram.getId() + " arrived at the waiting point for the intstation "
				//		+ this.waitingPoint.getNextPlatform().getName() + ".");
				final TramArrivesIntStationEvent tramArrivesIntermediateEvent = new TramArrivesIntStationEvent(
						(IntPlatform) this.waitingPoint.getNextPlatform(), this.tram);
				EventScheduler.get().scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
				this.waitingPoint.popNextTramWaiting();
			}
		}
	}

	@Override
	public String toString() {
		return "ArriveWaitingPointEvent [waitingPoint=" + this.waitingPoint + ", tram=" + this.tram + "]";
	}

}