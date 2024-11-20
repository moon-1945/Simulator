package org.example.simulator.violationGenerators.roomViolationGenerators;

import org.example.simulator.violationGenerators.RoomState;

public class RoomFireGenerator implements IRoomViolationGenerator {

	@Override
	public RoomState generate(RoomState currentState) {
		RoomState resultState = new RoomState(
				currentState.getMovementLevel(),
				200,
				50
		);
		return resultState;
	}
}
