package org.example.simulator.schemas;

import java.util.List;

class Room {
    private Long roomId;
    private double area;
    private int windows;
    private int doors;
    private List<Sensor> sensors;

    public Room() {}

    public Room(Long roomId, double area, int windows, int doors, List<Sensor> sensors) {
        this.roomId = roomId;
        this.area = area;
        this.windows = windows;
        this.doors = doors;
        this.sensors = sensors;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getWindows() {
        return windows;
    }

    public void setWindows(int windows) {
        this.windows = windows;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", area=" + area +
                ", windows=" + windows +
                ", doors=" + doors +
                ", sensors=" + sensors +
                '}';
    }
}