package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public final class WFF {
    /** DOCUMENT ME! */
    public static int BANDWIDTH = 32;

    /** DOCUMENT ME! */
    private static final byte[] SUBSTITUTION_BUFFER = new byte[65536];

    /** DOCUMENT ME! */
    private static final byte[] EMPTY = new byte[0];

    /** DOCUMENT ME! */
    public static final byte IMPLIES = 0;

    /** DOCUMENT ME! */
    public static final byte NOT = 1;

    /** DOCUMENT ME! */
    public static final byte UNIVERSAL = 2;

    /** DOCUMENT ME! */
    public static final byte BI = 3;

    /** DOCUMENT ME! */
    public static final byte OR = 4;

    /** DOCUMENT ME! */
    public static final byte AND = 5;

    /** DOCUMENT ME! */
    public static final byte OR3 = 6;

    /** DOCUMENT ME! */
    public static final byte AND3 = 7;

    /** DOCUMENT ME! */
    public static final byte EQUALS = 8;

    /** DOCUMENT ME! */
    public static final byte ELEMENT_OF = 9;

    /** DOCUMENT ME! */
    public static final byte EXISTS = 10;

    /** DOCUMENT ME! */
    public static final byte SUBSTITUTION = 11;

    /** DOCUMENT ME! */
    public static final byte A = 12;

    /** DOCUMENT ME! */
    private WFF hypothesis;

    /** DOCUMENT ME! */
    private DistinctVariables distinct;

    /** DOCUMENT ME! */
    private byte[] data;

    /** DOCUMENT ME! */
    private int offset;

    /** DOCUMENT ME! */
    private int length;

    /** DOCUMENT ME! */
    private int end;

    /** DOCUMENT ME! */
    private int hash = 0;

/**
     * Creates a new WFF object.
     *
     * @param wff DOCUMENT ME!
     */
    public WFF(String wff) {
        int start = wff.lastIndexOf(':');

        if (start > 0) {
            this.hypothesis = new WFF(wff.substring(0, start));
        }

        start++;

        int wffLength = wff.length();
        byte[] newData = new byte[wffLength - start];
        int idx = 0;

        for (int i = start; i < wffLength; i++, idx++) {
            char c = wff.charAt(i);

            if (c == '>') {
                newData[idx] = IMPLIES;
            } else if (c == '~') {
                newData[idx] = NOT;
            } else if (c == 'A') {
                newData[idx] = UNIVERSAL;
            } else if (c == '|') {
                newData[idx] = OR;
            } else if (c == '&') {
                newData[idx] = AND;
            } else if (c == '!') {
                newData[idx] = OR3;
            } else if (c == '%') {
                newData[idx] = AND3;
            } else if (c == '<') {
                newData[idx] = BI;
            } else if (c == '=') {
                newData[idx] = EQUALS;
            } else if (c == '@') {
                newData[idx] = ELEMENT_OF;
            } else if (c == 'E') {
                newData[idx] = EXISTS;
            } else if (c == '[') {
                newData[idx] = SUBSTITUTION;
            } else {
                newData[idx] = (byte) (c - ('a' - A));
            }
        }

        this.data = newData;
        this.offset = 0;
        this.end = this.length = wffLength - start;
    }

    /*
     * Copy constructor
     */
    public WFF(WFF original) {
        int originalLength = original.length;
        this.hypothesis = (original.hypothesis == null) ? null
                                                        : new WFF(original.hypothesis);
        this.data = new byte[originalLength];
        System.arraycopy(original.data, original.offset, this.data, 0,
            originalLength);
        this.offset = 0;
        this.end = this.length = original.length;
    }

    /*
     * Implication constructor
     */
    public WFF(WFF left, WFF right) {
        int leftLength = left.length;
        int rightLength = right.length;
        int originalLength = leftLength + rightLength + 1;
        this.data = new byte[originalLength];
        this.data[0] = IMPLIES;
        System.arraycopy(left.data, left.offset, this.data, 1, leftLength);
        System.arraycopy(right.data, right.offset, this.data, 1 + leftLength,
            rightLength);
        this.offset = 0;
        this.end = this.length = originalLength;
    }

/**
     * Creates a new WFF object.
     *
     * @param hypothesis DOCUMENT ME!
     * @param data       DOCUMENT ME!
     * @param offset     DOCUMENT ME!
     * @param length     DOCUMENT ME!
     */
    private WFF(WFF hypothesis, byte[] data, int offset, int length) {
        this.hypothesis = hypothesis;
        this.data = data;
        this.offset = offset;
        this.length = length;
        this.end = offset + length;
    }

/**
     * Creates a new WFF object.
     *
     * @param in DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public WFF(DataInput in) throws IOException {
        int lengthIn = in.readUnsignedByte();

        if (lengthIn == 255) {
            lengthIn = in.readInt();
        }

        this.length = lengthIn;
        this.end = lengthIn;
        this.offset = 0;

        byte[] dataIn = new byte[lengthIn];
        in.readFully(dataIn);
        this.data = dataIn;
        this.hash = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param other DOCUMENT ME!
     */
    public void mergeHypotheses(WFF other) {
        if (other.hypothesis != null) {
            if (this.hypothesis != null) {
                this.hypothesis.mergeHypotheses(other);
            } else {
                this.hypothesis = other.hypothesis;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return this.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFF[] split() {
        if (this.data[offset] != IMPLIES) {
            return null;
        }

        int depth = 1;
        int split;
        int start = this.offset + 1;
        int max = this.offset + this.length;

        for (split = start; split < max; split++) {
            byte ch = this.data[split];

            if ((ch == IMPLIES) || (ch == UNIVERSAL) || (ch == BI) ||
                    (ch == AND) || (ch == OR) || (ch == EQUALS) ||
                    (ch == ELEMENT_OF) || (ch == EXISTS)) {
                depth++;
            } else if ((ch == AND3) || (ch == OR3) || (ch == SUBSTITUTION)) {
                depth += 2;
            } else if (ch != NOT) {
                depth--;
            }

            if (depth == 0) {
                split++;

                break;
            }
        }

        return new WFF[] {
            new WFF(this.hypothesis, this.data, start, split - start),
            new WFF(this.hypothesis, this.data, split, this.length - split)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte getMaxVariable() {
        byte maxVar = (this.hypothesis == null) ? 0
                                                : this.hypothesis.getMaxVariable();
        int theEnd = this.end;
        byte[] dat = this.data;

        for (int i = this.offset; i < theEnd; i++) {
            byte ch = dat[i];

            if ((ch >= A) && (ch > maxVar)) {
                maxVar = ch;
            }
        }

        return (byte) (maxVar - WFF.A);
    }

    /**
     * DOCUMENT ME!
     *
     * @param off DOCUMENT ME!
     */
    public void increaseVariables(byte off) {
        if (this.hypothesis != null) {
            this.hypothesis.increaseVariables(off);
        }

        off++;

        int theEnd = this.end;
        byte[] dat = this.data;

        for (int i = this.offset; i < theEnd; i++) {
            byte ch = dat[i];

            if (ch >= A) {
                dat[i] += off;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param off DOCUMENT ME!
     */
    public void decreaseVariables(byte off) {
        if (this.hypothesis != null) {
            this.hypothesis.decreaseVariables(off);
        }

        off++;

        int theEnd = this.end;
        byte[] dat = this.data;

        for (int i = this.offset; i < theEnd; i++) {
            byte ch = dat[i];

            if (ch >= A) {
                dat[i] -= off;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     * @param sub DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean substitute(byte m, WFF sub) {
        if (this.hypothesis != null) {
            this.hypothesis.substitute(m, sub);
        }

        byte[] buffer = SUBSTITUTION_BUFFER;
        byte[] dat = this.data;
        byte[] subDat = sub.data;
        int theEnd = this.end;
        int subStart = sub.offset;
        int subEnd = sub.end;
        int idx = 0;

        for (int i = this.offset; i < theEnd; i++) {
            byte c = dat[i];

            if (c == m) {
                for (int j = subStart; j < subEnd; j++) {
                    buffer[idx++] = subDat[j];

                    if (idx == BANDWIDTH) {
                        return false;
                    }
                }
            } else {
                buffer[idx++] = c;

                if (idx == BANDWIDTH) {
                    return false;
                }
            }
        }

        this.data = new byte[idx];
        this.offset = 0;
        this.end = this.length = idx;
        System.arraycopy(buffer, 0, this.data, 0, idx);

        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void tighten() {
        if (this.hypothesis != null) {
            this.hypothesis.tighten();
        }

        int len = this.length;
        byte[] dat = this.data;

        if (len < dat.length) {
            byte[] tightDat = new byte[len];
            System.arraycopy(dat, this.offset, tightDat, 0, len);
            this.data = tightDat;
            this.offset = 0;
            this.end = this.length = len;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param start DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WFF getSubWFF(int start) {
        start += this.offset;

        int theEnd = this.end;
        byte[] dat = this.data;
        int index;
        int depth = 1;

        for (index = start; index < theEnd; index++) {
            byte ch = dat[index];

            if ((ch == IMPLIES) || (ch == UNIVERSAL) || (ch == BI) ||
                    (ch == AND) || (ch == OR) || (ch == EQUALS) ||
                    (ch == ELEMENT_OF) || (ch == EXISTS)) {
                depth++;
            } else if ((ch == AND3) || (ch == OR3) || (ch == SUBSTITUTION)) {
                depth += 2;
            } else if (ch != NOT) {
                depth--;
            }

            if (depth == 0) {
                index++;

                break;
            }
        }

        return new WFF(this.hypothesis, dat, start, index - start);
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(int m) {
        if ((this.hypothesis != null) && this.hypothesis.contains(m)) {
            return true;
        }

        int theEnd = this.end;
        byte[] dat = this.data;

        for (int i = this.offset; i < theEnd; i++) {
            if (dat[i] == m) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte getToken(int index) {
        return this.data[this.offset + index];
    }

    /**
     * DOCUMENT ME!
     */
    public void normalize() {
        byte replacement = -A;
        WFF h = this.hypothesis;
        byte[] hyp;
        int hypOff;
        int hypEnd;

        if (h == null) {
            hyp = EMPTY;
            hypOff = 0;
            hypEnd = 0;
        } else {
            hyp = h.data;
            hypOff = h.offset;
            hypEnd = h.end;
        }

        byte[] dat = this.data;
        int theEnd = this.end;

        for (int i = hypOff; i < hypEnd; i++) {
            if (hyp[i] >= A) {
                byte replaced = hyp[i];

                for (int j = i; j < hypEnd; j++) {
                    if (hyp[j] == replaced) {
                        hyp[j] = replacement;
                    }
                }

                for (int j = i; j < theEnd; j++) {
                    if (dat[j] == replaced) {
                        dat[j] = replacement;
                    }
                }

                replacement--;
            }
        }

        for (int i = this.offset; i < theEnd; i++) {
            if (dat[i] >= A) {
                byte replaced = dat[i];

                for (int j = i; j < hypEnd; j++) {
                    if (hyp[j] == replaced) {
                        hyp[j] = replacement;
                    }
                }

                for (int j = i; j < theEnd; j++) {
                    if (dat[j] == replaced) {
                        dat[j] = replacement;
                    }
                }

                replacement--;
            }
        }

        for (int i = hypOff; i < hypEnd; i++) {
            if (hyp[i] < 0) {
                hyp[i] = (byte) -hyp[i];
            }
        }

        for (int i = this.offset; i < theEnd; i++) {
            if (dat[i] < 0) {
                dat[i] = (byte) -dat[i];
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param buf DOCUMENT ME!
     */
    public void spew(StringBuffer buf) {
        if (this.hypothesis != null) {
            this.hypothesis.spew(buf);
        }

        buf.append(':');

        byte[] thisData = this.data;
        int thisEnd = this.end;

        for (int i = this.offset; i < thisEnd; i++) {
            int c = thisData[i];

            if (c == IMPLIES) {
                buf.append('>');
            } else if (c == NOT) {
                buf.append('~');
            } else if (c == UNIVERSAL) {
                buf.append('A');
            } else if (c == OR) {
                buf.append('|');
            } else if (c == AND) {
                buf.append('&');
            } else if (c == OR3) {
                buf.append('!');
            } else if (c == AND3) {
                buf.append('%');
            } else if (c == BI) {
                buf.append('<');
            } else if (c == EQUALS) {
                buf.append('=');
            } else if (c == ELEMENT_OF) {
                buf.append('@');
            } else if (c == EXISTS) {
                buf.append('E');
            } else if (c == SUBSTITUTION) {
                buf.append('[');
            } else {
                /*
                 * This will look funny for expressions with more than 26
                 * variables.
                 */
                buf.append((char) (('a' - A) + c));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param v DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVariableCount(int v) {
        v += A;

        int count = 0;
        byte[] thisData = this.data;
        int thisEnd = this.end;

        for (int i = this.offset; i < thisEnd; i++) {
            if (thisData[i] == v) {
                count++;
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNotCount() {
        int count = 0;
        byte[] thisData = this.data;
        int thisEnd = this.end;

        for (int i = this.offset; i < thisEnd; i++) {
            if (thisData[i] == NOT) {
                count++;
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getImplicationCount() {
        int count = 0;
        byte[] thisData = this.data;
        int thisEnd = this.end;

        for (int i = this.offset; i < thisEnd; i++) {
            if (thisData[i] == IMPLIES) {
                count++;
            }
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNotStreamCount() {
        byte[] thisData = this.data;
        int thisEnd = this.end;
        int maxStream = 0;
        int stream = 0;

        for (int i = this.offset; i < thisEnd; i++) {
            byte b = thisData[i];

            if (b == NOT) {
                stream++;
            } else {
                if (stream > maxStream) {
                    maxStream = stream;
                }

                stream = 0;
            }
        }

        return maxStream;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getImplicationStreamCount() {
        byte[] thisData = this.data;
        int thisEnd = this.end;
        int maxStream = 0;
        int stream = 0;

        for (int i = this.offset; i < thisEnd; i++) {
            byte b = thisData[i];

            if (b == IMPLIES) {
                stream++;
            } else {
                if (stream > maxStream) {
                    maxStream = stream;
                }

                stream = 0;
            }
        }

        return maxStream;
    }

    /**
     * DOCUMENT ME!
     */
    public void generalize() {
        this.increaseVariables((byte) 0);

        int thisLength = this.length;
        byte[] result = new byte[thisLength + 2];
        result[0] = UNIVERSAL;
        result[1] = A;
        System.arraycopy(this.data, this.offset, result, 2, thisLength);
        this.data = result;
        this.offset = 0;
        this.length += 2;
        this.end = this.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buf = new StringBuffer(50);
        this.spew(buf);

        return buf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        if (this.hash == 0) {
            //        	this.hash = new String(this.data).hashCode();
            int h = (this.hypothesis == null) ? 0 : this.hypothesis.hashCode();
            int theEnd = this.end;
            byte[] dat = this.data;

            for (int i = this.offset; i < theEnd; i++) {
                byte c = dat[i];
                h ^= c;
                h = (h << 5) | (h >>> 27);
            }

            this.hash = h;
        }

        return this.hash;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof WFF) {
            WFF other = (WFF) obj;

            if (this.hypothesis == null) {
                if (other.hypothesis != null) {
                    return false;
                }
            } else {
                if ((other.hypothesis == null) ||
                        !this.hypothesis.equals(other.hypothesis)) {
                    return false;
                }
            }

            if (this.length != other.length) {
                return false;
            }

            int thisEnd = this.end;
            byte[] thisData = this.data;
            byte[] otherData = other.data;

            for (int i = this.offset, j = other.offset; i < thisEnd;
                    i++, j++) {
                if (thisData[i] != otherData[j]) {
                    return false;
                }
            }

            return true;
        } else if (obj instanceof String) {
            return this.toString().equals(obj);
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEquivalence() {
        if (this.offset < this.data.length) {
            return this.data[this.offset] == BI;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDoubleNegation() {
        return (this.length >= 2) && (this.data[this.offset] == NOT) &&
        (this.data[this.offset + 1] == NOT);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isImplication() {
        return this.data[this.offset] == IMPLIES;
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void store(DataOutput out) throws IOException {
        if (this.length >= 255) {
            out.writeByte(255);
            out.writeInt(this.length);
        } else {
            out.writeByte(this.length);
        }

        out.write(data, this.offset, this.length);
    }
}
