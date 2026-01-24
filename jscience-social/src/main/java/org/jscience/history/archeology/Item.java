package org.jscience.history.archeology;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.jscience.arts.Analysis;
import org.jscience.arts.Restoration;
import org.jscience.bibliography.Citation;
import org.jscience.biology.Human;
import org.jscience.economics.Organization;
import org.jscience.economics.money.Money;
import org.jscience.economics.resources.PhysicalObject;
import org.jscience.geography.Place;
import org.jscience.geography.Places;
import org.jscience.history.temporal.TemporalCoordinate;
import org.jscience.measure.Quantity;
import org.jscience.methodology.ScientificDescription;
import org.jscience.util.identity.Identification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents an archaeological item (fossil, artifact, etc.) found at a site.
 * Tracks discovery details, archaeological context, and scientific post-processing.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Item extends PhysicalObject implements Serializable {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Human> discoverers;

    @Attribute
    private final Instant discoveryDate;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private final Place originalPosition;

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Civilization civilization;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<ScientificDescription> extraDescriptions = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Citation> publications = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Analysis> analyses = new ArrayList<>();

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final List<Restoration> restorations = new ArrayList<>();

    public Item(String name, String description, Organization organization,
                TemporalCoordinate primaryDate, Identification identification, Set<Human> discoverers,
                Instant discoveryDate, Place originalPosition) {
        super(name, description, Quantity.ONE, organization, Places.EARTH,
            primaryDate, identification, Money.usd(0));

        this.discoverers = new HashSet<>(Objects.requireNonNull(discoverers, "Discoverers cannot be null"));
        if (this.discoverers.isEmpty()) {
            throw new IllegalArgumentException("Discoverers cannot be empty");
        }
        
        this.discoveryDate = Objects.requireNonNull(discoveryDate, "Discovery date cannot be null");
        this.originalPosition = Objects.requireNonNull(originalPosition, "Original position cannot be null");
    }

    public Set<Human> getDiscoverers() {
        return Collections.unmodifiableSet(discoverers);
    }

    public Instant getDiscoveryDate() {
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
