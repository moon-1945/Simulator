package org.example.simulator;

import org.example.simulator.schemas.input.Building;
import org.example.simulator.schemas.input.Room;
import org.example.simulator.schemas.output.OutputSimulatorMessage;
import org.example.simulator.schemas.output.RoomChangesParameter;
import org.example.simulator.utils.RoomStateExtractor;
import org.example.simulator.violationGenerators.BuildingViolationsEventArgs;
import org.example.simulator.violationGenerators.IBuildingViolationsEventListener;
import org.example.simulator.violationGenerators.RoomState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BuildingViolationsEventListener implements IBuildingViolationsEventListener {
    private final KafkaProducerService kafkaProducerService;
    private final Building building;
    private volatile Map<Long, RoomState> idToRoomState;
    
    public BuildingViolationsEventListener(KafkaProducerService kafkaProducerService, Building building) {
        this.kafkaProducerService = kafkaProducerService;
        this.building = building;
    }
    
    public Map<Long, RoomState> getIdToRoomState() {
        if (idToRoomState == null) {
            synchronized (this) {
                if (idToRoomState == null) {
                    idToRoomState = building.getFloors().stream()
                            .flatMap(floor -> floor.getRooms().stream())
                            .collect(Collectors.toMap(
                                    Room::getRoomId,
                                    room -> new RoomStateExtractor(room).extractRoomState()
                            ));
                }
            }
        }
        return idToRoomState;
    }

    @Override
    public void onBuildingViolationsReceived(BuildingViolationsEventArgs violations) {
        int totalCount = violations.getViolations().size();

        List<OutputSimulatorMessage> violationMessages = new ArrayList<>();

        for (Map.Entry<Long, RoomState> entry : violations.getViolations().entrySet()) {
            Long roomId = entry.getKey();
            RoomState violation = entry.getValue();
            RoomState currentRoomState = getIdToRoomState().get(roomId);

            RoomChangesParameter roomChanges = new RoomChangesParameter(
                    (int) (violation.getTemperature() - currentRoomState.getTemperature()),
                    violation.getSmokePercent() - currentRoomState.getSmokePercent(),
                    violation.getMovementLevel() - currentRoomState.getMovementLevel()
            );

            OutputSimulatorMessage outputSimulatorMessage = new OutputSimulatorMessage(
                    roomId,
                    roomChanges,
                    totalCount
            );

            violationMessages.add(outputSimulatorMessage);
        }

        violationMessages.parallelStream()
                .forEach(kafkaProducerService::sendViolation);
    }
}
