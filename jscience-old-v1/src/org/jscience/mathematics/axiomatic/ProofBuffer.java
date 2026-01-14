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
