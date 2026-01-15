package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProofTree {
    /** DOCUMENT ME! */
    private Node root;

/**
     * Creates a new ProofTree object.
     *
     * @param proof DOCUMENT ME!
     */
    public ProofTree(String proof) {
        Stack s = new Stack();

        for (int i = proof.length() - 1; i >= 0; i--) {
            char c = proof.charAt(i);

            if ((c == 'D') || (c == 'S')) {
                s.push(new BranchNode(c, (Node) s.pop(), (Node) s.pop()));
            } else {
                s.push(new LeafNode(c));
            }
        }

        this.root = (Node) s.pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @param freqs DOCUMENT ME!
     */
    public void scan(LexTable freqs) {
        this.root.scan(freqs);
    }

    /**
     * DOCUMENT ME!
     *
     * @param buf DOCUMENT ME!
     */
    public void spew(StringBuffer buf) {
        this.root.spew(buf);
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
