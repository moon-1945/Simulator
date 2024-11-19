package org.example.simulator.schemas;

import java.util.List;

public class Building {
    private static Building instance;
    private Long buildingId;
    private List<Floor> floors;

    private Building() {
    }

    public static Building getInstance() {
        if (instance == null) {
            synchronized (Building.class) {
                if (instance == null) {
                    instance = new Building();
                }
            }
        }
        return instance;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setFloors(List<Floor> floors) {
        this.floors = floors;
    }
}
