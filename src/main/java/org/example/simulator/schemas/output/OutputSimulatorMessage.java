package org.example.simulator.schemas.output;

public class OutputSimulatorMessage {
	private long room_id;
	private RoomChangesParameter room_changes;
	private int total_count;
	private int current_number;

	public OutputSimulatorMessage() {}

	public OutputSimulatorMessage(long room_id, RoomChangesParameter room_changes, int total_count, int current_number) {
		this.room_id = room_id;
		this.room_changes = room_changes;
		this.total_count = total_count;
		this.current_number = current_number;
	}

	public long getRoom_id() {
		return room_id;
	}

	public void setRoom_id(long room_id) {
		this.room_id = room_id;
	}

	public RoomChangesParameter getRoom_changes() {
		return room_changes;
	}

	public void setRoom_changes(RoomChangesParameter room_changes) {
		this.room_changes = room_changes;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getCurrent_number() {
		return current_number;
	}

	public void setCurrent_number(int current_number) {
		this.current_number = current_number;
	}
}
