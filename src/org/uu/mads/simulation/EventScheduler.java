package org.uu.mads.simulation;

public class EventScheduler {
	private Map<LocalTime, List<Event>> events; // needs to be sorted by time and priority

	public void ScheduleEvent(Event event, LocalTime time) {
		// implement
	}
}

public abstract class Event {
	private int priority;

	public void fire();

}

public class ScheduledLeaveEndStationEvent extends Event {
	private EndStation endStation;
}

public class TryOccupyJunctionEvent extends Event {
	private EndStation endStation;
	private WaitingPointJunction waitingPointJunction;
	private Junction junction;
}

public class FreeJunctionEvent extends Event {
	private Junction junction;
	private Tram tram;
}
 
public class ArriveEndStation extends Event {
	private EndStation endStation;
	private Tram tram;
}

public class ArriveWaitingPointEndStationEvent extends Event {
	private WaitingPointJunction waitingPointJunction;
	private Tram tram;
}

public class ArriveWaitingPointIntermediateStationEvent extends Event {
	private WaitingPointInt waitingPointInt;
	private Tram tram;
}

public class TramArrivesIntermediateEvent extends Event {
	private IntPlatform intPlatform;
	private Tram tram;
}

public class TramArrivesIntermediateStationEvent extends Event {
	private IntPlatform intPlatform;
	private Tram tram;
}

public class TramLeavesIntermediateStationEvent extends Event {
	private IntPlatform intPlatform;
	private Tram tram;
}

public class PlatformFreeEvent extends Event {
	private IntPlatform intPlatform;
}

