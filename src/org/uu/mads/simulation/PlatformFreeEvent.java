package org.uu.mads.simulation;

public class PlatformFreeEvent extends Event {
	private IntPlatform intPlatform;

  public PlatformFreeEvent(IntPlatform ip) {
    intPlatform = ip;
  }

  public void fire(EventScheduler eventScheduler) {
    intPlatform.setUnoccupied();

    WaitingPointInt wp = this.intPlatform.getLastWp();

    Tram nextTram = wp.getNextTramWaiting();
    if (nextTram != null) {
      wp.removeTram(nextTram);
      TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(this.intPlatform, nextTram);
      eventScheduler.scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
    }
  }

	private final IntPlatform intPlatform;

	public PlatformFreeEvent(final IntPlatform intPlatform) {
		super();
		this.intPlatform = intPlatform;
	}

	public IntPlatform getIntPlatform() {
		return this.intPlatform;
	}

	@Override
  public void fire(EventScheduler eventScheduler) {
    intPlatform.setUnoccupied();

    WaitingPointInt wp = this.intPlatform.getLastWp();

    Tram nextTram = wp.getNextTramWaiting();
    if (nextTram != null) {
      wp.removeTram(nextTram);
      TramArrivesIntermediateEvent tramArrivesIntermediateEvent = new TramArrivesIntermediateEvent(this.intPlatform, nextTram);
      eventScheduler.scheduleEventAhead(tramArrivesIntermediateEvent, Duration.ZERO);
    }
  }
}