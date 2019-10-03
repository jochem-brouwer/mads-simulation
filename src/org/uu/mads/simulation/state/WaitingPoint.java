package org.uu.mads.simulation.state;

import java.util.LinkedList;
import java.util.List;

import java.util.Iterator;

public class WaitingPoint {
	private final List<Tram> waitingTrams = new LinkedList<>();
	private int lastTramLeftWaitingPoint = 0;

	// Getters and setters are here

	public Tram getNextTramWaiting() {
		final int targetID = this.lastTramLeftWaitingPoint + 1;
		for (Iterator<Tram> iter = this.waitingTrams.iterator(); iter.hasNext();) {
			final Tram tram = iter.next();
			if (tram.getId() == targetID) {
				return tram;
			}
		}
		return null;
	}

	public void removeTram(final Tram tram) {
		final boolean hasRemoved = this.waitingTrams.remove(tram);
		if (hasRemoved) {
			this.lastTramLeftWaitingPoint = tram.getId();
		}
	}

	public void addTram(final Tram tram) {
		this.waitingTrams.add(tram);
	}
}
