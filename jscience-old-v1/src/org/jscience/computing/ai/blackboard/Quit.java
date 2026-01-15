/* Quit.java */
package org.jscience.computing.ai.blackboard;

/**
 * Quit class.  This class implements a terminating rule action, following
 * the execution of an instantiation of this class, the KBS will abandon
 * operation.
 *
 * @author:   Paul Brown
 * @version:  1.2, 04/26/96
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public class Quit implements org.jscience.computing.ai.blackboard.util.Executable {
    /** A reference to the system controller. */
    private Controller controller;

    /** A reference to the containing knowledge source. */
    private KnowledgeSource ks;

/**
         * Constructs a quit action.
         * @param controller a reference to the system controller
         * @param ks a reference to the containing knowledge source
         */
    public Quit(Controller controller, KnowledgeSource ks) {
        this.controller = controller;
        this.ks = ks;
    }

    /**
     * Executes the quit action, terminating the KBS.
     *
     * @param arg DOCUMENT ME!
     */
    public void execute(Object arg) {
        controller.clear();
        ks.clearAgenda();
    }
}
