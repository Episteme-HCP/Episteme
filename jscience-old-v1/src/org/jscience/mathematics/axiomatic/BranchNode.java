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
public class BranchNode implements Node {
    /** DOCUMENT ME! */
    private char type;

    /** DOCUMENT ME! */
    private Node left;

    /** DOCUMENT ME! */
    private Node right;

    /** DOCUMENT ME! */
    private int state = 0;

/**
     * Creates a new BranchNode object.
     *
     * @param type  DOCUMENT ME!
     * @param left  DOCUMENT ME!
     * @param right DOCUMENT ME!
     */
    public BranchNode(char type, Node left, Node right) {
        this.type = type;
        this.left = left;
        this.right = right;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        this.state = 0;
        this.left.reset();
        this.right.reset();
    }

    /**
     * DOCUMENT ME!
     *
     * @param history DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String walk(String history) {
        history += this.type;

        if (this.state == 0) {
            history += "xx";
            this.state = 1;
        } else if (this.state == 1) {
            history = this.left.walk(history);
            history += "x";
            this.state = 2;
        } else if (this.state == 2) {
            history += "x";
            history = this.right.walk(history);
            this.state = 3;
        } else if (this.state == 3) {
            history = this.left.walk(history);
            history = this.right.walk(history);
            this.state = 0;
        }

        return history;
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public void scan(LexTable table) {
        String full = this.toString();
        Set unique = new HashSet();

        for (;;) {
            String path = this.walk("");
            unique.add(path);

            if (path.equals(full) || (path.length() > 25)) {
                break;
            } else if (path.equals("DD2D1xx")) {
                System.out.print("syl");
            }
        }

        Iterator i = unique.iterator();

        while (i.hasNext()) {
            String path = (String) i.next();
            table.inc(path);
        }

        this.left.reset();
        this.left.scan(table);
        this.right.reset();
        this.right.scan(table);
    }

    /**
     * DOCUMENT ME!
     *
     * @param buf DOCUMENT ME!
     */
    public void spew(StringBuffer buf) {
        buf.append(this.type);
        this.left.spew(buf);
        this.right.spew(buf);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        this.spew(buf);

        return buf.toString();
    }
}
