package org.example.simulator.schemas.output;

public class OutputSimulatorMessage {
	private long room_id;
	private RoomChangesParameter room_changes;

	public OutputSimulatorMessage() {

	}

	public OutputSimulatorMessage(long room_id, RoomChangesParameter room_changes) {
		this.room_id = room_id;
		this.room_changes = room_changes;
	}

	public long getRoom_id() {
		return room_id;
	}
	public void setRoom_id(Integer room_id) {
		this.room_id = room_id;
	}

	public RoomChangesParameter getRoom_changes() {
		return room_changes;
	}
	public void setRoom_changes(RoomChangesParameter room_changes) {
		this.room_changes = room_changes;
	}
}
