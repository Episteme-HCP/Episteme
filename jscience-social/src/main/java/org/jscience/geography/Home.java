package org.jscience.geography;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.jscience.biology.Individual;
import org.jscience.earth.PlaceType;
import org.jscience.economics.EconomicAgent;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;
import org.jscience.util.persistence.Relation;

/**
 * Represents a residence, a specific place where individuals live.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Home extends OwnedPlace {

    private static final long serialVersionUID = 1L;

    @Attribute
    private final Address address;

    @Relation(type = Relation.Type.MANY_TO_MANY)
    private final Set<Individual> occupants = new HashSet<>();

    public Home(String name, Address address, Set<EconomicAgent> owners) {
        super(name, owners);
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.setType(PlaceType.BUILDING);
        this.address.setPlace(this);
    }

    public Address getAddress() {
        return address;
    }

    public Set<Individual> getOccupants() {
        return Collections.unmodifiableSet(occupants);
    }

    public void addOccupant(Individual person) {
        if (person != null) occupants.add(person);
    }

    public void removeOccupant(Individual person) {
        occupants.remove(person);
    }

    @Override
    public String toString() {
        return "Home: " + address.toString();
    }
}
