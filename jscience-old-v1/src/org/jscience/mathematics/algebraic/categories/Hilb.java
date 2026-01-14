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

package org.jscience.mathematics.algebraic.categories;

import org.jscience.mathematics.algebraic.algebras.BanachSpace;
import org.jscience.mathematics.algebraic.algebras.HilbertSpace;
import org.jscience.mathematics.algebraic.fields.Field;
import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.groups.AbelianGroup;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexMatrix;
import org.jscience.mathematics.algebraic.matrices.AbstractComplexVector;
import org.jscience.mathematics.algebraic.matrices.ComplexDiagonalMatrix;
import org.jscience.mathematics.algebraic.matrices.ComplexMatrix;
import org.jscience.mathematics.algebraic.modules.Module;
import org.jscience.mathematics.algebraic.modules.VectorSpace;
import org.jscience.mathematics.algebraic.numbers.Complex;
import org.jscience.mathematics.algebraic.numbers.Integer;


/**
 * The Hilb class encapsulates the category <b>Hilb</b>.
 *
 * @author Mark Hale
 * @version 1.0
 */
public class Hilb extends Object implements Category {
/**
     * Constructs a <b>Hilb</b> category.
     */
    public Hilb() {
    }

    /**
     * Returns the identity morphism for an object.
     *
     * @param a a HilbertSpace.
     *
     * @return DOCUMENT ME!
     */
    public Category.Morphism identity(Object a) {
        return new LinearMap(ComplexDiagonalMatrix.identity(
                ((HilbertSpace) a).dimension()));
    }

    /**
     * Returns the cardinality of an object.
     *
     * @param a a HilbertSpace.
     *
     * @return DOCUMENT ME!
     */
    public Object cardinality(Object a) {
        return new Integer(((HilbertSpace) a).dimension());
    }

    /**
     * Returns a hom-set.
     *
     * @param a a HilbertSpace.
     * @param b a HilbertSpace.
     *
     * @return a HilbertSpace.
     */
    public Category.HomSet hom(Object a, Object b) {
        return new OperatorSpace((HilbertSpace) a, (HilbertSpace) b);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    public class OperatorSpace extends HilbertSpace implements Category.HomSet {
        /**
         * DOCUMENT ME!
         */
        private final int rows;

        /**
         * DOCUMENT ME!
         */
        private final int cols;

        /**
         * Creates a new OperatorSpace object.
         *
         * @param a DOCUMENT ME!
         * @param b DOCUMENT ME!
         */
        public OperatorSpace(HilbertSpace a, HilbertSpace b) {
            super(a.dimension() * b.dimension());
            rows = b.dimension();
            cols = a.dimension();
        }

        /**
         * Returns an element of this hom-set.
         *
         * @param array DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public VectorSpace.Member getVector(Complex[][] array) {
            return new LinearMap(array);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    public class LinearMap implements BanachSpace.Member, Category.Morphism {
        /**
         * DOCUMENT ME!
         */
        private AbstractComplexMatrix matrix;

        /**
         * Creates a new LinearMap object.
         *
         * @param array DOCUMENT ME!
         */
        public LinearMap(Complex[][] array) {
            matrix = new ComplexMatrix(array);
        }

        /**
         * Creates a new LinearMap object.
         *
         * @param m DOCUMENT ME!
         */
        public LinearMap(AbstractComplexMatrix m) {
            matrix = m;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object domain() {
            return new HilbertSpace(matrix.numColumns());
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object codomain() {
            return new HilbertSpace(matrix.numRows());
        }

        /**
         * DOCUMENT ME!
         *
         * @param v DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object map(Object v) {
            return matrix.multiply((AbstractComplexVector) v);
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Category.Morphism compose(Category.Morphism m) {
            if (m instanceof LinearMap) {
                LinearMap lm = (LinearMap) m;

                if (matrix.numColumns() == lm.matrix.numRows()) {
                    return new LinearMap(lm.matrix.multiply(matrix));
                } else {
                    throw new UndefinedCompositionException();
                }
            } else {
                throw new IllegalArgumentException(
                    "Morphism is not a LinearMap.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public double norm() {
            return matrix.frobeniusNorm();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int dimension() {
            return matrix.numRows() * matrix.numColumns();
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Object getSet() {
            return matrix.getSet();
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbelianGroup.Member add(final AbelianGroup.Member m) {
            if (m instanceof LinearMap) {
                return new LinearMap(matrix.add(((LinearMap) m).matrix));
            } else {
                throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbelianGroup.Member negate() {
            return new LinearMap((AbstractComplexMatrix) matrix.negate());
        }

        /**
         * DOCUMENT ME!
         *
         * @param m DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public AbelianGroup.Member subtract(final AbelianGroup.Member m) {
            if (m instanceof LinearMap) {
                return new LinearMap(matrix.subtract(((LinearMap) m).matrix));
            } else {
                throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param z DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Module.Member scalarMultiply(Ring.Member z) {
            if (z instanceof Complex) {
                return new LinearMap(matrix.scalarMultiply((Complex) z));
            } else {
                throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param z DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public VectorSpace.Member scalarDivide(Field.Member z) {
            if (z instanceof Complex) {
                return new LinearMap(matrix.scalarMultiply((Complex) z));
            } else {
                throw new IllegalArgumentException(
                    "Member class not recognised by this method.");
            }
        }
    }
}
