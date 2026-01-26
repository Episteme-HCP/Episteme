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

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.linguistics.Language;
import org.jscience.philosophy.Belief;
import org.jscience.util.Commented;
import org.jscience.util.Named;
import org.jscience.util.identity.Identification;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents the cumulative shared elements of a social group or civilization.
 * A culture encompasses values (beliefs), norms (rituals), and societal artifacts,
 * as well as identifying traits like language and technological sophistication.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Culture implements Named, Commented, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Identification identification;

    @Attribute
    private String name;

    @Attribute
    private Language language;

    @Attribute
    private int technologicalLevel;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Belief> beliefs;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Celebration> celebrations;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Ritual> rituals;

    @Attribute
    private int maritalType;

    @Attribute
    private String comments;

    private final java.util.Map<String, Object> traits = new java.util.HashMap<>();

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
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.language = Objects.requireNonNull(language, "Language cannot be null");
        this.beliefs = new HashSet<>(Objects.requireNonNull(beliefs, "Beliefs set cannot be null"));
        this.celebrations = new HashSet<>(Objects.requireNonNull(celebrations, "Celebrations set cannot be null"));
        this.rituals = new HashSet<>(Objects.requireNonNull(rituals, "Rituals set cannot be null"));
        this.comments = Objects.requireNonNull(comments, "Comments cannot be null");
        this.technologicalLevel = technologicalLevel;
        this.maritalType = maritalType;
        this.identification = new SimpleIdentification(name + ":" + System.currentTimeMillis());
    }

    /** Legacy constructor. */
    public Culture(String name, Language language) {
        this(name, language, 0, new HashSet<>(), new HashSet<>(), new HashSet<>(), 0, "");
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the primary language of this culture.
     * @return the language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Returns the technological advancement level.
     * @return the level
     */
    public int getTechnologicalLevel() {
        return technologicalLevel;
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

    /** Legacy method to add celebration by name. */
    public void addCelebration(String name) {
        if (name != null) {
            celebrations.add(new Celebration(name));
        }
    }

    public void addCelebration(Celebration celebration) {
        if (celebration != null) {
            celebrations.add(celebration);
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

    @Override
    public String getComments() {
        return comments;
    }

    @Override
    public void setComments(String comments) {
        this.comments = Objects.requireNonNull(comments, "Comments cannot be null");
    }

    @Override
    public java.util.Map<String, Object> getTraits() {
        return traits;
    }

    /**
     * Returns the persistent identification for this culture.
     * @return the identification
     */
    public Identification getIdentification() {
        return identification;
    }

    @Override
    public String toString() {
        return name;
    }
}
