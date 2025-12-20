package org.jscience.psychology;

import java.time.Instant;

/**
 * Represents a cognitive decision made by an agent.
 */
public class Decision {

    private final String subject; // The agent making the decision
    private final String choice; // The option selected
    private final String rationale;
    private final Instant timestamp;

    public Decision(String subject, String choice, String rationale) {
        this.subject = subject;
        this.choice = choice;
        this.rationale = rationale;
        this.timestamp = Instant.now();
    }

    public String getSubject() {
        return subject;
    }

    public String getChoice() {
        return choice;
    }

    public String getRationale() {
        return rationale;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s decided to %s because %s", timestamp, subject, choice, rationale);
    }
}
