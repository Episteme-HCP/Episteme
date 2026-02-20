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

import org.jscience.natural.biology.taxonomy.Species;
import org.jscience.natural.earth.Place;

/**
 * Represents a nuclear or extended PsychologicalFamily PsychologicalGroup.
 * A PsychologicalFamily is a specialized {@link PsychologicalGroup} where members typically share genetic 
 * or formal kinship bonds.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PsychologicalFamily extends PsychologicalGroup {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new PsychologicalFamily for a specific species.
     *
     * @param species the biological species of the PsychologicalFamily
     */
    public PsychologicalFamily(Species species) {
        super(species);
    }

    /**
     * Creates a new PsychologicalFamily with a designated home territory.
     *
     * @param species         the biological species
     * @param formalTerritory the physical site or home associated with the PsychologicalFamily
     */
    public PsychologicalFamily(Species species, Place formalTerritory) {
        super(species, formalTerritory);
    }
}

