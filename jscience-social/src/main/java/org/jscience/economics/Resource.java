/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.economics;

import org.jscience.geography.Place;
import org.jscience.mathematics.numbers.real.Real;
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
 * * @author Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * 
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
    private Real quantity;

    private Organization producer;
    private Set<Person> owners;
    private Place place;
    private Date productionDate;

    public Resource(String name, String description, Real quantity, Organization producer, Place place) {
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

    public Real getQuantity() {
        return quantity;
    }

    public void setQuantity(Real quantity) {
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
