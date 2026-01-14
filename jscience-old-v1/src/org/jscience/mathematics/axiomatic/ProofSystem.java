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
import java.util.Iterator;
import java.util.Map.Entry;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProofSystem {
    /** DOCUMENT ME! */
    private ProofTree[] proofs;

/**
     * Creates a new ProofSystem object.
     *
     * @param rawProofs DOCUMENT ME!
     */
    public ProofSystem(String[] rawProofs) {
        this.proofs = new ProofTree[rawProofs.length];

        for (int i = 0; i < rawProofs.length; i++) {
            this.proofs[i] = new ProofTree(rawProofs[i]);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void analyze() {
        String[] things = new String[10000];
        LexTable freqs = new LexTable();

        for (int i = 0; i < this.proofs.length; i++) {
            this.proofs[i].scan(freqs);
        }

        Iterator iter = freqs.iterator();

        while (iter.hasNext()) {
            Entry e = (Entry) iter.next();
            int weight = ((Integer) e.getValue()).intValue();
            int length = 0;
            String proof = (String) e.getKey();

            for (int i = 0; i < proof.length(); i++) {
                if (proof.charAt(i) != 'x') {
                    length++;
                }
            }

            weight *= length;
            things[weight] = proof;
        }

        for (int i = things.length - 1; i >= 0; i--) {
            if (things[i] != null) {
                System.out.println(i + ":" + things[i]);
            }
        }
    }
}
