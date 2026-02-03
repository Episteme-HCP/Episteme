/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.natural.computing.ai.agents.services;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.jscience.natural.computing.ai.agents.Agent;

/**
 * The Directory Facilitator (DF) provides "Yellow Pages" services for the MAS.
 * <p>
 * Agents can register services they provide and search for other agents providing specific services.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class DirectoryFacilitator {

    private static final DirectoryFacilitator INSTANCE = new DirectoryFacilitator();
    
    private final Map<String, Set<UUID>> services = new ConcurrentHashMap<>();
    private final Map<UUID, Agent> agentRegistry = new ConcurrentHashMap<>();

    private DirectoryFacilitator() {}

    public static DirectoryFacilitator getInstance() {
        return INSTANCE;
    }

    /**
     * Registers an agent and the services it provides.
     * @param agent the agent to register.
     * @param providedServices list of service names.
     */
    public void register(Agent agent, String... providedServices) {
        UUID agentId = agent.getId();
        agentRegistry.put(agentId, agent);
        for (String service : providedServices) {
            services.computeIfAbsent(service, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                    .add(agentId);
        }
    }

    /**
     * Unregisters an agent from all services.
     * @param agentId the ID of the agent to remove.
     */
    public void deregister(UUID agentId) {
        agentRegistry.remove(agentId);
        for (Set<UUID> providers : services.values()) {
            providers.remove(agentId);
        }
    }

    /**
     * Searches for agents providing a specific service.
     * @param service Name of the service.
     * @return List of agents providing the service.
     */
    public List<Agent> search(String service) {
        Set<UUID> providers = services.get(service);
        if (providers == null) return Collections.emptyList();
        
        List<Agent> result = new ArrayList<>();
        for (UUID id : providers) {
            Agent agent = agentRegistry.get(id);
            if (agent != null) {
                result.add(agent);
            }
        }
        return result;
    }
}
