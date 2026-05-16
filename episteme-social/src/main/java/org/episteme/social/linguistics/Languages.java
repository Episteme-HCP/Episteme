/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.linguistics;

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

    public static final Language FRENCH = createLanguage("fr", "French", "FranÃ§ais", "Latin",
            "abcdefghijklmnopqrstuvwxyzÃ Ã¢Ã¤Ã©Ã¨ÃªÃ«Ã¯Ã®Ã´Ã¹Ã»Ã¼Ã¿Å“Ã¦Ã§ABCDEFGHIJKLMNOPQRSTUVWXYZÃ€Ã‚Ã„Ã‰ÃˆÃŠÃ‹ÃÃŽÃ”Ã™Ã›ÃœÅ¸Å’Ã†Ã‡");

    public static final Language GERMAN = createLanguage("de", "German", "Deutsch", "Latin",
            "abcdefghijklmnopqrstuvwxyzÃ¤Ã¶Ã¼ÃŸABCDEFGHIJKLMNOPQRSTUVWXYZÃ„Ã–Ãœ");

    public static final Language SPANISH = createLanguage("es", "Spanish", "EspaÃ±ol", "Latin",
            "abcdefghijklmnÃ±opqrstuvwxyzÃ¡Ã©Ã­Ã³ÃºÃ¼ABCDEFGHIJKLMNÃ‘OPQRSTUVWXYZÃÃ‰ÃÃ“ÃšÃœ");

    public static final Language JAPANESE = createLanguage("ja", "Japanese", "æ—¥æœ¬èªž", "Mixed (Kanji, Hiragana, Katakana)", null);

    public static final Language CHINESE = createLanguage("zh", "Chinese", "ä¸­æ–‡", "Han (Simplified)", null);

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

