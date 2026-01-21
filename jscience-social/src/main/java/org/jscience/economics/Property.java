/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2014 - JScience (http://jscience.org/)
 * All rights reserved.
 * 
 * Permission to use, copy, modify, and distribute this software is
 * freely granted, provided that this notice is preserved.
 */
package org.jscience.economics;

import org.jscience.economics.money.Money;
import java.util.Set;

/**
 * An interface defining ownership and value of assets.
 * Implemented by classes representing physical or intellectual property.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.2
 */
public interface Property {
    
    /**
     * Returns the set of owners of this property.
     * 
     * @return a Set of {@link EconomicAgent} who own this property
     */
    Set<EconomicAgent> getOwners();

    /**
     * Returns the monetary value of this property.
     * 
     * @return the value as a {@link Money} object
     */
    Money getValue();
}
