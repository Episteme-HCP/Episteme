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

package org.jscience.biology.loaders.biopax;

import java.util.*;

/** BioPAX model containing pathways, entities, and interactions.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
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
