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

package org.episteme.social.history.archeology;

import org.episteme.core.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models archaeological stratigraphy and topological relationships between strata.
 * Supports the analysis of Harris Matrices and verifies logical consistency of stratigraphic sequences.
 */
public final class StratigraphyModel {

    private StratigraphyModel() {
        // Prevent instantiation
    }

    /**
     * Topographic relationship between stratigraphic units.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
    public enum Relationship {
        ABOVE, BELOW, EQUAL, CUT_BY, COVERS
    }

    /**
     * Verifies if a list of strata and their declared relationships are logically symmetric and consistent.
     * 
     * @param sequence list of stratigraphic units
     * @return true if the sequence is consistent according to topological laws
     * @throws NullPointerException if sequence is null
     */
    public static boolean isSequenceConsistent(List<Stratum> sequence) {
        Objects.requireNonNull(sequence, "Stratigraphy sequence cannot be null");
        Map<String, Stratum> idMap = new HashMap<>();
        for (Stratum s : sequence) {
            idMap.put(s.getId().toString(), s);
        }

        for (Stratum s : sequence) {
            for (Map.Entry<String, Relationship> rel : s.getRelations().entrySet()) {
                Relationship type = rel.getValue();
                String targetId = rel.getKey();
                Stratum target = idMap.get(targetId);
                
                if (target == null) continue;

                // Validate Law of Superposition symmetry
                if (type == Relationship.ABOVE && target.getRelations().get(s.getId().toString()) != Relationship.BELOW) return false;
                if (type == Relationship.BELOW && target.getRelations().get(s.getId().toString()) != Relationship.ABOVE) return false;
                if (type == Relationship.EQUAL && target.getRelations().get(s.getId().toString()) != Relationship.EQUAL) return false;
            }
        }
        return true;
    }

    /**
     * Estimates age of a stratum based on depth and estimated sedimentation rate.
     * 
     * @param depth depth in meters
     * @param sedimentationRateCmPerCentury rate in centimeters per 100 years
     * @return estimated age in years as a Real number
     */
    public static Real estimateAgeFromDepth(double depth, double sedimentationRateCmPerCentury) {
        if (sedimentationRateCmPerCentury <= 0) return Real.ZERO;
        // years = (depth_m * 100 cm/m) / (rate_cm / 100 years)
        // years = (depth * 100 / rate) * 100
        double years = (depth * 100.0 / sedimentationRateCmPerCentury) * 100.0;
        return Real.of(years);
    }
}

