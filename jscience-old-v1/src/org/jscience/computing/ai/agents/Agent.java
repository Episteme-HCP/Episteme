package org.jscience.computing.ai.agents;

/**
 * The Agent interface is the base class to define an autonoumous (possibly
 * reproductive and moving) element.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface Agent {
    /**
     * DOCUMENT ME!
     */
    public void initialize();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getState(); //returns the full internal state of the agent (as for an automaton)

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AgentEnvironment getEnvironment(); //this allows to perceive the surrounding environment

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     * @param parameters DOCUMENT ME!
     */
    public void fireAction(int action, Object parameters); //from the set of predefined agent actions

    /**
     * DOCUMENT ME!
     */
    public void step(); //this is the way an agent should switch between actions
}
