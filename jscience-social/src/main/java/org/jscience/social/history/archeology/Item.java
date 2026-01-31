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

package org.jscience.social.history.archeology;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.jscience.social.arts.Analysis;
import org.jscience.social.arts.Restoration;
import org.jscience.core.bibliography.Citation;
import org.jscience.social.sociology.Human;
import org.jscience.social.economics.Organization;
import org.jscience.social.economics.money.Money;
import org.jscience.social.economics.resources.PhysicalObject;
import org.jscience.natural.earth.Place;
import org.jscience.natural.earth.Places;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.history.time.TimePoint;
import org.jscience.core.methodology.ScientificDescription;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents an archaeological item (fossil, artifact, etc.) found at a site.
 * Tracks discovery details, archaeological context, and scientific post-processing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Item extends PhysicalObject {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Human> discoverers;

    @Attribute
    private final TimeCoordinate discoveryDate;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place originalPosition;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Civilization civilization;

    /** Estimated age determined through dating methods (e.g., C-14). */
    @Relation(type = Relation.Type.ONE_TO_ONE)
    private TimeCoordinate dating;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<ScientificDescription> extraDescriptions = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Citation> publications = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Analysis> analyses = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Restoration> restorations = new ArrayList<>();

    public Item(String name, String description, Organization organization,
                TimeCoordinate dating, Identification identification, Set<Human> discoverers,
                TimeCoordinate discoveryDate, Place originalPosition) {
        super(name, description, org.jscience.core.measure.Quantities.create(1, org.jscience.core.measure.Units.ONE), organization, Places.EARTH,
            dating != null ? dating : TimePoint.now(), identification, Money.usd(0));

        this.dating = Objects.requireNonNull(dating, "Dating cannot be null");
        this.discoverers = new HashSet<>(Objects.requireNonNull(discoverers, "Discoverers cannot be null"));
        if (this.discoverers.isEmpty()) {
            throw new IllegalArgumentException("Discoverers cannot be empty");
        }
        
        this.discoveryDate = Objects.requireNonNull(discoveryDate, "Discovery date cannot be null");
        this.originalPosition = Objects.requireNonNull(originalPosition, "Original position cannot be null");
    }

    /**
     * Returns the estimated dating of the artifact.
     *
     * @return the dating
     */
    public TimeCoordinate getDating() {
        return dating;
    }

    /**
     * Sets the estimated dating of the artifact.
     * @param dating the new dating
     */
    public void setDating(TimeCoordinate dating) {
        this.dating = Objects.requireNonNull(dating, "Dating cannot be null");
    }

    public Set<Human> getDiscoverers() {
        return Collections.unmodifiableSet(discoverers);
    }

    public TimeCoordinate getDiscoveryDate() {
        return discoveryDate;
    }

    public Place getOriginalPosition() {
        return originalPosition;
    }

    public Civilization getCivilization() {
        return civilization;
    }

    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    public List<ScientificDescription> getExtraDescriptions() {
        return Collections.unmodifiableList(extraDescriptions);
    }

    public void addExtraDescription(ScientificDescription description) {
        if (description != null) extraDescriptions.add(description);
    }

    public List<Citation> getPublications() {
        return Collections.unmodifiableList(publications);
    }

    public void addPublication(Citation citation) {
        if (citation != null) publications.add(citation);
    }

    public List<Analysis> getAnalyses() {
        return Collections.unmodifiableList(analyses);
    }

    public void addAnalysis(Analysis analysis) {
        if (analysis != null) analyses.add(analysis);
    }

    public List<Restoration> getRestorations() {
        return Collections.unmodifiableList(restorations);
    }

    public void addRestoration(Restoration restoration) {
        if (restoration != null) restorations.add(restoration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(discoveryDate, item.discoveryDate) && 
               Objects.equals(originalPosition, item.originalPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discoveryDate, originalPosition);
    }
}

