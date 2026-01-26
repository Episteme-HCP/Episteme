/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics.resources;

import java.time.Instant;
import org.jscience.measure.Quantity;
import org.jscience.measure.quantity.Energy;

/**
 * An interface representing a substance that can be eaten or drunk to provide nutritional support.
 * Food contains nutrients such as carbohydrates, fats, proteins, vitamins, or minerals.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public interface Food {
    
    /**
     * Returns the composition of the food.
     *
     * @return the composition as a string description
     */
    String getComposition();

    /**
     * Returns the energy content (calories) of the food.
     * 
     * @return the energy quantity, or null if unknown
     */
    default Quantity<Energy> getEnergyContent() {
        return null;
    }

    /**
     * Returns the expiration date ("best before" date) of the food.
     * 
     * @return the expiration instant, or null if non-perishable or unknown
     */
    default Instant getExpirationDate() {
        return null;
    }
}
