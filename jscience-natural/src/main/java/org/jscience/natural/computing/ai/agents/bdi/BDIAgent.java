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
package org.jscience.natural.computing.ai.agents.bdi;

import java.util.Queue;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import org.jscience.natural.computing.ai.agents.Agent;
import org.jscience.natural.computing.ai.agents.Behavior;
import org.jscience.natural.computing.ai.agents.Environment;
import org.jscience.natural.computing.ai.agents.acl.ACLMessage;

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
    
    protected UUID id = UUID.randomUUID();
    protected String name;
    protected ConcurrentHashMap<String, Belief> beliefs = new ConcurrentHashMap<>();
    protected List<Desire> desires = new ArrayList<>();
    protected List<Behavior> behaviors = new ArrayList<>();
    protected Queue<BDIEvent> eventQueue = new ConcurrentLinkedQueue<>();
    
    /**
     * The intention stack represents the active commitments of the agent.
     * Real BDI systems use a stack to allow for sub-goaling.
     */
    protected List<Intention> intentions = new ArrayList<>();
    protected List<Plan> planLibrary = new ArrayList<>();
    
    protected Environment environment;

    public BDIAgent(String name) {
        this.name = name;
    }

    @Override public UUID getId() { return id; }
    @Override public String getName() { return name; }
    
    @Override
    public void run() {
        // Broad perception
        perceive();

        // Handle events
        while (!eventQueue.isEmpty()) {
            handleEvent(eventQueue.poll());
        }

        // Run behaviors
        for (Behavior b : new ArrayList<>(behaviors)) {
            b.action();
            if (b.done()) {
                b.onEnd();
                behaviors.remove(b);
            }
        }

        // BDI Deliberation
        deliberate();
        plan();
        act();
    }

    /**
     * Handles an internal or external BDI event.
     */
    protected void handleEvent(BDIEvent event) {
        // Extensions should override this
        if (event.getType() == BDIEvent.Type.MESSAGE_RECEIVED) {
            System.out.println("Processing message event for " + getName());
        }
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
        // simple planner: for each desire, find a plan
        for (Desire desire : desires) {
             boolean intentionExists = intentions.stream().anyMatch(i -> i.getGoal().equals(desire));
             if (!intentionExists) {
                 for (Plan plan : planLibrary) {
                     if (plan.isApplicable(new ArrayList<>(beliefs.values()), desire)) {
                         // Commit to this plan (create Intention)
                         Intention intention = new BaseIntention(desire, plan, this);
                         intentions.add(intention);
                         break; // Found a plan for this desire
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
        eventQueue.add(new BDIEvent(BDIEvent.Type.BELIEF_ADDED, this, belief));
    }
    
    public Belief getBelief(String name) {
        return beliefs.get(name);
    }

    @Override
    public void addBehavior(Behavior behavior) {
        behavior.setAgent(this);
        behavior.onStart();
        behaviors.add(behavior);
    }

    @Override
    public void removeBehavior(Behavior behavior) {
        behaviors.remove(behavior);
    }
    
    public void addPlan(Plan plan) {
        planLibrary.add(plan);
    }

    /**
     * Internal implementation of a committed plan.
     */
    private static class BaseIntention implements Intention {
        private final Desire target;
        private final Plan plan;
        private final BDIAgent agent;
        private boolean done = false;

        BaseIntention(Desire target, Plan plan, BDIAgent agent) {
            this.target = target;
            this.plan = plan;
            this.agent = agent;
        }

        @Override public Desire getGoal() { return target; }
        @Override public void step() { 
            plan.execute(agent);
            done = true; // For now, plans execute in one step
        }
        @Override public boolean isDone() { return done; }
    }
    
    @Override
    public void receive(ACLMessage message) {
        // Trigger BDI event
        eventQueue.add(BDIEvent.message(message));
    }

    @Override
    public void interact(Agent other) {
        // Basic interaction
    }

    @Override
    public Environment getEnvironment() {
        return environment;
    }
    
    public void setEnvironment(Environment env) {
        this.environment = env;
    }
}
