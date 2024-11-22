package org.example.simulator.violationGenerators;

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
		Random random = new Random();

		int roomCount = floor.getRooms().size();

		int roomsWithViolationsCount =
				(int)Math.floor(clip((roomCount * 1.0 / 2) * (1 + random.nextGaussian() / 2.57), 0, roomCount) + 0.5);

		List<Long> keys = new ArrayList<>(currentState.keySet());
		Collections.shuffle(keys);

		Map<Long, RoomState> resultState = new HashMap<>();
		for(int i = 0; i < keys.size(); i++) {
			Long key = keys.get(i);

			if(i >= roomsWithViolationsCount) {
				resultState.put(key, currentState.get(key));
				continue;
			}

			resultState.put(key, roomViolationGenerator.generate(currentState.get(key)));
		}

		return resultState;
	}

	private static double clip(double value, double min, double max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		}
		return value;
	}
}
