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

package org.jscience.history;

import java.io.Serializable;
import java.util.*;
import org.jscience.util.persistence.Attribute;
import org.jscience.util.persistence.Persistent;

/**
 * Assists in dating and identifying historical manuscripts based on paleographic analysis of writing styles.
 * Classifies scripts into established historical types and maps diagnostic features to time periods.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public final class PaleographyAssistant {

    private PaleographyAssistant() {
        // Prevent instantiation
    }

    /**
     * Major historical Western European script types.
     */
    public enum ScriptType {
        ROMAN_CAPITALS(1, 500, "Capitalis monumentalis"),
        RUSTIC_CAPITALS(100, 600, "Capitalis rustica"),
        UNCIAL(400, 900, "Uncialis"),
        HALF_UNCIAL(500, 900, "Semiuncialis"),
        INSULAR(600, 900, "Scriptura insularis"),
        VISIGOTHIC(700, 1200, "Littera Visigothica"),
        BENEVENTAN(800, 1300, "Littera Beneventana"),
        CAROLINGIAN_MINUSCULE(800, 1200, "Minuscula Carolina"),
        GOTHIC_TEXTURA(1100, 1500, "Textualis"),
        GOTHIC_ROTUNDA(1200, 1500, "Rotunda"),
        BATARDE(1300, 1600, "Bastarda"),
        HUMANIST_MINUSCULE(1400, 1600, "Antiqua"),
        SECRETARY(1400, 1700, "Secretaria"),
        ITALIC(1500, 2100, "Cancelleresca");

        private final int startYear;
        private final int endYear;
        private final String latinName;

        ScriptType(int start, int end, String latinName) {
            this.startYear = start;
            this.endYear = end;
            this.latinName = latinName;
        }

        public int getStartYear() { return startYear; }
        public int getEndYear() { return endYear; }
        public String getLatinName() { return latinName; }
    }

    /**
     * Specific calligraphic or typographic feature used to distinguish scripts.
     * 
     * @param name             feature name
     * @param description      technical description
     * @param associatedScripts list of scripts where this feature is common
     */
    @Persistent
    public record ScriptFeature(
        @Attribute String name,
        @Attribute String description,
        @Attribute List<ScriptType> associatedScripts
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public ScriptFeature {
            Objects.requireNonNull(name, "Feature name cannot be null");
            associatedScripts = associatedScripts != null ? List.copyOf(associatedScripts) : List.of();
        }
    }

    /**
     * Encapsulates the statistical results of a paleographical analysis.
     * 
     * @param estimatedYear        central year estimate
     * @param confidenceRangeYears margin of error in years
     * @param likelyScript         primary script identification
     * @param possibleScripts      alternative script identifications
     * @param diagnosticFeatures   list of observed features that matched
     * @param confidence           probability score (0.0 to 1.0)
     */
    @Persistent
    public record DatingResult(
        @Attribute int estimatedYear,
        @Attribute int confidenceRangeYears,
        @Attribute ScriptType likelyScript,
        @Attribute List<ScriptType> possibleScripts,
        @Attribute List<String> diagnosticFeatures,
        @Attribute double confidence
    ) implements Serializable {
        private static final long serialVersionUID = 1L;

        public DatingResult {
            Objects.requireNonNull(likelyScript, "likelyScript cannot be null");
            possibleScripts = possibleScripts != null ? List.copyOf(possibleScripts) : List.of();
            diagnosticFeatures = diagnosticFeatures != null ? List.copyOf(diagnosticFeatures) : List.of();
        }
    }

    // Database of known diagnostic features
    private static final List<ScriptFeature> FEATURES = List.of(
        new ScriptFeature("Tall ascenders", "Ascenders extend well above x-height",
            Arrays.asList(ScriptType.INSULAR, ScriptType.CAROLINGIAN_MINUSCULE)),
        new ScriptFeature("Biting", "Adjacent curved letters share strokes",
            Arrays.asList(ScriptType.GOTHIC_TEXTURA, ScriptType.GOTHIC_ROTUNDA)),
        new ScriptFeature("Forked ascenders", "Tops of ascenders split into two",
            Collections.singletonList(ScriptType.GOTHIC_TEXTURA)),
        new ScriptFeature("Round letterforms", "Letters d, e, o are rounded",
            Arrays.asList(ScriptType.CAROLINGIAN_MINUSCULE, ScriptType.HUMANIST_MINUSCULE)),
        new ScriptFeature("Angular letterforms", "Letters have broken, angular strokes",
            Collections.singletonList(ScriptType.GOTHIC_TEXTURA)),
        new ScriptFeature("Ligatures rt/st", "R and t, s and t connected",
            Collections.singletonList(ScriptType.BENEVENTAN)),
        new ScriptFeature("Open-topped a", "Letter 'a' has open top (like modern)",
            Arrays.asList(ScriptType.UNCIAL, ScriptType.HALF_UNCIAL, ScriptType.INSULAR)),
        new ScriptFeature("Two-compartment a", "Letter 'a' has closed double-loop",
            Arrays.asList(ScriptType.CAROLINGIAN_MINUSCULE, ScriptType.GOTHIC_TEXTURA)),
        new ScriptFeature("Long s", "S extends below baseline or is tall",
            Arrays.asList(ScriptType.CAROLINGIAN_MINUSCULE, ScriptType.GOTHIC_TEXTURA, ScriptType.SECRETARY)),
        new ScriptFeature("Tironian et", "Ampersand-like abbreviation for 'et'",
            Arrays.asList(ScriptType.INSULAR, ScriptType.CAROLINGIAN_MINUSCULE))
    );

    /**
     * Estimates the likely script type and creation date based on observed manuscript features.
     * 
     * @param observedFeatures list of feature names observed in the manuscript
     * @param region           geographical region of the manuscript (for regional adjustments)
     * @return dating analysis results as {@link DatingResult}
     * @throws NullPointerException if observedFeatures is null
     */
    public static DatingResult analyzeScript(List<String> observedFeatures, String region) {
        Objects.requireNonNull(observedFeatures, "Observed features list cannot be null");
        Map<ScriptType, Integer> scriptScores = new EnumMap<>(ScriptType.class);
        List<String> matchedFeatures = new ArrayList<>();
        
        for (String observed : observedFeatures) {
            String finalObserved = observed.toLowerCase().trim();
            for (ScriptFeature feature : FEATURES) {
                String featureName = feature.name().toLowerCase();
                if (featureName.contains(finalObserved) || finalObserved.contains(featureName)) {
                    matchedFeatures.add(feature.name());
                    for (ScriptType script : feature.associatedScripts()) {
                        scriptScores.merge(script, 1, (a, b) -> a + b);
                    }
                }
            }
        }
        
        List<ScriptType> possibleScripts = scriptScores.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .map(Map.Entry::getKey)
            .limit(3)
            .toList();
        
        if (possibleScripts.isEmpty()) {
            return new DatingResult(1000, 500, ScriptType.CAROLINGIAN_MINUSCULE,
                Collections.emptyList(), matchedFeatures, 0.1);
        }
        
        ScriptType likelyScript = possibleScripts.get(0);
        int estimatedYear = (likelyScript.getStartYear() + likelyScript.getEndYear()) / 2;
        int range = (likelyScript.getEndYear() - likelyScript.getStartYear()) / 2;
        
        if (region != null) {
            estimatedYear = adjustForRegion(estimatedYear, likelyScript, region);
        }
        
        double confidence = Math.min(0.95, 0.3 + 0.15 * scriptScores.getOrDefault(likelyScript, 0));
        
        return new DatingResult(
            estimatedYear, range, likelyScript, possibleScripts, 
            matchedFeatures, confidence
        );
    }

    /**
     * Returns all script types known to be active in a specific year.
     * 
     * @param year the year to query
     * @return unmodifiable list of active script types
     */
    public static List<ScriptType> getActiveScripts(int year) {
        return Arrays.stream(ScriptType.values())
            .filter(s -> year >= s.getStartYear() && year <= s.getEndYear())
            .toList();
    }

    /**
     * Suggests diagnostic features associated with a specific script type.
     * 
     * @param script the script type
     * @return unmodifiable list of associated features
     * @throws NullPointerException if script is null
     */
    public static List<ScriptFeature> getDiagnosticFeatures(ScriptType script) {
        Objects.requireNonNull(script, "ScriptType cannot be null");
        return FEATURES.stream()
            .filter(f -> f.associatedScripts().contains(script))
            .toList();
    }

    /**
     * Quantifies the stylistic similarity between two manuscripts using Jaccard index.
     * 
     * @param features1 features of first manuscript
     * @param features2 features of second manuscript
     * @return similarity score (0.0 to 1.0)
     */
    public static double manuscriptSimilarity(List<String> features1, List<String> features2) {
        if (features1 == null || features2 == null || (features1.isEmpty() && features2.isEmpty())) {
            return 0.0;
        }
        Set<String> set1 = new HashSet<>();
        for (String f : features1) set1.add(f.toLowerCase().trim());
        
        Set<String> set2 = new HashSet<>();
        for (String f : features2) set2.add(f.toLowerCase().trim());
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        if (union.isEmpty()) return 0.0;
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        return (double) intersection.size() / union.size();
    }

    private static int adjustForRegion(int baseYear, ScriptType script, String region) {
        String r = region.toLowerCase().trim();
        
        if (script == ScriptType.CAROLINGIAN_MINUSCULE) {
            if (r.contains("spain") || r.contains("iberia")) return baseYear + 50;
            if (r.contains("england") || r.contains("britain")) return baseYear + 30;
        }
        
        if (script == ScriptType.GOTHIC_TEXTURA) {
            if (r.contains("italy")) return baseYear - 50;
        }
        
        if (script == ScriptType.HUMANIST_MINUSCULE) {
            if (r.contains("germany") || r.contains("england")) return baseYear + 50;
        }
        
        return baseYear;
    }
}
