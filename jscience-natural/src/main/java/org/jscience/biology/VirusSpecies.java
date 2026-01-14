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

package org.jscience.biology;

import java.util.*;

/**
 * Represents a virus species in biological taxonomy.
 * <p>
 * VirusSpecies provides taxonomy-level information about a virus,
 * distinguishing species definitions from individual virus particles
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class VirusSpecies {

    private final String name;
    private final String commonName;
    private final String family;
    private final String genus;
    private final Virus.GenomeType genomeType;
    private final Virus.Morphology morphology;
    private final List<String> hostSpecies = new ArrayList<>();
    private final Map<String, String> additionalClassification = new LinkedHashMap<>();
    private String description;
    private double typicalDiameterNm;

    /**
     * Creates a new VirusSpecies.
     *
     * @param name       scientific species name
     * @param family     virus family (e.g., Coronaviridae)
     * @param genus      genus (e.g., Betacoronavirus)
     * @param genomeType type of genetic material
     * @param morphology capsid shape
     */
    public VirusSpecies(String name, String family, String genus,
            Virus.GenomeType genomeType, Virus.Morphology morphology) {
        this.name = Objects.requireNonNull(name);
        this.family = family;
        this.genus = genus;
        this.genomeType = genomeType;
        this.morphology = morphology;
        this.commonName = null;
    }

    /**
     * Creates a new VirusSpecies with common name.
     */
    public VirusSpecies(String name, String commonName, String family, String genus,
            Virus.GenomeType genomeType, Virus.Morphology morphology) {
        this.name = Objects.requireNonNull(name);
        this.commonName = commonName;
        this.family = family;
        this.genus = genus;
        this.genomeType = genomeType;
        this.morphology = morphology;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCommonName() {
        return commonName;
    }

    public String getFamily() {
        return family;
    }

    public String getGenus() {
        return genus;
    }

    public Virus.GenomeType getGenomeType() {
        return genomeType;
    }

    public Virus.Morphology getMorphology() {
        return morphology;
    }

    public List<String> getHostSpecies() {
        return Collections.unmodifiableList(hostSpecies);
    }

    public String getDescription() {
        return description;
    }

    public double getTypicalDiameterNm() {
        return typicalDiameterNm;
    }

    // Setters and builders
    public VirusSpecies addHostSpecies(String host) {
        hostSpecies.add(host);
        return this;
    }

    public VirusSpecies setDescription(String description) {
        this.description = description;
        return this;
    }

    public VirusSpecies setTypicalDiameterNm(double diameter) {
        this.typicalDiameterNm = diameter;
        return this;
    }

    public VirusSpecies setClassification(String rank, String value) {
        additionalClassification.put(rank, value);
        return this;
    }

    /**
     * Creates a new individual virus particle of this species.
     *
     * @return a Virus instance
     */
    public Virus createIndividual() {
        return new Virus(name, family, genomeType, morphology, null, typicalDiameterNm);
    }

    @Override
    public String toString() {
        return String.format("VirusSpecies[%s (%s), %s, %s]",
                name, commonName != null ? commonName : "no common name",
                family, genomeType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof VirusSpecies other))
            return false;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // ========== Common Virus Species Factory Methods ==========

    public static VirusSpecies sarsCov2() {
        return new VirusSpecies("Severe acute respiratory syndrome coronavirus 2",
                "SARS-CoV-2", "Coronaviridae", "Betacoronavirus",
                Virus.GenomeType.RNA_SINGLE_STRANDED_POSITIVE, Virus.Morphology.ENVELOPED)
                .addHostSpecies("Homo sapiens")
                .addHostSpecies("Rhinolophus affinis")
                .setTypicalDiameterNm(120)
                .setDescription("Causative agent of COVID-19");
    }

    public static VirusSpecies influenzaA() {
        return new VirusSpecies("Influenza A virus", "Influenza A",
                "Orthomyxoviridae", "Alphainfluenzavirus",
                Virus.GenomeType.RNA_SINGLE_STRANDED_NEGATIVE, Virus.Morphology.ENVELOPED)
                .addHostSpecies("Homo sapiens")
                .addHostSpecies("Sus scrofa")
                .addHostSpecies("Aves")
                .setTypicalDiameterNm(100)
                .setDescription("Causes seasonal flu epidemics");
    }

    public static VirusSpecies hiv1() {
        return new VirusSpecies("Human immunodeficiency virus 1", "HIV-1",
                "Retroviridae", "Lentivirus",
                Virus.GenomeType.RNA_REVERSE_TRANSCRIBING, Virus.Morphology.ENVELOPED)
                .addHostSpecies("Homo sapiens")
                .setTypicalDiameterNm(120)
                .setDescription("Causative agent of AIDS");
    }

    public static VirusSpecies bacteriophageT4() {
        return new VirusSpecies("Escherichia virus T4", "Bacteriophage T4",
                "Myoviridae", "Tequatrovirus",
                Virus.GenomeType.DNA_DOUBLE_STRANDED, Virus.Morphology.COMPLEX)
                .addHostSpecies("Escherichia coli")
                .setTypicalDiameterNm(200)
                .setDescription("Well-studied bacterial virus");
    }
}


