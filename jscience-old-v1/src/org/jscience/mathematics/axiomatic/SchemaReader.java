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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SchemaReader {
    /** DOCUMENT ME! */
    private static final int SIZE = 1000000;

    /** DOCUMENT ME! */
    private static Map proofs = new HashMap();

    /** DOCUMENT ME! */
    private static Stack stack = new Stack();

    /** DOCUMENT ME! */
    private static int depth = 0;

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static String readName(Reader in) throws IOException {
        StringBuffer buf = new StringBuffer();

        for (;;) {
            int c = in.read();

            if (c == '=') {
                return buf.toString();
            } else {
                buf.append((char) c);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static boolean readComment(Reader in) throws IOException {
        in.mark(1);

        int c = in.read();

        if (c == '#') {
            for (;;) {
                c = in.read();

                if (c == '\n') {
                    return true;
                }
            }
        } else {
            in.reset();

            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void readProof(Reader in) throws IOException {
        if (!readComment(in)) {
            String name = readName(in);
            StringBuffer buf = new StringBuffer(128);

            for (;;) {
                int c = in.read();

                if (c == '\n') {
                    break;
                } else if (c != '\r') {
                    buf.append((char) c);
                }
            }

            proofs.put(name, buf.toString());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void readFile(Reader in) throws IOException {
        for (;;) {
            in.mark(1);

            int c = in.read();

            if (c == -1) {
                break;
            }

            in.reset();
            readProof(in);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @throws IllegalStateException DOCUMENT ME!
     */
    private static void produce(String name) {
        depth++;

        String proof = (String) proofs.get(name);

        if (proof == null) {
            System.out.println("Couldn't find '" + name + "'");
            System.exit(0);
        }

        int proofLength = proof.length();
        int state = 0;
        int subIdx = 0;
        String va = null;
        String vb = null;
        String vc = null;
        String vd = null;
        String ve = null;
        int popCount = 0;

        for (int i = 0; i < proofLength; i++) {
            char c = proof.charAt(i);

            if (state == 0) {
                if (c == '(') {
                    state = 1;
                } else if (c == 'v') {
                    if (popCount < 1) {
                        popCount = 1;
                    }
                } else if (c == 'w') {
                    if (popCount < 2) {
                        popCount = 2;
                    }
                } else if (c == 'x') {
                    if (popCount < 3) {
                        popCount = 3;
                    }
                } else if (c == 'y') {
                    if (popCount < 4) {
                        popCount = 4;
                    }
                } else if (c == 'z') {
                    if (popCount < 5) {
                        popCount = 5;
                    }
                }
            } else if (state == 1) {
                if (c == ')') {
                    state = 0;
                }
            }
        }

        if (popCount == 5) {
            ve = (String) stack.pop();
            vd = (String) stack.pop();
            vc = (String) stack.pop();
            vb = (String) stack.pop();
            va = (String) stack.pop();

            if ((va == null) || (vb == null) || (vc == null) || (vd == null) ||
                    (ve == null)) {
                System.out.println("5 null");
                System.exit(0);
            }
        }

        if (popCount == 4) {
            vd = (String) stack.pop();
            vc = (String) stack.pop();
            vb = (String) stack.pop();
            va = (String) stack.pop();

            if ((va == null) || (vb == null) || (vc == null) || (vd == null)) {
                System.out.println("4 null");
                System.exit(0);
            }
        }

        if (popCount == 3) {
            vc = (String) stack.pop();
            vb = (String) stack.pop();
            va = (String) stack.pop();

            if ((va == null) || (vb == null) || (vc == null)) {
                System.out.println("3 null");
                System.exit(0);
            }
        }

        if (popCount == 2) {
            vb = (String) stack.pop();
            va = (String) stack.pop();

            if ((va == null) || (vb == null)) {
                System.out.println("2 null");
                System.exit(0);
            }
        }

        if (popCount == 1) {
            va = (String) stack.pop();

            if (va == null) {
                System.out.println("1 null");
                System.exit(0);
            }
        }

        for (int i = 0; i < proofLength; i++) {
            char c = proof.charAt(i);

            if (state == 0) {
                if (c == '(') {
                    state = 1;
                    subIdx = i + 1;
                } else if (c == '1') {
                    stack.push("1");
                } else if (c == '2') {
                    stack.push("2");
                } else if (c == '3') {
                    stack.push("3");
                } else if (c == '4') {
                    stack.push("4");
                } else if (c == '5') {
                    stack.push("5");
                } else if (c == '6') {
                    stack.push("6");
                } else if (c == '7') {
                    stack.push("7");
                } else if (c == '8') {
                    stack.push("8");
                } else if (c == '9') {
                    stack.push("9");
                } else if (c == 'a') {
                    stack.push("a");
                } else if (c == 'b') {
                    stack.push("b");
                } else if (c == 'c') {
                    stack.push("c");
                } else if (c == 'd') {
                    stack.push("d");
                } else if (c == 'e') {
                    stack.push("e");
                } else if (c == 'f') {
                    stack.push("f");
                } else if (c == 'g') {
                    stack.push("g");
                } else if (c == 'h') {
                    stack.push("h");
                } else if (c == 'i') {
                    stack.push("i");
                } else if (c == 'j') {
                    stack.push("j");
                } else if (c == 'k') {
                    stack.push("k");
                } else if (c == 'l') {
                    stack.push("l");
                } else if (c == 'm') {
                    stack.push("m");
                } else if (c == 'n') {
                    stack.push("n");
                } else if (c == '<') {
                    stack.push("<");
                } else if (c == '|') {
                    stack.push("|");
                } else if (c == '&') {
                    stack.push("&");
                } else if (c == '%') {
                    stack.push("%");
                } else if (c == '/') {
                    stack.push("/");
                } else if (c == '[') {
                    stack.push("[");
                } else if (c == 'E') {
                    stack.push("E");
                } else if (c == '!') {
                    stack.push("!");
                } else if (c == '*') {
                    stack.push("*");
                } else if (c == '@') {
                    stack.push("@");
                } else if (c == 'V') {
                    stack.push("V");
                } else if (c == '{') {
                    stack.push("{");
                } else if (c == '}') {
                    stack.push("}");
                } else if (c == 'A') {
                    stack.push("A");
                } else if (c == 'X') {
                    stack.push("X");
                } else if (c == '\\') {
                    stack.push("\\");
                } else if (c == '?') {
                    stack.push("?");
                } else if (c == ']') {
                    stack.push("]");
                } else if (c == '~') {
                    stack.push("~");
                } else if (c == '-') {
                    stack.push("-");
                } else if (c == '^') {
                    stack.push("^");
                } else if (c == 'U') {
                    stack.push("U");
                } else if (c == 'v') {
                    stack.push(va);
                } else if (c == 'w') {
                    stack.push(vb);
                } else if (c == 'x') {
                    stack.push(vc);
                } else if (c == 'y') {
                    stack.push(vd);
                } else if (c == 'z') {
                    stack.push(ve);
                } else if (c == 'D') {
                    stack.push("D" + stack.pop() + stack.pop());
                } else if (c == 'G') {
                    stack.push("G" + stack.pop());
                } else if (c == '=') {
                    stack.push("=" + stack.pop());
                } else {
                    System.out.println("What's a '" + c + "'?");
                    System.exit(0);
                }
            } else if (state == 1) {
                if (c == ')') {
                    produce(proof.substring(subIdx, i));
                    state = 0;
                }
            }
        }

        proof = (String) stack.pop();

        if (proof.length() > SIZE) {
            throw new IllegalStateException("TOO LONG (" + proof.length() +
                ")");
        } else {
            stack.push(proof);
        }

        depth--;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        Reader in = new BufferedReader(new FileReader(args[0]), 1 << 20);
        System.out.print("Reading...");
        readFile(in);
        System.out.println("DONE");

        //		produce("id");
        //		produce("id");
        //		produce("bicon1");
        //		produce("bii");
        //		System.exit(0);
        //		System.exit(0);
        PrintWriter out = new PrintWriter(new FileOutputStream(args[1]));
        String[] section = new String[SIZE];
        Iterator i = proofs.keySet().iterator();

        while (i.hasNext()) {
            String key = (String) i.next();
            String spec = (String) proofs.get(key);

            //			int depth = 0;
            boolean hypo = false;

            for (int j = 0; j < spec.length(); j++) {
                char c = spec.charAt(j);

                if (c == '(') {
                    depth++;
                } else if (c == ')') {
                    depth--;
                } else if ((c == 'v') && (depth == 0)) {
                    hypo = true;

                    break;
                }
            }

            if (!hypo) {
                System.out.println(key);

                try {
                    produce(key);

                    String proof = (String) stack.pop();
                    int proofLength = proof.length();

                    if (section[proofLength] == null) {
                        section[proofLength] = "";
                    }

                    section[proofLength] = section[proofLength] + "~" + key +
                        ";\r\n" + proof + ";\r\n\r\n";
                } catch (Exception ex) {
                    depth = 0;
                    stack.clear();
                    System.out.println("TOO LONG!");
                }
            }
        }

        for (int j = 0; j < SIZE; j++) {
            String s = section[j];

            if (s != null) {
                out.print(s);
            }
        }

        out.close();
    }
}
