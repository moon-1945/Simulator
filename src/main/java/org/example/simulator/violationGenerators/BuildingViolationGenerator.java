package org.example.simulator.violationGenerators;

import org.example.simulator.schemas.input.Building;
import org.example.simulator.schemas.input.Floor;
import org.example.simulator.violationGenerators.roomViolationGenerators.IRoomViolationGenerator;
import org.example.simulator.violationGenerators.roomViolationGenerators.RoomFireGenerator;
import org.example.simulator.violationGenerators.roomViolationGenerators.RoomRobberyGenerator;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class BuildingViolationGenerator {
	private static final List<IRoomViolationGenerator> violationGenerators = new ArrayList<>();

	static {
		violationGenerators.add(new RoomFireGenerator());
		violationGenerators.add(new RoomRobberyGenerator());
	}

	private final Building building;
	private final IRoomViolationGenerator roomViolationGenerator;
	private final ExecutorService executorService;
	private final FloorViolationEvent floorViolationEvent;

	public BuildingViolationGenerator(Building building, ExecutorService executorService, FloorViolationEvent floorViolationEvent) {
		this.building = building;
		this.roomViolationGenerator = getRandomGenerator();
		this.executorService = executorService;
		this.floorViolationEvent = floorViolationEvent;
	}

	private static IRoomViolationGenerator getRandomGenerator() {
		if (violationGenerators.isEmpty()) {
			throw new IllegalStateException("No violation generators available");
		}
		Random random = new Random();
		int randomIndex = random.nextInt(violationGenerators.size());
		return violationGenerators.get(randomIndex);
	}

	public void startGenerateViolations() {
		List<Floor> floors = building.getFloors();
 		Collections.shuffle(floors);

		for(int i = 0; i < Math.min(3, floors.size()); i++) {
			Floor floor = floors.get(i);

			executorService.submit(() -> {
				FloorViolationGenerator floorViolationGenerator = new FloorViolationGenerator(floor, roomViolationGenerator);
				Map<Long, RoomState> violations = floorViolationGenerator.generateViolations();
				floorViolationEvent.notifyListeners(violations);
			});
		}
	}
}


