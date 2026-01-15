package org.jscience.tests.mathematics.analysis.functions.vectorial;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class VectorialValuedPairTest extends TestCase {
/**
     * Creates a new VectorialValuedPairTest object.
     *
     * @param name DOCUMENT ME!
     */
    public VectorialValuedPairTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void testConstructor() {
        double[] tab = new double[2];
        tab[0] = -8.4;
        tab[1] = -3.2;

        VectorialValuedPair pair = new VectorialValuedPair(1.2, tab);
        assertTrue(Math.abs(pair.getX() - 1.2) < 1.0e-10);
        assertTrue(Math.abs(pair.getY()[0] + 8.4) < 1.0e-10);
        assertTrue(Math.abs(pair.getY()[1] + 3.2) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     */
    public void testCopyConstructor() {
        double[] tab = new double[2];
        tab[0] = -8.4;
        tab[1] = -3.2;

        VectorialValuedPair pair1 = new VectorialValuedPair(1.2, tab);
        VectorialValuedPair pair2 = new VectorialValuedPair(pair1);
        assertTrue(Math.abs(pair2.getX() - pair1.getX()) < 1.0e-10);
        assertTrue(Math.abs(pair2.getY()[0] - pair1.getY()[0]) < 1.0e-10);
        assertTrue(Math.abs(pair2.getY()[1] - pair1.getY()[1]) < 1.0e-10);
        assertTrue(Math.abs(pair2.getX() - 1.2) < 1.0e-10);
        assertTrue(Math.abs(pair2.getY()[0] + 8.4) < 1.0e-10);
        assertTrue(Math.abs(pair2.getY()[1] + 3.2) < 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(VectorialValuedPairTest.class);
    }
}
