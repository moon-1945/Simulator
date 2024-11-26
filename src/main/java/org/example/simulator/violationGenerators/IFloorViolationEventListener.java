package org.example.simulator.violationGenerators;

import java.util.Map;

public interface IFloorViolationEventListener {
    void onFloorViolationsReceived(FloorViolationEventArgs violationsInfo);
}

