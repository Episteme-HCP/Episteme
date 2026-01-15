package org.jscience.tests.mathematics;

import junit.framework.Test;
import junit.framework.TestSuite;

import java.lang.reflect.Constructor;


/**
 * Testcase for matrices.
 *
 * @author Mark Hale
 */
public class MatrixTest extends junitx.extensions.EqualsHashCodeTestCase {
    /** DOCUMENT ME! */
    private static final Class[] classes = new Class[] {
            DoubleMatrix.class, DoubleTridiagonalMatrix.class,
            DoubleDiagonalMatrix.class, DoubleSparseMatrix.class
        };

    /** DOCUMENT ME! */
    private static int classesIndex;

    /** DOCUMENT ME! */
    private final Class matrixClass;

    /** DOCUMENT ME! */
    private final int N = 5;

    /** DOCUMENT ME! */
    private final int M = 5;

    /** DOCUMENT ME! */
    private Constructor constructor;

    /** DOCUMENT ME! */
    private double[][] array;

    /** DOCUMENT ME! */
    private double[][] array2;

/**
     * Creates a new MatrixTest object.
     *
     * @param name DOCUMENT ME!
     */
    public MatrixTest(String name) {
        super(name);
        matrixClass = classes[classesIndex];
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
        TestSuite suite = new TestSuite(MatrixTest.class.toString());

        for (classesIndex = 0; classesIndex < classes.length; classesIndex++)
            suite.addTest(new TestSuite(MatrixTest.class,
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
        array = new double[N][M];
        array2 = new double[N][M];
        constructor = matrixClass.getConstructor(new Class[] { double[][].class });

        if (DoubleDiagonalMatrix.class.isAssignableFrom(matrixClass)) {
            setUpDiagonal();
        } else if (DoubleTridiagonalMatrix.class.isAssignableFrom(matrixClass)) {
            setUpTridiagonal();
        } else {
            setUpRectangular();
        }

        super.setUp();
    }

    /**
     * DOCUMENT ME!
     */
    private void setUpRectangular() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                array[i][j] = Math.random();
                array2[i][j] = Math.random();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setUpTridiagonal() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if ((j >= (i - 1)) && (j <= (i + 1))) {
                    array[i][j] = Math.random();
                    array2[i][j] = Math.random();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void setUpDiagonal() {
        for (int i = 0; i < N; i++) {
            array[i][i] = Math.random();
            array2[i][i] = Math.random();
        }
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
            throw new RuntimeException(e.getMessage());
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
            throw new RuntimeException(e);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testConstructor() {
        DoubleMatrix mat = (DoubleMatrix) createInstance();
        assertEquals(N, mat.rows());
        assertEquals(M, mat.columns());

        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.columns(); j++)
                assertEquals(array[i][j], mat.getElement(i, j),
                    org.jscience.GlobalSettings.ZERO_TOL);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testSetGet() {
        DoubleMatrix mat = (DoubleMatrix) createInstance();

        for (int i = 0; i < mat.rows(); i++) {
            for (int j = 0; j < mat.columns(); j++) {
                try {
                    mat.setElement(i, j, -1.0);
                    assertEquals(-1.0, mat.getElement(i, j),
                        org.jscience.GlobalSettings.ZERO_TOL);
                    mat.setElement(i, j, array[i][j]);
                } catch (IllegalDimensionException e) {
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testAdd() {
        DoubleMatrix mat = (DoubleMatrix) createInstance();
        DoubleMatrix ans = mat.add(mat);

        for (int i = 0; i < ans.rows(); i++) {
            for (int j = 0; j < ans.columns(); j++)
                assertEquals(array[i][j] + array[i][j], ans.getElement(i, j),
                    org.jscience.GlobalSettings.ZERO_TOL);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testSubtract() {
        DoubleMatrix mat = (DoubleMatrix) createInstance();
        DoubleMatrix ans = mat.subtract(mat);

        for (int i = 0; i < ans.rows(); i++) {
            for (int j = 0; j < ans.columns(); j++)
                assertEquals(array[i][j] - array[i][j], ans.getElement(i, j),
                    org.jscience.GlobalSettings.ZERO_TOL);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void testMultiply() {
        DoubleMatrix mat = (DoubleMatrix) createInstance();
        DoubleMatrix ans = (DoubleMatrix) mat.multiply(mat.transpose());

        for (int i = 0; i < ans.rows(); i++) {
            for (int j = 0; j < ans.columns(); j++) {
                double sum = 0.0;

                for (int k = 0; k < mat.columns(); k++)
                    sum += (array[i][k] * array[j][k]);

                assertEquals(sum, ans.getElement(i, j),
                    org.jscience.GlobalSettings.ZERO_TOL);
            }
        }
    }
}
