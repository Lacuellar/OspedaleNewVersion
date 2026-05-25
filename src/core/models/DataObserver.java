package core.models;

/**
 * Observer interface for the Observer pattern (Bonus requirement).
 * Views implementing this interface will be notified automatically
 * whenever a model is created or modified in the DataStore.
 */
public interface DataObserver {
    /**
     * Called when data in the DataStore changes.
     * @param entityType "appointments", "hospitalizations", "patients", or "doctors"
     */
    void onDataChanged(String entityType);
}
