/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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
import java.util.HashSet;
import java.util.Set;

/**
 * Martin-Michiellot
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Factory extends Organization {

    // Helper set to know what this factory can produce
    private Set<String> productionTypes;

    public Factory(String name, Place headquarters, Money initialCapital) {
        super(name, headquarters, initialCapital);
        this.productionTypes = new HashSet<>();
    }

    public void addProductionType(String resourceName) {
        productionTypes.add(resourceName);
    }

    public boolean canProduce(String resourceName) {
        return productionTypes.contains(resourceName);
    }

    public MaterialResource<?> produce(String name, double quantity, Money cost) {
        if (!canProduce(name)) {
            throw new IllegalArgumentException("This factory cannot produce " + name);
        }

        // Deduction of capital (cost of production)
        // In a real simulation, this would consume raw materials.
        // For V1, we just deduct money if available.
        if (getCapital().getAmount().doubleValue() < cost.getAmount().doubleValue()) {
            throw new IllegalStateException("Not enough capital to produce");
        }

        setCapital(getCapital().subtract(cost));

        @SuppressWarnings("rawtypes")
        MaterialResource product = new MaterialResource(name, "Produced at " + getName(),
                org.jscience.mathematics.numbers.real.Real.of(quantity), this,
                getHeadquarters(), cost);
        addResource(product);
        return product;
    }
}
