package org.example.simulator.violationGenerators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FloorViolationEvent {
	private final List<IFloorViolationEventListener> listeners = new ArrayList<>();

	/**
	 * Adds a listener to the list of listeners.
	 *
	 * @param listener the listener to add
	 */
	public void addListener(IFloorViolationEventListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a listener from the list of listeners.
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(IFloorViolationEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all registered listeners about an event.
	 *
	 * @param violations the event to notify listeners about floor violations
	 */
	public void notifyListeners(FloorViolationEventArgs violations) {
		for (IFloorViolationEventListener listener : listeners) {
			listener.onFloorViolationsReceived(violations);
		}
	}
}
