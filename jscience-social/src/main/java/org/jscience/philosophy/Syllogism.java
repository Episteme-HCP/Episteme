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

package org.jscience.philosophy;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a formal logical argument where a conclusion is inferred from 
 * two premises.
 * 
 * <p> A classic syllogism consists of a Major Premise, a Minor Premise, and 
 *     a Conclusion, following Aristotelian logical structures.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Syllogism implements Serializable {


    private static final long serialVersionUID = 1L;

    private final Premise majorPremise;
    private final Premise minorPremise;
    private final Premise conclusion;

    /**
     * Constructs a Syllogism.
     * @param majorPremise the major premise (containing the major term)
     * @param minorPremise the minor premise (containing the minor term)
     * @param conclusion   the inferred conclusion
     * @throws NullPointerException if any premise is null
     */
    public Syllogism(Premise majorPremise, Premise minorPremise, Premise conclusion) {
        this.majorPremise = Objects.requireNonNull(majorPremise, "Major Premise cannot be null");
        this.minorPremise = Objects.requireNonNull(minorPremise, "Minor Premise cannot be null");
        this.conclusion = Objects.requireNonNull(conclusion, "Conclusion cannot be null");
    }

    public Premise getMajorPremise() {
        return majorPremise;
    }

    public Premise getMinorPremise() {
        return minorPremise;
    }

    public Premise getConclusion() {
        return conclusion;
    }

    /**
     * Checks if the argument is "sound".
     * A sound argument is one that is both valid (logical structure) and has true premises.
     * Note: This method only checks the truth values of the components as defined; 
     * it does not validate the logical inference form itself (Use LogicSolver for formal validity).
     * 
     * @return true if all premises and the conclusion are marked as true
     */
    public boolean isSound() {
        return majorPremise.isTrue() && minorPremise.isTrue() && conclusion.isTrue();
    }

    @Override
    public String toString() {
        return String.format("Major: %s%nMinor: %s%nTherefore: %s",
                majorPremise.getStatement(), minorPremise.getStatement(), conclusion.getStatement());
    }
}
