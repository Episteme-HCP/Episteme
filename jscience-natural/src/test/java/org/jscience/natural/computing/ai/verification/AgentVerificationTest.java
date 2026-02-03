package org.jscience.natural.computing.ai.verification;

import org.jscience.natural.computing.ai.agents.Agent;
import org.jscience.natural.computing.ai.agents.Behavior;
import org.jscience.natural.computing.ai.agents.Environment;
import org.jscience.natural.computing.ai.agents.providers.VirtualThreadAgentProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Verification class to test the Virtual Thread Agent Provider.
 */
public class AgentVerificationTest {

    @Test
    public void testVirtualThreadAgents() throws InterruptedException {
        System.out.println("Starting Agent Verification...");
        
        VirtualThreadAgentProvider env = new VirtualThreadAgentProvider();
        AtomicInteger counter = new AtomicInteger(0);
        int agentCount = 1000; // Reduced from 10,000 for faster unit testing
        
        System.out.println("Spawning " + agentCount + " agents on Virtual Threads...");
        
        for (int i = 0; i < agentCount; i++) {
            SimpleAgent agent = new SimpleAgent("Agent-" + i, counter);
            env.addAgent(agent);
        }
        
        // Let them run for a short period
        Thread.sleep(500);
        
        env.shutdown();
        
        System.out.println("Verification Complete.");
        System.out.println("Total behaviors executed ~ " + counter.get());
        
        assertTrue(counter.get() > 0, "Agents should have executed some behaviors");
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
                    Thread.sleep(10); 
                } catch (InterruptedException e) {
                    running = false;
                }
            }
        }

        @Override
        public Environment getEnvironment() { return environment; }

        @Override
        public void setEnvironment(Environment environment) { this.environment = environment; }

        @Override
        public void receive(org.jscience.natural.computing.ai.agents.acl.ACLMessage message) {
            // No interaction for simple agent verification
        }

        @Override
        public void interact(Agent other) {
            // No interaction for simple agent verification
        }
    }
}
