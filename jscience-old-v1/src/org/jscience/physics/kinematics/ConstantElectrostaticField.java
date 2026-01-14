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

package org.jscience.physics.kinematics;

import org.jscience.mathematics.algebraic.matrices.AbstractDoubleVector;


/**
 * The ElectrostaticField class provides an object for encapsulating
 * electrostatic fields forces.
 *
 * @author Silvere Martin-Michiellot
 * @author Mark Hale
 * @version 1.0
 */

//this is the E vector of F = q * E
public class ConstantElectrostaticField extends ConstantField {
/**
     * Constructs the field.
     *
     * @param p            DOCUMENT ME!
     * @param doubleVector DOCUMENT ME!
     */
    public ConstantElectrostaticField(AbstractClassicalParticle p,
        AbstractDoubleVector doubleVector) {
        super(p, doubleVector);
    }

    /**
     * Creates the force acting on a particle in this field.
     *
     * @return DOCUMENT ME!
     */
    public Force createForce() {
        return new ConstantElectrostaticFieldForce(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private class ConstantElectrostaticFieldForce extends Force {
        /** DOCUMENT ME! */
        private ConstantElectrostaticField constantElectrostaticField;

/**
         * Creates a new ConstantElectrostaticFieldForce object.
         *
         * @param e DOCUMENT ME!
         */
        public ConstantElectrostaticFieldForce(ConstantElectrostaticField e) {
            super(e.getParticle());

            if (e != null) {
                //super(e.getParticle());
                this.constantElectrostaticField = e;
            } else {
                throw new IllegalArgumentException(
                    "ConstantElectrostaticFieldForce doesn't accept null arguments.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public ConstantElectrostaticField getConstantElectrostaticField() {
            return constantElectrostaticField;
        }

        /**
         * DOCUMENT ME!
         *
         * @param t DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbstractDoubleVector getVector(double t) {
            return getConstantElectrostaticField().getVector()
                       .scalarMultiply(getConstantElectrostaticField()
                                           .getParticle().getCharge());
        }
    }
}
