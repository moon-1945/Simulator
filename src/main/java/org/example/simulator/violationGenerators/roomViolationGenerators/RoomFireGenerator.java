package org.example.simulator.violationGenerators.roomViolationGenerators;

import org.example.simulator.violationGenerators.RoomState;

import java.util.Random;

public class RoomFireGenerator implements IRoomViolationGenerator {

	private static final double TEMPERATURE_MEAN = 250.0;
	private static final double TEMPERATURE_STD_DEV = 50.0 / 2.57; // Approximation for 95% confidence interval
	private static final double TEMPERATURE_MIN = 200.0;
	private static final double TEMPERATURE_MAX = 300.0;

	private static final double SMOKE_PERCENT_MEAN = 70.0;
	private static final double SMOKE_PERCENT_STD_DEV = 30.0 / 2.57; // Approximation for 95% confidence interval
	private static final double SMOKE_PERCENT_MIN = 40.0;
	private static final double SMOKE_PERCENT_MAX = 100.0;

	private final Random random = new Random();

	@Override
	public RoomState generate(RoomState currentState) {
		double movementLevel = currentState.getMovementLevel();
		double temperature = clip(generateNormalValue(TEMPERATURE_MEAN, TEMPERATURE_STD_DEV), TEMPERATURE_MIN, TEMPERATURE_MAX);
		int smokePercent = (int) clip(generateNormalValue(SMOKE_PERCENT_MEAN, SMOKE_PERCENT_STD_DEV), SMOKE_PERCENT_MIN, SMOKE_PERCENT_MAX);

		return new RoomState(movementLevel, temperature, smokePercent);
	}

	private double generateNormalValue(double mean, double stdDev) {
		return mean + random.nextGaussian() * stdDev;
	}

	private double clip(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}
}

