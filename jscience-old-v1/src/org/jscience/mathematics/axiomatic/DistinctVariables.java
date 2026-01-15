package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DistinctVariables {
    /** DOCUMENT ME! */
    private Set distinct = new HashSet();

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public void add(int a, int b) {
        this.distinct.add(new VariablePair(a, b));
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void increment(int offset) {
        Iterator i = this.distinct.iterator();

        while (i.hasNext()) {
            VariablePair pair = (VariablePair) i.next();
            pair.increment(offset);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void decrement(int offset) {
        Iterator i = this.distinct.iterator();

        while (i.hasNext()) {
            VariablePair pair = (VariablePair) i.next();
            pair.decrement(offset);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     * @param sub DOCUMENT ME!
     */
    public void recordSubstitution(int v, WFF sub) {
    }
}
