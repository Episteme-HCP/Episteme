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

package org.jscience.ui.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jscience.ui.ThemeManager;

/**
 * Internationalization Manager for JScience Killer Apps.
 * Provides multi-language support via ResourceBundle.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class I18nManager {

    private static I18nManager instance;
    // List to store multiple bundles (Core, Apps, etc.) - newer added first for
    // override priority
    private final java.util.List<ResourceBundle> bundles = new java.util.ArrayList<>();
    private final java.util.List<String> bundleNames = new java.util.ArrayList<>();
    private Locale currentLocale;

    private I18nManager() {
        // Always load core bundle first
        addBundle("org.jscience.ui.i18n.messages_core");
        setLocale(Locale.getDefault());
    }

    public static synchronized I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager();
        }
        return instance;
    }

    /**
     * Registers a new resource bundle base name.
     */
    public void addBundle(String baseName) {
        if (!bundleNames.contains(baseName)) {
            bundleNames.add(0, baseName); // Add to front for priority
            reloadBundles();
        }
    }

    /**
     * Sets the current locale and reloads all resource bundles.
     */
    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        ThemeManager.getInstance().notifyLocaleChange(locale); // Notify theme/UI if needed
        reloadBundles();
    }

    private void reloadBundles() {
        bundles.clear();
        for (String baseName : bundleNames) {
            try {
                ResourceBundle rb = ResourceBundle.getBundle(baseName, currentLocale);
                bundles.add(rb);
            } catch (Exception e) {
                // Fallback to English
                try {
                    ResourceBundle rb = ResourceBundle.getBundle(baseName, Locale.ENGLISH);
                    bundles.add(rb);
                } catch (Exception ex) {
                    System.err.println("Could not load bundle: " + baseName);
                }
            }
        }
    }

    /**
     * Gets a localized string by key.
     */
    public String get(String key) {
        for (ResourceBundle bundle : bundles) {
            if (bundle.containsKey(key)) {
                return bundle.getString(key);
            }
        }
        return "!" + key + "!";
    }

    /**
     * Gets a localized string by key and formats it with arguments.
     */
    public String get(String key, Object... args) {
        String pattern = get(key);
        if (pattern.startsWith("!") && pattern.endsWith("!")) {
            return pattern;
        }
        try {
            return java.text.MessageFormat.format(pattern, args);
        } catch (Exception e) {
            return "!" + key + " (format error)!";
        }
    }

    /**
     * Gets the current locale.
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }

    /**
     * Supported locales.
     */
    public static Locale[] getSupportedLocales() {
        return new Locale[] {
                Locale.ENGLISH,
                Locale.FRENCH,
                Locale.GERMAN,
                Locale.of("es"), // Spanish
                Locale.CHINESE
        };
    }
}
