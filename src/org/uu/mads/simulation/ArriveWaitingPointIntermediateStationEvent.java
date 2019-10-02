package org.uu.mads.simulation;

import java.time.Duration;

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
	public void fire(final EventScheduler eventScheduler) {
		this.waitingPointInt.addTram(this.tram);
		final Tram nextTram = this.waitingPointInt.getNextTramWaiting();

		if (nextTram != null) {
			this.waitingPointInt.removeTram(nextTram);
			final TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(
					this.waitingPointInt.getNextPlatform(), this.tram);
			eventScheduler.scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
		}
	}

}