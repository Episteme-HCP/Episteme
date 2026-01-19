package org.jscience.arts;

import org.jscience.history.time.UncertainDate;
import org.jscience.geography.Place;
import org.jscience.economics.Money;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A class representing a piece of art (Artwork).
 * Integrates history (UncertainDate), geography (Place), economics (Money), and scientific analysis.
 */
public class Artwork {

    private final String name;
    private final String description;
    private final UncertainDate productionDate;
    private final Place productionPlace;
    private final ArtForm category;

    private final Set<String> authors; // Names of authors/artists
    private Money estimatedValue;
    
    private final Set<Analysis> analyses;
    private final Set<Restoration> restorations;

    public Artwork(String name, String description, UncertainDate productionDate, Place productionPlace, ArtForm category) {
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.description = description;
        this.productionDate = productionDate;
        this.productionPlace = productionPlace;
        this.category = Objects.requireNonNull(category, "Category cannot be null");
        this.authors = new HashSet<>();
        this.analyses = new HashSet<>();
        this.restorations = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UncertainDate getProductionDate() {
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
