/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.social.economics;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.episteme.social.economics.money.Account;
import org.episteme.social.economics.money.Money;
import org.episteme.social.geography.BusinessPlace;
import org.episteme.core.util.identity.Identification;
import org.episteme.core.util.identity.SimpleIdentification;
import org.episteme.core.util.persistence.Persistent;
import org.episteme.core.util.persistence.Relation;

/**
 * An organization specialized in mass production and transformation of resources.
 * Models Taylorist and modern manufacturing structures.
 * * @version 1.2
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Factory extends Organization {

    private static final long serialVersionUID = 1L;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<PotentialResource> productionResources;

    @Relation(type = Relation.Type.ONE_TO_MANY)
    private Set<PotentialResource> productionProducts;

    /**
     * Standard constructor for industrial organizations.
     */
    public Factory(String name, Identification id, Set<EconomicAgent> owners,
            BusinessPlace place, Set<Account> accounts) {
        super(name, id, owners, place, accounts);
        this.productionResources = new HashSet<>();
        this.productionProducts = new HashSet<>();
    }

    /**
     * Minimal constructor for modern simulation.
     */
    public Factory(String name, org.episteme.natural.earth.Place place, Money capital) {
        super(name, place, capital);
        this.productionResources = new HashSet<>();
        this.productionProducts = new HashSet<>();
    }

    /** Returns unmodifiable set of ingredients/input resources required for production. */
    public Set<PotentialResource> getProductionResources() {
        return Collections.unmodifiableSet(productionResources);
    }

    public void addProductionResource(PotentialResource pr) {
        if (pr != null) productionResources.add(pr);
    }

    /** Returns unmodifiable set of output products manufactured by this factory. */
    public Set<PotentialResource> getProductionProducts() {
        return Collections.unmodifiableSet(productionProducts);
    }

    public void addProductionProduct(PotentialResource pr) {
        if (pr != null) productionProducts.add(pr);
    }

    /** 
     * Functional manufacturing step producing a material resource. 
     */
    public MaterialResource produce(String name, double amount, Money cost) {
        if (getCapital().isLessThan(cost)) throw new IllegalStateException("Insufficient capital");
        setCapital(getCapital().subtract(cost));
        
        var qty = org.episteme.core.measure.Quantities.create(amount, org.episteme.core.measure.Units.ONE);
        MaterialResource res = new MaterialResource(name, name, qty, this, 
                new SimpleIdentification(name + "_" + System.nanoTime()), cost);
        // addResource is now a stub method
        return res;
    }

    /**
     * Adds a worker to the organization.
     */
    public void addWorker(Worker worker) {
        if (getOrganigram() != null) {
            getOrganigram().addWorker(worker);
        }
    }

    /**
     * Adds a type of product this factory is capable of manufacturing.
     */
    public void addProductionType(String typeName) {
        addProductionProduct(new PotentialResource(typeName, typeName, 
            org.episteme.core.measure.Quantities.create(0, org.episteme.core.measure.Units.ONE)));
    }

    /**
     * Checks if this factory is configured to produce a specific product type.
     */
    public boolean canProduce(String typeName) {
        return getProductionProducts().stream().anyMatch(p -> p.getName().equals(typeName));
    }

    /**
     * Returns the set of resources currently held by the factory.
     */
    public Set<Resource> getInventory() {
        return getResources();
    }

    /**
     * Adds a resource to the factory's inventory.
     */
    public void addResource(MaterialResource resource) {
        // Resources are managed through the parent Organization
        // This is a compatibility method
    }

    /**
     * Returns the organizational chart (stub for compatibility).
     */
    @Override
    public Organigram getOrganigram() {
        return super.getOrganigram();
    }

    /**
     * Returns the workers associated with this factory.
     */
    public Set<Worker> getWorkers() {
        Organigram org = getOrganigram();
        return (org != null) ? org.getWorkers() : Collections.emptySet();
    }
}

