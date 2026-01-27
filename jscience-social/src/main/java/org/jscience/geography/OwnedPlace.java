package org.jscience.geography;

import org.jscience.economics.money.Money;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.earth.Place;
import org.jscience.earth.PlaceType;
import org.jscience.economics.EconomicAgent;
import org.jscience.economics.Property;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a piece of land or building that is owned by one or more economic agents.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class OwnedPlace extends Place implements Property {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<EconomicAgent> owners = new HashSet<>();

    @Attribute
    private Money value;

    public OwnedPlace(String name, Set<EconomicAgent> initialOwners) {
        super(name, PlaceType.OTHER);
        if (initialOwners == null || initialOwners.isEmpty()) {
            throw new IllegalArgumentException("Property must have at least one owner");
        }
        this.owners.addAll(initialOwners);
    }

    @Override
    public Set<EconomicAgent> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public void addOwner(EconomicAgent owner) {
        if (owner != null) owners.add(owner);
    }

    public void removeOwner(EconomicAgent owner) {
        if (owners.size() > 1) { // Prevent ownerless property
            owners.remove(owner);
        }
    }

    @Override
    public Money getValue() {
        return value;
    }

    public void setValue(Money value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getName() + " (Owned by " + owners.size() + ")";
    }
}
