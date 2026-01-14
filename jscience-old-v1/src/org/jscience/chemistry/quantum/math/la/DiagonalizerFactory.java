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
 * DiagonalizerFactory.java
 *
 * Created on August 5, 2004, 7:20 AM
 */
package org.jscience.chemistry.quantum.math.la;

import java.lang.ref.WeakReference;


/**
 * Supplying Diagonalizers! Follows a singleton pattern.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class DiagonalizerFactory {
    /** DOCUMENT ME! */
    private static WeakReference _diagonalizerFactory = null;

    /** Holds value of property defaultDiagonalizer. */
    private Diagonalizer defaultDiagonalizer;

/**
     * Creates a new instance of DiagonalizerFactory
     */
    private DiagonalizerFactory() {
        defaultDiagonalizer = new JacobiDiagonalizer();
    }

    /**
     * Get an instance (and the only one) of BasisReader
     *
     * @return DiagonalizerFactory instance
     */
    public static DiagonalizerFactory getInstance() {
        if (_diagonalizerFactory == null) {
            _diagonalizerFactory = new WeakReference(new DiagonalizerFactory());
        } // end if

        DiagonalizerFactory diagonalizerFactory = (DiagonalizerFactory) _diagonalizerFactory.get();

        if (diagonalizerFactory == null) {
            diagonalizerFactory = new DiagonalizerFactory();
            _diagonalizerFactory = new WeakReference(diagonalizerFactory);
        } // end if

        return diagonalizerFactory;
    }

    /**
     * Return an appropriate instance of Diagonalizer
     *
     * @param dt the diagonalizer type
     *
     * @return appropriate Diagonalizer instance
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public Diagonalizer getDiagonalizer(DiagonalizerType dt) {
        if (dt.equals(DiagonalizerType.JACOBI)) {
            return new JacobiDiagonalizer();
        } else {
            throw new UnsupportedOperationException("Diagonalizer not " +
                "supported : " + dt.toString());
        } // end if
    }

    /**
     * Getter for property defaultDiagonalizer.
     *
     * @return Value of property defaultDiagonalizer.
     */
    public Diagonalizer getDefaultDiagonalizer() {
        return this.defaultDiagonalizer;
    }

    /**
     * Setter for property defaultDiagonalizer.
     *
     * @param defaultDiagonalizer New value of property defaultDiagonalizer.
     */
    public void setDefaultDiagonalizer(Diagonalizer defaultDiagonalizer) {
        this.defaultDiagonalizer = defaultDiagonalizer;
    }
} // end of class DiagonalizerFactory
