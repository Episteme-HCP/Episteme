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

package org.episteme.social.economics.resources;

import org.episteme.social.history.time.TimeCoordinate;
import org.episteme.social.economics.Community;
import org.episteme.social.economics.money.Money;
import org.episteme.natural.earth.Place;
import org.episteme.core.measure.Quantity;
import org.episteme.core.util.identity.Identification;

/**
 * Represents a large-scale structure or artwork installed in a specific location.
 * Distinguished from Buildings by purpose (often artistic or infrastructural).
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Installation extends Artifact {

    private static final long serialVersionUID = 1L;

    /**
     * Initializes a new installation.
     */
    public Installation(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Identification identification, Money value) {
        super(name, description, amount, producer, productionPlace, productionDate, identification, value);
    }
}

