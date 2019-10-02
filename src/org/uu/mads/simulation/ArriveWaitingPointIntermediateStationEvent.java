package org.uu.mads.simulation;

public class ArriveWaitingPointIntermediateStationEvent extends Event {
	private final WaitingPointInt waitingPointInt;
	private final Tram tram;

  public ArriveWaitingPointIntermediateStationEvent(WaitingPointInt wp, Tram t){
    waitingPointInt = wp;
    tram = t;
  }

  public void fire(EventScheduler eventScheduler) {
    this.waitingPointInt.addTram(this.tram);
    Tram nextTram = this.waitingPointInt.nextTram();

    if (nextTram != null) {
      this.waitingPointInt.removeTram(nextTram);
      TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(this.WaitingPointInt.getNextPlatform(), this.tram);
      eventScheduler.scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
    }
    
  }
}