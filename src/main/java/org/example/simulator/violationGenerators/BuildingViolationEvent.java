package org.example.simulator.violationGenerators;

import java.util.ArrayList;
import java.util.List;

public class BuildingViolationEvent {
	private final List<IBuildingViolationsEventListener> listeners = new ArrayList<>();

	/**
	 * Adds a listener to the list of listeners.
	 *
	 * @param listener the listener to add
	 */
	public void addListener(IBuildingViolationsEventListener listener) {
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Removes a listener from the list of listeners.
	 *
	 * @param listener the listener to remove
	 */
	public void removeListener(IBuildingViolationsEventListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notifies all registered listeners about an event.
	 *
	 * @param violations the event to notify listeners about floor violations
	 */
	public void notifyListeners(BuildingViolationsEventArgs violations) {
		for (IBuildingViolationsEventListener listener : listeners) {
			listener.onBuildingViolationsReceived(violations);
		}
	}
}
