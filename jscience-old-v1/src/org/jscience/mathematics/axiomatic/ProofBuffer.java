package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.io.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProofBuffer {
    /** DOCUMENT ME! */
    private File file;

    /** DOCUMENT ME! */
    private DataOutputStream out;

    /** DOCUMENT ME! */
    private int writtenCount;

/**
     * Creates a new ProofBuffer object.
     *
     * @param forProofLength DOCUMENT ME!
     * @param append         DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public ProofBuffer(int forProofLength, boolean append)
        throws IOException {
        this.file = new File("buf-" + forProofLength + ".prf");
        this.out = new DataOutputStream(new BufferedOutputStream(
                    new FileOutputStream(file, append)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param proof DOCUMENT ME!
     * @param result DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void add(String proof, WFF result) throws IOException {
        result.store(out);
        out.writeInt(proof.length());
        out.write(proof.getBytes());
        this.writtenCount++;
    }

    /**
     * DOCUMENT ME!
     *
     * @param preStep DOCUMENT ME!
     * @param postSteps DOCUMENT ME!
     * @param result DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void add(byte preStep, byte[] postSteps, WFF result)
        throws IOException {
        result.store(out);
        out.writeInt(postSteps.length + 1);
        out.write(preStep);
        out.write(postSteps);
        this.writtenCount++;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public ProofIterator iterator() throws IOException {
        return new ProofIterator(new DataInputStream(
                new BufferedInputStream(new FileInputStream(this.file))));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWritten() {
        return this.writtenCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void flush() throws IOException {
        this.out.close();
    }
}
