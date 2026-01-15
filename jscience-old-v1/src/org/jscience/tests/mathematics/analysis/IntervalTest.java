package org.jscience.tests.mathematics.analysis.utilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntervalTest extends TestCase {
/**
     * Creates a new IntervalTest object.
     *
     * @param name DOCUMENT ME!
     */
    public IntervalTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void test1() {
        check(new Interval(-10.0, 10.0), new Interval(11.0, 12.0), 2.5, true,
            false, false, new Interval(-10.0, 12.0), new Interval(11.0, 11.0));
    }

    /**
     * DOCUMENT ME!
     */
    public void test2() {
        check(new Interval(-10.0, 10.0), new Interval(9.0, 12.0), 50.0, false,
            false, true, new Interval(-10.0, 12.0), new Interval(9.0, 10.0));
    }

    /**
     * DOCUMENT ME!
     */
    public void test3() {
        check(new Interval(-10.0, 10.0), new Interval(-12.0, -11.0), 0.0, true,
            false, false, new Interval(-12.0, 10.0), new Interval(-10.0, -10.0));
    }

    /**
     * DOCUMENT ME!
     */
    public void test4() {
        check(new Interval(-10.0, 10.0), new Interval(-4.0, 5.0), 0.0, true,
            true, true, new Interval(-10.0, 10.0), new Interval(-4.0, 5.0));
    }

    /**
     * DOCUMENT ME!
     */
    public void test5() {
        check(new Interval(-10.0, 10.0), new Interval(-10.0, 10.0), 0.0, true,
            true, true, new Interval(-10.0, 10.0), new Interval(-10.0, 10.0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param i1 DOCUMENT ME!
     * @param i2 DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param b1 DOCUMENT ME!
     * @param b2 DOCUMENT ME!
     * @param b3 DOCUMENT ME!
     * @param add DOCUMENT ME!
     * @param inter DOCUMENT ME!
     */
    private void check(Interval i1, Interval i2, double x, boolean b1,
        boolean b2, boolean b3, Interval add, Interval inter) {
        assertTrue(i1.contains(x) ^ (!b1));
        assertTrue(i1.contains(i2) ^ (!b2));
        assertTrue(i1.intersects(i2) ^ (!b3));

        assertEquals(add.getInf(), Interval.add(i1, i2).getInf(), 1.0e-10);
        assertEquals(add.getSup(), Interval.add(i1, i2).getSup(), 1.0e-10);
        assertEquals(inter.getInf(), Interval.intersection(i1, i2).getInf(),
            1.0e-10);
        assertEquals(inter.getSup(), Interval.intersection(i1, i2).getSup(),
            1.0e-10);

        Interval ia = new Interval(i1);
        ia.addToSelf(i2);
        assertEquals(add.getInf(), ia.getInf(), 1.0e-10);
        assertEquals(add.getSup(), ia.getSup(), 1.0e-10);

        Interval ib = new Interval(i1);
        ib.intersectSelf(i2);
        assertEquals(inter.getInf(), ib.getInf(), 1.0e-10);
        assertEquals(inter.getSup(), ib.getSup(), 1.0e-10);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(IntervalTest.class);
    }
}
