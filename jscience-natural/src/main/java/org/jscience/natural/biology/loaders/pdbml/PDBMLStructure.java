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

package org.jscience.natural.biology.loaders.pdbml;

import java.util.*;

/** Represents a macromolecular structure from PDBML.
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class PDBMLStructure {
    private String entryId;
    private String title;
    private String depositionDate;
    private String experimentalMethod;
    private double resolution;
    
    private final List<AtomSite> atoms = new ArrayList<>();
    private final List<PolymerEntity> entities = new ArrayList<>();
    private final List<SecondaryStructure> secondaryStructures = new ArrayList<>();

    public String getEntryId() { return entryId; }
    public void setEntryId(String id) { this.entryId = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDepositionDate() { return depositionDate; }
    public void setDepositionDate(String date) { this.depositionDate = date; }
    
    public String getExperimentalMethod() { return experimentalMethod; }
    public void setExperimentalMethod(String method) { this.experimentalMethod = method; }
    
    public double getResolution() { return resolution; }
    public void setResolution(double r) { this.resolution = r; }
    
    public void addAtom(AtomSite atom) { if (atom != null) atoms.add(atom); }
    public List<AtomSite> getAtoms() { return Collections.unmodifiableList(atoms); }
    
    public void addEntity(PolymerEntity e) { if (e != null) entities.add(e); }
    public List<PolymerEntity> getEntities() { return Collections.unmodifiableList(entities); }
    
    public void addSecondaryStructure(SecondaryStructure ss) { if (ss != null) secondaryStructures.add(ss); }
    public List<SecondaryStructure> getSecondaryStructures() { return Collections.unmodifiableList(secondaryStructures); }
    
    /** Get unique chain IDs. */
    public Set<String> getChainIds() {
        Set<String> chains = new LinkedHashSet<>();
        for (AtomSite atom : atoms) {
            if (atom.getChainId() != null) chains.add(atom.getChainId());
        }
        return chains;
    }
    
    /** Get atoms for a specific chain. */
    public List<AtomSite> getAtomsForChain(String chainId) {
        List<AtomSite> result = new ArrayList<>();
        for (AtomSite atom : atoms) {
            if (chainId.equals(atom.getChainId())) result.add(atom);
        }
        return result;
    }
    
    /** Get only CA (alpha carbon) atoms for backbone analysis. */
    public List<AtomSite> getBackboneAtoms() {
        List<AtomSite> result = new ArrayList<>();
        for (AtomSite atom : atoms) {
            if ("CA".equals(atom.getAtomName())) result.add(atom);
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "PDBMLStructure{id='" + entryId + "', atoms=" + atoms.size() + 
               ", chains=" + getChainIds().size() + "}";
    }
}

