package org.episteme.tests.mathematics.analysis;

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
                "org.episteme.tests.mathematics.analysis");

        suite.addTest(org.episteme.tests.mathematics.analysis.linalg.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.estimation.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.functions.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.roots.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.fitting.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.ode.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.quadrature.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.utilities.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.geometry.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.algebra.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.random.AllTests.suite());
        suite.addTest(org.episteme.tests.mathematics.analysis.optimization.AllTests.suite());

        return suite;
    }
}
