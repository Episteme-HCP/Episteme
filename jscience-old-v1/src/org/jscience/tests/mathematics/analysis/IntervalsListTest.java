package org.jscience.tests.mathematics.analysis.utilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class IntervalsListTest extends TestCase {
/**
     * Creates a new IntervalsListTest object.
     *
     * @param name DOCUMENT ME!
     */
    public IntervalsListTest(String name) {
        super(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void test1() {
        IntervalsList list1 = new IntervalsList(-2.0, -1.0);
        IntervalsList list2 = new IntervalsList(new Interval(-0.9, -0.8));
        check(list1, list2, 2.5, true, false, 1, true, false, 1, false,
            new Interval[] { new Interval(-2.0, -1.0), new Interval(-0.9, -0.8) },
            new Interval[0]);

        list2.addToSelf(new Interval(1.0, 3.0));
        check(list1, list2, 2.5, true, false, 1, false, false, 2, false,
            new Interval[] {
                new Interval(-2.0, -1.0), new Interval(-0.9, -0.8),
                new Interval(1.0, 3.0)
            }, new Interval[0]);

        list1.addToSelf(new Interval(-1.2, 0.0));
        check(list1, list2, -1.1, true, false, 1, false, false, 2, true,
            new Interval[] { new Interval(-2.0, 0.0), new Interval(1.0, 3.0) },
            new Interval[] { new Interval(-0.9, -0.8) });

        IntervalsList list = new IntervalsList(new Interval(-10.0, -8.0));
        list.addToSelf(new Interval(-6.0, -4.0));
        list.addToSelf(new Interval(-0.85, 1.2));
        list1.addToSelf(list);
        check(list1, list2, 0, false, false, 3, false, false, 2, true,
            new Interval[] {
                new Interval(-10.0, -8.0), new Interval(-6.0, -4.0),
                new Interval(-2.0, 3.0)
            },
            new Interval[] { new Interval(-0.9, -0.8), new Interval(1.0, 1.2) });
    }

    /**
     * DOCUMENT ME!
     *
     * @param l1 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param b1 DOCUMENT ME!
     * @param b2 DOCUMENT ME!
     * @param i1 DOCUMENT ME!
     * @param b3 DOCUMENT ME!
     * @param b4 DOCUMENT ME!
     * @param i2 DOCUMENT ME!
     * @param b5 DOCUMENT ME!
     * @param add DOCUMENT ME!
     * @param inter DOCUMENT ME!
     */
    private void check(IntervalsList l1, IntervalsList l2, double x,
        boolean b1, boolean b2, int i1, boolean b3, boolean b4, int i2,
        boolean b5, Interval[] add, Interval[] inter) {
        assertTrue(l1.isConnex() ^ (!b1));
        assertTrue(l1.isEmpty() ^ (!b2));
        assertEquals(i1, l1.getIntervals().size());
        assertTrue(l2.isConnex() ^ (!b3));
        assertTrue(l2.isEmpty() ^ (!b4));
        assertEquals(i2, l2.getIntervals().size());
        assertTrue(l1.contains(x) ^ (!b5));
        checkEquals(add, IntervalsList.add(l1, l2));
        checkEquals(inter, IntervalsList.intersection(l1, l2));
    }

    /**
     * DOCUMENT ME!
     *
     * @param sa DOCUMENT ME!
     * @param sb DOCUMENT ME!
     */
    private void checkEquals(Interval[] sa, IntervalsList sb) {
        assertEquals(sa.length, sb.getIntervals().size());

        Iterator iterB = sb.getIntervals().iterator();

        for (int i = 0; i < sa.length; ++i) {
            Interval ib = (Interval) iterB.next();
            assertEquals(sa[i].getInf(), ib.getInf(), 1.0e-10);
            assertEquals(sa[i].getSup(), ib.getSup(), 1.0e-10);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Test suite() {
        return new TestSuite(IntervalsListTest.class);
    }
}
