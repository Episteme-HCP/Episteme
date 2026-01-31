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

package org.jscience.social.arts;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.jscience.social.economics.Community;
import org.jscience.social.economics.money.Money;
import org.jscience.social.economics.resources.Artifact;
import org.jscience.natural.earth.Place;
import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.social.history.time.TimePoint;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.UUIDIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a piece of art (Artwork), integrating historical, geographical, 
 * economic, and scientific dimensions. This class serves as the central model 
 * for artworks, tracking their production, authorship, valuation, and 
 * conservation history.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Artwork extends Artifact {

    private static final long serialVersionUID = 3L;

    @Attribute
    private final ArtForm category;

    @Attribute
    private final Set<String> authors; // Names of authors/artists
    
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
    public Artwork(String name, String description, TimeCoordinate productionDate, Place productionPlace, ArtForm category) {
        this(name, description, Quantities.create(1, Units.ONE), null, productionPlace, 
             productionDate != null ? productionDate : TimePoint.now(),
             new UUIDIdentification(UUID.randomUUID().toString()), Money.usd(0), category);
    }

    /**
     * Full constructor for Artwork as a resource.
     */
    public Artwork(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Identification identification, Money value, ArtForm category) {
        super(name, description, amount, producer, productionPlace, productionDate, identification, value);
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.authors = new HashSet<>();
        this.analyses = new HashSet<>();
        this.restorations = new HashSet<>();
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
        return String.format("%s (%s)", getName(), category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Artwork artwork)) return false;
        if (!super.equals(o)) return false;
        return category == artwork.category && 
               Objects.equals(getName(), artwork.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category);
    }
}

