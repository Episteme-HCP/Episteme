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

import org.jscience.biology.genetics.BioSequence;
import org.jscience.util.identity.AbstractIdentifiedEntity;
import org.jscience.util.identity.SimpleIdentification;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Represents a virus - an infectious agent that replicates inside living cells.
 * Extends AbstractIdentifiedEntity to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Virus extends AbstractIdentifiedEntity {

    private static final long serialVersionUID = 1L;

    /**
     * Virus lifecycle stages.
     */
    public enum Stage {
        DORMANT, ATTACHMENT, PENETRATION, REPLICATION, ASSEMBLY, RELEASE
    }

    /**
     * Type of genetic material.
     */
    public enum GenomeType {
        DNA_DOUBLE_STRANDED, DNA_SINGLE_STRANDED, RNA_DOUBLE_STRANDED,
        RNA_SINGLE_STRANDED_POSITIVE, RNA_SINGLE_STRANDED_NEGATIVE,
        RNA_REVERSE_TRANSCRIBING, DNA_REVERSE_TRANSCRIBING
    }

    /**
     * Virus morphology.
     */
    public enum Morphology {
        ICOSAHEDRAL, HELICAL, COMPLEX, ENVELOPED
    }

    @Attribute
    private final String family;
    
    @Attribute
    private final GenomeType genomeType;
    
    @Attribute
    private final Morphology morphology;
    
    @Attribute
    private final BioSequence genome;
    
    @Attribute
    private Stage currentStage;
    
    @Attribute
    private final double capsidDiameterNm;

    public Virus(String name, String family, GenomeType genomeType,
            Morphology morphology, BioSequence genome, double capsidDiameterNm) {
        super(new SimpleIdentification(name.toLowerCase().replace(' ', '-')));
        setName(name);
        this.family = family;
        this.genomeType = genomeType;
        this.morphology = morphology;
        this.genome = genome;
        this.capsidDiameterNm = capsidDiameterNm;
        this.currentStage = Stage.DORMANT;
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

    public void nextStage() {
        Stage[] stages = Stage.values();
        int next = (currentStage.ordinal() + 1) % stages.length;
        currentStage = stages[next];
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public int getGenomeSize() {
        return genome != null ? genome.getSequence().length() : 0;
    }

    public boolean isRNAVirus() {
        return genomeType.name().startsWith("RNA");
    }

    public boolean isRetrovirus() {
        return genomeType == GenomeType.RNA_REVERSE_TRANSCRIBING;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s, %s, %d bp, %.0f nm",
                getName(), family, genomeType, morphology, getGenomeSize(), capsidDiameterNm);
    }

    // Factory methods
    public static Virus sarsCov2() {
        return new Virus("SARS-CoV-2", "Coronaviridae",
                GenomeType.RNA_SINGLE_STRANDED_POSITIVE, Morphology.ENVELOPED,
                null, 120);
    }

    public static Virus influenzaA() {
        return new Virus("Influenza A", "Orthomyxoviridae",
                GenomeType.RNA_SINGLE_STRANDED_NEGATIVE, Morphology.ENVELOPED,
                null, 100);
    }

    public static Virus hiv1() {
        return new Virus("HIV-1", "Retroviridae",
                GenomeType.RNA_REVERSE_TRANSCRIBING, Morphology.ENVELOPED,
                null, 120);
    }

    public static Virus bacteriophageT4() {
        return new Virus("Bacteriophage T4", "Myoviridae",
                GenomeType.DNA_DOUBLE_STRANDED, Morphology.COMPLEX,
                null, 200);
    }
}
