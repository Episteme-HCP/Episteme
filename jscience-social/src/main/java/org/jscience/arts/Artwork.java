package org.jscience.arts;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;
import org.jscience.geography.Place;
import org.jscience.economics.money.Money;
import org.jscience.util.identity.Identified;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Id;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Relation;

/**
 * A class representing a piece of art (Artwork).
 * Integrates history (Instant), geography (Place), economics (Money), and scientific analysis.
 */
@Persistent
public class Artwork implements Identified<String> {

    @Id
    private final String id;
    @Attribute
    private final String name;
    @Attribute
    private final String description;
    @Attribute
    private final Instant productionDate;
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

    public Artwork(String name, String description, Instant productionDate, Place productionPlace, ArtForm category) {
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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getProductionDate() {
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
