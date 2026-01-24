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

package org.jscience.arts;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jscience.economics.money.Money;
import org.jscience.geography.Place;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.util.Named;
import org.jscience.util.Temporal;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a piece of art (Artwork), integrating historical, geographical, 
 * economic, and scientific dimensions. This class serves as the central model 
 * for artworks, tracking their production, authorship, valuation, and 
 * conservation history.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 2.0
 * @since 1.0
 */
@Persistent
public class Artwork implements Identified<String>, Named, Temporal<TemporalCoordinate>, Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    private final String id;
    @Attribute
    private final String name;
    @Attribute
    private final String description;
    @Attribute
    private final TemporalCoordinate productionDate;
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place productionPlace;
    @Attribute
    private final ArtForm category;

    @Attribute
    private final Set<String> authors; // Names of authors/artists
    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Money estimatedValue;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Analysis> analyses;
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Restoration> restorations;

    /**
     * Creates a new Artwork.
     * 
     * @param name common name of the work
     * @param description detailed description
     * @param productionDate estimated or precise date of creation
     * @param productionPlace location where the work was created
     * @param category the form of art (e.g., PAINTING, SCULPTURE)
     */
    public Artwork(String name, String description, TemporalCoordinate productionDate, Place productionPlace, ArtForm category) {
        this.id = UUID.randomUUID().toString();
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.productionDate = productionDate;
        this.productionPlace = productionPlace;
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.authors = new HashSet<>();
        this.analyses = new HashSet<>();
        this.restorations = new HashSet<>();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public TemporalCoordinate getWhen() {
        return productionDate;
    }

    public String getDescription() {
        return description;
    }

    public TemporalCoordinate getProductionDate() {
        return productionDate;
    }

    public Place getProductionPlace() {
        return productionPlace;
    }

    public ArtForm getCategory() {
        return category;
    }

    public Set<String> getAuthors() {
        return Collections.unmodifiableSet(authors);
    }
    
    public void addAuthor(String authorName) {
        if (authorName != null) {
            authors.add(authorName);
        }
    }

    public void addAuthor(Artist artist) {
        if (artist != null) {
            authors.add(artist.getName());
        }
    }

    public Money getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(Money estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public Set<Analysis> getAnalyses() {
        return Collections.unmodifiableSet(analyses);
    }
    
    public void addAnalysis(Analysis analysis) {
        if (analysis != null) {
            analyses.add(analysis);
        }
    }

    public Set<Restoration> getRestorations() {
        return Collections.unmodifiableSet(restorations);
    }
    
    public void addRestoration(Restoration restoration) {
        if (restoration != null) {
            restorations.add(restoration);
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artwork artwork)) return false;
        return category == artwork.category && 
               Objects.equals(name, artwork.name) && 
               Objects.equals(productionDate, artwork.productionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, productionDate, category);
    }
}
