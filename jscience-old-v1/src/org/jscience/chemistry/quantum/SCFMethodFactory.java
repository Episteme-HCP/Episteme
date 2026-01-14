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
 * SCFMethodFactory.java
 *
 * Created on August 15, 2004, 2:35 PM
 */
package org.jscience.chemistry.quantum;

import java.lang.ref.WeakReference;


/**
 * Factory of SCF methods. <br> Follows a singleton pattern.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class SCFMethodFactory {
    /** DOCUMENT ME! */
    private static WeakReference _scfMethodFactory = null;

/**
     * Creates a new instance of SCFMethodFactory
     */
    private SCFMethodFactory() {
    }

    /**
     * Get an instance (and the only one) of SCFMethodFactory
     *
     * @return SCFMethodFactory instance
     */
    public static SCFMethodFactory getInstance() {
        if (_scfMethodFactory == null) {
            _scfMethodFactory = new WeakReference(new SCFMethodFactory());
        } // end if

        SCFMethodFactory scfMethodFactory = (SCFMethodFactory) _scfMethodFactory.get();

        if (scfMethodFactory == null) {
            scfMethodFactory = new SCFMethodFactory();
            _scfMethodFactory = new WeakReference(scfMethodFactory);
        } // end if

        return scfMethodFactory;
    }

    /**
     * Return an appropriate class appropriate <code>SCFType</code>
     *
     * @param molecule the molecule for which calculations are to be performed
     * @param oneEI 1E integrals for this molecule at appropriate basis
     * @param twoEI 2E integrals for this molecule at appropriate basis
     * @param type the SCFType instance
     *
     * @return DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */
    public SCFMethod getSCFMethod(Molecule molecule,
        OneElectronIntegrals oneEI, TwoElectronIntegrals twoEI, SCFType type) {
        if (type.equals(SCFType.HARTREE_FOCK)) {
            return new HartreeFockSCFMethod(molecule, oneEI, twoEI);
        } else {
            throw new UnsupportedOperationException("The type : " + type +
                " has no known implementing class!");
        } // end if
    }
} // end of class SCFMethodFactory
