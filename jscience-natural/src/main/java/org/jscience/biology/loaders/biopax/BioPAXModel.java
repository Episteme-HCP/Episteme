/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** 
 * BioPAX model containing pathways, entities, and interactions.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BioPAXModel {
    private final List<BioPAXPathway> pathways = new ArrayList<>();
    private final Map<String, BioPAXPhysicalEntity> entities = new LinkedHashMap<>();
    private final List<BioPAXInteraction> interactions = new ArrayList<>();

    public void addPathway(BioPAXPathway p) { if (p != null) pathways.add(p); }
    public List<BioPAXPathway> getPathways() { return Collections.unmodifiableList(pathways); }
    
    public void addEntity(BioPAXPhysicalEntity e) { 
        if (e != null && e.getRdfId() != null) entities.put(e.getRdfId(), e); 
    }
    public Collection<BioPAXPhysicalEntity> getEntities() { return Collections.unmodifiableCollection(entities.values()); }
    public BioPAXPhysicalEntity getEntity(String id) { return entities.get(id); }
    
    public void addInteraction(BioPAXInteraction i) { if (i != null) interactions.add(i); }
    public List<BioPAXInteraction> getInteractions() { return Collections.unmodifiableList(interactions); }
    
    public List<BioPAXPhysicalEntity> getProteins() {
        List<BioPAXPhysicalEntity> result = new ArrayList<>();
        for (BioPAXPhysicalEntity e : entities.values()) {
            if ("Protein".equals(e.getType())) result.add(e);
        }
        return result;
    }
    
    public List<BioPAXInteraction> getReactions() {
        List<BioPAXInteraction> result = new ArrayList<>();
        for (BioPAXInteraction i : interactions) {
            if (i instanceof BioPAXBiochemicalReaction || "BiochemicalReaction".equals(i.getType())) result.add(i);
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
