/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.economics;

import org.jscience.geography.BusinessPlace;

import org.jscience.measure.Identification;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;


/**
 * Factories are organizations meant to produce at low cost a small set of
 * products using mechanisms such as taylorization.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//there is no formal distinction with an organization although one is to expect to have a known list of possible products
//factory ususally produce PotentialResource.SECONDARY kind of products
public class Factory extends Organization {
    /** DOCUMENT ME! */
    private Set productionResources; //the raw materials used into production of the products, not including machines and human time ; sort of the ingredients

    /** DOCUMENT ME! */
    private Set productionProducts; //the expected product that should get out the factory if the actual factory has 1. the machines and the human labor and 2. the prodcution resources.

/**
     * Creates a new Factory object.
     *
     * @param name           DOCUMENT ME!
     * @param identification DOCUMENT ME!
     * @param owners         DOCUMENT ME!
     * @param place          DOCUMENT ME!
     * @param accounts       DOCUMENT ME!
     */
    public Factory(String name, Identification identification, Set owners,
        BusinessPlace place, Set accounts) {
        super(name, identification, owners, place, accounts);

        productionResources = Collections.EMPTY_SET;
        productionProducts = Collections.EMPTY_SET;
    }

    //the raw materials used into production of the products, not including machines and human time ; sort of the ingredients
    /**
     * DOCUMENT ME!
     *
     * @param potentialResource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getProductionResources(PotentialResource potentialResource) {
        return productionResources;
    }

    /**
     * DOCUMENT ME!
     *
     * @param potentialResource DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //you should only assign PotentialResources here and not subclasses
    //or you can assign a copy of actual resources from the getResources() list
    public void addProductionResources(PotentialResource potentialResource) {
        if (potentialResource != null) {
            productionResources.add(potentialResource);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null PotentialResource.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param potentialResource DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeProductionResources(PotentialResource potentialResource) {
        if (potentialResource != null) {
            productionResources.remove(potentialResource);
        } else {
            throw new IllegalArgumentException(
                "You can't remove null PotentialResource.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param productionResources DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //you should only assign PotentialResources here and not subclasses
    //or you can assign a copy of actual resources from the getResources() list, thus tagging them as the resources you actually want to sell
    public void setProductionResources(Set productionResources) {
        Iterator iterator;
        boolean valid;

        if (productionResources != null) {
            valid = true;
            iterator = productionResources.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof PotentialResource;
            }

            if (valid) {
                this.productionResources = productionResources;
            } else {
                throw new IllegalArgumentException(
                    "The Set of PotentialResources should contain only PotentialResources.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of PotentialResources shouldn't be null.");
        }
    }

    //the expected product that should get out the factory if the actual factory has 1. the machines and the human labor and 2. the prodcution resources.
    /**
     * DOCUMENT ME!
     *
     * @param potentialResource DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getProductionProducts(PotentialResource potentialResource) {
        return productionProducts;
    }

    /**
     * DOCUMENT ME!
     *
     * @param potentialResource DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //you should only assign PotentialResources here and not subclasses
    public void addProductionProducts(PotentialResource potentialResource) {
        if (potentialResource != null) {
            productionProducts.add(potentialResource);
        } else {
            throw new IllegalArgumentException(
                "You can't add a null PotentialResource.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param potentialResource DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeProductionProducts(PotentialResource potentialResource) {
        if (potentialResource != null) {
            productionProducts.remove(potentialResource);
        } else {
            throw new IllegalArgumentException(
                "You can't remove null PotentialResource.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param productionProducts DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //you should only assign PotentialResources here and not subclasses
    public void setProductionProducts(Set productionProducts) {
        Iterator iterator;
        boolean valid;

        if (productionResources != null) {
            valid = true;
            iterator = productionProducts.iterator();

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof PotentialResource;
            }

            if (valid) {
                this.productionProducts = productionProducts;
            } else {
                throw new IllegalArgumentException(
                    "The Set of PotentialResources should contain only PotentialResources.");
            }
        } else {
            throw new IllegalArgumentException(
                "The Set of PotentialResources shouldn't be null.");
        }
    }
}
