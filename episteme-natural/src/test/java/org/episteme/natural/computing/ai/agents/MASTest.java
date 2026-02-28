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

import org.episteme.natural.computing.ai.agents.acl.ACLMessage;
import org.episteme.natural.computing.ai.agents.acl.Performative;
import org.episteme.natural.computing.ai.agents.bdi.BDIAgent;
import org.episteme.natural.computing.ai.agents.bdi.BDIEvent;
import org.episteme.natural.computing.ai.agents.services.ServiceDescription;
import org.episteme.natural.computing.ai.agents.services.YellowPages;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verification for Multi-Agent Systems (MAS).
 */
public class MASTest {

    @Test
    public void testYellowPagesAndBDI() {
        // Create a provider agent
        BDIAgent provider = new BDIAgent("Optimizer") {
            @Override public void perceive() {}
            @Override public void handleEvent(BDIEvent event) {
                if (event.getType() == BDIEvent.Type.MESSAGE_RECEIVED) {
                    ACLMessage msg = (ACLMessage) event.getData();
                    System.out.println(getName() + " received: " + msg.getContent());
                    if (msg.getPerformative() == Performative.REQUEST) {
                        ACLMessage reply = msg.createReply(Performative.INFORM);
                        reply.setContent("Optimized data result");
                        // In a real system, we'd send via environment. For test, we manual call
                        System.out.println(getName() + " replying to " + msg.getSender());
                    }
                }
            }
        };

        // Register with YellowPages
        ServiceDescription sd = new ServiceDescription("optimization", "GlobalOptimizer");
        YellowPages.getInstance().register(provider.getName(), Collections.singletonList(sd));

        // Discovery
        List<String> results = YellowPages.getInstance().search("optimization");
        assertEquals(1, results.size());
        assertEquals("Optimizer", results.get(0));

        // BDI Cycle Test
        ACLMessage req = new ACLMessage(Performative.REQUEST);
        req.setSender("Requester");
        req.setContent("Please optimize this");
        
        provider.receive(req); // This adds an event
        provider.run(); // This processes the event
    }

    @Test
    public void testBeliefEvents() {
        final boolean[] beliefTriggered = {false};
        BDIAgent agent = new BDIAgent("TestAgent") {
            @Override public void perceive() {}
            @Override public void handleEvent(BDIEvent event) {
                if (event.getType() == BDIEvent.Type.BELIEF_ADDED) {
                    beliefTriggered[0] = true;
                }
            }
        };

        agent.addBelief(new org.episteme.natural.computing.ai.agents.bdi.Belief() {
            @Override public String getName() { return "test"; }
            @Override public boolean isValid() { return true; }
            @Override public Object getValue() { return "value"; }
        });

        agent.run();
        assertTrue(beliefTriggered[0], "Belief addition ignored by reasoning cycle");
    }
}
