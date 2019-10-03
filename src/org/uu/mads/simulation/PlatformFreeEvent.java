package org.uu.mads.simulation;

import java.time.Duration;

public class PlatformFreeEvent extends Event {
	private final IntPlatform intPlatform;

	public PlatformFreeEvent(final IntPlatform intPlatform) {
		super();
		this.intPlatform = intPlatform;
	}

	public IntPlatform getIntPlatform() {
		return this.intPlatform;
	}

	@Override
	public void fire(final EventScheduler eventScheduler) {
		this.intPlatform.setUnoccupied();

		final WaitingPointInt wp = this.intPlatform.getLastWp();

		final Tram nextTram = wp.getNextTramWaiting();
		if (nextTram != null) {
			wp.removeTram(nextTram);
			final TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(
					this.intPlatform, nextTram);
			eventScheduler.scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
		}
	}

	@Override
	public String toString() {
		return "PlatformFreeEvent [intPlatform=" + this.intPlatform + "]";
	}
}