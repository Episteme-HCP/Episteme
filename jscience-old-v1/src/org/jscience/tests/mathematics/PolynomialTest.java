package org.jscience.tests.mathematics;

import org.jscience.mathematics.Complex;
import org.jscience.mathematics.ExtraMath;
import org.jscience.mathematics.polynomials.ComplexPolynomial;
import org.jscience.mathematics.polynomials.PolynomialMath;


/**
 * Testcase for polynomials.
 */
public class PolynomialTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private final int n = 10;

/**
     * Creates a new PolynomialTest object.
     *
     * @param name DOCUMENT ME!
     */
    public PolynomialTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(PolynomialTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    public void testFrobenius() {
        Complex[][] c = new Complex[2][n];
        double[][] d = new double[2][n];

        for (int k = 0; k < n; k++) {
            d[0][k] = k + 1;
            c[0][k] = new Complex(d[0][k], d[0][k]);
            c[1][k] = new Complex(1, 0);
            d[1][k] = 1.;
        }

        System.out.println(PolynomialMath.getFrobeniusMatrix(
                new ComplexPolynomial(c[0])));

        double[] dc = PolynomialMath.interpolateLagrange(d);
        Complex[] cc = PolynomialMath.interpolateLagrange(c);

        for (int k = 0; k < n; k++) {
            System.out.println("Real " +
                PolynomialMath.evalPolynomial(dc, d[0][k]) + "complex " +
                PolynomialMath.evalPolynomial(cc, c[0][k]));
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testInterpolation() {
        for (int k = 0; k < n; k++) {
            System.out.println();

            for (int j = 0; j <= k; j++) {
                System.out.print(ExtraMath.binomial(k, j) + " ");
            }
        }

        double[][] data = new double[2][n];

        for (int k = 0; k < n; k++) {
            if (k > 0) {
                data[0][k] = data[0][k - 1] + Math.random();
            } else {
                data[0][k] = Math.random();
            }

            data[1][k] = Math.random();
        }

        long start = System.currentTimeMillis();
        double[] r = PolynomialMath.interpolateLagrange(data);
        long end = System.currentTimeMillis();
        System.out.println(" Interpolation Polynomial degree " + n +
            " lasted " + (end - start) + " ms");

        for (int k = 0; k < n; k++) {
            System.out.println(data[0][k] + " \t" + data[1][k] + " \t" +
                PolynomialMath.evalPolynomial(r, data[0][k]) + " \t" +
                PolynomialMath.interpolateLagrange(data, data[0][k]));
        }
    }
}
