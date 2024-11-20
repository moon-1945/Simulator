package org.example.simulator.violationGenerators.roomViolationGenerators;

import org.example.simulator.violationGenerators.RoomState;

public interface IRoomViolationGenerator {
	public RoomState generate(RoomState currentState);
}
