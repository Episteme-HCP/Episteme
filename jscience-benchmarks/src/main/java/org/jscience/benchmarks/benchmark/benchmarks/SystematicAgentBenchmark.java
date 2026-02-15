package org.jscience.benchmarks.benchmark.benchmarks;

import org.jscience.benchmarks.benchmark.RunnableBenchmark;
import com.google.auto.service.AutoService;
import org.jscience.natural.computing.ai.agents.Agent;
import org.jscience.natural.computing.ai.agents.Behavior;
import org.jscience.natural.computing.ai.agents.Environment;
import org.jscience.natural.computing.ai.agents.acl.ACLMessage;
import org.jscience.natural.computing.ai.agents.providers.VirtualThreadAgentProvider;

import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Systematic Benchmark for Agent Systems.
 * Measures the scaling capability of Virtual Thread Agents.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 */
@AutoService(RunnableBenchmark.class)
public class SystematicAgentBenchmark implements RunnableBenchmark {

    private VirtualThreadAgentProvider environment;
    private static final int AGENT_COUNT = 1000;
    private CountDownLatch latch;

    @Override public String getId() { return "agents-scaling-1k"; }
    @Override public String getName() { return "Virtual Thread Agents (1k)"; }
    @Override public String getDescription() { return "Benchmarks startup and execution of 1000 concurrent agents using Virtual Threads."; }
    @Override public String getDomain() { return "AI / Multi-Agent Systems"; }
    @Override public String getAlgorithmType() { return "Agent Simulation"; }
    @Override public String getAlgorithmProvider() { return "VirtualThreadAgentProvider (Loom)"; }

    @Override
    public boolean isAvailable() {
        try {
            Class.forName("org.jscience.natural.computing.ai.agents.providers.VirtualThreadAgentProvider");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public void setup() {
        environment = new VirtualThreadAgentProvider();
        latch = new CountDownLatch(AGENT_COUNT);
    }

    @Override
    public void run() {
        // Create and start agents
        for (int i = 0; i < AGENT_COUNT; i++) {
            environment.addAgent(new BenchmarkAgent(latch));
        }
        
        try {
            // Wait for all agents to complete their initialization/run cycle
            boolean completed = latch.await(10, TimeUnit.SECONDS);
            if (!completed) {
                // System.err.println("Warning: Agent benchmark timed out waiting for agents.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void teardown() {
        if (environment != null) {
            environment.shutdown();
        }
    }

    @Override
    public int getSuggestedIterations() {
        return 5;
    }

    // Inner class for Benchmark Agent
    private static class BenchmarkAgent implements Agent {
        private final UUID id = UUID.randomUUID();
        private Environment env;
        private final CountDownLatch latch;

        public BenchmarkAgent(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override public UUID getId() { return id; }
        @Override public String getName() { return "Agent-" + id; }
        @Override public void addBehavior(Behavior behavior) {}
        @Override public void removeBehavior(Behavior behavior) {}
        
        @Override 
        public void run() {
            // Simulate agent "thinking"
            double x = 0;
            for(int i=0; i<1000; i++) x += Math.sin(i);
            
            // Signal completion
            latch.countDown();
        }

        @Override public Environment getEnvironment() { return env; }
        @Override public void setEnvironment(Environment environment) { this.env = environment; }
        @Override public void receive(ACLMessage message) {}
        @Override public void interact(Agent other) {}
    }
}
