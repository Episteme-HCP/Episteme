package org.jscience.mathematics.vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import org.jscience.mathematics.number.Real;
import org.jscience.mathematics.sets.Reals;

class MatrixTest {

        @Test
        void testDenseMatrixCreation() {
                List<List<Real>> rows = Arrays.asList(
                                Arrays.asList(Real.of(1.0), Real.of(2.0)),
                                Arrays.asList(Real.of(3.0), Real.of(4.0)));
                Matrix<Real> m = new DenseMatrix<>(rows, Reals.getInstance());

                assertEquals(2, m.rows());
                assertEquals(2, m.cols());
                assertEquals(Real.of(1.0), m.get(0, 0));
                assertEquals(Real.of(4.0), m.get(1, 1));
        }

        @Test
        void testAdd() {
                Matrix<Real> m1 = new DenseMatrix<>(Arrays.asList(
                                Arrays.asList(Real.of(1.0), Real.of(2.0)),
                                Arrays.asList(Real.of(3.0), Real.of(4.0))), Reals.getInstance());

                Matrix<Real> m2 = new DenseMatrix<>(Arrays.asList(
                                Arrays.asList(Real.of(5.0), Real.of(6.0)),
                                Arrays.asList(Real.of(7.0), Real.of(8.0))), Reals.getInstance());

                Matrix<Real> sum = m1.add(m2);

                assertEquals(Real.of(6.0), sum.get(0, 0));
                assertEquals(Real.of(8.0), sum.get(0, 1));
                assertEquals(Real.of(10.0), sum.get(1, 0));
                assertEquals(Real.of(12.0), sum.get(1, 1));
        }

        @Test
        void testMultiply() {
                // [1 2] * [5 6] = [1*5+2*7 1*6+2*8] = [19 22]
                // [3 4] [7 8] [3*5+4*7 3*6+4*8] [43 50]

                Matrix<Real> m1 = new DenseMatrix<>(Arrays.asList(
                                Arrays.asList(Real.of(1.0), Real.of(2.0)),
                                Arrays.asList(Real.of(3.0), Real.of(4.0))), Reals.getInstance());

                Matrix<Real> m2 = new DenseMatrix<>(Arrays.asList(
                                Arrays.asList(Real.of(5.0), Real.of(6.0)),
                                Arrays.asList(Real.of(7.0), Real.of(8.0))), Reals.getInstance());

                Matrix<Real> product = m1.multiply(m2);

                assertEquals(Real.of(19.0), product.get(0, 0));
                assertEquals(Real.of(22.0), product.get(0, 1));
                assertEquals(Real.of(43.0), product.get(1, 0));
                assertEquals(Real.of(50.0), product.get(1, 1));
        }

        @Test
        void testTranspose() {
                Matrix<Real> m = new DenseMatrix<>(Arrays.asList(
                                Arrays.asList(Real.of(1.0), Real.of(2.0)),
                                Arrays.asList(Real.of(3.0), Real.of(4.0))), Reals.getInstance());

                Matrix<Real> t = m.transpose();

                assertEquals(Real.of(1.0), t.get(0, 0));
                assertEquals(Real.of(3.0), t.get(0, 1));
                assertEquals(Real.of(2.0), t.get(1, 0));
                assertEquals(Real.of(4.0), t.get(1, 1));
        }

        @Test
        void testDeterminant() {
                Matrix<Real> m = new DenseMatrix<>(Arrays.asList(
                                Arrays.asList(Real.of(1.0), Real.of(2.0)),
                                Arrays.asList(Real.of(3.0), Real.of(4.0))), Reals.getInstance());

                // det = 1*4 - 2*3 = 4 - 6 = -2
                assertEquals(Real.of(-2.0), m.determinant());
        }
}
