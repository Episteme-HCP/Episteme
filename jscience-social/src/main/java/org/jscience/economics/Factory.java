package org.jscience.economics;

import org.jscience.geography.Place;
import java.util.HashSet;
import java.util.Set;

/**
 * An Organization that produces goods (MaterialResources).
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

        MaterialResource<?> product = new MaterialResource<>(name, "Produced at " + getName(), quantity, this,
                getHeadquarters(), cost);
        addResource(product);
        return product;
    }
}
