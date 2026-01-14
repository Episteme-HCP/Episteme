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

/*
 * DensityGuesser.java
 *
 * Created on August 8, 2004, 8:29 AM
 */
package org.jscience.chemistry.quantum;

import org.jscience.chemistry.quantum.math.matrix.Matrix;


/**
 * An external guess for Density Matrix (DM) should implement this interface.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public interface DensityGuesser {
    /**
     * Guess the DM, using some intelligent techniques ... (like
     * Molecular Tailoring Approach - MTA)
     *
     * @param scfMethod the SCF method for which the guess is to be done
     *
     * @return the density matrix (DM) of the molecule
     */
    public Matrix guessDM(SCFMethod scfMethod);
} // end of interface DensityGuesser
