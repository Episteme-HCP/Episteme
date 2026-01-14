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

package org.jscience.computing.ai.reinforcement.qlearning;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Q-table implementation
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class QTable {
    /** DOCUMENT ME! */
    private Hashtable mainTable;

    /** DOCUMENT ME! */
    private Vector stateHashtables;

    /** DOCUMENT ME! */
    private Hashtable bestActionTable;

    /** DOCUMENT ME! */
    private Hashtable tmpHashtable;

/**
     * Creates a q-table with default parameters
     */
    public QTable() {
        mainTable = new Hashtable();
        bestActionTable = new Hashtable();
        stateHashtables = new Vector();
    }

    /**
     * Updates q-table with given state, action and value information
     *
     * @param state state before the action is done
     * @param action action performed for given reward
     * @param value reinforcement taken after the action is done
     */
    public void updateValue(Object state, Object action, double value) {
        if (!mainTable.containsKey(state)) {
            stateHashtables.add(new Hashtable());
            mainTable.put(state, new Integer(stateHashtables.size() - 1));
            bestActionTable.put(state, action);

            // Get hashtable for this state
            tmpHashtable = (Hashtable) stateHashtables.elementAt(((Integer) mainTable.get(
                        state)).intValue());
        } else {
            // Get hashtable for this state
            tmpHashtable = (Hashtable) stateHashtables.elementAt(((Integer) mainTable.get(
                        state)).intValue());

            // if this value is greater than the best value found so far,
            // then make this action as the best value.
            double bestValue = ((Double) tmpHashtable.get(bestActionTable.get(
                        state))).doubleValue();

            if (bestValue < value) {
                bestActionTable.put(state, action);
            }
        }

        // Add this state, action and value
        tmpHashtable.put(action, new Double(value));
    }

    /**
     * Returns best action for given state
     *
     * @param state state to look for best action
     *
     * @return selected action
     *
     * @throws QTableEntryNotFoundException if the given state is not found in
     *         q-table
     */
    public Object getBestAction(Object state)
        throws QTableEntryNotFoundException {
        if (!bestActionTable.containsKey(state)) {
            throw new QTableEntryNotFoundException(
                "QTable does not have any entry for this state yet.");
        }

        return bestActionTable.get(state);
    }

    /**
     * Returns reinforcement value for given state and action
     *
     * @param state state to look for reinforcement value
     * @param action action to look for reinforcement value
     *
     * @return reinforcement value
     *
     * @throws QTableEntryNotFoundException if the given state-action pair can
     *         not be found in q-table
     */
    public double getValue(Object state, Object action)
        throws QTableEntryNotFoundException {
        if (!mainTable.containsKey(state)) {
            throw new QTableEntryNotFoundException(
                "QTable does not have any entry for this state yet.");
        }

        // Get hashtable for this state
        tmpHashtable = (Hashtable) stateHashtables.elementAt(((Integer) mainTable.get(
                    state)).intValue());

        if (!tmpHashtable.containsKey(action)) {
            throw new QTableEntryNotFoundException(
                "QTable does not have any entry for this state yet.");
        }

        return ((Double) tmpHashtable.get(action)).doubleValue();
    }

    /**
     * Returns the best reinforcement value for given state
     *
     * @param state state to look for the best reinforcement value
     *
     * @return the best reinforcement vlaue for given state
     *
     * @throws QTableEntryNotFoundException if the given state is not found in
     *         the q-table
     */
    public double getBestValue(Object state)
        throws QTableEntryNotFoundException {
        if (!bestActionTable.containsKey(state)) {
            throw new QTableEntryNotFoundException(
                "QTable does not have any entry for this state yet.");
        }

        // Get hashtable for this state
        tmpHashtable = (Hashtable) stateHashtables.elementAt(((Integer) mainTable.get(
                    state)).intValue());

        return ((Double) tmpHashtable.get(bestActionTable.get(state))).doubleValue();
    }

    /**
     * Chacks whether the q-table contains the given state
     *
     * @param state state to search in the q-table
     *
     * @return true if the state is found in the q-table else returns false
     */
    public boolean hasState(Object state) {
        if (!mainTable.containsKey(state)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Chacks whether the q-table contains the given state-action pair
     *
     * @param state state to search in the q-table
     * @param action action to search in the q-table
     *
     * @return true if the state is found in the q-table else returns false
     */
    public boolean hasStateActionPair(Object state, Object action) {
        try {
            // If we get an exception while trying to get the value
            // then this state+action pair does not have a corresponding value
            getValue(state, action);

            return true;
        } catch (QTableEntryNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns the number of different states in the q-table
     *
     * @return number of different states in the q-table
     */
    public int getNumberOfStates() {
        return mainTable.size();
    }

    /**
     * Returns different states in the q-table.
     *
     * @return different states in the q-table.
     */
    public Enumeration getStates() {
        return mainTable.keys();
    }

    /**
     * Returns all of the best actions for each states tried so far
     *
     * @return all of the best actions
     */
    public Collection getBestActions() {
        return bestActionTable.values();
    }

    /**
     * Returns the number of entries in q-table
     *
     * @return size of this q-table
     */
    public int getSize() {
        int size = 0;

        for (Enumeration e = mainTable.elements(); e.hasMoreElements();) {
            // Get hashtable for this state
            tmpHashtable = (Hashtable) stateHashtables.elementAt(((Integer) e.nextElement()).intValue());
            size = size + tmpHashtable.size();
        }

        return size;
    }

    // Other Methods
    /**
     * Clears the content of q-table
     */
    public void clear() {
        bestActionTable.clear();
        stateHashtables.clear();
        mainTable.clear();
    }
}
