package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class VariablePair {
    /** DOCUMENT ME! */
    private int a;

    /** DOCUMENT ME! */
    private int b;

/**
     * Creates a new VariablePair object.
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public VariablePair(int a, int b) {
        if (a < b) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getComplement(int x) {
        if (this.a == x) {
            return this.b;
        }

        if (this.b == x) {
            return this.a;
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void increment(int offset) {
        offset++;
        this.a += offset;
        this.b += offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param offset DOCUMENT ME!
     */
    public void decrement(int offset) {
        offset++;
        this.a -= offset;
        this.b -= offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        VariablePair other = (VariablePair) obj;

        return (this.a == other.a) && (this.b == other.b);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return (this.a << 16) ^ this.b;
    }
}
