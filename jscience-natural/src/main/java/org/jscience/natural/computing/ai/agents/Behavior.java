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

/**
 * Represents a specific task or behavior exhibited by an {@link Agent}.
 * <p>
 * Behaviors can be simple (one-shot actions) or complex (finite state machines,
 * cyclic tasks).
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public interface Behavior {

    /**
     * Execution logic of the behavior.
     * This method is called repeatedly by the agent scheduler until {@link #done()} returns true.
     */
    void action();

    /**
     * Checks if the behavior has completed its task.
     *
     * @return true if the behavior is finished, false otherwise.
     */
    boolean done();
    
    /**
     * Lifecycle method called when the behavior starts.
     */
    default void onStart() {}

    /**
     * Lifecycle method called when the behavior ends.
     */
    default void onEnd() {}
    
    /**
     * Sets the agent this behavior belongs to.
     * @param agent the owner agent.
     */
    void setAgent(Agent agent);
    
    /**
     * Gets the agent this behavior belongs to.
     * @return the owner agent.
     */
    Agent getAgent();
}
