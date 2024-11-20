package org.example.simulator.utils;

import org.example.simulator.schemas.input.Room;
import org.example.simulator.schemas.input.Sensor;
import org.example.simulator.violationGenerators.RoomState;

import java.util.Objects;

//I'm not sure about that thing, but I don't see better solution
public class RoomStateExtractor {
	private final Room room;

	public RoomStateExtractor(Room room) {
		this.room = room;
	}

	public RoomState extractRoomState() {
		RoomState roomState = new RoomState();

		for(Sensor sensor: room.getSensors()){

			if(Objects.equals(sensor.getSensorType(), "Fire")){
				roomState.setSmokePercent((int)sensor.getSensorValue());
			}
			else if(Objects.equals(sensor.getSensorType(), "Temperature")){
				roomState.setTemperature(sensor.getSensorValue());
			}
			else if(Objects.equals(sensor.getSensorType(), "Intrusion")){
				roomState.setMovementLevel(sensor.getSensorValue());
			}
		}

		return roomState;
	}
}
