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

import org.jscience.sociology.Person;
import org.jscience.geography.Place;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Represents an economic organization (e.g., Company, Factory). * @author
 * Silvere Martin-Michiellot
 * 
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 * 
 */
public class Organization implements Serializable {

    private String name;
    private final String id;
    private Place headquarters;

    private Set<Worker> workers;
    private Set<Person> owners;
    private Money capital;

    // Resources owned by the organization (inventory)
    private Set<Resource<?>> inventory;

    public Organization(String name, Place headquarters, Money initialCapital) {
        this.name = name;
        this.id = UUID.randomUUID().toString();
        this.headquarters = headquarters;
        this.capital = initialCapital;
        this.workers = new HashSet<>();
        this.owners = new HashSet<>();
        this.inventory = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public Place getHeadquarters() {
        return headquarters;
    }

    public Money getCapital() {
        return capital;
    }

    public void setCapital(Money capital) {
        this.capital = capital;
    }

    public void addWorker(Worker worker) {
        this.workers.add(worker);
        worker.setOrganization(this);
    }

    public void removeWorker(Worker worker) {
        this.workers.remove(worker);
        if (worker.getOrganization() == this) {
            worker.setOrganization(null);
        }
    }

    public Set<Worker> getWorkers() {
        return workers;
    }

    public void addOwner(Person person) {
        owners.add(person);
    }

    public Set<Person> getOwners() {
        return owners;
    }

    public void addResource(Resource<?> resource) {
        inventory.add(resource);
    }

    public Set<Resource<?>> getInventory() {
        return inventory;
    }
}
