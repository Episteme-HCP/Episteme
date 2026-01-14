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

package org.jscience.measure.random;

/**
 * This interface represent a random generator for scalars.
 * Additionally, normalized generator should provide null mean and unit standard deviation
 * scalars.
 *
 * @author L. Maisonobe
 * @version $Id: RandomGenerator.java,v 1.2 2007-10-21 17:46:13 virtualcall Exp $
 */
public interface RandomGenerator {

    /**
     * Generate a random scalar with null mean and unit standard deviation.
     * <p/>
     * <p/>
     * This method does <strong>not</strong> specify the shape of the
     * distribution, it is the implementing class that provides it. The only
     * contract here is to generate numbers with null mean and unit standard
     * deviation.
     * </p>
     *
     * @return a random scalar
     */
    public double nextDouble();

}
