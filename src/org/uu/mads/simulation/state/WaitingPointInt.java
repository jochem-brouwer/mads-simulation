package org.uu.mads.simulation.state;

public class WaitingPointInt extends WaitingPoint {
	private IntPlatform nextPlatform;

  public WaitingPointInt(IntPlatform nextPlatform) {
    super();
    this.nextPlatform = nextPlatform;
  }

  public IntPlatform getNextPlatform() {
    return this.nextPlatform;
  }
}