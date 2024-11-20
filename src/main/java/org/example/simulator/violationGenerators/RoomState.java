package org.example.simulator.violationGenerators;

public class RoomState {
	private double movementLevel;
	private double temperature;
	private int smokePercent;

	public RoomState() {}

	public RoomState(double movementLevel, double temperature, int smokePercent) {
		this.movementLevel = movementLevel;
		this.temperature = temperature;
		this.smokePercent = smokePercent;
	}

	public double getMovementLevel() {
		return movementLevel;
	}

	public double getTemperature() {
		return temperature;
	}

	public int getSmokePercent() {
		return smokePercent;
	}

	public void setMovementLevel(double movementLevel) {
		this.movementLevel = movementLevel;
	}

	public void setSmokePercent(int smokePercent) {
		this.smokePercent = smokePercent;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
}
