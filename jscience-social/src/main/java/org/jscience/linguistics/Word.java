/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.linguistics;

import java.util.*;

/**
 * Represents a word in a language with etymology and translations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Word {

    public enum PartOfSpeech {
        NOUN, VERB, ADJECTIVE, ADVERB, PRONOUN, PREPOSITION,
        CONJUNCTION, INTERJECTION, ARTICLE, DETERMINER
    }

    private final String text;
    private final Language language;
    private PartOfSpeech partOfSpeech;
    private String definition;
    private String pronunciation; // IPA notation
    private String etymology;
    private final List<String> synonyms = new ArrayList<>();
    private final List<String> antonyms = new ArrayList<>();
    private final Map<Language, String> translations = new HashMap<>();

    public Word(String text, Language language) {
        this.text = text;
        this.language = language;
    }

    // Getters
    public String getText() {
        return text;
    }

    public Language getLanguage() {
        return language;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public String getEtymology() {
        return etymology;
    }

    public List<String> getSynonyms() {
        return Collections.unmodifiableList(synonyms);
    }

    public List<String> getAntonyms() {
        return Collections.unmodifiableList(antonyms);
    }

    public Map<Language, String> getTranslations() {
        return Collections.unmodifiableMap(translations);
    }

    // Setters
    public void setPartOfSpeech(PartOfSpeech pos) {
        this.partOfSpeech = pos;
    }

    public void setDefinition(String def) {
        this.definition = def;
    }

    public void setPronunciation(String pron) {
        this.pronunciation = pron;
    }

    public void setEtymology(String etymology) {
        this.etymology = etymology;
    }

    public void addSynonym(String word) {
        synonyms.add(word);
    }

    public void addAntonym(String word) {
        antonyms.add(word);
    }

    public void addTranslation(Language lang, String translation) {
        translations.put(lang, translation);
    }

    public String getTranslation(Language lang) {
        return translations.get(lang);
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %s)", text, language.getName(), partOfSpeech);
    }
}
