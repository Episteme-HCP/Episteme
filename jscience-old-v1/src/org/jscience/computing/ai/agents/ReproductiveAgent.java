package org.jscience.computing.ai.agents;

import java.util.Set;


/**
 * The ReproductiveAgent interface provides a better support for social agents.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface ReproductiveAgent extends MobileAgent {
    /** DOCUMENT ME! */
    public final static int BABY = 0; //unable to feed/survive by itself

    /** DOCUMENT ME! */
    public final static int YOUNG = 1; //unable to reproduce

    /** DOCUMENT ME! */
    public final static int ADULT = 2; //able to reproduce

    /** DOCUMENT ME! */
    public final static int OLD = 3; //less strong

    /** DOCUMENT ME! */
    public final static int DEAD = 4; //but still not removed from the environment

    //you can use the constants above or some other (complementary) paradigm (for example: sex, hungerLevel, moveAbility, sight, friends, wealth...)
    /** DOCUMENT ME! */
    public final static int EAT = 2; //non exclusive

    /** DOCUMENT ME! */
    public final static int REPRODUCE = 4;

    //reproduction needing multiple partners at once and/or producing many offsprings
    /**
     * DOCUMENT ME!
     *
     * @param agents DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set reproduce(Set agents); //reproduction with other agents at a time, seeding new agents, agents must be part of this environment. When reproducing, the agent is firing the corresponding action
}
