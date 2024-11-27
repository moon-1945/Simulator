package org.example.simulator.violationGenerators;

import org.example.simulator.schemas.input.Building;
import org.example.simulator.schemas.input.Floor;
import org.example.simulator.violationGenerators.roomViolationGenerators.IRoomViolationGenerator;
import org.example.simulator.violationGenerators.roomViolationGenerators.RoomFireGenerator;
import org.example.simulator.violationGenerators.roomViolationGenerators.RoomRobberyGenerator;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class BuildingViolationGenerator {
	private static final List<IRoomViolationGenerator> violationGenerators = new ArrayList<>();

	static {
		violationGenerators.add(new RoomFireGenerator());
		violationGenerators.add(new RoomRobberyGenerator());
	}

	private final Building building;
	private final ExecutorService executorService;
	private final BuildingViolationEvent buildingViolationsEvent;

	public BuildingViolationGenerator(Building building, ExecutorService executorService, BuildingViolationEvent floorViolationEvent) {
		this.building = building;
		this.executorService = executorService;
		this.buildingViolationsEvent = floorViolationEvent;
	}

	private static IRoomViolationGenerator getRandomGenerator() {
		if (violationGenerators.isEmpty()) {
			throw new IllegalStateException("No violation generators available");
		}
		Random random = new Random();
		return violationGenerators.get(random.nextInt(violationGenerators.size()));
	}

	public void generateViolations() {
		Map<Long, RoomState> violations = receiveViolations();
		buildingViolationsEvent.notifyListeners(new BuildingViolationsEventArgs(violations));
	}

	private Map<Long, RoomState> receiveViolations() {
		List<Floor> floors = building.getFloors();

		List<Future<Map<Long, RoomState>>> futuresGeneratingViolations = new ArrayList<>();

		int floorCountWithViolations = Math.min(3, floors.size());
		for (int i = 0; i < floorCountWithViolations; i++) {
			Floor floor = floors.get(i);

			Future<Map<Long, RoomState>> future = executorService.submit(() -> {
				IRoomViolationGenerator roomViolationGenerator = getRandomGenerator();
				FloorViolationGenerator floorViolationGenerator = new FloorViolationGenerator(floor, roomViolationGenerator);
				Map<Long, RoomState> violations = floorViolationGenerator.generateViolations();

				return violations;
			});

			futuresGeneratingViolations.add(future);
		}

		ConcurrentLinkedQueue<Map<Long, RoomState>> aggregatedViolations = new ConcurrentLinkedQueue<>();

		for (Future<Map<Long, RoomState>> future : futuresGeneratingViolations) {
			try {
				Map<Long, RoomState> violations = future.get();
				aggregatedViolations.add(violations);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		Map<Long, RoomState> allViolations = new HashMap<>();
		for (Map<Long, RoomState> violations : aggregatedViolations) {
			allViolations.putAll(violations);
		}

		return allViolations;
	}
}
