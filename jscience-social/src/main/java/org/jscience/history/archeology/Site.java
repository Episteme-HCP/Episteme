package org.jscience.history.archeology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.mathematics.geometry.boundaries.Boundary;
import org.jscience.earth.coordinates.EarthCoordinate;
import org.jscience.earth.Place;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents an archaeological excavation site.
 * Tracks the excavation start date, site description, and all discovered items.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public class Site extends Place {

    private static final long serialVersionUID = 2L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate excavationStartDate;

    @Attribute
    private String description = "";

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private Boundary<EarthCoordinate> limits;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Item> discoveredItems = new HashSet<>();

    public Site(String name, TimeCoordinate excavationStartDate, Boundary<EarthCoordinate> limits) {
        super(name, Type.NATURAL_FEATURE);
        this.limits = limits;
        this.excavationStartDate = Objects.requireNonNull(excavationStartDate, "Start date cannot be null");
    }

    public Boundary<EarthCoordinate> getLimits() {
        return limits;
    }

    public void setLimits(Boundary<EarthCoordinate> limits) {
        this.limits = limits;
    }

    public TimeCoordinate getExcavationStartDate() {
        return excavationStartDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, "Description cannot be null");
    }

    public Set<Item> getDiscoveredItems() {
        return Collections.unmodifiableSet(discoveredItems);
    }

    public void addDiscoveredItem(Item item) {
        if (item != null) discoveredItems.add(item);
    }

    public void removeDiscoveredItem(Item item) {
        discoveredItems.remove(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Site site)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(excavationStartDate, site.excavationStartDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), excavationStartDate);
    }

    @Override
    public String toString() {
        return String.format("Archaeological Site: %s (Started %s, %d items)", 
            getName(), excavationStartDate, discoveredItems.size());
    }
}
