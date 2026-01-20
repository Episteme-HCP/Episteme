package org.jscience.geography;

import org.jscience.biology.Individual;
import org.jscience.biology.human.Human;

import org.jscience.economics.Property;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;

import org.jscience.measure.Amount;

import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a geographical a human home.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public class Home extends OwnedPlace implements Property {
    /** DOCUMENT ME! */
    private Address address;

    /** DOCUMENT ME! */
    private Set owners; //humans

    /** DOCUMENT ME! */
    private Set occupants; //individuals

    /** DOCUMENT ME! */
    private Amount<Money> value;

    //there also should be a field for the property contract but we are not sure it really exist (it depends on the country) and this would really be unconvenient for the developer
    /**
     * Creates a new Home object.
     *
     * @param name DOCUMENT ME!
     * @param boundary DOCUMENT ME!
     * @param address DOCUMENT ME!
     * @param owners DOCUMENT ME!
     * @param occupants DOCUMENT ME!
     */
    public Home(String name, Boundary boundary, Address address, Set owners,
        Set occupants) {
        super(name, boundary, owners);

        Iterator iterator;
        boolean valid;

        if ((address != null) && (occupants != null)) {
            iterator = occupants.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.address = address;
                address.setPosition(this);
                this.owners = owners;
                this.occupants = occupants;
                this.value = Amount.valueOf(0, Currency.USD);
            } else {
                throw new IllegalArgumentException(
                    "The occupants Set must contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Home constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Address getAddress() {
        return address;
    }

    /**
     * DOCUMENT ME!
     *
     * @param address DOCUMENT ME!
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOwners() {
        return owners;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOwner(Human owner) {
        if (owner != null) {
            owners.add(owner);
        } else {
            throw new IllegalArgumentException("You can't add a null owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeOwner(Human owner) {
        if ((owners.size() > 1)) {
            owners.remove(owner);
        } else {
            throw new IllegalArgumentException("You can't remove last owner.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owners DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOwners(Set owners) {
        Iterator iterator;
        boolean valid;

        if ((owners != null) && (owners.size() > 0)) {
            iterator = owners.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Human;
            }

            if (valid) {
                this.owners = owners;
            } else {
                throw new IllegalArgumentException(
                    "The owners Set must contain only Humans.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null or empty owners set.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOccupants() {
        return occupants;
    }

    /**
     * DOCUMENT ME!
     *
     * @param occupant DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addOccupant(Individual occupant) {
        if (occupant != null) {
            occupants.add(occupant);
        } else {
            throw new IllegalArgumentException("You can't add a null occupant.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param occupant DOCUMENT ME!
     */
    public void removeOccupant(Individual occupant) {
        occupants.remove(occupant);
    }

    /**
     * DOCUMENT ME!
     *
     * @param occupants DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setOccupants(Set occupants) {
        Iterator iterator;
        boolean valid;

        if (occupants != null) {
            iterator = occupants.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Individual;
            }

            if (valid) {
                this.occupants = occupants;
            } else {
                throw new IllegalArgumentException(
                    "The occupants Set must contain only Individuals.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null occupants set.");
        }
    }

    //this is the price if sold by owners
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Amount<Money> getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setValue(Amount<Money> value) {
        if (value != null) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("You can't set a null value.");
        }
    }
}
