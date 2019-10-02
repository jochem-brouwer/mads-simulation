package org.uu.mads.simulation;

import java.util.List;

public class WaitingPoint {
	private List<Tram> waitingTrams = new LinkedList<>();
  private int lastTramLeftWaitingPoint = 0;


	// Getters and setters are here

  public Tram getNextTramWaiting() {
    int targetID = this.lastTramLeftWaitingPoint + 1;
    for (Iterator<Tram> iter = waitingTrams.iterator(); iter.hasNext(); ) {
      Tram tram = iter.next();
      if (tram.getId() == targetID) {
        return tram;
      }
    } 
  }

  public void removeTram(Tram tram) {
    boolean hasRemoved = this.waitingTrams.remove(tram);
    if (hasRemoved) {
      this.lastTramLeftWaitingPoint = tram.getId();
    }
  }

  public void addTram(Tram tram) {
    waitingTrams.add(tram);
  }
}

