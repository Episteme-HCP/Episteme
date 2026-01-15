/*
 * SyncMMAX.java
 *
 * Created on September 16, 2003, 2:27 AM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                    Hajo Keffer <hajokeffer@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jscience.ml.tigerxml.tools;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.NT;
import org.jscience.ml.tigerxml.T;

import java.util.ArrayList;


/**
 * Provides static methods for synchronizing and converting data structures
 * between the org.jscience.ml.tigerxml and MMAX.
 * <p/>
 * <p/>
 * (<a href=http://www.eml.org/nlp>European Media Laboratory, NLP Group</a>).
 * This class is for static use.
 * </p>
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84 $Id: SyncMMAX.java,v 1.2 2007-10-21 17:47:09 virtualcall Exp $
 */
public class SyncMMAX {
    /**
     * Expand a given span (String) and return an ArrayList containing all ID
     * Strings. Example: "s1_1..s1_3,s1_5" --> ["s1_1", "s1_2", "s1_3",
     * "s1_5"]
     *
     * @param span The (possibly condensed) String representation of the span.
     * @return An ArrayList containing all ID Strings.
     */
    public static final ArrayList parseSpan(String span) {
        StringBuffer currentSpan = new StringBuffer();
        ArrayList spanList = new ArrayList();
        int spanLen = span.length();
        ArrayList fragList;

        for (int i = 0; i < spanLen; i++) {
            if (span.charAt(i) != ',') {
                currentSpan.append(span.charAt(i));
            } else {
                /////currentSpan.trim();
                fragList = parseSpanFragment(currentSpan.toString());

                if (fragList.size() > 1) {
                    spanList.addAll(fragList);
                } else {
                    spanList.add(fragList.get(0));
                }

                currentSpan = new StringBuffer();
                fragList = null;
            }
        }

        /////currentSpan.trim();
        fragList = parseSpanFragment(currentSpan.toString());

        if (fragList.size() > 1) {
            spanList.addAll(fragList);
        } else {
            spanList.add(fragList.get(0));
        }

        return spanList;
    }

    /**
     * DOCUMENT ME!
     *
     * @param span DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private static ArrayList parseSpanFragment(String span) {
        ArrayList newWordsIDList = new ArrayList();

        if (span.indexOf("..") == -1) {
            newWordsIDList.add(span);

            return newWordsIDList;
        }

        String nameSpace = span.substring(0, span.indexOf("_") + 1);
        String firstIDString = span.substring(0, span.indexOf("."));
        String lastIDString = span.substring(span.lastIndexOf(".") + 1);
        int firstIDInteger = Integer.parseInt(firstIDString.substring(firstIDString.indexOf(
                "_") + 1));
        int lastIDInteger = Integer.parseInt(lastIDString.substring(lastIDString.indexOf(
                "_") + 1));

        for (int i = firstIDInteger; i <= lastIDInteger; i++) {
            newWordsIDList.add(nameSpace + String.valueOf(i));
        }

        return newWordsIDList;
    }

    /**
     * Condense a given span (String) and return the condensed span (String).
     * Example: "s1_1,s1_2,s1_3,s1_4,s1_5" --> "s1_1..s1_5"
     *
     * @param inSpan The String representation of the span to be condensed.
     * @return The condensed span String.
     */
    public static final String condenseSpan(String inSpan) {
        ArrayList idStrings = parseSpan(inSpan);

        return condenseSpanRecurse(idStrings);
    }

    /**
     * DOCUMENT ME!
     *
     * @param idStrings DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    private static String condenseSpanRecurse(ArrayList idStrings) {
        String span = "";
        String spanRest = "";
        int currentPos = 0;
        int idCount = 0;
        String currentId = "";
        String currentNameSpace = "";
        String nextNameSpace = "";
        String nextId = "";
        int currentNum = 0;
        int nextNumber = 0;

        if (idStrings.size() == 1) {
            return (String) idStrings.get(0);
        }

        currentId = (String) idStrings.get(currentPos);
        currentNameSpace = currentId.substring(0, currentId.indexOf("_"));

        try {
            currentNum = Integer.parseInt(currentId.substring(currentId.indexOf(
                    "_") + 1));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        nextId = (String) idStrings.get(currentPos + 1);
        nextNameSpace = nextId.substring(0, nextId.indexOf("_"));

        try {
            nextNumber = Integer.parseInt(nextId.substring(nextId.indexOf("_") +
                    1));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }

        if (!currentNameSpace.equals(nextNameSpace) ||
                (nextNumber != (currentNum + 1))) {
            span = currentId;
            idStrings.remove(0);
            span = span + "," + condenseSpanRecurse(idStrings);

            return span;
        }

        span = currentId + "..";
        spanRest = nextId;
        idStrings.remove(0);
        idStrings.remove(0);
        idCount = idStrings.size();

        for (int u = 0; u < idCount; u++) {
            nextId = (String) idStrings.get(u);
            nextNameSpace = nextId.substring(0, nextId.indexOf("_"));

            try {
                nextNumber = Integer.parseInt(nextId.substring(nextId.indexOf(
                        "_") + 1));
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }

            if (nextNameSpace.equals(currentNameSpace) &&
                    (nextNumber == (currentNum + 2 + u))) {
                spanRest = nextId;
            } else {
                span = span + spanRest + ",";

                for (int o = 0; o < u; o++) {
                    idStrings.remove(0);
                }

                span = span + condenseSpanRecurse(idStrings);

                return span;
            }
        }

        return span + spanRest;
    } // condenseSpanRecurse()

    /**
     * Defines the concept of a markable according to the MMAX guidelines in
     * terms of NEGRA/TIGER syntax. A markable is characterized as follows:
     * NP-like nodes are markables. Appositions are excluded from being
     * markables. A terminal that is an attributive pronoun is a markable If a
     * noun or a substituting pronoun that is not a relative pronoun is not
     * immediately dominated by an NP-like node other than CNP, then it is a
     * markable.
     *
     * @param node The node to be classified as a Markable/not a Markable.
     * @return True if <code>node</code> is a Markable according to the MMAX
     *         guidelines.
     */
    public static final boolean isMarkable(GraphNode node) {
        boolean value = false;

        if (!SyntaxTools.isApposition(node)) {
            if (node.isTerminal()) {
                //cast GraphNode to Terminal
                T terminal = (T) node;

                if (SyntaxTools.isAttributiveMarkablePronoun(terminal)) {
                    value = true;
                } else {
                    if ((SyntaxTools.isNoun(terminal)) ||
                            (SyntaxTools.isSubstitutingMarkablePronoun(terminal))) {
                        String pos = terminal.getPos();
                        NT mother = node.getMother();
                        String mother_cat = "";

                        if (mother != null) {
                            mother_cat = mother.getCat();
                        }

                        if ((!(SyntaxTools.isNpLikeNode(mother))) ||
                                (mother_cat.equals("CNP"))) {
                            value = true;
                        }
                    }
                }
            } else {
                //cast GraphNode to Non-Terminal
                NT non_terminal = (NT) node;
                value = SyntaxTools.isNpLikeNode(non_terminal);
            }
        }

        return value;
    } // isMarkable()

    /**
     * Main: For testing purposes.
     *
     * @param args Test input (span)
     */
    public static void main(String[] args) {
        System.out.print("Input:  " + args[0] + "\nOutput: ");

        ArrayList list = parseSpan(args[0]);

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + ",");
        }

        System.out.println("\n");
    }
}
