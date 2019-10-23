package org.uu.mads.simulation.events;

import java.time.Duration;
import java.util.Objects;

import org.uu.mads.simulation.EventScheduler;
import org.uu.mads.simulation.state.IntPlatform;
import org.uu.mads.simulation.state.WaitingPoint;

public class PlatformFreeEvent extends Event {
	private final IntPlatform intPlatform;

	public PlatformFreeEvent(final IntPlatform intPlatform) {
		super();

		Objects.requireNonNull(intPlatform, "Given intPlatform must not be null!");

		this.intPlatform = intPlatform;
	}

	@Override
	public void fire() {
		this.intPlatform.setUnoccupied();

		final WaitingPoint waitingPoint = this.intPlatform.getLastWaitingPoint();

		if (waitingPoint.isTramWaitingInCorrectOrder()) {
			final TramArrivesIntPlatformEvent tramArrivesIntermediateEvent = new TramArrivesIntPlatformEvent(
					this.intPlatform, waitingPoint.popNextTramWaiting());
			EventScheduler.getInstance().scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
		}
	}

	@Override
	public String toString() {
		return "PlatformFreeEvent [intPlatform=" + this.intPlatform + "]";
	}
}