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
import java.util.UUID;
import org.jscience.core.mathematics.numbers.real.Real;
import org.jscience.natural.biology.Individual;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.natural.biology.ecology.Population;
import org.jscience.natural.biology.taxonomy.Species;
import org.jscience.natural.earth.Place;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a PsychologicalGroup of individuals from a psychological and social perspective.
 * Extends {@link Population} for biological/ecological management.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */

@Persistent
public class PsychologicalGroup extends Population<Individual> {

    private static final long serialVersionUID = 2L;

    /** Individuals designated as leaders or rulers within the PsychologicalGroup. */
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
     * Default Constructor with ID generation.
     */
    public PsychologicalGroup(String name, Species species, Place territory) {
        this(name, species, territory, null);
    }

    public PsychologicalGroup(String name, Species species, Place territory, org.jscience.natural.engineering.eventdriven.EventDrivenEngine engine) {
        super(new UUIDIdentification(UUID.randomUUID().toString()), name, species, territory, engine);
        this.leaders = new HashSet<>();
        this.relations = new HashMap<>();
    }

    /**
     * Constructor used by subclasses or for explicit ID.
     */
    public PsychologicalGroup(Identification id, String name, Species species, Place territory) {
        this(id, name, species, territory, null);
    }

    public PsychologicalGroup(Identification id, String name, Species species, Place territory, org.jscience.natural.engineering.eventdriven.EventDrivenEngine engine) {
        super(id, name, species, territory, engine);
        this.leaders = new HashSet<>();
        this.relations = new HashMap<>();
    }

    /**
     * Legacy/Convenience constructor.
     */
    public PsychologicalGroup(Species species) {
        this(null, species, null);
    }

    /**
     * Legacy/Convenience constructor.
     */
    public PsychologicalGroup(Species species, Place formalTerritory) {
        this(null, species, formalTerritory);
    }

    // PsychologicalGroup specific methods

    public Place getFormalTerritory() {
        return getTerritory();
    }

    public void setFormalTerritory(Place territory) {
        setTerritory(territory);
    }

    public Set<Individual> getLeaders() {
        return leaders;
    }

    public void setLeaders(Set<Individual> leaders) {
        this.leaders = Objects.requireNonNull(leaders, "Leaders set cannot be null");
    }

    public Map<Individual, Map<Individual, Real>> getRelations() {
        return relations;
    }

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

    
    // Placeholder for Event processing if PsychologicalGroup is treated as event-driven
    // Inherited from Population now
}


