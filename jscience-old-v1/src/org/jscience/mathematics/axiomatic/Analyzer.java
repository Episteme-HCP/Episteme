package org.jscience.mathematics.axiomatic;


//author: Greg Bush
//email: GBush@HealthMarket.com
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.util.*;
import java.util.Map.Entry;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Analyzer {
    /**
     * DOCUMENT ME!
     *
     * @param proofs DOCUMENT ME!
     */
    public static void analyze(String[] proofs) {
        Integer one = new Integer(1);
        Map m = new HashMap();

        for (int proofIndex = 0; proofIndex < proofs.length; proofIndex++) {
            String proof = proofs[proofIndex];
            int maxLen = 12;
            Stack s = new Stack();

            for (int i = proof.length() - 1; i >= 0; i--) {
                List e = new ArrayList();
                char c = proof.charAt(i);
                e.add("*");

                if ((c == 'D') || (c == 'S')) {
                    List leftList = (List) s.pop();
                    List rightList = (List) s.pop();
                    Iterator iLeft = leftList.iterator();

                    while (iLeft.hasNext()) {
                        String left = (String) iLeft.next();
                        Iterator iRight = rightList.iterator();

                        while (iRight.hasNext()) {
                            String right = (String) iRight.next();
                            String p = "" + c + left + right;

                            if (p.length() <= maxLen) {
                                e.add(p);
                            }
                        }
                    }
                } else {
                    e.add("" + c);
                }

                s.push(e);

                Iterator iE = e.iterator();

                while (iE.hasNext()) {
                    String inst = (String) iE.next();

                    /*
                     * Yes, it's okay to use primitive != here...
                     */
                    if (inst != "*") {
                        Integer count = (Integer) m.get(inst);

                        if (count == null) {
                            count = one;
                        } else {
                            count = new Integer(count.intValue() + 1);
                        }

                        m.put(inst, count);
                    }
                }
            }
        }

        String[] commons = new String[1000000];

        //        String maxString = null;
        //int maxWeight = 0;
        Iterator mIter = m.entrySet().iterator();

        while (mIter.hasNext()) {
            Entry e = (Entry) mIter.next();
            Integer repI = (Integer) e.getValue();

            /*
             * Likewise, here.
             */
            if (repI != one) {
                int rep = repI.intValue();
                String proof = (String) e.getKey();
                int len = 0;
                int hypCount = 0;

                for (int i = proof.length() - 1; i >= 0; i--) {
                    if (proof.charAt(i) != '*') {
                        len++;
                    } else {
                        hypCount++;
                    }
                }

                int weight = (len - 1) * rep;
                commons[weight] = proof;
            }
        }

        for (int i = commons.length - 1; i >= 0; i--) {
            String s = commons[i];

            if (s != null) {
                System.out.println(i + ":" + s);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException {
        Reader in = new BufferedReader(new FileReader(args[0]));
        int state = 0;
        StringBuffer name = new StringBuffer();
        StringBuffer proof = new StringBuffer();
        List nameList = new ArrayList(300);
        List originalProofList = new ArrayList(300);

        for (;;) {
            int c = in.read();

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
                    nameList.add(nameStr);
                    name = new StringBuffer();
                } else if (c != '\r') {
                    name.append((char) c);
                }
            } else if (state == 1) {
                if (c == ';') {
                    state = 2;

                    String prf = proof.toString();
                    originalProofList.add(prf);
                    proof = new StringBuffer();
                } else if (!Character.isWhitespace((char) c)) {
                    proof.append((char) c);
                }
            }
        }

        String[] originalProofs = (String[]) originalProofList.toArray(new String[originalProofList.size()]);
        analyze(originalProofs);
    }
}
