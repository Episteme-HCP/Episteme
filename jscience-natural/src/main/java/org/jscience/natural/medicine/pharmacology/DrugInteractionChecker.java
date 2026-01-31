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

package org.jscience.natural.medicine.pharmacology;

import java.util.*;

/**
 * Drug interaction database and checker.
 */
public final class DrugInteractionChecker {

    private DrugInteractionChecker() {}

    public enum InteractionSeverity {
        CONTRAINDICATED,  // Never combine
        MAJOR,            // Avoid, serious harm possible
        MODERATE,         // Use with caution
        MINOR,            // Monitor
        UNKNOWN
    }

    public enum InteractionMechanism {
        PHARMACOKINETIC,  // Absorption, metabolism, excretion
        PHARMACODYNAMIC,  // Synergistic or antagonistic effects
        ADDITIVE,         // Combined effect
        UNKNOWN
    }

    public record Drug(
        String name,
        String genericName,
        String drugClass,
        String cyp450Metabolism,  // CYP3A4, CYP2D6, etc.
        boolean qtProlonging,
        boolean sedating,
        boolean anticoagulant
    ) {}

    public record DrugInteraction(
        Drug drug1,
        Drug drug2,
        InteractionSeverity severity,
        InteractionMechanism mechanism,
        String description,
        String recommendation
    ) {}

    // Interaction database
    private static final List<DrugInteraction> INTERACTION_DATABASE = new ArrayList<>();
    private static final Map<String, Drug> DRUG_DATABASE = new HashMap<>();

    static {
        // Add some common drugs
        Drug warfarin = new Drug("Warfarin", "warfarin", "Anticoagulant", "CYP2C9", false, false, true);
        Drug aspirin = new Drug("Aspirin", "acetylsalicylic acid", "NSAID", "None", false, false, true);
        Drug ibuprofen = new Drug("Ibuprofen", "ibuprofen", "NSAID", "CYP2C9", false, false, false);
        Drug metoprolol = new Drug("Metoprolol", "metoprolol", "Beta-blocker", "CYP2D6", false, false, false);
        Drug fluoxetine = new Drug("Prozac", "fluoxetine", "SSRI", "CYP2D6", false, false, false);
        Drug simvastatin = new Drug("Zocor", "simvastatin", "Statin", "CYP3A4", false, false, false);
        Drug erythromycin = new Drug("Erythromycin", "erythromycin", "Macrolide", "CYP3A4", true, false, false);
        Drug alprazolam = new Drug("Xanax", "alprazolam", "Benzodiazepine", "CYP3A4", false, true, false);
        Drug oxycodone = new Drug("OxyContin", "oxycodone", "Opioid", "CYP3A4", false, true, false);
        
        DRUG_DATABASE.put("warfarin", warfarin);
        DRUG_DATABASE.put("aspirin", aspirin);
        DRUG_DATABASE.put("ibuprofen", ibuprofen);
        DRUG_DATABASE.put("metoprolol", metoprolol);
        DRUG_DATABASE.put("fluoxetine", fluoxetine);
        DRUG_DATABASE.put("simvastatin", simvastatin);
        DRUG_DATABASE.put("erythromycin", erythromycin);
        DRUG_DATABASE.put("alprazolam", alprazolam);
        DRUG_DATABASE.put("oxycodone", oxycodone);
        
        // Add known interactions
        INTERACTION_DATABASE.add(new DrugInteraction(
            warfarin, aspirin, InteractionSeverity.MAJOR, InteractionMechanism.ADDITIVE,
            "Increased bleeding risk due to combined anticoagulant effects",
            "Avoid combination if possible; monitor INR closely if used together"
        ));
        
        INTERACTION_DATABASE.add(new DrugInteraction(
            warfarin, ibuprofen, InteractionSeverity.MAJOR, InteractionMechanism.PHARMACOKINETIC,
            "NSAIDs inhibit CYP2C9, increasing warfarin levels and bleeding risk",
            "Use acetaminophen as alternative; if NSAID needed, use lowest dose for shortest duration"
        ));
        
        INTERACTION_DATABASE.add(new DrugInteraction(
            simvastatin, erythromycin, InteractionSeverity.CONTRAINDICATED, InteractionMechanism.PHARMACOKINETIC,
            "Erythromycin inhibits CYP3A4, greatly increasing simvastatin levels and risk of rhabdomyolysis",
            "Use alternative statin (pravastatin) or alternative antibiotic"
        ));
        
        INTERACTION_DATABASE.add(new DrugInteraction(
            metoprolol, fluoxetine, InteractionSeverity.MODERATE, InteractionMechanism.PHARMACOKINETIC,
            "Fluoxetine inhibits CYP2D6 which metabolizes metoprolol, increasing beta-blocker effect",
            "Monitor heart rate and blood pressure; consider dose reduction"
        ));
        
        INTERACTION_DATABASE.add(new DrugInteraction(
            alprazolam, oxycodone, InteractionSeverity.MAJOR, InteractionMechanism.PHARMACODYNAMIC,
            "Combined CNS depression can be fatal - respiratory depression risk",
            "Avoid combination; black box warning"
        ));
    }

    /**
     * Checks for interactions between two drugs.
     */
    public static Optional<DrugInteraction> checkInteraction(String drug1Name, String drug2Name) {
        Drug d1 = findDrug(drug1Name);
        Drug d2 = findDrug(drug2Name);
        
        if (d1 == null || d2 == null) return Optional.empty();
        
        return INTERACTION_DATABASE.stream()
            .filter(i -> (i.drug1().genericName().equalsIgnoreCase(d1.genericName()) &&
                         i.drug2().genericName().equalsIgnoreCase(d2.genericName())) ||
                        (i.drug1().genericName().equalsIgnoreCase(d2.genericName()) &&
                         i.drug2().genericName().equalsIgnoreCase(d1.genericName())))
            .findFirst();
    }

    /**
     * Checks all interactions for a medication list.
     */
    public static List<DrugInteraction> checkMedicationList(List<String> medications) {
        List<DrugInteraction> interactions = new ArrayList<>();
        
        for (int i = 0; i < medications.size(); i++) {
            for (int j = i + 1; j < medications.size(); j++) {
                checkInteraction(medications.get(i), medications.get(j))
                    .ifPresent(interactions::add);
            }
        }
        
        // Sort by severity
        interactions.sort((a, b) -> a.severity().compareTo(b.severity()));
        return interactions;
    }

    /**
     * Infers potential interaction from drug properties.
     */
    public static List<String> inferPotentialInteractions(Drug drug1, Drug drug2) {
        List<String> warnings = new ArrayList<>();
        
        // Same CYP metabolism pathway
        if (!drug1.cyp450Metabolism().equals("None") &&
            drug1.cyp450Metabolism().equals(drug2.cyp450Metabolism())) {
            warnings.add("Both drugs metabolized by " + drug1.cyp450Metabolism() + 
                " - potential for altered drug levels");
        }
        
        // Combined sedation
        if (drug1.sedating() && drug2.sedating()) {
            warnings.add("Combined sedating effect - increased CNS depression risk");
        }
        
        // Combined anticoagulation
        if (drug1.anticoagulant() && drug2.anticoagulant()) {
            warnings.add("Combined anticoagulant effect - increased bleeding risk");
        }
        
        // QT prolongation
        if (drug1.qtProlonging() && drug2.qtProlonging()) {
            warnings.add("Combined QT prolongation - risk of cardiac arrhythmias");
        }
        
        return warnings;
    }

    /**
     * Adds a new drug to the database.
     */
    public static void addDrug(Drug drug) {
        DRUG_DATABASE.put(drug.genericName().toLowerCase(), drug);
    }

    /**
     * Adds a new interaction to the database.
     */
    public static void addInteraction(DrugInteraction interaction) {
        INTERACTION_DATABASE.add(interaction);
    }

    private static Drug findDrug(String name) {
        String lower = name.toLowerCase();
        if (DRUG_DATABASE.containsKey(lower)) return DRUG_DATABASE.get(lower);
        
        // Search by brand name
        return DRUG_DATABASE.values().stream()
            .filter(d -> d.name().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}

