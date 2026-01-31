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

package org.jscience.natural.biology.taxonomy;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jscience.natural.biology.Organ;
import org.jscience.natural.biology.Tissue;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.persistence.Relation;

/**
 * Represents a biological species with full taxonomic classification.
 * Uses the trait system for flexible attribute management.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 * Modernized to use extensible ConservationStatus.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Species implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    @Relation(type = Relation.Type.MANY_TO_ONE)
    private Species ancestor;

    @Attribute
    private ConservationStatus conservationStatus;
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Organ> organs = new HashSet<>();
    
    @Relation(type = Relation.Type.ONE_TO_MANY)
    private final Set<Tissue> tissues = new HashSet<>();

    // Taxonomic ranks
    @Attribute
    private String kingdom;
    @Attribute
    private String phylum;
    @Attribute
    private String taxonomicClass;
    @Attribute
    private String order;
    @Attribute
    private String family;
    @Attribute
    private String genus;
    @Attribute
    private String specificEpithet;

    public Species(String commonName, String scientificName) {
        this.id = new SimpleIdentification(scientificName);
        setName(Objects.requireNonNull(commonName, "Common name cannot be null"));
        this.conservationStatus = ConservationStatus.NOT_EVALUATED;
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getCommonName() {
        return getName();
    }

    public String getScientificName() {
        return id.toString();
    }

    public Species getAncestor() {
        return ancestor;
    }

    public void setAncestor(Species ancestor) {
        this.ancestor = ancestor;
    }

    public ConservationStatus getConservationStatus() {
        return conservationStatus;
    }

    public void setConservationStatus(ConservationStatus status) {
        this.conservationStatus = Objects.requireNonNull(status);
    }

    public String getKingdom() { return kingdom; }
    public void setKingdom(String kingdom) { this.kingdom = kingdom; }

    public String getPhylum() { return phylum; }
    public void setPhylum(String phylum) { this.phylum = phylum; }

    public String getTaxonomicClass() { return taxonomicClass; }
    public void setTaxonomicClass(String taxonomicClass) { this.taxonomicClass = taxonomicClass; }

    public String getOrder() { return order; }
    public void setOrder(String order) { this.order = order; }

    public String getFamily() { return family; }
    public void setFamily(String family) { this.family = family; }

    public String getGenus() { return genus; }
    public void setGenus(String genus) { this.genus = genus; }

    public String getSpecificEpithet() { return specificEpithet; }
    public void setSpecificEpithet(String specificEpithet) { this.specificEpithet = specificEpithet; }

    public void addAttribute(String name, String value) {
        setTrait(name, value);
    }

    public String getAttribute(String name) {
        Object val = getTrait(name);
        return val != null ? val.toString() : null;
    }

    public void addOrgan(Organ organ) {
        organs.add(Objects.requireNonNull(organ));
    }

    public Set<Organ> getOrgans() {
        return Collections.unmodifiableSet(organs);
    }

    public void addTissue(Tissue tissue) {
        tissues.add(Objects.requireNonNull(tissue));
    }

    public Set<Tissue> getTissues() {
        return Collections.unmodifiableSet(tissues);
    }

    public String getLineage() {
        StringBuilder sb = new StringBuilder();
        if (kingdom != null) sb.append(kingdom);
        if (phylum != null) sb.append(" > ").append(phylum);
        if (taxonomicClass != null) sb.append(" > ").append(taxonomicClass);
        if (order != null) sb.append(" > ").append(order);
        if (family != null) sb.append(" > ").append(family);
        if (genus != null) sb.append(" > ").append(genus);
        return sb.toString();
    }

    public boolean isEndangered() {
        return conservationStatus == ConservationStatus.VULNERABLE ||
                conservationStatus == ConservationStatus.ENDANGERED ||
                conservationStatus == ConservationStatus.CRITICALLY_ENDANGERED;
    }

    public boolean isExtinct() {
        return conservationStatus == ConservationStatus.EXTINCT ||
                conservationStatus == ConservationStatus.EXTINCT_IN_WILD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Species species)) return false;
        return Objects.equals(id, species.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getScientificName() + " (" + getCommonName() + ") [" + conservationStatus.getCode() + "]";
    }

    public static final Species HUMAN = new Species("Human", "Homo sapiens");
}


