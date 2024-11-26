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

	public void startGenerateViolations_() {
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
						//floorViolationEvent.notifyListeners(violations);
					} finally {
						lock.unlock();
					}
				});
			}
		} finally {
			lock.unlock();
		}
	}

	public void startGenerateViolations() {
		ConcurrentLinkedQueue<Map<Long, RoomState>> violations = receiveViolations();
		notifyListeners(violations);
	}

	public ConcurrentLinkedQueue<Map<Long, RoomState>> receiveViolations() {
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

		//every element from that queue is violations on certain floor I don't know what of its exactly because of concurency
		ConcurrentLinkedQueue<Map<Long, RoomState>> aggregatedViolations = new ConcurrentLinkedQueue<>();

		for (Future<Map<Long, RoomState>> future : futuresGeneratingViolations) {
			try {
				Map<Long, RoomState> violations = future.get();
				aggregatedViolations.add(violations);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		return aggregatedViolations;
	}

	public void notifyListeners(ConcurrentLinkedQueue<Map<Long, RoomState>> violations) {
		int totalCount = violations.stream()
				.mapToInt(Map::size)
				.sum();

		for (Map<Long, RoomState> floorViolations : violations) {
			floorViolationEvent.notifyListeners(new FloorViolationEventArgs(floorViolations, totalCount));
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
