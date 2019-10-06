package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPointInt;

public class ArriveWaitingPointIntermediateStationEvent extends Event {
	private final WaitingPointInt waitingPointInt;
	private final Tram tram;

	public ArriveWaitingPointIntermediateStationEvent(final WaitingPointInt waitingPointInt, final Tram tram) {
		super();
		this.waitingPointInt = waitingPointInt;
		this.tram = tram;
	}

	public WaitingPointInt getWaitingPointInt() {
		return this.waitingPointInt;
	}

	public Tram getTram() {
		return this.tram;
	}

	@Override
	public void fire() {
		this.waitingPointInt.addTram(this.tram);
		final Tram nextTram = this.waitingPointInt.popNextTramWaiting();

		if (nextTram != null) {
			this.waitingPointInt.removeTram(nextTram);
			final TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(
					this.waitingPointInt.getNextPlatform(), this.tram);
			EventScheduler.get().scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
		}
	}

	@Override
	public String toString() {
		return "ArriveWaitingPointIntermediateStationEvent [waitingPointInt=" + this.waitingPointInt + ", tram="
				+ this.tram + "]";
	}

}