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

package org.jscience.economics.resources;

import java.io.Serializable;
import java.time.Instant;
import javax.media.j3d.Group;
import org.jscience.economics.Community;
import org.jscience.economics.Resource;
import org.jscience.geography.Place;
import org.jscience.measure.Quantity;

/**
 * Represents a tangible physical object that occupies space and may have a 3D representation.
 * Base class for all physical resources in the economy simulation.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class Thing extends Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 3D geometry representation. */
    private Group group;

    /**
     * Initializes a new physical thing.
     */
    public Thing(String name, String description, Quantity<?> amount,
            Community producer, Place productionPlace, Instant productionDate) {
        super(name, description, amount, producer, productionPlace, productionDate);
    }

    /**
     * Returns the 3D geometry group.
     * @return the J3D group, or null if not set
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the 3D geometry group.
     * @param group the J3D group
     */
    public void setGroup(Group group) {
        this.group = group;
    }
}
