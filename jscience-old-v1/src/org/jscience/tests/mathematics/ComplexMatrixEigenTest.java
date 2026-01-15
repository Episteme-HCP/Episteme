package org.jscience.tests.mathematics;

/**
 * Testcase for complex eigenvalue/vector methods.
 *
 * @author Mark Hale
 */
public class ComplexMatrixEigenTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private final int N = 5;

    /** DOCUMENT ME! */
    private ComplexSquareMatrix sqmat;

    /** DOCUMENT ME! */
    private double[] eval;

    /** DOCUMENT ME! */
    private ComplexVector[] evec;

/**
     * Creates a new ComplexMatrixEigenTest object.
     *
     * @param name DOCUMENT ME!
     */
    public ComplexMatrixEigenTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(ComplexMatrixEigenTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-6;
        sqmat = MatrixToolkit.randomComplexSquareMatrix(N);
        sqmat = (ComplexSquareMatrix) sqmat.add(sqmat.hermitianAdjoint()); // make hermitian
        eval = new double[N];
        evec = new ComplexVector[N];

        try {
            eval = LinearMath.eigenSolveHermitian(sqmat, evec);
        } catch (MaximumIterationsExceededException e) {
            fail(e.toString());
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testEigenvectors() {
        for (int i = 0; i < N; i++)
            assertEquals(sqmat.multiply(evec[i]),
                evec[i].scalarMultiply(eval[i]));
    }

    /**
     * DOCUMENT ME!
     */
    public void testTrace() {
        double tr = 0.0;

        for (int i = 0; i < N; i++)
            tr += eval[i];

        assertEquals(sqmat.trace(), new Complex(tr, 0.0));
    }

    /**
     * DOCUMENT ME!
     */
    public void testDet() {
        double det = 1.0;

        for (int i = 0; i < N; i++)
            det *= eval[i];

        assertEquals(sqmat.det(), new Complex(det, 0.0));
    }
}
