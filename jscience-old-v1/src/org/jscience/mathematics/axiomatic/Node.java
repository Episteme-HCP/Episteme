package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public interface Node {
    /**
     * DOCUMENT ME!
     *
     * @param history DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String walk(String history);

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public void scan(LexTable table);

    /**
     * DOCUMENT ME!
     *
     * @param buf DOCUMENT ME!
     */
    public void spew(StringBuffer buf);

    /**
     * DOCUMENT ME!
     */
    public void reset();
}
