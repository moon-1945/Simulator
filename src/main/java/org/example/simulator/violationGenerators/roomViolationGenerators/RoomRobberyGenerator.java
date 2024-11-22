package org.example.simulator.violationGenerators.roomViolationGenerators;

import org.example.simulator.violationGenerators.RoomState;

import java.util.Random;

public class RoomRobberyGenerator implements IRoomViolationGenerator {

	private static final double MOVEMENT_LEVEL_MEAN = 1.0;
	private static final double MOVEMENT_LEVEL_STD_DEV = 0.2 / 2.57; // 95% confidence interval approximation
	private static final double MOVEMENT_LEVEL_MIN = 0.8;
	private static final double MOVEMENT_LEVEL_MAX = 1.2;

	private final Random random = new Random();

	@Override
	public RoomState generate(RoomState currentState) {
		double movementLevel = clip(generateNormalValue(MOVEMENT_LEVEL_MEAN, MOVEMENT_LEVEL_STD_DEV),
				MOVEMENT_LEVEL_MIN,
				MOVEMENT_LEVEL_MAX);
		double temperature = currentState.getTemperature(); // Retain current temperature
		int smokePercent = currentState.getSmokePercent(); // Retain current smoke percentage

		return new RoomState(movementLevel, temperature, smokePercent);
	}

	private double generateNormalValue(double mean, double stdDev) {
		return mean + random.nextGaussian() * stdDev;
	}

	private double clip(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}
}
