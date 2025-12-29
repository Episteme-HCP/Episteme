/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.biology.taxonomy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a biological species with full taxonomic classification.
 * <p>
 * Uses Linnaean taxonomy with the standard ranks:
 * Kingdom → Phylum → Class → Order → Family → Genus → Species
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Species {

    /**
     * IUCN Red List conservation status.
     */
    public enum ConservationStatus {
        NOT_EVALUATED("NE"),
        DATA_DEFICIENT("DD"),
        LEAST_CONCERN("LC"),
        NEAR_THREATENED("NT"),
        VULNERABLE("VU"),
        ENDANGERED("EN"),
        CRITICALLY_ENDANGERED("CR"),
        EXTINCT_IN_WILD("EW"),
        EXTINCT("EX");

        private final String code;

        ConservationStatus(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    private final String commonName;
    private final String scientificName;
    private final Map<String, String> attributes = new HashMap<>();
    private Species ancestor;
    private ConservationStatus conservationStatus;

    // Taxonomic ranks
    private String kingdom;
    private String phylum;
    private String taxonomicClass;
    private String order;
    private String family;
    private String genus;
    private String specificEpithet;

    public Species(String commonName, String scientificName) {
        this.commonName = commonName;
        this.scientificName = scientificName;
        this.conservationStatus = ConservationStatus.NOT_EVALUATED;
    }

    // ========== Getters ==========
    public String getCommonName() {
        return commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public Species getAncestor() {
        return ancestor;
    }

    public ConservationStatus getConservationStatus() {
        return conservationStatus;
    }

    public String getKingdom() {
        return kingdom;
    }

    public String getPhylum() {
        return phylum;
    }

    public String getTaxonomicClass() {
        return taxonomicClass;
    }

    public String getOrder() {
        return order;
    }

    public String getFamily() {
        return family;
    }

    public String getGenus() {
        return genus;
    }

    public String getSpecificEpithet() {
        return specificEpithet;
    }

    // ========== Setters ==========
    public void setAncestor(Species ancestor) {
        this.ancestor = ancestor;
    }

    public void setConservationStatus(ConservationStatus status) {
        this.conservationStatus = status;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public void setPhylum(String phylum) {
        this.phylum = phylum;
    }

    public void setTaxonomicClass(String taxonomicClass) {
        this.taxonomicClass = taxonomicClass;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public void setSpecificEpithet(String specificEpithet) {
        this.specificEpithet = specificEpithet;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * Returns full taxonomic lineage string.
     */
    public String getLineage() {
        StringBuilder sb = new StringBuilder();
        if (kingdom != null)
            sb.append(kingdom);
        if (phylum != null)
            sb.append(" > ").append(phylum);
        if (taxonomicClass != null)
            sb.append(" > ").append(taxonomicClass);
        if (order != null)
            sb.append(" > ").append(order);
        if (family != null)
            sb.append(" > ").append(family);
        if (genus != null)
            sb.append(" > ").append(genus);
        return sb.toString();
    }

    /**
     * Checks if this species is endangered (VU, EN, or CR).
     */
    public boolean isEndangered() {
        return conservationStatus == ConservationStatus.VULNERABLE ||
                conservationStatus == ConservationStatus.ENDANGERED ||
                conservationStatus == ConservationStatus.CRITICALLY_ENDANGERED;
    }

    /**
     * Checks if this species is extinct.
     */
    public boolean isExtinct() {
        return conservationStatus == ConservationStatus.EXTINCT ||
                conservationStatus == ConservationStatus.EXTINCT_IN_WILD;
    }

    @Override
    public String toString() {
        return scientificName + " (" + commonName + ") [" + conservationStatus.getCode() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Species other))
            return false;
        return scientificName.equals(other.scientificName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scientificName);
    }
}
