package org.uu.mads.simulation.state;

import java.util.HashSet;
import java.util.Set;

import org.uu.mads.simulation.Simulation;

public class WaitingPoint {
	private final Platform nextPlatform;
	private final Set<Tram> waitingTrams = new HashSet<>();
	private int lastTramLeftWaitingPoint = 0;

	public WaitingPoint(final Platform nextPlatform) {
		super();
		this.nextPlatform = nextPlatform;
	}

	public void setLastTramLeftWaitingPoint(int lastTramLeftWaitingPoint) {
		this.lastTramLeftWaitingPoint = lastTramLeftWaitingPoint;
	}

	public Platform getNextPlatform() {
		return this.nextPlatform;
	}

	public void addTram(final Tram tram) {
		this.waitingTrams.add(tram);
	}



	/**
	 * Gets the next tram from the set of the waiting trams if it fits the correct
	 * order and removes it from the set afterwards. If the tram of correct order
	 * hasn't arrived yet or if there are no trams waiting, null is returned.
	 *
	 * @return next tram waiting or null
	 */
	public Tram popNextTramWaiting() {
		final Tram nextTramWaiting = getNextTramWaiting();
		if (nextTramWaiting != null) {
			this.waitingTrams.remove(nextTramWaiting);
			this.lastTramLeftWaitingPoint = nextTramWaiting.getId();
		}
		return nextTramWaiting;
	}

	public boolean isTramWaiting() {
		return getNextTramWaiting() != null;
	}

	public Tram getNextTramWaiting() {
		final int targetID = (this.lastTramLeftWaitingPoint) % Simulation.NUMBER_OF_TRAMS + 1;
		for (final Tram tram : this.waitingTrams) {
			if (tram.getId() == targetID) {
				return tram;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "WaitingPoint [waitingTrams=" + this.waitingTrams + ", lastTramLeftWaitingPoint="
				+ this.lastTramLeftWaitingPoint + "]";
	}
}
