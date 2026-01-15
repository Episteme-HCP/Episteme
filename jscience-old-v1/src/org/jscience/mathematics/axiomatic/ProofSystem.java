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
