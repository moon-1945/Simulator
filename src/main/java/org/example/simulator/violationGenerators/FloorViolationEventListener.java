package org.example.simulator.violationGenerators;

import java.util.Map;

public interface FloorViolationEventListener {
    void onFloorViolationsReceived(Map<Long, RoomState> violations);
}
