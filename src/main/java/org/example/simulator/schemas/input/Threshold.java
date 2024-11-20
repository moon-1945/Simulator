package org.example.simulator.schemas.input;


class Threshold {
    private double min;
    private double max;

    public Threshold() {}

    public Threshold(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Threshold{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
