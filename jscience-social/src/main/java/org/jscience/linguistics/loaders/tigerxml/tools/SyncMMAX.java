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
package org.jscience.linguistics.loaders.tigerxml.tools;

import org.jscience.linguistics.loaders.tigerxml.GraphNode;
import org.jscience.linguistics.loaders.tigerxml.NT;
import org.jscience.linguistics.loaders.tigerxml.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides static methods for synchronizing and converting data structures between TigerXML and MMAX.
 */
public class SyncMMAX {
    /**
     * Expand a given span (String) and return a List containing all ID Strings.
     */
    public static final List<String> parseSpan(String span) {
        if (span == null) return new ArrayList<>();
        StringBuilder currentSpan = new StringBuilder();
        List<String> spanList = new ArrayList<>();
        int spanLen = span.length();

        for (int i = 0; i < spanLen; i++) {
            if (span.charAt(i) != ',') {
                currentSpan.append(span.charAt(i));
            } else {
                List<String> fragList = parseSpanFragment(currentSpan.toString());
                spanList.addAll(fragList);
                currentSpan = new StringBuilder();
            }
        }
        spanList.addAll(parseSpanFragment(currentSpan.toString()));
        return spanList;
    }

    private static List<String> parseSpanFragment(String span) {
        List<String> newWordsIDList = new ArrayList<>();
        if (span == null || span.isEmpty()) return newWordsIDList;

        if (span.indexOf("..") == -1) {
            newWordsIDList.add(span.trim());
            return newWordsIDList;
        }

        String nameSpace = span.substring(0, span.indexOf("_") + 1);
        String firstIDString = span.substring(0, span.indexOf("."));
        String lastIDString = span.substring(span.lastIndexOf(".") + 1);
        
        try {
            int firstIDInteger = Integer.parseInt(firstIDString.substring(firstIDString.indexOf("_") + 1));
            int lastIDInteger = Integer.parseInt(lastIDString.substring(lastIDString.indexOf("_") + 1));

            for (int i = firstIDInteger; i <= lastIDInteger; i++) {
                newWordsIDList.add(nameSpace + i);
            }
        } catch (Exception e) {
            // Log or ignore malformed fragment
        }

        return newWordsIDList;
    }

    /**
     * Condense a given span (String) and return the condensed span (String).
     */
    public static final String condenseSpan(String inSpan) {
        List<String> idStrings = parseSpan(inSpan);
        return condenseSpanRecurse(new ArrayList<>(idStrings));
    }

    private static String condenseSpanRecurse(List<String> idStrings) {
        if (idStrings == null || idStrings.isEmpty()) return "";
        if (idStrings.size() == 1) return idStrings.get(0);

        String currentId = idStrings.get(0);
        String currentNameSpace = currentId.substring(0, currentId.indexOf("_"));
        int currentNum = Integer.parseInt(currentId.substring(currentId.indexOf("_") + 1));

        String nextId = idStrings.get(1);
        String nextNameSpace = nextId.substring(0, nextId.indexOf("_"));
        int nextNumber = Integer.parseInt(nextId.substring(nextId.indexOf("_") + 1));

        if (!currentNameSpace.equals(nextNameSpace) || (nextNumber != (currentNum + 1))) {
            idStrings.remove(0);
            return currentId + "," + condenseSpanRecurse(idStrings);
        }

        String span = currentId + "..";
        String spanRest = nextId;
        idStrings.remove(0); // current
        idStrings.remove(0); // next
        
        int u = 0;
        while (!idStrings.isEmpty()) {
            nextId = idStrings.get(0);
            nextNameSpace = nextId.substring(0, nextId.indexOf("_"));
            nextNumber = Integer.parseInt(nextId.substring(nextId.indexOf("_") + 1));

            if (nextNameSpace.equals(currentNameSpace) && (nextNumber == (currentNum + 2 + u))) {
                spanRest = nextId;
                idStrings.remove(0);
                u++;
            } else {
                break;
            }
        }
        
        String result = span + spanRest;
        if (!idStrings.isEmpty()) {
            result += "," + condenseSpanRecurse(idStrings);
        }
        return result;
    }

    /**
     * Defines the concept of a markable according to the MMAX guidelines.
     */
    public static final boolean isMarkable(GraphNode node) {
        if (node == null) return false;
        if (SyntaxTools.isApposition(node)) return false;

        if (node.isTerminal()) {
            T terminal = (T) node;
            if (SyntaxTools.isAttributiveMarkablePronoun(terminal)) return true;
            
            if (SyntaxTools.isNoun(terminal) || SyntaxTools.isSubstitutingMarkablePronoun(terminal)) {
                NT mother = node.getMother();
                String mother_cat = (mother != null) ? mother.getCat() : "";
                if (!SyntaxTools.isNpLikeNode(mother) || "CNP".equals(mother_cat)) {
                    return true;
                }
            }
        } else {
            return SyntaxTools.isNpLikeNode((NT) node);
        }
        return false;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println("Input:  " + args[0]);
            List<String> list = parseSpan(args[0]);
            System.out.println("Output: " + String.join(",", list));
        }
    }
}
