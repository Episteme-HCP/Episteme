package org.jscience.linguistics;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Standard registry for common world languages.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class Languages implements Serializable {

    private static final long serialVersionUID = 2L;

    public static final Language ENGLISH = createLanguage("en", "English", "English", "Latin", 
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");

    public static final Language FRENCH = createLanguage("fr", "French", "Français", "Latin",
            "abcdefghijklmnopqrstuvwxyzàâäéèêëïîôùûüÿœæçABCDEFGHIJKLMNOPQRSTUVWXYZÀÂÄÉÈÊËÏÎÔÙÛÜŸŒÆÇ");

    public static final Language GERMAN = createLanguage("de", "German", "Deutsch", "Latin",
            "abcdefghijklmnopqrstuvwxyzäöüßABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜ");

    public static final Language SPANISH = createLanguage("es", "Spanish", "Español", "Latin",
            "abcdefghijklmnñopqrstuvwxyzáéíóúüABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚÜ");

    public static final Language JAPANESE = createLanguage("ja", "Japanese", "日本語", "Mixed (Kanji, Hiragana, Katakana)", null);

    public static final Language CHINESE = createLanguage("zh", "Chinese", "中文", "Han (Simplified)", null);

    private static final Map<String, Language> REGISTRY = new HashMap<>();

    static {
        register(ENGLISH);
        register(FRENCH);
        register(GERMAN);
        register(SPANISH);
        register(JAPANESE);
        register(CHINESE);
    }

    private Languages() {}

    private static Language createLanguage(String iso, String name, String nativeName, String script, String alpha) {
        Language lang = new Language(iso, name);
        lang.setNativeName(nativeName);
        lang.setScriptName(script);
        if (alpha != null) {
            lang.addGraphemes(alpha);
        }
        return lang;
    }

    public static void register(Language language) {
        REGISTRY.put(language.getIsoCode().toLowerCase(), language);
    }

    public static Optional<Language> get(String isoCode) {
        return Optional.ofNullable(REGISTRY.get(isoCode.toLowerCase()));
    }
}
