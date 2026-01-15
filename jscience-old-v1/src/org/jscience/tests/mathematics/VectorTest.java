package org.jscience.tests.mathematics;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.lang.reflect.Constructor;


/**
 * Testcase for vectors.
 *
 * @author Mark Hale
 */
public class VectorTest extends junitx.extensions.EqualsHashCodeTestCase {
    /** DOCUMENT ME! */
    private static final Class[] classes = new Class[] {
            DoubleVector.class, DoubleSparseVector.class
        };

    /** DOCUMENT ME! */
    private static int classesIndex;

    /** DOCUMENT ME! */
    private final Class vectorClass;

    /** DOCUMENT ME! */
    private final int N = 5;

    /** DOCUMENT ME! */
    private Constructor constructor;

    /** DOCUMENT ME! */
    private double[] array;

    /** DOCUMENT ME! */
    private double[] array2;

/**
     * Creates a new VectorTest object.
     *
     * @param name DOCUMENT ME!
     */
    public VectorTest(String name) {
        super(name);
        vectorClass = classes[classesIndex];
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(VectorTest.class.toString());

        for (classesIndex = 0; classesIndex < classes.length; classesIndex++)
            suite.addTest(new TestSuite(VectorTest.class,
                    classes[classesIndex].toString()));

        return suite;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected void setUp() throws Exception {
        org.jscience.GlobalSettings.ZERO_TOL = 1.0e-6;
        array = new double[N];
        array2 = new double[N];
        constructor = vectorClass.getConstructor(new Class[] { double[].class });

        for (int i = 0; i < N; i++) {
            array[i] = Math.random();
            array2[i] = Math.random();
        }

        super.setUp();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    protected Object createInstance() {
        try {
            return constructor.newInstance(new Object[] { array });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    protected Object createNotEqualInstance() {
        try {
            return constructor.newInstance(new Object[] { array2 });
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testConstructor() {
        AbstractDoubleVector vec = (AbstractDoubleVector) createInstance();
        assertEquals(N, vec.dimension());

        for (int i = 0; i < vec.dimension(); i++)
            assertEquals(array[i], vec.getComponent(i),
                org.jscience.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSetGet() {
        AbstractDoubleVector vec = (AbstractDoubleVector) createInstance();

        for (int i = 0; i < vec.dimension(); i++) {
            vec.setComponent(i, -1.0);
            assertEquals(-1.0, vec.getComponent(i),
                org.jscience.GlobalSettings.ZERO_TOL);
            vec.setComponent(i, array[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testAdd() {
        AbstractDoubleVector vec = (AbstractDoubleVector) createInstance();
        AbstractDoubleVector ans = vec.add(vec);

        for (int i = 0; i < ans.dimension(); i++)
            assertEquals(array[i] + array[i], ans.getComponent(i),
                org.jscience.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testSubtract() {
        AbstractDoubleVector vec = (AbstractDoubleVector) createInstance();
        AbstractDoubleVector ans = vec.subtract(vec);

        for (int i = 0; i < ans.dimension(); i++)
            assertEquals(array[i] - array[i], ans.getComponent(i),
                org.jscience.GlobalSettings.ZERO_TOL);
    }

    /**
     * DOCUMENT ME!
     */
    public void testScalarProduct() {
        AbstractDoubleVector vec = (AbstractDoubleVector) createInstance();
        double ans = vec.scalarProduct(vec);
        double sp = ArrayMath.scalarProduct(array, array);
        assertEquals(sp, ans, org.jscience.GlobalSettings.ZERO_TOL);
    }
}
