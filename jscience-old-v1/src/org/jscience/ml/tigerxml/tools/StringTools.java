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

package org.jscience.ml.tigerxml.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Provides methods related to Strings. This class is for static use.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84
 *          $Id: StringTools.java,v 1.2 2007-10-21 17:47:09 virtualcall Exp $
 */
public class StringTools {

    /**
     * Cleans an XML <tt>String</tt>  by translating special characters
     * into XML entity references. E.g. '&amp', '&lt', '&gt' and
     * '&quot;' by '&amp;amp;', '&amp;lt;', '&amp;gt;' and '&amp;quot;',
     * respectively, and all characters with ASCII code > 127
     * by the numeric representation.
     *
     * @param dirtyXMLString The XML <tt>String</tt> to be cleaned
     * @return The cleaned XML <tt>String</tt>
     */
    public final static String cleanXMLString(String dirtyXMLString) {

        StringBuffer cleanStrBuffer = new StringBuffer();

        // iterate over all chars in string, replace <, >, & or " if encountered
        // also take care of non <127 ASCII chars by replacing them with &#charNum;
        for (int i = 0; i < dirtyXMLString.length(); i++) {
            char c = dirtyXMLString.charAt(i);
            short s = (short) c;

            if (c == '&') {
                cleanStrBuffer.append("&amp;");
            } else if (c == '<') {
                cleanStrBuffer.append("&lt;");
            } else if (c == '>') {
                cleanStrBuffer.append("&gt;");
            } else if (c == '"') {
                cleanStrBuffer.append("&quot;");
            } else if (s > 127) {
                cleanStrBuffer.append("&#" + s + ";");
            } else {
                cleanStrBuffer.append(c);
            }
        } // for
        return cleanStrBuffer.toString();
    } // cleanXMLString()

    /**
     * Formats a given integer value interpreted as number of bytes to a String
     * representing the value in bytes, KBs, MBs, GBs, or TBs.
     *
     * @param bytes An integer representing a number of bytes.
     * @return A String representing <code>bytes</code> in bytes, KBs, MBs, GBs,
     *         or TBs.
     */
    public String formatBytes(int bytes) {
        if (bytes < 1024) {
            return new String(bytes + " bytes");
        }
        if (bytes < Math.pow(1024, 2)) {
            return new String((bytes / Math.pow(1024, 2)) + " KB");
        }
        if (bytes < Math.pow(1024, 3)) {
            return new String((bytes / Math.pow(1024, 3)) + " KB");
        }
        if (bytes < Math.pow(1024, 4)) {
            return new String((bytes / Math.pow(1024, 4)) + " MB");
        }
        if (bytes < Math.pow(1024, 5)) {
            return new String((bytes / Math.pow(1024, 5)) + " GB");
        }
        if (bytes < Math.pow(1024, 6)) {
            return new String((bytes / Math.pow(1024, 5)) + " TB");
        }
        return new String(bytes + "bytes");
    }

    /**
     * Returns an <code>ArrayList</code> consisting of the single characters
     * (<code>char</code>) of the argument <code>str</code>. Each character
     * is packed into a <code>String</code> object.
     *
     * @param str The <code>String</code> to be expanded.
     * @return An <code>ArrayList</code> of <code>String</code> objects - one
     *         <code>String</code> object for each character (<code>char</code>) of
     *         the argument <code>str</code>.
     */
    public final static ArrayList string2ArrayList(String str) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < str.length(); i++) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(str.charAt(i));
            stringBuffer.append(str.charAt(i));
            stringBuffer.append(str.charAt(i));
            arrayList.add(stringBuffer.toString());
        }
        return arrayList;
    }

    /**
     * Computes the Minumum Edit Distance between two <code>String</code> objects.
     * The returned integer is the number of operations
     * (substitution, deletion or insertion) necessary to transform one
     * <code>String</code> to the other.
     * <p/>
     * The Minimum Edit Distance has been used as a measure for similarity
     * between strings. For a detailed description of the algorithm see:
     * <p/>
     * Robert A. Wagner and Michael J. Fischer. 1974.<br>
     * The string-to-string correction problem.<br>
     * Journal of the ACM, 21(1):168 173.<br>
     *
     * @param strA The first <code>String</code>.
     * @param strB The second <code>String</code>.
     * @return The minimum number of operations to transform <code>strA</code>
     *         into <code>strB</code>.
     */
    public final static int minEditDistance(String strA, String strB) {
        int d[][]; // matrix
        int n; // length of strA
        int m; // length of strB
        int i; // iterates through strA
        int j; // iterates through strB
        char strA_i; // ith character of strA
        char strB_j; // jth character of strB
        int cost; // cost
        // Step 1
        n = strA.length();
        m = strB.length();
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // Step 2
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        // Step 3
        for (i = 1; i <= n; i++) {
            strA_i = strA.charAt(i - 1);
            // Step 4
            for (j = 1; j <= m; j++) {
                strB_j = strB.charAt(j - 1);
                // Step 5
                if (strA_i == strB_j) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                // Step 6
                d[i][j] = GeneralTools.minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
                        d[i - 1][j - 1] + cost);
            }
        }
        // Step 7
        return d[n][m];
    }

    /**
     * Writes a given <code>String</code> to a file.
     *
     * @param fileName The name of the file to be written.
     * @param str      The <code>String</code> to be written.
     */
    public static void writeStringToFile(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final static void main(String[] args) {
        System.out.print("MED of " + args[0] + " <--> " + args[1] + " = ");
        System.out.println(minEditDistance(args[0], args[1]));
        System.out.println("Test string2ArrayList(args[0]): "
                + string2ArrayList(args[0]));
        System.out.println("Test string2ArrayList(args[1]): "
                + string2ArrayList(args[1]));
    }
}
