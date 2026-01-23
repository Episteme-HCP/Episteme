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

package org.jscience.history.archeology;

import org.jscience.arts.Analysis;
import org.jscience.arts.Restoration;
import org.jscience.bibliography.Citation;
import org.jscience.biology.Human;
import org.jscience.economics.Organization;
import org.jscience.economics.money.Money;
import org.jscience.economics.resources.PhysicalObject;
import org.jscience.geography.Place;
import org.jscience.geography.Places;
import org.jscience.measure.Amount;
import org.jscience.methodology.ScientificDescription;
import org.jscience.util.identity.Identification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;

/**
 * Represents an archaeological item (fossil, artifact, etc.) found at a site.
 * Tracks discovery details, archaeological context (civilization), and post-discovery 
 * activities like analysis, restoration, and publications.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.3
 * @since 1.0
 */
@Persistent
public class Item extends PhysicalObject implements Serializable {

    private static final long serialVersionUID = 1L;

    /** People who discovered the item. */
    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Human> discoverers;

    /** Date and time of discovery. */
    @Attribute
    private final Instant discoveryDate;

    /** Geographical location where the item was originally found. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place originalPosition;

    /** Historical civilization associated with the item. */
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Civilization civilization;

    /** Additional descriptive entries by researchers or experts. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private List<ScientificDescription> extraDescriptions = new ArrayList<>();

    /** Bibliographical references citing or documenting this item. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private List<Citation> publications = new ArrayList<>();

    /** Scientific analyses performed on the item. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private List<Analysis> analyses = new ArrayList<>();

    /** Restoration records for the item. */
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private List<Restoration> restorations = new ArrayList<>();

    /**
     * Creates a new archaeological Item.
     *
     * @param name             the name or designation
     * @param description      general description
     * @param organization     the curator/owner organization
     * @param productionDate   estimated date of creation (pre-discovery)
     * @param identification   formal identification data
     * @param discoverers      set of humans involved in discovery
     * @param discoveryDate    moment of discovery
     * @param originalPosition location of discovery
     * @throws NullPointerException if any required argument is null
     * @throws IllegalArgumentException if discoverers set is empty
     */
    public Item(String name, String description, Organization organization,
                Instant productionDate, Identification identification, Set<Human> discoverers,
                Instant discoveryDate, Place originalPosition) {
        super(name, description, Amount.ONE, organization, Places.EARTH,
            Date.from(productionDate), identification, Money.usd(0));

        this.discoverers = new HashSet<>(Objects.requireNonNull(discoverers, "Discoverers cannot be null"));
        if (this.discoverers.isEmpty()) {
            throw new IllegalArgumentException("Discoverers cannot be empty");
        }
        
        this.discoveryDate = Objects.requireNonNull(discoveryDate, "Discovery date cannot be null");
        this.originalPosition = Objects.requireNonNull(originalPosition, "Original position cannot be null");
    }

    /**
     * Returns an unmodifiable view of the discoverers.
     *
     * @return the set of discoverers
     */
    public Set<Human> getDiscoverers() {
        return Collections.unmodifiableSet(discoverers);
    }

    /**
     * Returns the date when the item was discovered.
     *
     * @return discovery date
     */
    public Instant getDateOfDiscovery() {
        return discoveryDate;
    }

    /**
     * Returns the original geographical position of the find.
     *
     * @return discovery location
     */
    public Place getOriginalPosition() {
        return originalPosition;
    }

    /**
     * Calculates the estimated physical age of the object in seconds.
     * 
     * @return age in seconds
     */
    public double getAge() {
        return (double) (Instant.now().getEpochSecond() - getProductionDate().toInstant().getEpochSecond());
    }

    /**
     * Returns the associated civilization.
     *
     * @return the civilization, or null if unknown
     */
    public Civilization getCivilization() {
        return civilization;
    }

    /**
     * Assigns a civilization to the item.
     *
     * @param civilization the civilization
     */
    public void setCivilization(Civilization civilization) {
        this.civilization = civilization;
    }

    /**
     * Returns the list of extra descriptions.
     *
     * @return descriptions list
     */
    public List<ScientificDescription> getExtraDescriptions() {
        return Collections.unmodifiableList(extraDescriptions);
    }

    /**
     * Sets the extra descriptions.
     *
     * @param extraDescriptions list of descriptions
     * @throws NullPointerException if list is null
     */
    public void setExtraDescriptions(List<ScientificDescription> extraDescriptions) {
        this.extraDescriptions = new ArrayList<>(Objects.requireNonNull(extraDescriptions, "Description list cannot be null"));
    }

    /**
     * Returns the list of publications.
     *
     * @return publications list
     */
    public List<Citation> getPublications() {
        return Collections.unmodifiableList(publications);
    }

    /**
     * Sets the publications references.
     *
     * @param publications list of citations
     * @throws NullPointerException if list is null
     */
    public void setPublications(List<Citation> publications) {
        this.publications = new ArrayList<>(Objects.requireNonNull(publications, "Publication list cannot be null"));
    }

    /**
     * Returns the list of scientific analyses.
     *
     * @return analysis list
     */
    public List<Analysis> getAnalyses() {
        return Collections.unmodifiableList(analyses);
    }

    /**
     * Sets the scientific analyses.
     *
     * @param analyses list of analysis results
     * @throws NullPointerException if list is null
     */
    public void setAnalyses(List<Analysis> analyses) {
        this.analyses = new ArrayList<>(Objects.requireNonNull(analyses, "Analysis list cannot be null"));
    }

    /**
     * Returns the list of restoration records.
     *
     * @return restorations list
     */
    public List<Restoration> getRestorations() {
        return Collections.unmodifiableList(restorations);
    }

    /**
     * Sets the restoration records.
     *
     * @param restorations list of restoration events
     * @throws NullPointerException if list is null
     */
    public void setRestorations(List<Restoration> restorations) {
        this.restorations = new ArrayList<>(Objects.requireNonNull(restorations, "Restoration list cannot be null"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(discoverers, item.discoverers) && 
               Objects.equals(discoveryDate, item.discoveryDate) && 
               Objects.equals(originalPosition, item.originalPosition) && 
               Objects.equals(civilization, item.civilization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), discoverers, discoveryDate, originalPosition, civilization);
    }
}
