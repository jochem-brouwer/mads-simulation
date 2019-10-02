package org.uu.mads.simulation;

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
		// TODO Auto-generated method stub

	}

}