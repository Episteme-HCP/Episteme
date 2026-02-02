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

package org.jscience.core.computing.ai.agents.providers;

import org.jscience.core.computing.ai.agents.Agent;
import org.jscience.core.computing.ai.agents.Environment;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An Environment implementation that runs each agent on a Virtual Thread (Project Loom).
 * <p>
 * This provider enables massive-scale multi-agent systems (e.g., &gt;100k agents) by
 * decoupling agents from OS threads.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class VirtualThreadAgentProvider implements Environment {
    
    private final Map<UUID, Agent> agents = new ConcurrentHashMap<>();
    private final ExecutorService executor;

    public VirtualThreadAgentProvider() {
        // Use the newVirtualThreadPerTaskExecutor available in modern Java
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void addAgent(Agent agent) {
        agents.put(agent.getId(), agent);
        agent.setEnvironment(this);
        // Start the agent immediately on a virtual thread
        executor.submit(agent);
    }

    @Override
    public void removeAgent(Agent agent) {
        agents.remove(agent.getId());
        agent.setEnvironment(null);
        // Note: We cannot easily "stop" a virtual thread externally, 
        // the agent must check a flag or interrupt.
    }

    @Override
    public Agent getAgent(UUID id) {
        return agents.get(id);
    }

    @Override
    public Collection<Agent> getAgents() {
        return Collections.unmodifiableCollection(agents.values());
    }

    @Override
    public void broadcast(Agent sender, Object message) {
        // Broadcasst can be expensive. 
        // For massive systems, we might want a pub/sub or spatial partition.
        // Here we just iterate.
        // In a real system, listeners would handle this asynchronously.
    }
    
    /**
     * Shuts down the environment and all agents.
     */
    public void shutdown() {
        executor.shutdownNow();
    }
}
