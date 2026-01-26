package org.jscience.linguistics;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jscience.biology.Individual;
import org.jscience.sociology.Situation;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents the interaction of participants communicating.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class ChatSituation extends Situation {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<VerbalCommunication> communications = new ArrayList<>();

    public ChatSituation(String name, String description) {
        super(name, description);
    }

    public void addLocutor(Individual individual) {
        if (getLocutor(individual) == null) {
            super.addRole(new Locutor(individual, this));
        }
    }

    public void addLocutor(Locutor locutor) {
        super.addRole(locutor);
    }

    public Set<Locutor> getLocutors() {
        return getRoles().stream()
                .filter(Locutor.class::isInstance)
                .map(Locutor.class::cast)
                .collect(Collectors.toUnmodifiableSet());
    }

    private Locutor getLocutor(Individual individual) {
        return getLocutors().stream()
                .filter(l -> l.getIndividual().equals(individual))
                .findFirst()
                .orElse(null);
    }

    public List<VerbalCommunication> getVerbalCommunications() {
        return Collections.unmodifiableList(communications);
    }

    public void addVerbalCommunication(VerbalCommunication communication) {
        Objects.requireNonNull(communication, "Communication cannot be null");
        if (!getLocutors().contains(communication.getSpeaker())) {
            addLocutor(communication.getSpeaker());
        }
        communications.add(communication);
    }

    @Override
    public String toString() {
        return String.format("ChatSituation: %s (%d communications)", getName(), communications.size());
    }
}
