package org.example.simulator.violationGenerators.roomViolationGenerators;

import org.example.simulator.utils.MathUtils;
import org.example.simulator.violationGenerators.RoomState;

import java.util.Random;

public class RoomRobberyGenerator implements IRoomViolationGenerator {

	private static final double MOVEMENT_LEVEL_MEAN = 1.0;
	private static final double MOVEMENT_LEVEL_STD_DEV = 0.2 / 2.57; // 95% confidence interval approximation
	private static final double MOVEMENT_LEVEL_MIN = 0.8;
	private static final double MOVEMENT_LEVEL_MAX = 1.2;

	@Override
	public RoomState generate(RoomState currentState) {
		double movementLevel = MathUtils.clip(
				MathUtils.generateNormalValue(MOVEMENT_LEVEL_MEAN, MOVEMENT_LEVEL_STD_DEV),
				MOVEMENT_LEVEL_MIN,
				MOVEMENT_LEVEL_MAX);
		double temperature = currentState.getTemperature();
		int smokePercent = currentState.getSmokePercent();

		return new RoomState(movementLevel, temperature, smokePercent);
	}
}
