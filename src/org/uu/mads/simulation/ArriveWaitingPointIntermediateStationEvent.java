package org.uu.mads.simulation;

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