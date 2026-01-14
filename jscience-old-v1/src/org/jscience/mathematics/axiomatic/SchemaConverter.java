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

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SchemaConverter {
    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void skipWhitespace(Reader in) throws IOException {
        for (;;) {
            in.mark(1);

            char c = (char) in.read();

            if (!Character.isWhitespace(c)) {
                in.reset();

                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void skipToWhitespace(Reader in) throws IOException {
        for (;;) {
            in.mark(1);

            char c = (char) in.read();

            if (Character.isWhitespace(c)) {
                in.reset();

                break;
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
        skipWhitespace(in);
        in.mark(1);

        char c = (char) in.read();

        if (c != '#') {
            in.reset();

            return false;
        }

        while (in.read() != '\n')
            ;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     * @param word DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static boolean readWord(Reader in, String word)
        throws IOException {
        int wordLength = word.length();
        in.mark(wordLength + 1);

        for (int i = 0; i < wordLength; i++) {
            if (in.read() != word.charAt(i)) {
                in.reset();

                return false;
            }
        }

        char ws = (char) in.read();

        if (Character.isWhitespace(ws)) {
            return true;
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
    private static void skipParen(Reader in) throws IOException {
        skipWhitespace(in);

        int depth = 0;

        for (;;) {
            int c = in.read();

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
            }

            if (depth == 0) {
                return;
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
    private static boolean readImport(Reader in) throws IOException {
        skipWhitespace(in);

        if (readWord(in, "import")) {
            skipParen(in);

            return true;
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
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static boolean readVar(Reader in) throws IOException {
        skipWhitespace(in);

        if (readWord(in, "var")) {
            skipParen(in);

            return true;
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
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static String readStep(Reader in) throws IOException {
        skipWhitespace(in);

        StringBuffer buf = new StringBuffer();

        for (;;) {
            char c = (char) in.read();

            if (Character.isWhitespace(c)) {
                break;
            }

            buf.append(c);
        }

        return buf.toString();
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
    private static boolean checkCloseParen(Reader in) throws IOException {
        skipWhitespace(in);
        in.mark(1);

        if (in.read() == ')') {
            return true;
        }

        in.reset();

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param in DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void skipParenOrVar(Reader in) throws IOException {
        skipWhitespace(in);
        in.mark(1);

        if (in.read() == '(') {
            in.reset();
            skipParen(in);
        } else {
            skipToWhitespace(in);
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
    private static String readHypothesis(Reader in) throws IOException {
        skipWhitespace(in);

        int c = in.read();

        if (c != '(') {
            return null;
        }

        String hyp = readStep(in);
        skipParenOrVar(in);
        skipWhitespace(in);

        if (in.read() != ')') {
            return null;
        }

        return hyp;
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
    private static String[] readHypotheses(Reader in) throws IOException {
        skipWhitespace(in);

        int c = in.read();

        if (c != '(') {
            return null;
        }

        List hypotheses = new ArrayList();

        for (;;) {
            String hypothesis = readHypothesis(in);

            if (hypothesis == null) {
                break;
            }

            hypotheses.add(hypothesis);
        }

        return (String[]) hypotheses.toArray(new String[hypotheses.size()]);
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
    private static boolean readThm(Reader in) throws IOException {
        skipWhitespace(in);

        if (readWord(in, "thm")) {
            skipWhitespace(in);

            int c = in.read();

            if (c != '(') {
                return false;
            }

            String name = readStep(in);
            System.out.print(name + "=");
            skipParen(in);

            String[] hyps = readHypotheses(in);
            skipParenOrVar(in);
            skipWhitespace(in);
            c = in.read();

            if (c != '(') {
                return false;
            }

            while (!checkCloseParen(in)) {
                String step = readStep(in);
                boolean foundStep = false;

                for (int i = 0; i < hyps.length; i++) {
                    if (step.equals(hyps[i])) {
                        foundStep = true;
                        System.out.print((char) ('v' + i));
                    }
                }

                if (!foundStep) {
                    if (step.equals("ax-1")) {
                        System.out.print('1');
                    } else if (step.equals("ax-2")) {
                        System.out.print('2');
                    } else if (step.equals("ax-3")) {
                        System.out.print('3');
                    } else if (step.equals("ax-4")) {
                        System.out.print('4');
                    } else if (step.equals("ax-5")) {
                        System.out.print('5');
                    } else if (step.equals("ax-6")) {
                        System.out.print('6');
                    } else if (step.equals("ax-7")) {
                        System.out.print('7');
                    } else if (step.equals("ax-8")) {
                        System.out.print('8');
                    } else if (step.equals("ax-9")) {
                        System.out.print('9');
                    } else if (step.equals("ax-10")) {
                        System.out.print('a');
                    } else if (step.equals("ax-11")) {
                        System.out.print('b');
                    } else if (step.equals("ax-12")) {
                        System.out.print('c');
                    } else if (step.equals("ax-13")) {
                        System.out.print('d');
                    } else if (step.equals("ax-14")) {
                        System.out.print('e');
                    } else if (step.equals("ax-16")) {
                        System.out.print('f');
                    } else if (step.equals("ax-17")) {
                        System.out.print('g');
                    } else if (step.equals("ax-ext")) {
                        System.out.print('h');
                    } else if (step.equals("ax-rep")) {
                        System.out.print('i');
                    } else if (step.equals("ax-un")) {
                        System.out.print('j');
                    } else if (step.equals("ax-pow")) {
                        System.out.print('k');
                    } else if (step.equals("ax-reg")) {
                        System.out.print('l');
                    } else if (step.equals("ax-inf")) {
                        System.out.print('m');
                    } else if (step.equals("ax-ac")) {
                        System.out.print('n');
                    } else if (step.equals("ax-mp")) {
                        System.out.print('D');
                    } else if (step.equals("ax-gen")) {
                        System.out.print('G');
                    } else if (step.equals("df-bi")) {
                        System.out.print('<');
                    } else if (step.equals("df-an")) {
                        System.out.print('&');
                    } else if (step.equals("df-or")) {
                        System.out.print('|');
                    } else if (step.equals("df-3an")) {
                        System.out.print('%');
                    } else if (step.equals("df-3or")) {
                        System.out.print('/');
                    } else if (step.equals("df-ex")) {
                        System.out.print('E');
                    } else if (step.equals("df-mo")) {
                        System.out.print('*');
                    } else if (step.equals("df-sb")) {
                        System.out.print('[');
                    } else if (step.equals("df-eu")) {
                        System.out.print('!');
                    } else if (step.equals("df-cleq")) {
                        System.out.print('=');
                    } else if (step.equals("df-clel")) {
                        System.out.print('@');
                    } else if (step.equals("df-v")) {
                        System.out.print('V');
                    } else if (step.equals("df-clab")) {
                        System.out.print('{');
                    } else if (step.equals("df-rab")) {
                        System.out.print('}');
                    } else if (step.equals("df-ral")) {
                        System.out.print('A');
                    } else if (step.equals("df-rex")) {
                        System.out.print('X');
                    } else if (step.equals("df-ne")) {
                        System.out.print('\\');
                    } else if (step.equals("df-nel")) {
                        System.out.print('?');
                    } else if (step.equals("df-sbc")) {
                        System.out.print(']');
                    } else if (step.equals("df-reu")) {
                        System.out.print('~');
                    } else if (step.equals("df-dif")) {
                        System.out.print('-');
                    } else if (step.equals("df-in")) {
                        System.out.print('^');
                    } else if (step.equals("df-un")) {
                        System.out.print('U');
                    } else if (!(step.equals("ph") || step.equals("ps") ||
                            step.equals("ch") || step.equals("t") ||
                            step.equals("u") || step.equals("v") ||
                            step.equals("w") || step.equals("x") ||
                            step.equals("y") || step.equals("z") ||
                            step.equals("A") || step.equals("B") ||
                            step.equals("C") || step.equals("D") ||
                            step.equals("F") || step.equals("R") ||
                            step.equals("S") || step.equals("V") ||
                            step.equals("->") || step.equals("cv") ||
                            step.equals("th") || step.equals("ta") ||
                            step.equals("et") || step.equals("ze") ||
                            step.equals("-.") || step.equals("<->") ||
                            step.equals("=") || step.equals("A.") ||
                            step.equals("A.e.") || step.equals("E.") ||
                            step.equals("E!") || step.equals("E*") ||
                            step.equals("E.e.") || step.equals("e.") ||
                            step.equals("{e.|}") || step.equals("e/") ||
                            step.equals("\\") || step.equals("[/]") ||
                            step.equals("{|}") || step.equals("/\\") ||
                            step.equals("\\/\\/") || step.equals("\\/"))) {
                        System.out.print("(" + step + ")");
                    }
                }
            }

            System.out.println();
            skipWhitespace(in);

            if (in.read() != ')') {
                return false;
            }

            return true;
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
    private static void readFile(Reader in) throws IOException {
        while (readComment(in) || readImport(in) || readVar(in) || readThm(in))
            ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] args) throws Exception {
        Reader in = new BufferedReader(new FileReader(args[0]));
        readFile(in);
    }
}
