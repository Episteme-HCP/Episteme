package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class LeafNode implements Node {
    /** DOCUMENT ME! */
    private char axOrD;

/**
     * Creates a new LeafNode object.
     *
     * @param axOrD DOCUMENT ME!
     */
    public LeafNode(char axOrD) {
        this.axOrD = axOrD;
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public void scan(LexTable table) {
        String path = this.walk("");
        table.inc(path);
    }

    /**
     * DOCUMENT ME!
     *
     * @param history DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String walk(String history) {
        return history + this.axOrD;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buf DOCUMENT ME!
     */
    public void spew(StringBuffer buf) {
        buf.append(this.axOrD);
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
    }
}
