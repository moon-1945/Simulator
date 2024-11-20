package org.example.simulator.schemas.input;

public class Sensor {
    private Long sensorId;
    private String sensorType;
    private double sensorValue;
    private Threshold thresholds;
    private String status;

    public Sensor() {}

    public Sensor(Long sensorId, String sensorType, double sensorValue, Threshold thresholds, String status) {
        this.sensorId = sensorId;
        this.sensorType = sensorType;
        this.sensorValue = sensorValue;
        this.thresholds = thresholds;
        this.status = status;
    }

    public Long getSensorId() {
        return sensorId;
    }

    public void setSensorId(Long sensorId) {
        this.sensorId = sensorId;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public double getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(double sensorValue) {
        this.sensorValue = sensorValue;
    }

    public Threshold getThresholds() {
        return thresholds;
    }

    public void setThresholds(Threshold thresholds) {
        this.thresholds = thresholds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "sensorId=" + sensorId +
                ", sensorType='" + sensorType + '\'' +
                ", sensorValue=" + sensorValue +
                ", thresholds=" + thresholds +
                ", status='" + status + '\'' +
                '}';
    }
}
