package org.jscience.tests.integration;

import org.jscience.natural.computing.ai.agents.Agent;
import org.jscience.natural.computing.ai.agents.Behavior;
import org.jscience.natural.computing.ai.agents.Environment;
import org.jscience.natural.computing.ai.agents.acl.ACLMessage;
import org.jscience.natural.computing.ai.agents.services.YellowPages;
import org.jscience.social.economics.money.Money;
import org.jscience.natural.computing.ai.agents.acl.Performative;
import org.jscience.natural.computing.ai.agents.services.ServiceDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test validating interactions between:
 * - Natural: Agents, Behaviors, ACL
 * - Social: Money, Economics
 * - Core: Quantities, Real
 */
public class EconomicAgentIntegrationTest {

    private YellowPages yellowPages;

    @BeforeEach
    void setUp() {
        yellowPages = YellowPages.getInstance();
    }

    @Test
    void testEconomicTrade() throws InterruptedException {
        // 1. Setup Agents
        EconomicAgent buyer = new EconomicAgent("Buyer", Money.usd(100));
        EconomicAgent seller = new EconomicAgent("Seller", Money.usd(0));

        List<ServiceDescription> buyerServices = java.util.Collections.singletonList(
            new ServiceDescription("trade", "buyer-service")
        );
        yellowPages.register(buyer.getId().toString(), buyerServices);

        List<ServiceDescription> sellerServices = java.util.Collections.singletonList(
            new ServiceDescription("trade", "seller-service")
        );
        yellowPages.register(seller.getId().toString(), sellerServices);

        // 2. Buyer logic (Behavior)
        // Simple behavior: If I have money, propose to buy from Seller
        buyer.addBehavior(new Behavior() {
            private boolean done = false;
            @Override
            public void action() {
                if (!done) {
                    ACLMessage proposal = new ACLMessage(Performative.PROPOSE);
                    proposal.setSender(buyer.getId().toString());
                    proposal.setReceiver(seller.getId().toString());
                    proposal.setContent("BUY Apple 10 USD");
                    ((EconomicAgent)getAgent()).send(proposal);
                    done = true;
                }
            }
            @Override
            public boolean done() { return done; }
            @Override
            public void setAgent(Agent agent) {}
            @Override
            public Agent getAgent() { return buyer; }
        });

        // 3. Seller logic (Behavior)
        // Simple behavior: If I receive PROPOSE, ACCEPT and transfer money
        seller.addBehavior(new Behavior() {
            private boolean done = false;
            @Override
            public void action() {
                ACLMessage msg = ((EconomicAgent)getAgent()).receive();
                if (msg != null && msg.getPerformative() == Performative.PROPOSE) {
                    // "Process" trade
                    // In a real system, this would be more complex. Here we cheat and directly manipulate for test speed.
                    // Or ideally, send a REPLY.
                    buyer.setWealth(buyer.getWealth().subtract(Money.usd(10)));
                    seller.setWealth(seller.getWealth().add(Money.usd(10)));
                    done = true;
                }
            }
            @Override
            public boolean done() { return done; }
            @Override
            public void setAgent(Agent agent) {}
            @Override
            public Agent getAgent() { return seller; }
        });

        // 4. Run Simulation Loop (Mocking a scheduler)
        for (int i = 0; i < 10; i++) {
            buyer.run();
            // Simulate message passing "delay" or transport (Mock Agent.send just puts in mailbox)
            // Here we assume direct reference for simplicity in this test scope
            transferMessages(buyer, seller);
            
            seller.run();
            transferMessages(seller, buyer);
        }

        // 5. Verify Social/Economic State
        assertEquals(Money.usd(90), buyer.getWealth());
        assertEquals(Money.usd(10), seller.getWealth());
    }

    // Helper to simulate message transport since we aren't using the full platform Service
    private void transferMessages(EconomicAgent from, EconomicAgent to) {
        ACLMessage msg;
        while ((msg = from.pollOutbox()) != null) {
            to.receive(msg);
        }
    }


    // Minimal Agent implementation for Testing
    static class EconomicAgent implements Agent {
        private final UUID id = UUID.randomUUID();
        private final String name;
        private Money wealth;
        private final BlockingQueue<ACLMessage> inbox = new LinkedBlockingQueue<>();
        private final BlockingQueue<ACLMessage> outbox = new LinkedBlockingQueue<>();
        private java.util.List<Behavior> behaviors = new java.util.ArrayList<>();

        public EconomicAgent(String name, Money wealth) {
            this.name = name;
            this.wealth = wealth;
        }

        public Money getWealth() { return wealth; }
        public void setWealth(Money wealth) { this.wealth = wealth; }

        @Override public UUID getId() { return id; }
        @Override public String getName() { return name; }
        @Override public void addBehavior(Behavior behavior) { behaviors.add(behavior); }
        @Override public void removeBehavior(Behavior behavior) { behaviors.remove(behavior); }
        @Override public Environment getEnvironment() { return null; }
        @Override public void setEnvironment(Environment environment) { }

        @Override public void receive(ACLMessage message) {
            inbox.offer(message);
        }
        
        // Custom for test
        public ACLMessage receive() {
            return inbox.poll();
        }

        // Custom for test
        public void send(ACLMessage message) {
            outbox.offer(message);
        }

        public ACLMessage pollOutbox() {
            return outbox.poll();
        }

        @Override public void interact(Agent other) { }

        @Override
        public void run() {
            for (Behavior b : behaviors) {
                if (!b.done()) b.action();
            }
        }
    }
}
