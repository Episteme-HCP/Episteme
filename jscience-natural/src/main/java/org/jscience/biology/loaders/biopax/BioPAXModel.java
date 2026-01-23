/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** BioPAX model containing pathways, entities, and interactions. */
public class BioPAXModel {
    private final List<Pathway> pathways = new ArrayList<>();
    private final Map<String, PhysicalEntity> entities = new LinkedHashMap<>();
    private final List<Interaction> interactions = new ArrayList<>();

    public void addPathway(Pathway p) { if (p != null) pathways.add(p); }
    public List<Pathway> getPathways() { return Collections.unmodifiableList(pathways); }
    
    public void addEntity(PhysicalEntity e) { 
        if (e != null && e.getRdfId() != null) entities.put(e.getRdfId(), e); 
    }
    public Collection<PhysicalEntity> getEntities() { return Collections.unmodifiableCollection(entities.values()); }
    public PhysicalEntity getEntity(String id) { return entities.get(id); }
    
    public void addInteraction(Interaction i) { if (i != null) interactions.add(i); }
    public List<Interaction> getInteractions() { return Collections.unmodifiableList(interactions); }
    
    public List<PhysicalEntity> getProteins() {
        List<PhysicalEntity> result = new ArrayList<>();
        for (PhysicalEntity e : entities.values()) {
            if ("Protein".equals(e.getType())) result.add(e);
        }
        return result;
    }
    
    public List<Interaction> getReactions() {
        List<Interaction> result = new ArrayList<>();
        for (Interaction i : interactions) {
            if ("BiochemicalReaction".equals(i.getType())) result.add(i);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "BioPAXModel{pathways=" + pathways.size() + 
               ", entities=" + entities.size() + 
               ", interactions=" + interactions.size() + "}";
    }
}
