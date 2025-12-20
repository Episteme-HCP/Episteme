package org.jscience.economics;

import org.jscience.geography.Place;
import org.jscience.sociology.Person;
import javax.measure.Quantity;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A class representing the stuff that is extracted from the soil (coil,
 * fruits...), a final product or human labor. A product is usually a resource
 * for another factory/consumer.
 *
 * Use Generic Q for the type of Quantity (Mass, Volume, Dimensionless, etc.)
 */
public abstract class Resource<Q extends Quantity<Q>> implements Serializable {

    private final String name;
    private final String description;
    // Using simple double for now, or we could use javax.measure.Measure if
    // available
    // For V1 simplicity in social module, we might stick to simple Amount or just
    // double quantity
    // Legacy used 'Amount'. Let's use a generic generic type for now or standard
    // Number.
    // Actually, jscience-social likely doesn't have the full physics units set up
    // in its dependencies
    // properly for all social things, but let's try to be generic.

    // Simplification for V1 Social: "Amount" is effectively "Quantity"
    // We will use a double magnitude and a String unit for display, or just
    // abstract it.
    private double quantity;

    private Organization producer;
    private Set<Person> owners;
    private Place place;
    private Date productionDate;

    public Resource(String name, String description, double quantity, Organization producer, Place place) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.producer = producer;
        this.place = place;
        this.productionDate = new Date();
        this.owners = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Organization getProducer() {
        return producer;
    }

    public Place getLocation() {
        return place;
    }

    public void setLocation(Place place) {
        this.place = place;
    }

    public Set<Person> getOwners() {
        return Collections.unmodifiableSet(owners);
    }

    public void addOwner(Person person) {
        this.owners.add(person);
    }

    public void removeOwner(Person person) {
        this.owners.remove(person);
    }

    public Date getProductionDate() {
        return productionDate;
    }

    @Override
    public String toString() {
        return name + " (" + quantity + ")";
    }
}
