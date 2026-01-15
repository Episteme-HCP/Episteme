/* Prioritised.java */
package org.jscience.computing.ai.blackboard.util;

/**
 * Prioritised interface.  This interface may be implemented by any class
 * with an associated priority value.  The only method required by this class
 * is priority(), which returns an integer priority value.
 *
 * @version:        1.2, 04/26/96
 * @author:        Paul Brown
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public interface Prioritised {
    /**
     * When implemented, this method will return a priority value for
     * the implementing class.
     *
     * @return DOCUMENT ME!
     */
    public int priority();
}
