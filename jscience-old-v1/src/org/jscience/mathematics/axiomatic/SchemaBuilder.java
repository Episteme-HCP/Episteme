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

import java.util.*;
import java.util.Map.Entry;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SchemaBuilder {
    /**
     * DOCUMENT ME!
     *
     * @param big DOCUMENT ME!
     * @param little DOCUMENT ME!
     * @param replacement DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String replaceAll(String big, String little,
        String replacement) {
        //			System.out.println(big.length());
        //		System.out.print('[');
        StringBuffer buf = new StringBuffer(big.length());
        int begin = 0;
        int end = 0;
        int littleLength = little.length();

        while ((end = big.indexOf(little, begin)) >= 0) {
            System.out.print('X');

            //			System.out.println("B:" + begin + " to E:" + end);
            buf.append(big.substring(begin, end));
            buf.append(replacement);
            begin = end + littleLength;

            //			System.out.println(buf.length());
        }

        buf.append(big.substring(begin));

        //		System.out.print(']');
        return buf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param proof DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static String[] tokenize(String proof) {
        List tokens = new ArrayList();
        int proofLength = proof.length();

        for (int i = 0; i < proofLength; i++) {
            char c = proof.charAt(i);

            if (c == '(') {
                int start = i;

                while (proof.charAt(i) != ')') {
                    i++;
                }

                tokens.add(proof.substring(start, i + 1));
            } else {
                tokens.add("" + c);
            }
        }

        return (String[]) tokens.toArray(new String[tokens.size()]);
    }

    //	private static String replaceSubtrees(
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        List order = new ArrayList();
        Map schema = new HashMap();
        BufferedReader in = new BufferedReader(new FileReader(args[0]));

        for (;;) {
            String line = in.readLine();

            if (line == null) {
                break;
            }

            int brk = line.indexOf('=');
            String name = line.substring(0, brk);
            String def = line.substring(brk + 1);
            schema.put(name, def);
            order.add(name);

            String[] tokens = tokenize(def);
            int wildCount = 0;

            for (int i = 0; i < tokens.length; i++) {
                int varIdx = tokens[i].charAt(0) - 'a' + 1;

                if ((varIdx >= 1) && (varIdx <= 20)) {
                    if (varIdx > wildCount) {
                        wildCount = varIdx;
                    }
                }
            }

            //			argCounts.put("(" + name + ")", new Integer(wildCount - 1));
        }

        //		argCounts.put("1", new Integer(-1));
        //		argCounts.put("2", new Integer(-1));
        //		argCounts.put("3", new Integer(-1));
        //		argCounts.put("B", new Integer(-1));
        //		argCounts.put("&", new Integer(-1));
        //		argCounts.put("|", new Integer(-1));
        //		argCounts.put("%", new Integer(-1));
        //		argCounts.put("!", new Integer(-1));
        //		argCounts.put("D", new Integer(1));
        in = new BufferedReader(new FileReader(args[1]));

        int state = 2;
        StringBuffer name = new StringBuffer();
        StringBuffer proof = new StringBuffer();
        List nameList = new ArrayList(300);
        List originalProofList = new ArrayList(300);

        for (;;) {
            int cin = in.read();

            if (cin == -1) {
                break;
            }

            char c = (char) cin;

            if (state == 2) {
                if ((c == '~') || (c == '(')) {
                    state = 0;
                }
            } else if (state == 0) {
                if (c == ';') {
                    state = 3;
                } else {
                    name.append(c);
                }
            } else if (state == 3) {
                if (c == '\n') {
                    state = 1;
                }
            } else if (state == 1) {
                if (c == ';') {
                    state = 2;

                    String prf = proof.reverse().toString();
                    nameList.add(name.toString());
                    order.remove(name.toString());
                    originalProofList.add(prf);
                    proof = new StringBuffer();
                    name = new StringBuffer();
                } else if (!Character.isWhitespace(c)) {
                    proof.append(c);
                }
            }
        }

        //		int replaced;
        //		do {
        //			replaced = 0;
        ListIterator p = originalProofList.listIterator();
        Iterator n = nameList.iterator();

        while (p.hasNext()) {
            String rawProofStr = (String) p.next();
            String proofStr = rawProofStr;
            String nameStr = "(" + ((String) n.next()) + ")";

            if (proofStr.length() > nameStr.length()) {
                System.out.println("Utilizing " + nameStr);

                ListIterator s = originalProofList.listIterator();

                while (s.hasNext()) {
                    String biggerProof = (String) s.next();

                    if (biggerProof.length() > rawProofStr.length()) {
                        String reducedProof = replaceAll(biggerProof, proofStr,
                                nameStr);

                        if (!reducedProof.equals(biggerProof)) {
                            System.out.print(" (" + biggerProof.length() +
                                "->" + reducedProof.length() + ")");

                            //								++replaced;
                            s.set(reducedProof);
                        }

                        System.out.print('/');
                    }
                }

                System.out.println();
            }
        }

        //		}
        //		while (replaced > 0);
        //		Iterator iter = order.iterator();
        //		while (iter.hasNext()) {
        //			String operationName = (String) iter.next();
        //			String operationDef = (String) schema.get(operationName);
        //			ListIterator q = originalProofList.listIterator();
        //			while (q.hasNext()) {
        //				String prf = (String) q.next();
        //				prf =
        //					replaceSubtrees(
        //						prf,
        //						"a2D",
        //						"(a2i)");
        //				prf =
        //					replaceSubtrees(
        //						prf,
        //						"a1D",
        //						"(a1i)");
        //				q.set(prf);
        //			}
        //		}
        p = originalProofList.listIterator();
        n = nameList.iterator();

        while (p.hasNext()) {
            String nameStr = (String) n.next();
            String proofStr = ((String) p.next());
            proofStr = proofStr.replaceAll("1D2DD", "(syl)");
            schema.put(nameStr, proofStr);
        }

        PrintWriter out = new PrintWriter(new BufferedOutputStream(
                    new FileOutputStream(args[2])));
        Iterator iter = schema.entrySet().iterator();

        while (iter.hasNext()) {
            Entry e = (Entry) iter.next();
            out.println(e.getKey() + "=" + e.getValue());
        }

        out.flush();
        out.close();
    }
}
