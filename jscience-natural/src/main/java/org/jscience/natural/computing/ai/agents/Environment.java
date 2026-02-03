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

package org.jscience.natural.computing.ai.agents;

import java.util.Collection;
import java.util.UUID;

/**
 * The environment in which agents live and interact.
 * <p>
 * It acts as a container, service registry, and communication medium.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Environment {

    /**
     * Registers an agent in this environment.
     *
     * @param agent the agent to add.
     */
    void addAgent(Agent agent);

    /**
     * Removes an agent from this environment.
     *
     * @param agent the agent to remove.
     */
    void removeAgent(Agent agent);
    
    /**
     * Finds an agent by its ID.
     * 
     * @param id the UUID of the agent.
     * @return the Agent if found, null otherwise.
     */
    Agent getAgent(UUID id);

    /**
     * Returns a read-only view of all agents in the environment.
     *
     * @return collection of agents.
     */
    Collection<Agent> getAgents();
    
    /**
     * Broadcasts a message (object payload) to all agents.
     * 
     * @param sender the sender agent (can be null for system messages).
     * @param message the content.
     */
    void broadcast(Agent sender, Object message);
}
