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

package org.jscience.economics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.util.identity.Identification;
import org.jscience.biology.taxonomy.Species;
import org.jscience.earth.Place;
import org.jscience.psychology.social.Group;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a primitive or cooperative social group where resources are shared.
 * Suitable for modeling anything from animal social structures to human cooperatives.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
@Persistent
public class Community extends Group implements TaskProcessor {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<Resource> resources;

    /**
     * Creates a new empty Community at a specific place.
     * 
     * @param species predominant species
     * @param place   location
     */
    public Community(Species species, Place place) {
        this(null, species, place);
    }

    public Community(Identification id, Species species, Place place) {
        super(id, species, place);
        this.resources = new HashSet<>();
    }

    /**
     * Creates a new Community with initial members.
     */
    public Community(Species species, Set<Individual> individuals, Place place) {
        super(species, place);
        setIndividuals(individuals);
        this.resources = new HashSet<>();
    }

    @Override
    public Set<Resource> getResources() {
        return Collections.unmodifiableSet(resources);
    }

    @Override
    public boolean consumeResources(Task task) {
        Objects.requireNonNull(task, "Task cannot be null");
        if (resources.containsAll(task.getResources())) {
             resources.removeAll(task.getResources());
             resources.addAll(task.getProducts());
             return true;
        }
        return false;
    }

    public void addResource(Resource resource) {
        resources.add(Objects.requireNonNull(resource, "Resource cannot be null"));
    }

    public void removeResource(Resource resource) {
        resources.remove(resource);
    }

    public void setResources(Set<Resource> resources) {
        this.resources = new HashSet<>(Objects.requireNonNull(resources, "Resources set cannot be null"));
    }

    /** Performs a direct resource swap with another community. */
    public void barterResources(Set<Resource> offered, Community other, Set<Resource> wanted) {
        Objects.requireNonNull(offered); Objects.requireNonNull(other); Objects.requireNonNull(wanted);
        if (!this.resources.containsAll(offered)) throw new IllegalArgumentException("Missing offered resources");
        if (!other.getResources().containsAll(wanted)) throw new IllegalArgumentException("Other party missing wanted resources");

        this.resources.removeAll(offered);
        this.resources.addAll(wanted);

        Set<Resource> otherRes = new HashSet<>(other.getResources());
        otherRes.removeAll(wanted);
        otherRes.addAll(offered);
        other.setResources(otherRes);
    }
}
