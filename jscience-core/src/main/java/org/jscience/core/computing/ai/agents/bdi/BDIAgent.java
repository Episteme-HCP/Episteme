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
package org.jscience.core.computing.ai.agents.bdi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.jscience.core.computing.ai.agents.Agent;
import org.jscience.core.computing.ai.agents.Environment;

/**
 * A base implementation of a BDI (Belief-Desire-Intention) Agent.
 * 
 * <p>This agent follows the reasoning cycle: Perceive -> Update Beliefs -> Deliberate (Generate Desires) -> Plan (Select Intentions) -> Act.</p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public abstract class BDIAgent implements Agent {
    
    protected ConcurrentHashMap<String, Belief> beliefs = new ConcurrentHashMap<>();
    protected List<Desire> desires = new ArrayList<>();
    protected List<Intention> intentions = new ArrayList<>();
    protected List<Plan> planLibrary = new ArrayList<>();
    
    protected Environment environment;
    
    @Override
    public void run() {
        // Main agent loop intended to be called typically in a thread or step
        perceive();
        deliberate();
        plan();
        act();
    }
    
    /**
     * Perceive changes from the environment and update beliefs.
     */
    public abstract void perceive();
    
    /**
     * Deliberate to update desires based on current beliefs.
     */
    public void deliberate() {
        // Default implementation: check if desires are achieved and remove them
        desires.removeIf(Desire::isAchieved);
        // Concrete subclasses should add new desires here
    }
    
    /**
     * Select intentions (plans) to achieve desires.
     */
    public void plan() {
        // Simple planner: for each desire, find a plan
        for (Desire desire : desires) {
             boolean intentionExists = intentions.stream().anyMatch(i -> i.getGoal().equals(desire));
             if (!intentionExists) {
                 for (Plan plan : planLibrary) {
                     if (plan.isApplicable(new ArrayList<>(beliefs.values()), desire)) {
                         // Commit to this plan (create Intention)
                         // For simplicity in this base class, we execute immediately in act() or store as intention object
                         // Real BDI would create an Intention stack.
                     }
                 }
             }
        }
    }
    
    /**
     * Execute intentions.
     */
    public void act() {
        for (Intention intention : intentions) {
            intention.step();
        }
        intentions.removeIf(Intention::isDone);
    }

    public void addBelief(Belief belief) {
        beliefs.put(belief.getName(), belief);
    }
    
    public Belief getBelief(String name) {
        return beliefs.get(name);
    }
    
    public void addPlan(Plan plan) {
        planLibrary.add(plan);
    }
    
    @Override
    public void interact(Agent other) {
        // Agent communications
    }
    
    public void setEnvironment(Environment env) {
        this.environment = env;
    }
}
