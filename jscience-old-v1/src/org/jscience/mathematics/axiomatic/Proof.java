package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class Proof {
    /** DOCUMENT ME! */
    private WFF result;

    /** DOCUMENT ME! */
    private byte[] steps;

/**
     * Creates a new Proof object.
     *
     * @param result DOCUMENT ME!
     * @param steps  DOCUMENT ME!
     */
    public Proof(WFF result, byte[] steps) {
        this.result = result;
        this.steps = steps;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFF getResult() {
        return this.result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getSteps() {
        return this.steps;
    }
}
