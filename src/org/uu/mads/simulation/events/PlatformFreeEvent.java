package org.uu.mads.simulation.events;

import java.time.Duration;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.Tram;
import org.uu.mads.simulation.state.WaitingPoint;

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
	public void fire() {
		this.intPlatform.setUnoccupied();

		final WaitingPoint wp = this.intPlatform.getLastWaitingPoint();

		final Tram nextTram = wp.popNextTramWaiting();
		if (nextTram != null) {
			final TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(
					this.intPlatform, nextTram);
			EventScheduler.get().scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
		}
	}

	@Override
	public String toString() {
		return "PlatformFreeEvent [intPlatform=" + this.intPlatform + "]";
	}
}