/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
