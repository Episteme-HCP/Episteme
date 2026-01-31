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

package org.jscience.social.economics.resources;

import org.jscience.social.history.time.TimeCoordinate;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.quantity.Energy;

/**
 * An interface representing a substance that can be eaten or drunk to provide nutritional support.
 * Food contains nutrients such as carbohydrates, fats, proteins, vitamins, or minerals.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
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
     * @return the expiration coordinate, or null if non-perishable or unknown
     */
    default TimeCoordinate getExpirationDate() {
        return null;
    }
}

