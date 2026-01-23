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

package org.jscience.history;

import org.jscience.util.persistence.Persistent;

/**
 * Official divisions of geological time according to the International Commission on Stratigraphy (ICS).
 * Time units are defined in Mega-annum (Ma = Million years ago).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public enum GeologicalTimeScale {
    
    // Eons
    HADEAN("Hadean", 4600.0, 4000.0),
    ARCHEAN("Archean", 4000.0, 2500.0),
    PROTEROZOIC("Proterozoic", 2500.0, 541.0),
    PHANEROZOIC("Phanerozoic", 541.0, 0.0),

    // Eras (Phanerozoic)
    PALEOZOIC("Paleozoic", 541.0, 252.0),
    MESOZOIC("Mesozoic", 252.0, 66.0),
    CENOZOIC("Cenozoic", 66.0, 0.0),

    // Periods
    TRIASSIC("Triassic", 252.0, 201.0),
    JURASSIC("Jurassic", 201.0, 145.0),
    CRETACEOUS("Cretaceous", 145.0, 66.0),
    PALEOGENE("Paleogene", 66.0, 23.0),
    NEOGENE("Neogene", 23.0, 2.58),
    QUATERNARY("Quaternary", 2.58, 0.0),

    // Epochs
    PLEISTOCENE("Pleistocene", 2.58, 0.0117),
    HOLOCENE("Holocene", 0.0117, 0.0);

    private final String name;
    private final double startMa; // Million Years Ago
    private final double endMa;   // Million Years Ago

    GeologicalTimeScale(String name, double startMa, double endMa) {
        this.name = name;
        this.startMa = startMa;
        this.endMa = endMa;
    }

    public String getName() {
        return name;
    }

    public double getStartMa() {
        return startMa;
    }

    public double getEndMa() {
        return endMa;
    }
    
    public double getStartYear() {
        return startMa * 1_000_000.0;
    }
    
    public double getEndYear() {
        return endMa * 1_000_000.0;
    }

    /**
     * Checks if a specific point in time (expressed in years before present) falls within this division.
     * 
     * @param yearsAgo positive value representing years before present
     * @return true if the year is contained within this geological time unit
     */
    public boolean contains(double yearsAgo) {
        return yearsAgo <= getStartYear() && yearsAgo >= getEndYear();
    }
}
