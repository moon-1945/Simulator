package org.example.simulator.violationGenerators;

import java.util.Map;

public class FloorViolationEventArgs {
    private Map<Long, RoomState> violations;
    private int totalCount;
    
    FloorViolationEventArgs(Map<Long, RoomState> violations, int totalCount) {
        this.violations = violations;
        this.totalCount = totalCount;
    }
    
    public Map<Long, RoomState> getViolations() {
        return violations;
    }
    
    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setViolations(Map<Long, RoomState> violations) {
        this.violations = violations;
    }
}
