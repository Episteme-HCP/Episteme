package org.jscience.mathematics.axiomatic;


//author: Greg Bush
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class StaticStringBuffer {
    /** DOCUMENT ME! */
    private char[] data;

    /** DOCUMENT ME! */
    private int cursor = 0;

/**
     * Creates a new StaticStringBuffer object.
     *
     * @param initSize DOCUMENT ME!
     */
    public StaticStringBuffer(int initSize) {
        this.data = new char[initSize];
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void append(char c) {
        if (cursor >= this.data.length) {
            char[] newData = new char[this.data.length * 2];
            System.arraycopy(this.data, 0, newData, 0, this.data.length);
            this.data = newData;
        }

        this.data[cursor++] = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     */
    public void setLength(int len) {
        this.cursor = len;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new String(this.data, 0, cursor);
    }
}
