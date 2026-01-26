/*
 * GeneralTools.java
 *
 * Created on September 20, 2003, 0:18 AM
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
import org.jscience.linguistics.loaders.tigerxml.T;

import java.util.*;

/**
 * Provides methods that might generally be useful when utilizing TigerXML.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 */
public class GeneralTools {

    /**
     * Returns the minimum of three integer values.
     */
    protected static int minimum(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    /**
     * This static method accepts a list of <code>GraphNodes</code> and sorts it
     * according to linear precedence.
     *
     * @param unsortedNodes The <code>List</code> of nodes to be sorted.
     * @return sorted list
     */
    public static <N extends GraphNode> List<N> sortNodes(List<N> unsortedNodes) {
        if (unsortedNodes == null) return null;
        List<N> sortedNodes = new ArrayList<>(unsortedNodes);
        
        // Use standard Collections.sort with custom comparator using before()
        sortedNodes.sort((n1, n2) -> {
            if (n1.before(n2)) return -1;
            if (n2.before(n1)) return 1;
            return 0;
        });
        
        return sortedNodes;
    }

    /**
     * Sorts a given list of Terminals according to linear precedence.
     */
    public static List<T> sortTerminals(List<T> unsortedTerminals) {
        if (unsortedTerminals == null) return null;
        List<T> sortedTerminals = new ArrayList<>(unsortedTerminals);
        
        sortedTerminals.sort(Comparator.comparingInt(T::getPosition));
        
        return sortedTerminals;
    }

    /**
     * Compute the Minimum Edit Distance between two Lists.
     */
    public static int minEditDistance(List<?> listA, List<?> listB) {
        int n = listA.size();
        int m = listB.size();
        if (n == 0) return m;
        if (m == 0) return n;
        
        int[][] d = new int[n + 1][m + 1];
        
        for (int i = 0; i <= n; i++) d[i][0] = i;
        for (int j = 0; j <= m; j++) d[0][j] = j;
        
        for (int i = 1; i <= n; i++) {
            Object a_i = listA.get(i - 1);
            for (int j = 1; j <= m; j++) {
                Object b_j = listB.get(j - 1);
                int cost = a_i.equals(b_j) ? 0 : 1;
                d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }
        return d[n][m];
    }

    /**
     * Converts a time value given in ms into minutes and seconds.
     */
    public static String timeConvert(long time) {
        long hours = time / 3600000;
        long minutes = (time % 3600000) / 60000;
        double seconds = (time % 60000) / 1000.0;
        if (hours != 0) {
            return hours + "h" + minutes + "m" + seconds + "s";
        }
        return minutes + "m" + seconds + "s";
    }

    /**
     * Generate and return a string with current date and time.
     */
    public static String getTimeStamp() {
        return new Date().toString();
    }

    /**
     * Converts a Map to a String.
     */
    public static String map2String(Map<?, ?> map) {
        return map2String(map, "");
    }

    /**
     * Converts a Map to a String with prefix.
     */
    public static String map2String(Map<?, ?> map, String prefix) {
        if (map == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            sb.append(prefix)
              .append(entry.getKey())
              .append(": ")
              .append(entry.getValue())
              .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
