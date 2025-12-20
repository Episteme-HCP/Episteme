package org.jscience.linguistics;

/**
 * Represents a basic unit of sound in a language.
 */
public class Phoneme {

    private final Language language;
    private final String symbol; // IPA symbol or representation

    public Phoneme(Language language, String symbol) {
        this.language = language;
        this.symbol = symbol;
        if (language != null) {
            language.addPhoneme(this);
        }
    }

    public Language getLanguage() {
        return language;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "/" + symbol + "/";
    }
}
