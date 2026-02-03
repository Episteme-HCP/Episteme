/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ai.simulation;

import org.jscience.natural.computing.ai.agents.Agent;
import org.jscience.natural.computing.ai.agents.Behavior;

/**
 * Behavior implementing the Reynolds flocking rules (Cohesion, Alignment, Separation).
 */
public class FlockingBehavior implements Behavior {
    
    private Agent agent;
    private final FlockingAgent agentProxy;
    private final int index;

    public FlockingBehavior(FlockingAgent agentProxy, int index) {
        this.agentProxy = agentProxy;
        this.index = index;
    }

    @Override
    public void action() {
        agentProxy.computeNewHeading(index);
        agentProxy.update();
    }

    @Override
    public boolean done() {
        return false;
    }

    @Override
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    @Override
    public Agent getAgent() {
        return agent;
    }
}

