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

package org.jscience.sociology;


import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.jscience.linguistics.Language;
import org.jscience.philosophy.Belief;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.ComprehensiveIdentification;
import org.jscience.util.identity.UUIDIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents the cumulative shared elements of a social group or civilization.
 * A culture encompasses values (beliefs), norms (rituals), and societal artifacts,
 * as well as identifying traits like language and technological sophistication.
 * Modernized to implement ComprehensiveIdentification and support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.2
 * @since 1.0
 */
@Persistent
public class Culture implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    private final Identification id;

    @Attribute
    private final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Language language;

    @Attribute
    private int technologicalLevel;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Belief> beliefs = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Celebration> celebrations = new HashSet<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Ritual> rituals = new HashSet<>();

    @Attribute
    private int maritalType;

    /**
     * Creates a new Culture with specified traits.
     *
     * @param name               civilization name
     * @param language           primary language
     * @param technologicalLevel complexity level
     * @param beliefs            set of shared beliefs
     * @param celebrations       set of cultural celebrations
     * @param rituals            set of cultural rituals
     * @param maritalType        preferred marital structure
     * @param comments           descriptive details
     * @throws NullPointerException if any required argument is null
     */
    public Culture(String name, Language language, int technologicalLevel,
            Set<Belief> beliefs, Set<Celebration> celebrations, Set<Ritual> rituals,
            int maritalType, String comments) {
        this.id = new UUIDIdentification(UUID.randomUUID());
        setName(Objects.requireNonNull(name, "Name cannot be null"));
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        if (beliefs != null) this.beliefs.addAll(beliefs);
        if (celebrations != null) this.celebrations.addAll(celebrations);
        if (rituals != null) this.rituals.addAll(rituals);
        setComments(Objects.requireNonNull(comments, "Comments cannot be null"));
        this.technologicalLevel = technologicalLevel;
        this.maritalType = maritalType;
    }

    /** Legacy constructor. */
    public Culture(String name, Language language) {
        this(name, language, 0, new HashSet<>(), new HashSet<>(), new HashSet<>(), 0, "");
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the primary language of this culture.
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = Objects.requireNonNull(language);
    }

    /**
     * Returns the technological advancement level.
     * @return the level
     */
    public int getTechnologicalLevel() {
        return technologicalLevel;
    }

    public void setTechnologicalLevel(int technologicalLevel) {
        this.technologicalLevel = technologicalLevel;
    }

    /**
     * Returns an unmodifiable set of shared beliefs.
     * @return the beliefs
     */
    public Set<Belief> getBeliefs() {
        return Collections.unmodifiableSet(beliefs);
    }

    /**
     * Returns an unmodifiable set of celebrations.
     * @return the celebrations
     */
    public Set<Celebration> getCelebrations() {
        return Collections.unmodifiableSet(celebrations);
    }

    public void addCelebration(Celebration celebration) {
        if (celebration != null) {
            celebrations.add(celebration);
        }
    }

    public void addCelebration(String name) {
        if (name != null) {
            addCelebration(new Celebration(name));
        }
    }

    /**
     * Returns an unmodifiable set of rituals.
     * @return the rituals
     */
    public Set<Ritual> getRituals() {
        return Collections.unmodifiableSet(rituals);
    }

    /**
     * Returns the marital system classification.
     * @return the marital type
     */
    public int getMaritalType() {
        return maritalType;
    }

    public void setMaritalType(int maritalType) {
        this.maritalType = maritalType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Culture that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
