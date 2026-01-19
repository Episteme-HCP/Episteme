package org.jscience.history;

import java.util.*;

/**
 * Assists with manuscript dating based on writing style analysis.
 */
public final class PaleographyAssistant {

    private PaleographyAssistant() {}

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

    public record ScriptFeature(
        String name,
        String description,
        List<ScriptType> associatedScripts
    ) {}

    public record DatingResult(
        int estimatedYear,
        int confidenceRangeYears,
        ScriptType likelyScript,
        List<ScriptType> possibleScripts,
        List<String> diagnosticFeatures,
        double confidence
    ) {}

    // Diagnostic features database
    private static final List<ScriptFeature> FEATURES = List.of(
        new ScriptFeature("Tall ascenders", "Ascenders extend well above x-height",
            List.of(ScriptType.INSULAR, ScriptType.CAROLINGIAN_MINUSCULE)),
        new ScriptFeature("Biting", "Adjacent curved letters share strokes",
            List.of(ScriptType.GOTHIC_TEXTURA, ScriptType.GOTHIC_ROTUNDA)),
        new ScriptFeature("Forked ascenders", "Tops of ascenders split into two",
            List.of(ScriptType.GOTHIC_TEXTURA)),
        new ScriptFeature("Round letterforms", "Letters d, e, o are rounded",
            List.of(ScriptType.CAROLINGIAN_MINUSCULE, ScriptType.HUMANIST_MINUSCULE)),
        new ScriptFeature("Angular letterforms", "Letters have broken, angular strokes",
            List.of(ScriptType.GOTHIC_TEXTURA)),
        new ScriptFeature("Ligatures rt/st", "R and t, s and t connected",
            List.of(ScriptType.BENEVENTAN)),
        new ScriptFeature("Open-topped a", "Letter 'a' has open top (like modern)",
            List.of(ScriptType.UNCIAL, ScriptType.HALF_UNCIAL, ScriptType.INSULAR)),
        new ScriptFeature("Two-compartment a", "Letter 'a' has closed double-loop",
            List.of(ScriptType.CAROLINGIAN_MINUSCULE, ScriptType.GOTHIC_TEXTURA)),
        new ScriptFeature("Long s", "S extends below baseline or is tall",
            List.of(ScriptType.CAROLINGIAN_MINUSCULE, ScriptType.GOTHIC_TEXTURA, ScriptType.SECRETARY)),
        new ScriptFeature("Tironian et", "Ampersand-like abbreviation for 'et'",
            List.of(ScriptType.INSULAR, ScriptType.CAROLINGIAN_MINUSCULE))
    );

    /**
     * Analyzes observed script features to estimate dating.
     */
    public static DatingResult analyzeScript(List<String> observedFeatures, String region) {
        Map<ScriptType, Integer> scriptScores = new HashMap<>();
        List<String> matchedFeatures = new ArrayList<>();
        
        // Score each script type based on matching features
        for (String observed : observedFeatures) {
            for (ScriptFeature feature : FEATURES) {
                if (feature.name().toLowerCase().contains(observed.toLowerCase()) ||
                    observed.toLowerCase().contains(feature.name().toLowerCase())) {
                    
                    matchedFeatures.add(feature.name());
                    for (ScriptType script : feature.associatedScripts()) {
                        scriptScores.merge(script, 1, Integer::sum);
                    }
                }
            }
        }
        
        // Find best matches
        List<ScriptType> possibleScripts = scriptScores.entrySet().stream()
            .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
            .map(Map.Entry::getKey)
            .limit(3)
            .toList();
        
        if (possibleScripts.isEmpty()) {
            return new DatingResult(1000, 500, ScriptType.CAROLINGIAN_MINUSCULE,
                List.of(), matchedFeatures, 0.1);
        }
        
        ScriptType likelyScript = possibleScripts.get(0);
        int estimatedYear = (likelyScript.getStartYear() + likelyScript.getEndYear()) / 2;
        int range = (likelyScript.getEndYear() - likelyScript.getStartYear()) / 2;
        
        // Apply regional adjustments
        if (region != null) {
            estimatedYear = adjustForRegion(estimatedYear, likelyScript, region);
        }
        
        double confidence = Math.min(0.95, 0.3 + 0.15 * scriptScores.get(likelyScript));
        
        return new DatingResult(
            estimatedYear, range, likelyScript, possibleScripts, 
            matchedFeatures, confidence
        );
    }

    /**
     * Gets scripts active in a given year.
     */
    public static List<ScriptType> getActiveScripts(int year) {
        List<ScriptType> active = new ArrayList<>();
        for (ScriptType script : ScriptType.values()) {
            if (year >= script.getStartYear() && year <= script.getEndYear()) {
                active.add(script);
            }
        }
        return active;
    }

    /**
     * Suggests diagnostic features to look for.
     */
    public static List<ScriptFeature> getDiagnosticFeatures(ScriptType script) {
        return FEATURES.stream()
            .filter(f -> f.associatedScripts().contains(script))
            .toList();
    }

    /**
     * Compares two manuscripts for stylistic similarity.
     */
    public static double manuscriptSimilarity(List<String> features1, List<String> features2) {
        Set<String> set1 = new HashSet<>(features1);
        Set<String> set2 = new HashSet<>(features2);
        
        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);
        
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);
        
        return union.isEmpty() ? 0 : (double) intersection.size() / union.size();
    }

    private static int adjustForRegion(int baseYear, ScriptType script, String region) {
        // Some scripts spread at different times to different regions
        String r = region.toLowerCase();
        
        if (script == ScriptType.CAROLINGIAN_MINUSCULE) {
            if (r.contains("spain") || r.contains("iberia")) return baseYear + 50;
            if (r.contains("england") || r.contains("britain")) return baseYear + 30;
        }
        
        if (script == ScriptType.GOTHIC_TEXTURA) {
            if (r.contains("italy")) return baseYear - 50; // Slower adoption
        }
        
        if (script == ScriptType.HUMANIST_MINUSCULE) {
            if (r.contains("germany") || r.contains("england")) return baseYear + 50;
        }
        
        return baseYear;
    }
}
