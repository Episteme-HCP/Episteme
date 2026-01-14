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
