/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.computing.ai.agents;

import java.util.UUID;

/**
 * Represents an autonomous entity capable of perceiving its environment and acting upon it.
 * <p>
 * Agents are the core building blocks of multi-agent systems. They encapsulate state
 * and behavior, and interact with other agents through the {@link Environment}.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Agent extends Runnable {

    /**
     * Returns the unique identifier of this agent.
     *
     * @return the agent's UUID.
     */
    UUID getId();

    /**
     * Returns the name of this agent.
     *
     * @return the agent's name.
     */
    String getName();

    /**
     * Adds a behavior to this agent.
     * Behaviors define the agent's actions and responses.
     *
     * @param behavior the behavior to add.
     */
    void addBehavior(Behavior behavior);

    /**
     * Removes a behavior from this agent.
     *
     * @param behavior the behavior to remove.
     */
    void removeBehavior(Behavior behavior);

    /**
     * The main execution cycle of the agent.
     * Usually invoked by the agent container or scheduler.
     */
    @Override
    void run();

    /**
     * Helper to get the current environment this agent is in.
     * 
     * @return the environment or null if not registered.
     */
    Environment getEnvironment();

    /**
     * Sets the environment for this agent. Called by the environment upon registration.
     * 
     * @param environment the environment wrapper.
     */
    void setEnvironment(Environment environment);

    /**
     * Receives a message from the environment.
     * 
     * @param message the ACL message to process.
     */
    void receive(org.episteme.natural.computing.ai.agents.acl.ACLMessage message);

    /**
     * Interacts with another agent.
     *
     * @param other the agent to interact with.
     */
    void interact(Agent other);
}
