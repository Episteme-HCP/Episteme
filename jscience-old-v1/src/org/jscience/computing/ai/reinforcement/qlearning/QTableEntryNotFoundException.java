package org.jscience.computing.ai.reinforcement.qlearning;

/**
 * Exception thrown when an entry not found in the q-table. Entry in this
 * context can be a state, or a state-action pair.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class QTableEntryNotFoundException extends Exception {
/**
     * Constructs a new QTableEntryNotFoundException instance with the given
     * error message.
     *
     * @param message An error message describing the reason of this exception
     */
    public QTableEntryNotFoundException(String message) {
        super(message);
    }
}
