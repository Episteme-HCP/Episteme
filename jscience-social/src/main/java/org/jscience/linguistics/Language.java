/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.linguistics;

/**
 * Represents a language with ISO 639 codes.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public class Language {

    private final String name;
    private final String nativeName;
    private final String iso639_1; // 2-letter code
    private final String iso639_2; // 3-letter code
    private final String family;
    private final long speakersEstimate;
    private final String writingSystem;

    public Language(String name, String nativeName, String iso639_1, String iso639_2,
            String family, long speakersEstimate, String writingSystem) {
        this.name = name;
        this.nativeName = nativeName;
        this.iso639_1 = iso639_1;
        this.iso639_2 = iso639_2;
        this.family = family;
        this.speakersEstimate = speakersEstimate;
        this.writingSystem = writingSystem;
    }

    public String getName() {
        return name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getIso639_1() {
        return iso639_1;
    }

    public String getIso639_2() {
        return iso639_2;
    }

    public String getFamily() {
        return family;
    }

    public long getSpeakersEstimate() {
        return speakersEstimate;
    }

    public String getWritingSystem() {
        return writingSystem;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", name, iso639_1, family);
    }

    // Major world languages
    public static final Language ENGLISH = new Language("English", "English", "en", "eng",
            "Indo-European/Germanic", 1_500_000_000L, "Latin");
    public static final Language MANDARIN = new Language("Mandarin Chinese", "普通话", "zh", "zho",
            "Sino-Tibetan", 1_100_000_000L, "Chinese characters");
    public static final Language SPANISH = new Language("Spanish", "Español", "es", "spa",
            "Indo-European/Romance", 550_000_000L, "Latin");
    public static final Language HINDI = new Language("Hindi", "हिन्दी", "hi", "hin",
            "Indo-European/Indo-Aryan", 600_000_000L, "Devanagari");
    public static final Language ARABIC = new Language("Arabic", "العربية", "ar", "ara",
            "Afro-Asiatic/Semitic", 420_000_000L, "Arabic");
    public static final Language FRENCH = new Language("French", "Français", "fr", "fra",
            "Indo-European/Romance", 300_000_000L, "Latin");
    public static final Language PORTUGUESE = new Language("Portuguese", "Português", "pt", "por",
            "Indo-European/Romance", 260_000_000L, "Latin");
    public static final Language RUSSIAN = new Language("Russian", "Русский", "ru", "rus",
            "Indo-European/Slavic", 260_000_000L, "Cyrillic");
    public static final Language JAPANESE = new Language("Japanese", "日本語", "ja", "jpn",
            "Japonic", 125_000_000L, "Japanese (Kanji/Kana)");
    public static final Language GERMAN = new Language("German", "Deutsch", "de", "deu",
            "Indo-European/Germanic", 135_000_000L, "Latin");
}
