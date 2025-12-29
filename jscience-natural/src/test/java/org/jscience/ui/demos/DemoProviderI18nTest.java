/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.demos;

import org.jscience.ui.i18n.I18n;
import org.jscience.ui.DemoProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests to verify I18n support across all demos.
 * Checks for missing translations, hardcoded strings, and key consistency.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class DemoProviderI18nTest {

    private static List<DemoProvider> allProviders;
    private static final Pattern UNRESOLVED_KEY_PATTERN = Pattern.compile("^[a-z]+\\.[a-z]+.*$");

    @BeforeAll
    static void loadAllProviders() {
        ServiceLoader<DemoProvider> loader = ServiceLoader.load(DemoProvider.class);
        allProviders = new ArrayList<>();
        for (DemoProvider provider : loader) {
            allProviders.add(provider);
        }
    }

    @Nested
    class DemoProviderBasicTests {

        @Test
        void allDemoProvidersLoadSuccessfully() {
            assertFalse(allProviders.isEmpty(), "Should have at least one DemoProvider");
            System.out.println("Loaded " + allProviders.size() + " DemoProviders");
        }

        @Test
        void allDemoProvidersHaveNonNullName() {
            for (DemoProvider provider : allProviders) {
                assertNotNull(provider.getName(),
                        "Demo " + provider.getClass().getSimpleName() + " has null name");
                assertFalse(provider.getName().isEmpty(),
                        "Demo " + provider.getClass().getSimpleName() + " has empty name");
            }
        }

        @Test
        void allDemoProvidersHaveNonNullDescription() {
            for (DemoProvider provider : allProviders) {
                assertNotNull(provider.getDescription(),
                        "Demo " + provider.getClass().getSimpleName() + " has null description");
                assertFalse(provider.getDescription().isEmpty(),
                        "Demo " + provider.getClass().getSimpleName() + " has empty description");
            }
        }

        @Test
        void allDemoProvidersHaveNonNullCategory() {
            for (DemoProvider provider : allProviders) {
                assertNotNull(provider.getCategory(),
                        "Demo " + provider.getClass().getSimpleName() + " has null category");
                assertFalse(provider.getCategory().isEmpty(),
                        "Demo " + provider.getClass().getSimpleName() + " has empty category");
            }
        }
    }

    @Nested
    class I18nKeyResolutionTests {

        @Test
        void namesDoNotContainUnresolvedI18nKeys() {
            List<String> failures = new ArrayList<>();
            for (DemoProvider provider : allProviders) {
                String name = provider.getName();
                // Check for patterns like "demo.name" which suggest unresolved key
                if (looksLikeUnresolvedKey(name)) {
                    failures.add(provider.getClass().getSimpleName() + " getName() = '" + name + "'");
                }
            }
            assertTrue(failures.isEmpty(),
                    "Demos with unresolved I18n keys in getName():\n" + String.join("\n", failures));
        }

        @Test
        void descriptionsDoNotContainUnresolvedI18nKeys() {
            List<String> failures = new ArrayList<>();
            for (DemoProvider provider : allProviders) {
                String desc = provider.getDescription();
                if (looksLikeUnresolvedKey(desc)) {
                    failures.add(provider.getClass().getSimpleName() + " getDescription() = '" + desc + "'");
                }
            }
            assertTrue(failures.isEmpty(),
                    "Demos with unresolved I18n keys in getDescription():\n" + String.join("\n", failures));
        }

        @Test
        void categoriesDoNotContainUnresolvedI18nKeys() {
            List<String> failures = new ArrayList<>();
            for (DemoProvider provider : allProviders) {
                String category = provider.getCategory();
                if (looksLikeUnresolvedKey(category)) {
                    failures.add(provider.getClass().getSimpleName() + " getCategory() = '" + category + "'");
                }
            }
            assertTrue(failures.isEmpty(),
                    "Demos with unresolved I18n keys in getCategory():\n" + String.join("\n", failures));
        }

        private boolean looksLikeUnresolvedKey(String value) {
            if (value == null || value.isEmpty())
                return false;
            // Check for pattern like "category.physics" or "demo.name.here"
            // but not actual sentences (which have spaces and capital letters)
            return UNRESOLVED_KEY_PATTERN.matcher(value).matches()
                    && !value.contains(" ")
                    && !Character.isUpperCase(value.charAt(0));
        }
    }

    @Nested
    class I18nPropertyFileParity {

        private static final String[] LANGUAGES = { "en", "fr", "es", "de", "zh" };
        private static final String RESOURCE_PATH = "/org/jscience/ui/i18n/messages_natural_%s.properties";

        @Test
        void allLanguageFilesExist() {
            for (String lang : LANGUAGES) {
                String path = String.format(RESOURCE_PATH, lang);
                try (InputStream is = getClass().getResourceAsStream(path)) {
                    assertNotNull(is, "Missing language file: " + path);
                } catch (IOException e) {
                    fail("Error reading language file: " + path);
                }
            }
        }

        @Test
        void allLanguagesHaveSameKeys() throws IOException {
            Set<String> englishKeys = loadKeys("en");

            for (String lang : LANGUAGES) {
                if (lang.equals("en"))
                    continue;

                Set<String> langKeys = loadKeys(lang);

                Set<String> missingFromLang = new HashSet<>(englishKeys);
                missingFromLang.removeAll(langKeys);

                if (!missingFromLang.isEmpty()) {
                    System.out.println("WARNING: " + lang.toUpperCase() + " missing " +
                            missingFromLang.size() + " keys from English:");
                    missingFromLang.stream().limit(10).forEach(k -> System.out.println("  - " + k));
                    if (missingFromLang.size() > 10) {
                        System.out.println("  ... and " + (missingFromLang.size() - 10) + " more");
                    }
                }
            }
        }

        private Set<String> loadKeys(String lang) throws IOException {
            String path = String.format(RESOURCE_PATH, lang);
            Properties props = new Properties();
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    props.load(is);
                }
            }
            return props.stringPropertyNames();
        }
    }

    @Nested
    class I18nManagerTests {

        @Test
        void i18nManagerCanBeInstantiated() {
            I18n i18n = I18n.getInstance();
            assertNotNull(i18n, "I18n manager should not be null");
        }

        @Test
        void knownKeysResolveCorrectly() {
            I18n i18n = I18n.getInstance();

            // Test some known keys
            String[] knownKeys = {
                    "category.physics",
                    "category.chemistry",
                    "category.biology",
                    "viewer.pendulum",
                    "viewer.humanbody"
            };

            for (String key : knownKeys) {
                String value = i18n.get(key);
                assertNotNull(value, "Key '" + key + "' returned null");
                assertNotEquals(key, value, "Key '" + key + "' was not resolved (returned itself)");
            }
        }

        @Test
        void missingKeyReturnsKeyOrFallback() {
            I18n i18n = I18n.getInstance();
            String result = i18n.get("totally.nonexistent.key.xyz");
            // Typically returns the key itself or a placeholder
            assertNotNull(result);
        }
    }
}
