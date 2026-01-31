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

package org.jscience.natural.biology;

import org.jscience.natural.biology.genetics.BioSequence;
import org.jscience.core.measure.Quantity;
import org.jscience.core.measure.Quantities;
import org.jscience.core.measure.Units;
import org.jscience.core.measure.quantity.Length;
import org.jscience.core.util.identity.ComprehensiveIdentification;
import org.jscience.core.util.identity.Identification;
import org.jscience.core.util.identity.SimpleIdentification;
import org.jscience.core.util.persistence.Attribute;
import org.jscience.core.util.persistence.Id;
import org.jscience.core.util.persistence.Persistent;
import org.jscience.core.util.ExtensibleEnum;
import org.jscience.core.util.EnumRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a virus - an infectious agent that replicates inside living cells.
 * Implements ComprehensiveIdentification to support dynamic traits and consistent identity.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
@Persistent
public class Virus implements ComprehensiveIdentification {

    private static final long serialVersionUID = 2L;

    @Id
    protected final Identification id;

    @Attribute
    protected final Map<String, Object> traits = new HashMap<>();

    /**
     * Virus lifecycle stages.
     */
    /**
     * Virus lifecycle stages.
     * Extensible enum since 2.0.
     */
    public static final class Stage extends ExtensibleEnum {
        private static final long serialVersionUID = 1L;
        
        public static final Stage DORMANT = new Stage("DORMANT", true);
        public static final Stage ATTACHMENT = new Stage("ATTACHMENT", true);
        public static final Stage PENETRATION = new Stage("PENETRATION", true);
        public static final Stage REPLICATION = new Stage("REPLICATION", true);
        public static final Stage ASSEMBLY = new Stage("ASSEMBLY", true);
        public static final Stage RELEASE = new Stage("RELEASE", true);
        
        static {
            EnumRegistry.register(Stage.class, DORMANT);
            EnumRegistry.register(Stage.class, ATTACHMENT);
            EnumRegistry.register(Stage.class, PENETRATION);
            EnumRegistry.register(Stage.class, REPLICATION);
            EnumRegistry.register(Stage.class, ASSEMBLY);
            EnumRegistry.register(Stage.class, RELEASE);
        }

        private final boolean builtIn;

        private Stage(String name, boolean builtIn) {
            super(name);
            this.builtIn = builtIn;
        }
        
        public Stage(String name) {
            super(name);
            this.builtIn = false;
            EnumRegistry.register(Stage.class, this);
        }

        @Override
        public boolean isBuiltIn() {
            return builtIn;
        }

        public static Stage valueOf(String name) {
            return (Stage) EnumRegistry.valueOf(Stage.class, name);
        }
        
        public static Stage[] values() {
             return EnumRegistry.values(Stage.class).toArray(new Stage[0]);
        }
    }

    @Attribute
    private final String family;
    
    @Attribute
    private final VirusGenomeType genomeType;
    
    @Attribute
    private final VirusMorphology morphology;
    
    @Attribute
    private final BioSequence genome;
    
    @Attribute
    private Stage currentStage;
    
    @Attribute
    private final Quantity<Length> capsidDiameter;

    public Virus(String name, String family, VirusGenomeType genomeType,
            VirusMorphology morphology, BioSequence genome, Quantity<Length> capsidDiameter) {
        this.id = new SimpleIdentification(name.toLowerCase().replace(' ', '-'));
        setName(name);
        this.family = family;
        this.genomeType = genomeType;
        this.morphology = morphology;
        this.genome = genome;
        this.capsidDiameter = capsidDiameter;
        this.currentStage = Stage.DORMANT;
    }

    public Virus(String name, String family, VirusGenomeType genomeType,
            VirusMorphology morphology, BioSequence genome, double capsidDiameterNm) {
        this(name, family, genomeType, morphology, genome, Quantities.create(capsidDiameterNm, Units.NANOMETER));
    }

    @Override
    public Identification getId() {
        return id;
    }

    @Override
    public Map<String, Object> getTraits() {
        return traits;
    }

    public String getFamily() {
        return family;
    }

    public VirusGenomeType getGenomeType() {
        return genomeType;
    }

    public VirusMorphology getMorphology() {
        return morphology;
    }

    public BioSequence getGenome() {
        return genome;
    }

    public Quantity<Length> getCapsidDiameter() {
        return capsidDiameter;
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
        return genomeType.equals(VirusGenomeType.RNA_REVERSE_TRANSCRIBING);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Virus virus)) return false;
        return Objects.equals(id, virus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s, %s, %d bp, %s",
                getName(), family, genomeType, morphology, getGenomeSize(), capsidDiameter);
    }

    // Factory methods
    public static Virus sarsCov2() {
        return new Virus("SARS-CoV-2", "Coronaviridae",
                VirusGenomeType.RNA_SINGLE_STRANDED_POSITIVE, VirusMorphology.ENVELOPED,
                null, 120);
    }

    public static Virus influenzaA() {
        return new Virus("Influenza A", "Orthomyxoviridae",
                VirusGenomeType.RNA_SINGLE_STRANDED_NEGATIVE, VirusMorphology.ENVELOPED,
                null, 100);
    }

    public static Virus hiv1() {
        return new Virus("HIV-1", "Retroviridae",
                VirusGenomeType.RNA_REVERSE_TRANSCRIBING, VirusMorphology.ENVELOPED,
                null, 120);
    }

    public static Virus bacteriophageT4() {
        return new Virus("Bacteriophage T4", "Myoviridae",
                VirusGenomeType.DNA_DOUBLE_STRANDED, VirusMorphology.COMPLEX,
                null, 200);
    }
}


