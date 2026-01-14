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

package org.jscience.politics.vote;

import java.util.*;


/**
 * This class represents the different choices available to someone in a
 * specific vote session. You should vote using numbers from the prefered to
 * the less prefered.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//current choices and options are unsorted: this is a ballot model
public class RankedBallot extends Ballot {
    /** DOCUMENT ME! */
    private Map choices;

/**
     * Creates a new RankedBallot object.
     */
    public RankedBallot() {
        choices = new HashMap();
    }

    // Adds a choice to the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     */
    public void addChoice(String title) {
        choices.put(title, new Vector());
    }

    //  Adds an option to a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     */
    public void addOptionToChoice(String title, String option) {
        Object options;
        Vector values;
        Object[] couple;

        options = choices.get(title);
        couple = new Object[2];
        couple[0] = option;
        couple[1] = new Boolean(false);

        if (options != null) {
            values = (Vector) options;
        } else {
            values = new Vector();
            values.add(couple);
        }

        choices.put(title, values);
    }

    // Returns the choices on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChoices() {
        return choices.keySet();
    }

    //  Returns a set of options for a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getOptionsForChoice(String title) {
        Object options;
        Iterator iterator;
        HashSet values;

        options = choices.get(title);

        if (options != null) {
            iterator = ((Vector) options).iterator();
            values = new HashSet();

            while (iterator.hasNext()) {
                values.add(((Object[]) iterator.next())[0]);
            }

            return values;
        } else {
            return new HashSet();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOptionSelected(String title, String option) {
        return getOptionSelection(title, option) > 0;
    }

    //  Returns whether the specified option for the specified choice is selected by the voter on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOptionSelection(String title, String option) {
        Object options;
        Iterator iterator;
        Object[] couple;
        boolean found;

        options = choices.get(title);

        if (options != null) {
            iterator = ((Vector) options).iterator();
            found = false;
            couple = new Object[2];

            while (iterator.hasNext() && !found) {
                couple = (Object[]) iterator.next();
                found = couple[0].equals(option);
            }

            if (found) {
                return ((Integer) couple[1]).intValue();
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    //Sets the voter's selection for an option in a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setOptionSelection(String title, String option, int value) {
        Object options;
        Iterator iterator;
        Object[] couple;
        boolean found;
        Vector values;

        options = choices.get(title);

        if (options != null) {
            iterator = ((Vector) options).iterator();
            found = false;
            couple = new Object[2];

            while (iterator.hasNext() && !found) {
                couple = (Object[]) iterator.next();
                found = couple[0].equals(option);
            }

            if (found) {
                values = (Vector) options;
                values.remove(couple); //is this needed ?
                couple[1] = new Integer(value);
                values.add(couple); //is this needed ?
            } else {
                values = (Vector) options;
                couple[0] = option;
                couple[1] = new Integer(value);
                values.add(couple);
            }
        } else {
            couple = new Object[2];
            couple[0] = option;
            couple[1] = new Integer(value);
            values = new Vector();
            values.add(couple);
            choices.put(title, values);
        }
    }

    //semi deep copy, strings ar NOT copied but choices are
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Iterator iterator1;
        Iterator iterator2;
        String choice;
        String option;
        RankedBallot ballot;

        ballot = new RankedBallot();
        iterator1 = getChoices().iterator();

        while (iterator1.hasNext()) {
            choice = (String) iterator1.next();
            iterator2 = getOptionsForChoice(choice).iterator();

            while (iterator2.hasNext()) {
                option = (String) iterator2.next();
                ballot.setOptionSelection(choice, option,
                    getOptionSelection(choice, option));
            }
        }

        return ballot;
    }
}
