package org.example.simulator.violationGenerators.roomViolationGenerators;

import org.example.simulator.violationGenerators.RoomState;

public class RoomRobberyGenerator implements IRoomViolationGenerator {

	@Override
	public RoomState generate(RoomState currentState) {
		RoomState resultState = new RoomState(
				1,
				currentState.getTemperature(),
				currentState.getSmokePercent()
		);
		return resultState;
	}
}
