package org.example.simulator.utils;

import java.util.Random;

public class MathUtils {
	private static final Random random = new Random();

	/**
	 * Clips a given value to a specified range.
	 *
	 * @param value the value to be clipped
	 * @param min   the minimum allowable value
	 * @param max   the maximum allowable value
	 * @return the clipped value
	 */
	public static double clip(double value, double min, double max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		}
		return value;
	}

	/**
	 * Generates a value from a normal distribution with the specified mean and standard deviation.
	 *
	 * @param mean   the mean of the normal distribution
	 * @param stdDev the standard deviation of the normal distribution
	 * @return a randomly generated value
	 */
	public static double generateNormalValue(double mean, double stdDev) {
		return mean + random.nextGaussian() * stdDev;
	}

	/**
	 * Rounds a double value to the nearest integer.
	 *
	 * @param value the value to be rounded
	 * @return the rounded integer value
	 */
	public static int roundToNearestInt(double value) {
		return (int) Math.floor(value + 0.5);
	}
}
