package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.io.DataInputStream;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProofIterator {
    /** DOCUMENT ME! */
    private DataInputStream in;

/**
     * Creates a new ProofIterator object.
     *
     * @param in DOCUMENT ME!
     */
    public ProofIterator(DataInputStream in) {
        this.in = in;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public boolean hasNext() throws IOException {
        in.mark(1);

        if (in.read() == -1) {
            in.close();

            return false;
        }

        in.reset();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public Proof next() throws IOException {
        WFF result = new WFF(in);
        int proofLength = in.readInt();
        byte[] proofStepsBuffer = new byte[proofLength];
        in.readFully(proofStepsBuffer);

        return new Proof(result, proofStepsBuffer);
    }
}
