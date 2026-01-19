package org.jscience.linguistics;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.data.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for linguistic analysis.
 * Supports syntax trees, word frequency (Zipf), and sentiment timelines.
 */
public final class LinguisticData implements UniversalDataModel {

    @Override
    public String getModelType() { return "LINGUISTIC_METRICS"; }

    public record SyntaxNode(String label, String word, List<SyntaxNode> children) {
        public SyntaxNode(String label, String word) { this(label, word, new ArrayList<>()); }
    }

    public record SentimentPoint(double time, Real score, String context) {}

    private SyntaxNode root;
    private final Map<String, Long> wordFrequencies = new LinkedHashMap<>();
    private final List<SentimentPoint> sentimentTimeline = new ArrayList<>();

    public void setRootNode(SyntaxNode root) { this.root = root; }
    public SyntaxNode getRootNode() { return root; }

    public void addWordFrequency(String word, long count) {
        wordFrequencies.put(word, count);
    }

    public Map<String, Long> getWordFrequencies() { return Collections.unmodifiableMap(wordFrequencies); }

    public void addSentiment(double time, Real score, String context) {
        sentimentTimeline.add(new SentimentPoint(time, score, context));
    }

    public List<SentimentPoint> getSentimentTimeline() { return Collections.unmodifiableList(sentimentTimeline); }
}
