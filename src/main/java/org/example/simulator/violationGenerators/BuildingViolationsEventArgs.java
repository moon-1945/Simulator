package org.example.simulator.violationGenerators;

import java.util.Map;

public class BuildingViolationsEventArgs {
    private Map<Long, RoomState> violations;

    BuildingViolationsEventArgs(Map<Long, RoomState> violations) {
        this.violations = violations;
    }
    
    public Map<Long, RoomState> getViolations() {
        return violations;
    }

    public void setViolations(Map<Long, RoomState> violations) {
        this.violations = violations;
    }
}
