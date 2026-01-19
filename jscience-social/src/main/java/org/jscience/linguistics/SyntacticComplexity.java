package org.jscience.linguistics;


/**
 * Models syntactic complexity of text.
 */
public final class SyntacticComplexity {

    private SyntacticComplexity() {}

    public record ComplexityMetrics(
        double meanLengthOfClause,
        double clausesPerSentence,
        double tUnitLength,
        double complexTUnitRatio,
        double dependencyDistance
    ) {}

    /**
     * Calculates complexity based on Hunt's T-unit analysis.
     * T-unit: One main clause plus any subordinate clauses.
     */
    public static ComplexityMetrics analyze(String text) {
        String[] sentences = text.split("[.!?]+");
        int sentenceCount = sentences.length;
        
        // Approximate counts using conjunctions as proxies for clauses
        // In reality, this needs a dependency parser
        int totalWords = 0;
        int clauseCount = 0;
        int tUnitCount = 0;
        int complexTUnits = 0;
        
        String[] subordinateConjunctions = {"because", "although", "while", "if", "since", "unless"};
        String[] coordinateConjunctions = {"and", "but", "or", "yet", "so"};

        for (String s : sentences) {
            String[] words = s.trim().split("\\s+");
            if (words.length < 2) continue;
            
            totalWords += words.length;
            tUnitCount++;
            clauseCount++; // Every sentence has at least one clause
            
            boolean hasSubordinate = false;
            for (String word : words) {
                String w = word.toLowerCase();
                for (String sub : subordinateConjunctions) {
                    if (w.equals(sub)) {
                        clauseCount++;
                        hasSubordinate = true;
                    }
                }
                for (String coord : coordinateConjunctions) {
                    if (w.equals(coord)) {
                        // Coordinates usually start a new T-unit in analysis
                        tUnitCount++;
                        clauseCount++;
                    }
                }
            }
            if (hasSubordinate) complexTUnits++;
        }

        return new ComplexityMetrics(
            (double) totalWords / clauseCount,
            (double) clauseCount / sentenceCount,
            (double) totalWords / tUnitCount,
            (double) complexTUnits / tUnitCount,
            estimateAvgDependencyDistance(text)
        );
    }

    /**
     * Estimates Mean Dependency Distance (MDD).
     * MDD = sum(dependency links) / number of links
     */
    private static double estimateAvgDependencyDistance(String text) {
        // Highly simplified: distance between related parts of speech
        // In this stub, we return a value based on sentence length
        String[] sentences = text.split("[.!?]+");
        double sumLen = 0;
        for (String s : sentences) {
            sumLen += Math.sqrt(s.trim().split("\\s+").length);
        }
        return sumLen / sentences.length;
    }

    /**
     * Identifies Yngve's depth (maximal number of left-branching nodes).
     */
    public static int calculateYngveDepth(String sentence) {
        // Simplified: count consecutive modifiers/prepositions at start
        String[] words = sentence.trim().split("\\s+");
        int depth = 0;
        int maxDepth = 0;
        for (String w : words) {
            if (w.matches("the|a|an|of|in|with|very|extremely")) {
                depth++;
            } else {
                maxDepth = Math.max(maxDepth, depth);
                depth = 0;
            }
        }
        return maxDepth;
    }
}
