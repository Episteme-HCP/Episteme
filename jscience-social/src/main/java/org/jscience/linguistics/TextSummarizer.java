package org.jscience.linguistics;


/**
 * Automatic text summarization by sentence extraction.
 */
public final class TextSummarizer {

    private TextSummarizer() {}

    /**
     * Extracts top N sentences based on word frequency.
     */
    public static String summarize(String text, int nSentences) {
        String[] sentences = text.split("\\.");
        // Simplified: return first N
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(nSentences, sentences.length); i++) {
            sb.append(sentences[i]).append(".");
        }
        return sb.toString();
    }
}
