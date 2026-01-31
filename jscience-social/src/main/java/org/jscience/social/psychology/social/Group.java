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

package org.jscience.social.psychology.social;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.biology.Individual;
import org.jscience.core.util.identity.Identification;
import org.jscience.natural.biology.ecology.Population;
import org.jscience.natural.biology.taxonomy.Species;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a group of individuals from a psychological and social perspective.
 * Unlike a simple {@link Population}, a Group includes structures such as 
 * leadership and a sociogram tracking interpersonal relations over time.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Group extends Population {

    private static final long serialVersionUID = 1L;

    /** Individuals designated as leaders or rulers within the group. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Set<Individual> leaders;

    /** 
     * Complex mapping of interpersonal relations.
     * Maps an individual to their perceived relations with other members.
     * The double value typically represents attraction/repulsion or dominance.
     */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private Map<Individual, Map<Individual, Real>> relations;

    /**
     * Creates a new Group for a specific species.
     *
     * @param species the species of the group members
     * @throws NullPointerException if species is null
     */
    public Group(Species species) {
        super(species);
        this.leaders = new HashSet<>();
        this.relations = new HashMap<>();
    }

    /**
     * Creates a new Group with a designated territory.
     *
     * @param species         the biological species
     * @param formalTerritory the geographic area controlled or inhabited by the group
     * @throws NullPointerException if any argument is null
     */
    public Group(Species species, Place formalTerritory) {
        this(null, species, formalTerritory);
    }

    public Group(Identification id, Species species, Place formalTerritory) {
        super(id, "Group", species, formalTerritory);
        this.leaders = new HashSet<>();
        this.relations = new HashMap<>();
    }

    /**
     * Returns the formal territory associated with this group.
     * @return the territory place
     */
    public Place getFormalTerritory() {
        return getTerritory();
    }

    /**
     * Sets the formal territory for this group.
     * @param territory the new territory
     */
    public void setFormalTerritory(Place territory) {
        setTerritory(territory);
    }

    /**
     * Returns the set of leaders within the group.
     * @return the leaders set
     */
    public Set<Individual> getLeaders() {
        return leaders;
    }

    /**
     * Sets the leaders for this group.
     * @param leaders the new set of leaders
     * @throws NullPointerException if leaders is null
     */
    public void setLeaders(Set<Individual> leaders) {
        this.leaders = Objects.requireNonNull(leaders, "Leaders set cannot be null");
    }

    /**
     * Returns the complete sociogram of relations.
     * @return the relations map
     */
    public Map<Individual, Map<Individual, Real>> getRelations() {
        return relations;
    }

    /**
     * Establishes or updates a relation between two individuals.
     *
     * @param from   the individual who holds the attitude
     * @param to     the target of the attitude
     * @param status numeric value representing the relation (e.g., attraction score)
     * @throws NullPointerException if from or to is null
     */
    public void setRelation(Individual from, Individual to, Real status) {
        Objects.requireNonNull(from, "Source individual cannot be null");
        Objects.requireNonNull(to, "Target individual cannot be null");
        
        Map<Individual, Real> individualRelations = relations.get(from);
        if (individualRelations == null) {
            individualRelations = new HashMap<>();
            relations.put(from, individualRelations);
        }
        individualRelations.put(to, status);
    }

    public void setRelation(Individual from, Individual to, double status) {
        setRelation(from, to, Real.of(status));
    }

    /**
     * Retrieves the relation score between two individuals.
     *
     * @param from the source individual
     * @param to   the target individual
     * @return the relation score, or 0.0 if no relation is defined
     */
    public Real getRelation(Individual from, Individual to) {
        Map<Individual, Real> individualRelations = relations.get(from);
        if (individualRelations != null) {
            Real status = individualRelations.get(to);
            if (status != null) {
                return status;
            }
        }
        return Real.ZERO;
    }
}

