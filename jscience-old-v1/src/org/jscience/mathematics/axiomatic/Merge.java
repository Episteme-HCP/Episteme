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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Merge {
    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     * @param map DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void readPM(String fileName, Map map)
        throws IOException {
        Reader in = new BufferedReader(new FileReader(fileName));
        int state = 0;
        StringBuffer name = new StringBuffer();
        StringBuffer proof = new StringBuffer();

        for (;;) {
            int c = in.read();
            char ch = (char) c;

            if (c == -1) {
                break;
            }

            if (state == 2) {
                if ((c == '~') || (c == '(')) {
                    state = 0;
                }
            }

            if (state == 0) {
                if (c == '\n') {
                    state = 1;
                } else if (c != '\r') {
                    name.append(ch);
                }
            } else if (state == 1) {
                if (c == ';') {
                    state = 2;

                    String prf = proof.toString();
                    prf = prf.replaceAll("N", "DD2DD2D13DD2D1311");

                    String nam = name.toString();
                    int semiIndex = nam.indexOf(';');
                    int spaceIndex = nam.indexOf(' ');

                    if ((spaceIndex >= 0) && (spaceIndex < semiIndex)) {
                        nam = nam.substring(0, spaceIndex);
                    } else {
                        nam = nam.substring(0, semiIndex);
                    }

                    map.put(nam, prf);
                    proof = new StringBuffer();
                    name = new StringBuffer();
                } else if (!Character.isWhitespace(ch)) {
                    proof.append(ch);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        Map original = new HashMap();
        Map modified = new HashMap();
        readPM(args[0], original);
        readPM(args[1], modified);

        Iterator iter = original.entrySet().iterator();

        while (iter.hasNext()) {
            Entry e = (Entry) iter.next();
            String name = (String) e.getKey();
            String oldProof = (String) e.getValue();
            String newProof = (String) modified.get(name);

            if (newProof == null) {
                System.out.println(name + ";");
                System.out.println(oldProof + ";");
            } else {
                int oldProofLength = oldProof.length();
                int newProofLength = newProof.length();

                if (newProofLength < oldProofLength) {
                    System.out.println(name + "; (M " + oldProofLength + "->" +
                        newProofLength + ")");
                    System.out.println(newProof + ";");
                } else {
                    System.out.println(name + ";");
                    System.out.println(oldProof + ";");
                }
            }

            System.out.println();
        }
    }
}
