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

package org.jscience.ml.tigerxml.tools;

import org.jscience.ml.tigerxml.GraphNode;
import org.jscience.ml.tigerxml.T;

import java.util.*;

/**
 * Provides methods that might generally be useful when utilizing
 * the org.jscience.ml.tigerxml. This class is for static use.
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: GeneralTools.java,v 1.2 2007-10-21 17:47:09 virtualcall Exp $
 */

public class GeneralTools {

    /**
     * Returns the minimum of three integer values.
     */
    protected static int minimum(int a, int b, int c) {
        int min;
        min = a;
        if (b < min) {
            min = b;
        }
        if (c < min) {
            min = c;
        }
        return min;
    }

    /**
     * This static method accepts a list of <code>GraphNodes</code> and sorts it
     * according to linear precedence.
     * <p/>
     * In order to get a sorted copy of an <code>ArrayList<code> of Terminals
     * use {@link #sortTerminals(ArrayListunsortedTerminals)}
     *
     * @param unsortedNodes The <code>GraphNode ArrayList</code> to be sorted.
     */
    public static final ArrayList sortNodes(ArrayList unsortedNodes) {
        // Copy the input list
        ArrayList sortedNodes = (ArrayList) unsortedNodes.clone();
        // insert sorting is used because it is
        // assumed that the list is already
        // partially ordered
        for (int sorted = -1; sorted < (sortedNodes.size() - 1); sorted++) {
            int current_pos = sorted + 1;
            GraphNode current_item = (GraphNode) sortedNodes.get(current_pos);
            for (int i = 0; i <= sorted; i++) {
                GraphNode item_i = (GraphNode) sortedNodes.get(i);
                if (current_item.before(item_i)) {
                    //insert current item at i and remove
                    //it from it previous position
                    sortedNodes.remove(current_pos);
                    sortedNodes.add(i, current_item);
                    break;
                }
            } // for i
        } // for sorted
        return sortedNodes;
    } // method sortNodes

    /**
     * Sorts a given list of Terminals according to linear precedence. Applies
     * Insertion Sort by using the position in the sentence as the sort key.
     * Returns a sorted <code>ArrayList</code> without touching the original
     * <code>ArrayList</code>.
     *
     * @param unsortedTerminals The ArrayList of terminal nodes to be sorted
     * @return sortedTerminals The sorted version of unsortedTerminals
     */
    public static final ArrayList sortTerminals(ArrayList unsortedTerminals) {
        // Make sure that the ArrayList contains nothing but terminals
        T protoT = new T();
        for (int i = 0; i < unsortedTerminals.size(); i++) {
            if (unsortedTerminals.get(i).getClass() != protoT.getClass()) {
                System.err.println("org.jscience.ml.tigerxml.tools.GeneralTools.sortTerminals"
                        + "(ArrayList):");
                System.err.println("\tError: "
                        + "Applicable to ArrayLists of T objects only.");
                System.err.println("\tArgument: " + unsortedTerminals);
                break;
            }
        }
        // Create an empty ArrayList of length this.terminals.size()
        ArrayList sortedTerminals = new ArrayList(unsortedTerminals.size());
        // Copy the list to a temp list, sorting it
        for (int i = 0; i < unsortedTerminals.size(); i++) {
            T terminal2Insert = (T) unsortedTerminals.get(i);
            boolean inserted = false;
            for (int j = 0; j < sortedTerminals.size() - 1; j++) {
                T currentTerminal = (T) sortedTerminals.get(j);
                T nextTerminal = (T) sortedTerminals.get(j + 1);
                if (terminal2Insert.getPosition() < currentTerminal.getPosition()) {
                    sortedTerminals.add(j, terminal2Insert);
                    inserted = true;
                    break;
                } // if
                if ((terminal2Insert.getPosition() > currentTerminal.getPosition())
                        && terminal2Insert.getPosition() < nextTerminal.getPosition()) {
                    sortedTerminals.add(j + 1, terminal2Insert);
                    inserted = true;
                    break;
                } // if
            } // for j
            if (inserted == false) {
                sortedTerminals.add(terminal2Insert);
                inserted = true;
            }
        } // for i
        return sortedTerminals;
    } // sortBySentenceOrder()

    /**
     * Compute the Minumum Edit Distance between two <code>ArrayLists</code>.
     * The returned integer is the number of operations
     * (substitution, deletion or insertion) necessary to transform one list to
     * the other. Note that the objects in the <code>ArrayLists</code> are
     * treated as identical if and only if they refer to the same object
     * (<code>listA.get(i)==listB.get(j)</code> has the value <code>true</code>).
     * <p/>
     * The Minimum Edit Distance has been used as a measure for similarity
     * between strings. For a detailed description of the algorithm see:
     * <p/>
     * Robert A. Wagner and Michael J. Fischer. 1974.<br>
     * The string-to-string correction problem.<br>
     * Journal of the ACM, 21(1):168 173.<br>
     *
     * @param listA The first <code>ArrayList</code>.
     * @param listB The second <code>ArrayList</code>.
     * @return The minimum number of operations to transform <code>listA</code>
     *         to <code>listB</code>.
     */
    public static final int minEditDistance(ArrayList listA, ArrayList listB) {
        int d[][]; // matrix
        int n; // length of listA
        int m; // length of listB
        int i; // iterates through listA
        int j; // iterates through listB
        Object a_i; // ith object of listA
        Object b_j; // jth object of listB
        int cost; // cost
        // Step 1
        n = listA.size();
        m = listB.size();
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
            a_i = (Object) listA.get(i - 1);
            // Step 4
            for (j = 1; j <= m; j++) {
                b_j = (Object) listB.get(j - 1);
                // Step 5
                if (a_i.equals(b_j)) {
                    cost = 0;
                } else {
                    cost = 1;
                }
                // Step 6
                d[i][j] = minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
                        d[i - 1][j - 1] + cost);
            }
        }
        // Step 7
        return d[n][m];
    }

    /**
     * Converts a time value given in ms into minutes and seconds.
     *
     * @param time A numerical value (long) representing milliseconds.
     * @return A string like this: "15m30.110s"
     */
    public static String timeConvert(long time) {
        double rest;
        double t = (double) time;
        long hours = (long) t / 3600000;
        rest = t - hours * 3600000;
        long minutes = (long) rest / 60000;
        rest = rest - minutes * 60000;
        double seconds = rest / 1000;
        if (hours != 0) {
            return hours + "h" + minutes + "m" + seconds + "s";
        }
        return minutes + "m" + seconds + "s";
    } // timeConvert()

    /**
     * Generate and return a string with current date and time.
     *
     * @return A <code>String</code> containing the current date and time.
     */
    public static final String getTimeStamp() {
        GregorianCalendar cal = new GregorianCalendar();
        String[] weekDays = {
                "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String time = new String(weekDays[cal.get(Calendar.DAY_OF_WEEK) - 1] + " "
                + cal.get(Calendar.YEAR) + "/"
                + (cal.get(Calendar.MONTH) + 1) + "/"
                + cal.get(Calendar.DATE) + " "
                + cal.get(Calendar.HOUR_OF_DAY) + ":"
                + cal.get(Calendar.MINUTE) + ":"
                + cal.get(Calendar.SECOND) + " (+"
                + cal.get(Calendar.MILLISECOND) + " ms) "
                + cal.getTimeZone().getDisplayName());
        return time;
    } // getTimeStamp()

    /**
     * Converts a <code>Map</code> to a <code>String</code>. Maps can be
     * AbstractMap, Attributes, HashMap, Hashtable, IdentityHashMap,
     * RenderingHints, TreeMap or WeakHashMap
     *
     * @param map The Map to be printed.
     * @return The String representing the given Map.
     */
    public static String map2String(Map map) {
        return map2String(map, "");
    } // map2String()

    /**
     * Converts a <code>Map</code> to a <code>String</code>. Maps can be
     * AbstractMap, Attributes, HashMap, Hashtable, IdentityHashMap,
     * RenderingHints, TreeMap or WeakHashMap. Use <code>prefix</code>
     * to add a String in front of each line.
     *
     * @param map    The Map to be printed.
     * @param prefix A String preceding each line.
     * @return The String representing the given Map.
     */
    public static String map2String(Map map, String prefix) {
        Iterator iterator = map.keySet().iterator();
        StringBuffer sb = new StringBuffer();
        while (iterator.hasNext()) {
            String key = "";
            String value = "";
            key = iterator.next().toString();
            value = map.get(key).toString();
            sb.append(prefix);
            sb.append(key);
            sb.append(": ");
            sb.append(value);
            sb.append("\n"); //todo: use the OS-dependent line-sep.
        } // while
        return sb.toString();
    } // map2String()

} // class
