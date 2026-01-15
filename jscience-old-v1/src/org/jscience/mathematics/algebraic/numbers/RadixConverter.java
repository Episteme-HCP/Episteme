/*************************************************************************
 *  Compilation:  javac Converter.java
 *  Execution:    java Converter
 *
 *  Converts an integer to and from its base b representation.
 *  No error checking is currently implemented.
 *
 *************************************************************************/

//found at http://www.cs.princeton.edu/introcs/51data/Converter.java.html

//distributed with permission uinder GPL
package org.jscience.mathematics.algebraic.numbers;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RadixConverter {
    // converts integer n into a base b string
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param base DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String toString(int n, int base) {
        // special case
        if (n == 0) {
            return "0";
        }

        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String s = "";

        while (n > 0) {
            int d = n % base;
            s = digits.charAt(d) + s;
            n = n / base;
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String toBinaryString(int n) {
        return toString(n, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String toHexString(int n) {
        return toString(n, 16);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    static void inputError(String s) {
        throw new RuntimeException("Input error with" + s);
    }

    // convert a String representing a base b integer into an int
    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static int fromString(String s, int b) {
        int result = 0;
        int digit = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if ((c >= '0') && (c <= '9')) {
                digit = c - '0';
            } else if ((c >= 'A') && (c <= 'Z')) {
                digit = (10 + c) - 'A';
            } else {
                inputError(s);
            }

            if (digit < b) {
                result = (b * result) + digit;
            } else {
                inputError(s);
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static int fromBinaryString(String s) {
        return fromString(s, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static int fromHexString(String s) {
        return fromString(s, 16);
    }

    // sample test client
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        int n = 47;
        String s = "101111";
        System.out.println(n + ":  " + toBinaryString(n));
        System.out.println(s + ":  " + fromBinaryString(s));
        System.out.println(n + ":  " + toHexString(n));
        System.out.println();

        n = 2620;
        s = "0A3C";
        System.out.println(n + ":  " + toBinaryString(n));
        System.out.println(s + ":  " + fromHexString(s));
        System.out.println(n + ":  " + toHexString(n));
    }
}
