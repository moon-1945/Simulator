package org.example.simulator.schemas.input;

import java.util.List;


public class Floor {
    private int floorNumber;
    private List<Room> rooms;

    public Floor() {}

    public Floor(int floorNumber, List<Room> rooms) {
        this.floorNumber = floorNumber;
        this.rooms = rooms;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "Floor{" +
                "floorNumber=" + floorNumber +
                ", rooms=" + rooms +
                '}';
    }
}
