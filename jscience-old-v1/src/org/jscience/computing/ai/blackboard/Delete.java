/* Delete.java */
package org.jscience.computing.ai.blackboard;

import java.util.Hashtable;


/**
 * Delete class.  A blackboard entry manipulation rule action class, used
 * to delete blackboard entries.
 *
 * @author:   Paul Brown
 * @version:  1.3, 04/26/96
 *
 * @see java.util.Observable#addObserver
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Delete extends java.util.Observable implements org.jscience.computing.ai.blackboard.util.Executable {
    /** A reference to the blackboard. */
    private Hashtable blackboard;

    /** The level which the action deletes from. */
    private Integer level;

    /** A variable identifier used to reference a bound entry identifier. */
    private Integer entry;

/**
         * Constructs a new delete action.
         * @param blackboard a reference to the blackboard
         * @param level the blackboard level from which this action deletes an
         * entry
         * @param entry a variable identifier for referencing a bound entry
         * identifier
         */
    public Delete(Hashtable blackboard, Integer level, Integer entry) {
        this.blackboard = blackboard;
        this.level = level;
        this.entry = entry;
    }

    /**
     * An accessor function for the level referenced by this action.
     *
     * @return a level identifier
     */
    public Integer level() {
        return (level);
    }

    /**
     * Deletes a specified entry from the blackboard.
     *
     * @param arg a hashtable of variable bindings
     */
    public void execute(Object arg) {
        Hashtable bindings = (Hashtable) arg;
        ((BlackboardLevel) blackboard.get(level)).remove((Integer) bindings.get(
                entry));
        setChanged();
        notifyObservers();
    }
}
