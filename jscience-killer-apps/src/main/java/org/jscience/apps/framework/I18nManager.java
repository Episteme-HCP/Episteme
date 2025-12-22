/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.apps.framework;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalization Manager for JScience Killer Apps.
 * Provides multi-language support via ResourceBundle.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class I18nManager {

    private static final String BUNDLE_BASE = "org.jscience.apps.i18n.messages";
    private static I18nManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;

    private I18nManager() {
        setLocale(Locale.getDefault());
    }

    public static synchronized I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager();
        }
        return instance;
    }

    /**
     * Sets the current locale and reloads the resource bundle.
     */
    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        try {
            this.bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);
        } catch (Exception e) {
            // Fallback to English
            this.bundle = ResourceBundle.getBundle(BUNDLE_BASE, Locale.ENGLISH);
        }
    }

    /**
     * Gets a localized string by key.
     */
    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!"; // Missing translation marker
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
            new Locale("es"), // Spanish
            Locale.CHINESE
        };
    }
}
