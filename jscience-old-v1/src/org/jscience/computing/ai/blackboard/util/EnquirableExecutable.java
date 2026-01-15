/* EnquirableExecutable.java */
package org.jscience.computing.ai.blackboard.util;

/**
 * EnquirableExecutable interface.  By implementing this interface, a class
 * must provide an execute() method (specified by the org.jscience.computing.ai.blackboard.util.Executable
 * interface) and an accessor method for it's list of actions (actions()).
 *
 * @version:  1.2, 04/26/96 
 * @author:   Paul Brown
 *
 * @see org.jscience.computing.ai.blackboard.util.Executable#execute
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public interface EnquirableExecutable extends org.jscience.computing.ai.blackboard.util.Executable {
    /**
     * This is an accessor method implemented by an Executable,
     * allowing a querying class to access it's actions.
     *
     * @return the executable's actions
     */
    public Executable[] actions();
}
