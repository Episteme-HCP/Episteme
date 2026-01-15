package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class LexTable {
    /** DOCUMENT ME! */
    private Map table = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param sequence DOCUMENT ME!
     */
    public void inc(String sequence) {
        Integer count = (Integer) this.table.get(sequence);

        if (count == null) {
            count = new Integer(1);
        } else {
            count = new Integer(count.intValue() + 1);
        }

        this.table.put(sequence, count);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return this.table.entrySet().iterator();
    }
}
