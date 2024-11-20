package org.example.simulator.schemas.output;

public class RoomChangesParameter {

	private final double  deltaTemperature;
	private final int deltaSmokePercent;
	private final double movementLevelChanges;

	public RoomChangesParameter(int deltaTemperature, int deltaSmokePercent, double movementLevelChanges) {
		this.deltaTemperature = deltaTemperature;
		this.deltaSmokePercent = deltaSmokePercent;
		this.movementLevelChanges = movementLevelChanges;
	}

	public double getDeltaTemperature() {
		return deltaTemperature;
	}

	public int getDeltaSmokePercent() {
		return deltaSmokePercent;
	}

	public double getMovementLevelChanges() {
		return movementLevelChanges;
	}
}
