package org.example.simulator.violationGenerators;

import org.example.simulator.schemas.input.Building;
import org.example.simulator.schemas.input.Floor;
import org.example.simulator.violationGenerators.roomViolationGenerators.IRoomViolationGenerator;
import org.example.simulator.violationGenerators.roomViolationGenerators.RoomFireGenerator;
import org.example.simulator.violationGenerators.roomViolationGenerators.RoomRobberyGenerator;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BuildingViolationGenerator {
	private static final List<IRoomViolationGenerator> violationGenerators = new ArrayList<>();
	private volatile boolean canGenerateViolations = true;
	private final Lock lock = new ReentrantLock();

	static {
		violationGenerators.add(new RoomFireGenerator());
		violationGenerators.add(new RoomRobberyGenerator());
	}

	private final Building building;
	private final ExecutorService executorService;
	private final FloorViolationEvent floorViolationEvent;

	public BuildingViolationGenerator(Building building, ExecutorService executorService, FloorViolationEvent floorViolationEvent) {
		this.building = building;
		this.executorService = executorService;
		this.floorViolationEvent = floorViolationEvent;
	}

	private static IRoomViolationGenerator getRandomGenerator() {
		if (violationGenerators.isEmpty()) {
			throw new IllegalStateException("No violation generators available");
		}
		Random random = new Random();
		return violationGenerators.get(random.nextInt(violationGenerators.size()));
	}

	public void startGenerateViolations() {
		lock.lock();
		try {
			if (!canGenerateViolations) {
				return;
			}

			int totalCount = building.getTotalCount();
			int currentCount = building.getCurrentNumber();

			if (totalCount > 0 && (currentCount * 100 / totalCount) >= 40) {
				canGenerateViolations = false;
				System.out.println("Generation stopped: more than 40% of rooms have violations.");
				return;
			}

			List<Floor> floors = building.getFloors();
			Collections.shuffle(floors);

			for (int i = 0; i < Math.min(3, floors.size()); i++) {
				Floor floor = floors.get(i);

				executorService.submit(() -> {
					IRoomViolationGenerator roomViolationGenerator = getRandomGenerator();
					FloorViolationGenerator floorViolationGenerator = new FloorViolationGenerator(floor, roomViolationGenerator);
					Map<Long, RoomState> violations = floorViolationGenerator.generateViolations();

					lock.lock();
					try {
						floorViolationEvent.notifyListeners(violations);
					} finally {
						lock.unlock();
					}
				});
			}
		} finally {
			lock.unlock();
		}
	}

	public void resetGeneration(Building newBuilding) {
		lock.lock();
		try {
			this.canGenerateViolations = true;
			this.building.storeBuildingData(newBuilding.getBuildingId(), newBuilding.getFloors());
			System.out.println("Generation reset with new building configuration.");
		} finally {
			lock.unlock();
		}
	}
}
