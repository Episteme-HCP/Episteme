package org.jscience.tests.mathematics.analysis.quadrature.vectorial;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class AllTests {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                "org.jscience.tests.mathematics.analysis.quadrature.vectorial");

        suite.addTest(GaussLegendreIntegratorTest.suite());

        return suite;
    }
}
