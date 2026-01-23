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

package org.jscience.politics;

import java.io.Serializable;

/**
 * Provides methods for structural comparison of national constitutions.
 * Used for comparative law and political science research.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class ConstitutionalComparator {

    private ConstitutionalComparator() {}

    /** Structural metadata for a national constitution. */
    public record ConstitutionInfo(
        String country, 
        boolean isFederal, 
        boolean isPresidential, 
        int termLength
    ) implements Serializable {}

    /**
     * Calculates a structural similarity coefficient between two constitutions.
     * 
     * @param c1 first constitution info
     * @param c2 second constitution info
     * @return similarity score between 0.0 and 1.0
     */
    public static double structuralSimilarity(ConstitutionInfo c1, ConstitutionInfo c2) {
        if (c1 == null || c2 == null) return 0.0;
        
        double score = 0;
        if (c1.isFederal() == c2.isFederal()) score += 0.33;
        if (c1.isPresidential() == c2.isPresidential()) score += 0.33;
        if (c1.termLength() == c2.termLength()) score += 0.34;
        
        return score;
    }
}
