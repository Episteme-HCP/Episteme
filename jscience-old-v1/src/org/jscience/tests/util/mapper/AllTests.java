package org.jscience.tests.mathematics.analysis.utilities;

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
                "org.jscience.tests.mathematics.analysis.utilities");

        suite.addTest(ArrayMapperTest.suite());
        suite.addTest(MappableArrayTest.suite());
        suite.addTest(MappableScalarTest.suite());
        suite.addTest(IntervalTest.suite());
        suite.addTest(IntervalsListTest.suite());

        return suite;
    }
}
