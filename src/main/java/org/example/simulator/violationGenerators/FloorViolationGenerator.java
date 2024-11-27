package org.example.simulator.violationGenerators;

import org.example.simulator.utils.MathUtils;
import org.example.simulator.utils.RoomStateExtractor;
import org.example.simulator.schemas.input.Floor;
import org.example.simulator.schemas.input.Room;
import org.example.simulator.violationGenerators.roomViolationGenerators.IRoomViolationGenerator;

import java.util.*;
import java.util.stream.Collectors;

public class FloorViolationGenerator {
	private final Floor floor;
	private final IRoomViolationGenerator roomViolationGenerator;

	public FloorViolationGenerator(Floor floor, IRoomViolationGenerator roomViolationGenerator) {
		this.floor = floor;
		this.roomViolationGenerator = roomViolationGenerator;
	}

	public Map<Long, RoomState> generateViolations()
	{
		Map<Long, RoomState> currentState = floor.getRooms().stream()
				.collect(Collectors.toMap(
						Room::getRoomId,
						room -> new RoomStateExtractor(room).extractRoomState()
				));

		return generate(currentState);
	}

	private Map<Long, RoomState> generate(Map<Long, RoomState> currentState) {
		int roomCount = floor.getRooms().size();

		int roomsWithViolationsCount =
				MathUtils.roundToNearestInt(
						MathUtils.clip(
								MathUtils.generateNormalValue((roomCount + 1) * 0.5, (roomCount - 1) * 0.5 / 1.95), 1, roomCount));

		List<Long> keys = new ArrayList<>(currentState.keySet());
		Collections.shuffle(keys);

		Map<Long, RoomState> resultState = new HashMap<>();
		for(int i = 0; i < keys.size(); i++) {
			if (i == roomsWithViolationsCount) break;
			Long key = keys.get(i);
			resultState.put(key, roomViolationGenerator.generate(currentState.get(key)));
		}

		return resultState;
	}
}
