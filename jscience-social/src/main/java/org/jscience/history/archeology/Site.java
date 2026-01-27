package org.jscience.history.archeology;

import org.jscience.mathematics.geometry.boundaries.Boundary;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.earth.Place;
import org.jscience.earth.PlaceType;
import org.jscience.earth.coordinates.EarthCoordinate;
import org.jscience.history.time.TimeCoordinate;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents an archaeological field site.
 * A Site is a physical location in which evidence of past activity is preserved and excavation occurs.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Site extends Place {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_ONE)
    private final TimeCoordinate excavationStartDate;

    @Attribute
    private String description;

    @Attribute
    private Boundary<EarthCoordinate> limits;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Item> discoveredItems = new HashSet<>();

    public Site(String name, TimeCoordinate excavationStartDate, Boundary<EarthCoordinate> limits) {
        super(name, PlaceType.LANDMARK);
        this.limits = limits;
        this.excavationStartDate = Objects.requireNonNull(excavationStartDate, "Start date cannot be null");
    }

    public TimeCoordinate getExcavationStartDate() {
        return excavationStartDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boundary<EarthCoordinate> getLimits() {
        return limits;
    }

    public void setLimits(Boundary<EarthCoordinate> limits) {
        this.limits = limits;
    }

    public Set<Item> getDiscoveredItems() {
        return Collections.unmodifiableSet(discoveredItems);
    }

    public void addItem(Item item) {
        if (item != null) discoveredItems.add(item);
    }

    public void removeItem(Item item) {
        discoveredItems.remove(item);
    }

    @Override
    public String toString() {
        return "Archeological Site: " + getName() + " (" + discoveredItems.size() + " items found)";
    }

    /**
     * Inner class representing a discovered item.
     * Can be moved to its own file later if it grows complex.
     */
    @Persistent
    public static class Item {
        @Attribute
        private String id;
        @Attribute
        private String type;
        @Attribute
        private String material;

        public Item(String type, String material) {
            this.id = java.util.UUID.randomUUID().toString();
            this.type = type;
            this.material = material;
        }
        
        // Getters omitted for brevity in this simple struct
        public String getType() { return type; }
    }
}
