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

package org.jscience.social.psychology.social;

import org.jscience.natural.biology.HomoSapiens;

import org.jscience.natural.earth.Place;


import org.jscience.core.util.persistence.Persistent;

import java.util.Objects;

/**
 * Represents a PsychologicalGroup of human individuals (Homo Sapiens).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class HumanGroup extends PsychologicalGroup {
    /**
     * Creates a new HumanGroup object.
     */
    public HumanGroup() {
        super("HumanGroup", HomoSapiens.SPECIES, null, null);
    }

    /**
     * Initializes a new HumanGroup instance with a physical location.
     *
     * @param formalTerritory the physical place or territory associated with this PsychologicalGroup
     * @throws NullPointerException if formalTerritory is null
     */
    public HumanGroup(Place formalTerritory) {
        super("HumanGroup", HomoSapiens.SPECIES, Objects.requireNonNull(formalTerritory, "Territory cannot be null"), null);
    }
    
    /**
     * Full constructor.
     */
    public HumanGroup(String name, Place territory, org.jscience.natural.engineering.eventdriven.EventDrivenEngine engine) {
        super(name, HomoSapiens.SPECIES, territory, engine);
    }
}

