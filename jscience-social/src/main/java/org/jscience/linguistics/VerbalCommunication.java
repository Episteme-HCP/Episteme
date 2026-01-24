package org.jscience.linguistics;

import java.io.Serializable;
import java.util.Objects;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a single act of verbal communication between locutors.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class VerbalCommunication implements Serializable {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Locutor speaker;

    @Attribute
    private final Phrase message;

    public VerbalCommunication(Locutor speaker, Phrase message) {
        this.speaker = Objects.requireNonNull(speaker, "Speaker cannot be null");
        this.message = Objects.requireNonNull(message, "Message cannot be null");
    }

    public Locutor getSpeaker() {
        return speaker;
    }

    public Phrase getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%s: \"%s\"", speaker.getIndividual().getName(), message);
    }
}
