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
public class Prover {
    /** DOCUMENT ME! */
    private static String[] axiomExpressions = new String[65536];

    /** DOCUMENT ME! */
    private static Map existingSubProofs = new HashMap();

    /** DOCUMENT ME! */
    private static Map[] proofs = new Map[65536];

    /** DOCUMENT ME! */
    private static ProofStack evalStack = new ProofStack(16);

    /** DOCUMENT ME! */
    private static String[] names;

    /** DOCUMENT ME! */
    private static String[] originalProofs = new String[0];

    /** DOCUMENT ME! */
    private static String modusPonens;

    /** DOCUMENT ME! */
    private static String generalization;

    /** DOCUMENT ME! */
    private static boolean modified;

    /**
     * DOCUMENT ME!
     *
     * @param result DOCUMENT ME!
     * @param newStep DOCUMENT ME!
     * @param replace DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws UnsynchedException DOCUMENT ME!
     */
    private static void record(WFF result, String newStep, boolean replace)
        throws FileNotFoundException, UnsynchedException {
        if (replace) {
            String priorVersion = (String) existingSubProofs.get(result);

            if ((priorVersion != null) && !newStep.equals(priorVersion)) {
                String from;
                String to;

                if ((priorVersion.length() < newStep.length()) ||
                        ((priorVersion.length() == newStep.length()) &&
                        (priorVersion.compareTo(newStep) <= 0))) {
                    from = newStep;
                    to = priorVersion;
                } else {
                    from = priorVersion;
                    to = newStep;
                }

                int diff = from.length() - to.length();
                StringBuffer pad = new StringBuffer(diff);

                for (int i = 0; i < diff; i++) {
                    pad.append('`');
                }

                String realTo = to;
                to += pad;

                int replaceLen = from.length();
                boolean replacedAny = false;

                for (int i = 0; i < originalProofs.length; i++) {
                    String original = originalProofs[i];
                    int originalLength = original.length();
                    boolean replaced = false;

                    if (original.indexOf(from) >= 0) {
                        System.out.print("NORMALIZING " + names[i] + " [");

                        StringBuffer buf = new StringBuffer(original);
                        int mark = 0;

                        while ((mark = buf.indexOf(from, mark)) >= 0) {
                            System.out.print('X');
                            replaced = true;

                            for (int j = 0; j < replaceLen; j++) {
                                buf.setCharAt(mark + j, to.charAt(j));
                            }
                        }

                        System.out.print("] ");

                        StringBuffer shorterBuf = new StringBuffer(originalLength);

                        for (int j = 0; j < originalLength; j++) {
                            char c = buf.charAt(j);

                            if (c != '`') {
                                shorterBuf.append(c);
                            }
                        }

                        original = shorterBuf.toString();
                        System.out.println("DONE");
                    }

                    if (replaced) {
                        replacedAny = true;
                        names[i] = names[i] + " (N " + originalLength + "->" +
                            original.length() + ")";
                        originalProofs[i] = original;
                    }
                }

                if (replacedAny) {
                    modified = true;
                    System.out.print("Writing...");

                    PrintWriter out = new PrintWriter(new BufferedOutputStream(
                                new FileOutputStream("dump.txt"), 1 << 20));

                    for (int j = 0; j < names.length; j++) {
                        out.println(names[j]);
                        out.println(originalProofs[j] + "; ! " +
                            originalProofs[j].length() + " steps");
                        out.println();
                    }

                    out.flush();
                    out.close();
                    System.out.println("DONE");
                    existingSubProofs.put(new WFF(result), realTo);
                } else {
                    System.out.print('?');
                    existingSubProofs.put(new WFF(result), realTo);
                    throw new UnsynchedException();
                }
            } else if (priorVersion == null) {
                System.out.print('.');
                existingSubProofs.put(new WFF(result), newStep);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prf DOCUMENT ME!
     * @param replace DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws UnsynchedException DOCUMENT ME!
     */
    private static WFF attempt(String prf, boolean replace)
        throws FileNotFoundException, UnsynchedException {
        ProofStack proofStack = new ProofStack(100);
        Stack stepStack = new Stack();

        for (int proofStep = prf.length() - 1; proofStep >= 0; proofStep--) {
            char proofChar = prf.charAt(proofStep);
            String expression = axiomExpressions[proofChar];

            if (expression != null) {
                proofStack.push(expression);
                stepStack.push(new String(new char[] { proofChar }));
            } else {
                if (proofChar == 'G') {
                    stepStack.push("G" + stepStack.pop());

                    WFF result = proofStack.pop();
                    result.generalize();
                    proofStack.push(result);
                } else if (proofChar == 'D') {
                    if (stepStack.size() < 2) {
                        System.out.println("SHORT?");

                        return null;
                    }

                    String stepL = (String) stepStack.pop();
                    String stepR = (String) stepStack.pop();
                    String newStep = "D" + stepL + stepR;
                    stepStack.push(newStep);

                    WFF result = proofStack.deduce();

                    if (result == null) {
                        return null;
                    }

                    record(result, newStep, replace);
                } else {
                    System.out.println("What's a '" + proofChar + "'?");
                }
            }
        }

        WFF result = proofStack.pop();

        if (!proofStack.isEmpty()) {
            System.out.println("TOO MUCH INFORMATION!");
            System.exit(0);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param unified DOCUMENT ME!
     * @param proof DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public static boolean factor(WFF unified, String proof)
        throws FileNotFoundException {
        int proofLength = proof.length();
        boolean found = false;

        for (int i = 0; i < proofLength; i++) {
            if (proofs[i].get(unified) != null) {
                found = true;

                break;
            }
        }

        if (!found) {
            proofs[proofLength - 1].put(new WFF(unified), proof);

            String existing = (String) existingSubProofs.get(unified);

            if (existing != null) {
                int existingLength = existing.length();

                if (existingLength > proof.length()) {
                    for (int i = 0; i < originalProofs.length; i++) {
                        String original = originalProofs[i];
                        int mark;
                        boolean replaced = false;
                        StringBuffer buf = new StringBuffer(original);

                        while ((mark = buf.indexOf(existing)) >= 0) {
                            replaced = true;
                            buf.replace(mark, mark + existing.length(), proof);
                        }

                        original = buf.toString();

                        if (replaced) {
                            names[i] = names[i] + " (" +
                                originalProofs[i].length() + "->" +
                                original.length() + " @ " + ProofStack.DEPTH +
                                "x" + WFF.BANDWIDTH + ")";
                            originalProofs[i] = original;
                            System.out.println(names[i]);
                            System.out.println(originalProofs[i] + "; ! " +
                                originalProofs[i].length() + " steps");

                            PrintWriter out = new PrintWriter(new FileOutputStream(
                                        "dump.txt"));

                            for (int j = 0; j < names.length; j++) {
                                out.println(names[j]);
                                out.println(originalProofs[j] + "; ! " +
                                    originalProofs[j].length() + " steps");
                                out.println();
                            }

                            out.flush();
                            out.close();
                        }
                    }

                    existingSubProofs.put(new WFF(unified), proof);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param proof DOCUMENT ME!
     * @param leftOriginal DOCUMENT ME!
     * @param rightOriginal DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public static boolean evaluate(String proof, WFF leftOriginal,
        WFF rightOriginal) throws FileNotFoundException {
        evalStack.clear();
        evalStack.push(new WFF(rightOriginal));
        evalStack.push(new WFF(leftOriginal));

        WFF unified = evalStack.deduce();

        if (unified == null) {
            return false;
        }

        return factor(unified, proof);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void readPM(String fileName) throws IOException {
        Reader in = new BufferedReader(new FileReader(fileName));
        int state = 0;
        StringBuffer name = new StringBuffer();
        StaticStringBuffer proof = new StaticStringBuffer(1024);
        List nameList = new ArrayList(300);
        List originalProofList = new ArrayList(300);

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

                    String nameStr = name.toString();
                    System.out.println("Loading " + nameStr);
                    nameList.add(nameStr);
                    name = new StringBuffer();
                } else if (c != '\r') {
                    name.append(ch);
                }
            } else if (state == 1) {
                if (c == ';') {
                    state = 2;

                    String prf = proof.toString();
                    originalProofList.add(prf);
                    proof.setLength(0);
                } else if (!Character.isWhitespace(ch)) {
                    proof.append(ch);
                }
            }
        }

        names = (String[]) nameList.toArray(new String[nameList.size()]);
        originalProofs = (String[]) originalProofList.toArray(new String[originalProofList.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     * @param axiomMap DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void readAxioms(String fileName, Map axiomMap)
        throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String line;

        while ((line = in.readLine()) != null) {
            if (line.length() > 0) {
                if (line.charAt(0) != '#') {
                    int brk = line.indexOf(':');
                    String axiomChar = line.substring(0, brk);
                    String expr = line.substring(brk);

                    if (expr.equals(":@mp")) {
                        modusPonens = axiomChar;
                    } else if (expr.equals(":@gen")) {
                        generalization = axiomChar;
                    } else {
                        axiomMap.put(new WFF(expr), axiomChar);
                        axiomExpressions[axiomChar.charAt(0)] = expr;
                    }
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
        for (int i = 0; i < 65536; i++) {
            proofs[i] = new HashMap();
        }

        WFF.BANDWIDTH = 256;
        ProofStack.DEPTH = 256;
        readAxioms(args[0], proofs[0]);
        readPM(args[1]);

        do {
            modified = false;

            for (int i = 0; i < originalProofs.length; i++) {
                System.out.println("Checking " + names[i]);

                try {
                    WFF result = attempt(originalProofs[i], true);

                    if (result == null) {
                        System.out.println("FAILED " + originalProofs[i]);

                        return;
                    } else {
                        System.out.println(result);
                    }
                } catch (UnsynchedException ex) {
                    // Just try again
                    i--;
                }
            }
        } while (modified);

        System.out.println("Starting search...");

        Set used = new HashSet();

        String params = args[2];
        int brk = params.indexOf('x');
        WFF.BANDWIDTH = Integer.parseInt(params.substring(brk + 1));
        ProofStack.DEPTH = Integer.parseInt(params.substring(0, brk));

        int totalProofs = 0;

        for (int x = 1; x < 10000; x++) {
            if (generalization != null) {
                /*
                 * Generalize our last round of theorems.
                 */
                Iterator theoremIter = proofs[x - 1].entrySet().iterator();

                while (theoremIter.hasNext()) {
                    Entry theorem = (Entry) theoremIter.next();
                    WFF newResult = new WFF((WFF) theorem.getKey());
                    newResult.generalize();
                    proofs[x].put(newResult, generalization +
                        theorem.getValue());
                }
            }

            if (modusPonens != null) {
                for (int i = 0; i < x; i++) {
                    Iterator iterLeft = proofs[i].entrySet().iterator();

                    while (iterLeft.hasNext()) {
                        Entry left = (Entry) iterLeft.next();
                        Iterator iterRight = proofs[x - i - 1].entrySet()
                                                              .iterator();

                        while (iterRight.hasNext()) {
                            Entry right = (Entry) iterRight.next();
                            String proofLeft = (String) left.getValue();
                            String proofRight = (String) right.getValue();
                            WFF resultLeft = (WFF) left.getKey();
                            WFF resultRight = (WFF) right.getKey();
                            String newProof = modusPonens + proofLeft +
                                proofRight;

                            if (evaluate(newProof, resultLeft, resultRight)) {
                                used.add(left);
                                used.add(right);
                                totalProofs++;
                            }
                        }
                    }
                }
            }

            int TOTAL = 200000;
            System.out.println((x + 2) + ":" + proofs[x + 1].size());

            if (totalProofs > TOTAL) {
                for (int i = 0; i < x; i++) {
                    List useless = new ArrayList(100000);
                    int removedCount = 0;
                    Iterator iter = proofs[i].entrySet().iterator();

                    while (iter.hasNext()) {
                        Entry result = (Entry) iter.next();

                        if (!used.contains(result)) {
                            useless.add(result);
                            removedCount++;
                        }
                    }

                    iter = useless.iterator();

                    while (iter.hasNext()) {
                        proofs[i].remove(((Entry) iter.next()).getKey());
                    }

                    if (removedCount > 0) {
                        System.out.println("Removed " + removedCount +
                            " unused proofs from level " + (i + 1));
                        totalProofs -= removedCount;
                    }
                }

                used.clear();

                int cullLength = WFF.BANDWIDTH;

                while (totalProofs > TOTAL) {
                    cullLength--;

                    if (cullLength == 0) {
                        System.out.println("Culled to zero");

                        return;
                    }

                    for (int j = 0; j < proofs.length; j++) {
                        Iterator iter = proofs[j].keySet().iterator();
                        List longList = new ArrayList(proofs[j].size());

                        while (iter.hasNext()) {
                            WFF result = (WFF) iter.next();

                            if (result.getLength() > cullLength) {
                                longList.add(result);
                                totalProofs--;
                            }
                        }

                        if (longList.size() > 0) {
                            iter = longList.iterator();

                            while (iter.hasNext()) {
                                proofs[j].remove(iter.next());
                            }
                        }
                    }
                }

                System.out.println("Culled to length " + cullLength);
                WFF.BANDWIDTH = cullLength;
            }
        }
    }
}
