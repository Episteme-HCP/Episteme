/* Executable.java */
package org.jscience.computing.ai.blackboard.util;

/**
 * Executable interface.  To implement this interface, a class must provide an
 * execute() method.
 *
 * @version:        1.2, 04/26/96
 * @author:        Paul Brown
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public interface Executable {
    /**
     * The execute method definition specifies a parameter of type
     * Object (this may be a subclass i.e. any other class).
     *
     * @param arg DOCUMENT ME!
     */
    void execute(Object arg);
}
