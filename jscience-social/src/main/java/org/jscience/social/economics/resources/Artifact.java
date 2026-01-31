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

import org.jscience.social.economics.Community;
import org.jscience.social.economics.MaterialResource;
import org.jscience.social.economics.money.Money;
import org.jscience.natural.earth.Place;
import org.jscience.core.measure.Quantity;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.social.history.time.TimeCoordinate;


/**
 * Represents a resource created by human craftsmanship (an artifact).
 * Artifacts are the result of human craftsmanship and have a specific purpose.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Artifact extends MaterialResource {

    private static final long serialVersionUID = 1L;

    /** Functional state of the artifact. */
    @Attribute
    private boolean broken;

    /**
     * Initializes a new artifact.
     */
    public Artifact(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, TimeCoordinate productionDate,
            Identification identification, Money value) {
        super(name, description, amount, producer, productionPlace, productionDate, identification, value);
        this.broken = false;
    }

    /**
     * Checks if the artifact is broken.
     * @return true if broken, false otherwise
     */
    public boolean isBroken() {
        return broken;
    }

    /**
     * Sets the functional state of the artifact.
     * @param broken true if broken, false otherwise
     */
    public void setBroken(boolean broken) {
        this.broken = broken;
    }
}

