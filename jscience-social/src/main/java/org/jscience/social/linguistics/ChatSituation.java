/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.social.linguistics;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.jscience.natural.biology.Individual;
import org.jscience.social.sociology.Situation;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents the interaction of participants communicating.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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

