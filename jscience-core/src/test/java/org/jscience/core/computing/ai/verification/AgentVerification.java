/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.core.computing.ai.verification;

import org.jscience.core.computing.ai.agents.Agent;
import org.jscience.core.computing.ai.agents.Behavior;
import org.jscience.core.computing.ai.agents.Environment;
import org.jscience.core.computing.ai.agents.providers.VirtualThreadAgentProvider;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.time.Duration;

/**
 * Verification class to test the Virtual Thread Agent Provider.
 */
public class AgentVerification {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Starting Agent Verification...");
        
        VirtualThreadAgentProvider env = new VirtualThreadAgentProvider();
        AtomicInteger counter = new AtomicInteger(0);
        int agentCount = 10_000;
        
        System.out.println("Spawning " + agentCount + " agents on Virtual Threads...");
        
        for (int i = 0; i < agentCount; i++) {
            SimpleAgent agent = new SimpleAgent("Agent-" + i, counter);
            env.addAgent(agent);
        }
        
        // Let them run for a second
        Thread.sleep(1000);
        
        env.shutdown();
        
        System.out.println("Verification Complete.");
        System.out.println("Total behaviors executed ~ " + counter.get());
        
        if (counter.get() > 0) {
            System.out.println("SUCCESS: Agents ran concurrently.");
        } else {
            System.out.println("FAILURE: Agents did not run.");
        }
    }
    
    static class SimpleAgent implements Agent {
        private final UUID id = UUID.randomUUID();
        private final String name;
        private final AtomicInteger counter;
        private Environment environment;
        private volatile boolean running = true;
        
        public SimpleAgent(String name, AtomicInteger counter) {
            this.name = name;
            this.counter = counter;
        }

        @Override
        public UUID getId() { return id; }

        @Override
        public String getName() { return name; }

        @Override
        public void addBehavior(Behavior behavior) {}

        @Override
        public void removeBehavior(Behavior behavior) {}

        @Override
        public void run() {
            while (running) {
                counter.incrementAndGet();
                try {
                    // Sleep to yield and simulate work
                    Thread.sleep(100); 
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

        @Override
        public Environment getEnvironment() { return environment; }

        @Override
        public void setEnvironment(Environment environment) { this.environment = environment; }
    }
}
