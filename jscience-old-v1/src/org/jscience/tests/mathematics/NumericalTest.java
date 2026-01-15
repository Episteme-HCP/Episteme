package org.jscience.tests.mathematics;

/**
 * Testcase for numerical integration methods.
 *
 * @author Mark Hale
 */
public class NumericalTest extends junit.framework.TestCase {
    /** DOCUMENT ME! */
    private final Mapping testFunc = new Mapping() {
            public double map(double x) {
                return 1.0 / x;
            }
        };

    /** DOCUMENT ME! */
    private final double testFunc_a = 1.0;

    /** DOCUMENT ME! */
    private final double testFunc_b = Math.E;

    /** DOCUMENT ME! */
    private final double testFuncExpected = 1.0;

    /** DOCUMENT ME! */
    private final RealPolynomial testDeriv = new RealPolynomial(new double[] {
                0.0, 1.0
            });

    /** DOCUMENT ME! */
    private final double testDerivInitial = 1.0;

    /** DOCUMENT ME! */
    private final double testDerivExpected = Math.E;

/**
     * Creates a new NumericalTest object.
     *
     * @param name DOCUMENT ME!
     */
    public NumericalTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(NumericalTest.class);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setUp() {
        JSci.GlobalSettings.ZERO_TOL = 1.0e-6;
    }

    /**
     * DOCUMENT ME!
     */
    public void testTrapezium() {
        double ans = NumericalMath.trapezium(500, testFunc, testFunc_a,
                testFunc_b);
        assertEquals(testFuncExpected, ans, JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSimpson() {
        double ans = NumericalMath.simpson(20, testFunc, testFunc_a, testFunc_b);
        assertEquals(testFuncExpected, ans, JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testRichardson() {
        double ans = NumericalMath.richardson(5, testFunc, testFunc_a,
                testFunc_b);
        assertEquals(testFuncExpected, ans, JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testGaussian4() {
        double ans = NumericalMath.gaussian4(2, testFunc, testFunc_a, testFunc_b);
        assertEquals(testFuncExpected, ans, JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testGaussian8() {
        double ans = NumericalMath.gaussian8(1, testFunc, testFunc_a, testFunc_b);
        assertEquals(testFuncExpected, ans, JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSimpleRungeKutta2() {
        double[] y = new double[5001];
        y[0] = testDerivInitial;
        NumericalMath.rungeKutta2(y, testDeriv, 1.0 / (y.length - 1));
        assertEquals(testDerivExpected, y[y.length - 1],
            JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSimpleRungeKutta4() {
        double[] y = new double[51];
        y[0] = testDerivInitial;
        NumericalMath.rungeKutta4(y, testDeriv, 1.0 / (y.length - 1));
        assertEquals(testDerivExpected, y[y.length - 1],
            JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testRungeKutta2() {
        double[] y = new double[5001];
        y[0] = testDerivInitial;
        NumericalMath.rungeKutta2(y, testDeriv.tensor(testDeriv), 0.0,
            Math.sqrt(2.0) / (y.length - 1));
        assertEquals(testDerivExpected, y[y.length - 1],
            JSci.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testRungeKutta4() {
        double[] y = new double[51];
        y[0] = testDerivInitial;
        NumericalMath.rungeKutta4(y, testDeriv.tensor(testDeriv), 0.0,
            Math.sqrt(2.0) / (y.length - 1));
        assertEquals(testDerivExpected, y[y.length - 1],
            JSci.GlobalSettings.ZERO_TOL);
    }
}
