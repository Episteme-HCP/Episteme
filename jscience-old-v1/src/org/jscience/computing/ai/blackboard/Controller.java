/* Controller.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.Executable;
import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Enumeration;
import java.util.Hashtable;


/**
 * Controller class.  This represents the system controller component of
 * the blackboard architecture.  Instantiations of this class are responsible
 * for the execution of the blackboard system.
 *
 * @author:        Paul Brown
 * @version:        1.4, 04/26/96
 *
 * @see org.jscience.computing.ai.blackboard.util.PriorityQueue
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Controller extends org.jscience.computing.ai.blackboard.util.PriorityQueue {
    /** A reference to the blackboard. */
    private Hashtable blackboard;

    /** These entries are used by the controller to initiate execution. */
    private ValuePair[] initial_entries;

/**
         * Constructs a controller with the specified initial entries.
         * @param blackboard a reference to the blackboard
         * @param initial_entries blackboard entries for initiating execution
         */
    public Controller(Hashtable blackboard, ValuePair[] initial_entries) {
        this.blackboard = blackboard;
        this.initial_entries = initial_entries;
    }

    /**
     * Executes the blackboard system, this is the method called by the
     * BlackboardSystem class.
     *
     * @see org.jscience.computing.ai.blackboard.BlackboardSystem
     */
    public void execute() {
        BlackboardLevel level;
        ValuePair[] values;
        Enumeration levels;

        // put initial entries onto blackboard
        for (int i = 0; i < initial_entries.length; i++) {
            level = (BlackboardLevel) blackboard.get(initial_entries[i].key());
            values = (ValuePair[]) initial_entries[i].data();
            level.put(values);
        }

        // main loop
        while (!empty())
            ((Executable) get()).execute(null);

        // clear blackboard
        levels = blackboard.elements();

        while (levels.hasMoreElements())
            ((BlackboardLevel) levels.nextElement()).clear();
    }
}
