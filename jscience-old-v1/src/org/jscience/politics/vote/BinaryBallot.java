package org.jscience.politics.vote;

import java.util.*;


/**
 * This class represents the different choices available to someone in a
 * specific vote session. You should vote using
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//current choices and options are unsorted: this is a ballot model
//you have to manage your GUI elsewhere and track the order there (and yes, it is important that all voters have the same GUI)
public class BinaryBallot extends Ballot {
    /** DOCUMENT ME! */
    private Map choices;

/**
     * Creates a new BinaryBallot object.
     */
    public BinaryBallot() {
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

    //  Returns whether the specified option for the specified choice is selected by the voter on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOptionSelected(String title, String option) {
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
                return ((Boolean) couple[1]).booleanValue();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //Sets the voter's selection for an option in a choice on the ballot.
    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param option DOCUMENT ME!
     * @param selected DOCUMENT ME!
     */
    public void setSelectionForOption(String title, String option,
        boolean selected) {
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
                couple[1] = new Boolean(selected);
                values.add(couple); //is this needed ?
            } else {
                values = (Vector) options;
                couple[0] = option;
                couple[1] = new Boolean(selected);
                values.add(couple);
            }
        } else {
            couple = new Object[2];
            couple[0] = option;
            couple[1] = new Boolean(selected);
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
        BinaryBallot ballot;

        ballot = new BinaryBallot();
        iterator1 = getChoices().iterator();

        while (iterator1.hasNext()) {
            choice = (String) iterator1.next();
            iterator2 = getOptionsForChoice(choice).iterator();

            while (iterator2.hasNext()) {
                option = (String) iterator2.next();
                ballot.setSelectionForOption(choice, option,
                    isOptionSelected(choice, option));
            }
        }

        return ballot;
    }
}
