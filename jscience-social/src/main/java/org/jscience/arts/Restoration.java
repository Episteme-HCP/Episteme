package org.jscience.arts;

import org.jscience.history.time.UncertainDate;
import java.util.Objects;

/**
 * A Restoration event for an Artwork.
 */
public class Restoration {

    private final String processName;
    private final String restorer; // Individual or Organization name
    private final UncertainDate date;
    private final String outcome;
    private String comments;

    public Restoration(String processName, String restorer, UncertainDate date, String outcome) {
        this.processName = Objects.requireNonNull(processName, "Process name cannot be null");
        this.restorer = Objects.requireNonNull(restorer, "Restorer cannot be null");
        this.date = Objects.requireNonNull(date, "Date cannot be null");
        this.outcome = Objects.requireNonNull(outcome, "Outcome cannot be null");
    }

    public String getProcessName() {
        return processName;
    }

    public String getRestorer() {
        return restorer;
    }

    public UncertainDate getDate() {
        return date;
    }

    public String getOutcome() {
        return outcome;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
        return String.format("Restoration[%s by %s on %s: %s]", processName, restorer, date, outcome);
    }
}
