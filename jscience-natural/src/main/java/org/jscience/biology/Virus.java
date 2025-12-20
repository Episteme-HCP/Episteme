/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology;

import org.jscience.biology.genetics.BioSequence;

/**
 * Represents a virus - an infectious agent that replicates inside living cells.
 * <p>
 * Viruses are not true living organisms as they cannot reproduce independently.
 * They contain genetic material (DNA or RNA) and use host cells for
 * replication.
 * </p>
 * <p>
 * Lifecycle stages:
 * <ul>
 * <li>DORMANT - Outside host, in capsid form</li>
 * <li>ATTACHMENT - Binding to host cell receptor</li>
 * <li>PENETRATION - Entering host cell</li>
 * <li>REPLICATION - Using host machinery to replicate</li>
 * <li>ASSEMBLY - New virions being assembled</li>
 * <li>RELEASE - Exiting host cell (lysis or budding)</li>
 * </ul>
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Virus {

    /**
     * Virus lifecycle stages.
     */
    public enum Stage {
        DORMANT,
        ATTACHMENT,
        PENETRATION,
        REPLICATION,
        ASSEMBLY,
        RELEASE
    }

    /**
     * Type of genetic material.
     */
    public enum GenomeType {
        DNA_DOUBLE_STRANDED,
        DNA_SINGLE_STRANDED,
        RNA_DOUBLE_STRANDED,
        RNA_SINGLE_STRANDED_POSITIVE,
        RNA_SINGLE_STRANDED_NEGATIVE,
        RNA_REVERSE_TRANSCRIBING,
        DNA_REVERSE_TRANSCRIBING
    }

    /**
     * Virus morphology.
     */
    public enum Morphology {
        ICOSAHEDRAL,
        HELICAL,
        COMPLEX,
        ENVELOPED
    }

    private final String name;
    private final String family;
    private final GenomeType genomeType;
    private final Morphology morphology;
    private final BioSequence genome;
    private Stage currentStage;
    private final double capsidDiameterNm; // nanometers

    /**
     * Creates a new Virus.
     *
     * @param name             common or scientific name
     * @param family           virus family (e.g., Coronaviridae)
     * @param genomeType       type of genetic material
     * @param morphology       capsid shape
     * @param genome           genetic sequence
     * @param capsidDiameterNm diameter in nanometers
     */
    public Virus(String name, String family, GenomeType genomeType,
            Morphology morphology, BioSequence genome, double capsidDiameterNm) {
        this.name = name;
        this.family = family;
        this.genomeType = genomeType;
        this.morphology = morphology;
        this.genome = genome;
        this.capsidDiameterNm = capsidDiameterNm;
        this.currentStage = Stage.DORMANT;
    }

    public String getName() {
        return name;
    }

    public String getFamily() {
        return family;
    }

    public GenomeType getGenomeType() {
        return genomeType;
    }

    public Morphology getMorphology() {
        return morphology;
    }

    public BioSequence getGenome() {
        return genome;
    }

    public double getCapsidDiameterNm() {
        return capsidDiameterNm;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    /**
     * Advances to next lifecycle stage.
     */
    public void nextStage() {
        Stage[] stages = Stage.values();
        int next = (currentStage.ordinal() + 1) % stages.length;
        currentStage = stages[next];
    }

    /**
     * Sets lifecycle stage.
     */
    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    /**
     * Returns genome size in base pairs.
     */
    public int getGenomeSize() {
        return genome != null ? genome.getSequence().length() : 0;
    }

    /**
     * Checks if this is an RNA virus.
     */
    public boolean isRNAVirus() {
        return genomeType.name().startsWith("RNA");
    }

    /**
     * Checks if this is a retrovirus.
     */
    public boolean isRetrovirus() {
        return genomeType == GenomeType.RNA_REVERSE_TRANSCRIBING;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s, %s, %d bp, %.0f nm",
                name, family, genomeType, morphology, getGenomeSize(), capsidDiameterNm);
    }

    // ========== Common Virus Factory Methods ==========

    /**
     * Creates a SARS-CoV-2 (COVID-19) virus instance.
     */
    public static Virus sarsCov2() {
        return new Virus("SARS-CoV-2", "Coronaviridae",
                GenomeType.RNA_SINGLE_STRANDED_POSITIVE, Morphology.ENVELOPED,
                null, 120);
    }

    /**
     * Creates an Influenza A virus instance.
     */
    public static Virus influenzaA() {
        return new Virus("Influenza A", "Orthomyxoviridae",
                GenomeType.RNA_SINGLE_STRANDED_NEGATIVE, Morphology.ENVELOPED,
                null, 100);
    }

    /**
     * Creates an HIV-1 virus instance.
     */
    public static Virus hiv1() {
        return new Virus("HIV-1", "Retroviridae",
                GenomeType.RNA_REVERSE_TRANSCRIBING, Morphology.ENVELOPED,
                null, 120);
    }

    /**
     * Creates a Bacteriophage T4 instance.
     */
    public static Virus bacteriophageT4() {
        return new Virus("Bacteriophage T4", "Myoviridae",
                GenomeType.DNA_DOUBLE_STRANDED, Morphology.COMPLEX,
                null, 200);
    }
}
