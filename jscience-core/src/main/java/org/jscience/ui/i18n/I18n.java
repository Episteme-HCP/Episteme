/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.ui.i18n;

import java.util.*;

/**
 * Internationalization helper for JScience UI.
 * <p>
 * Manages multiple resource bundles and provides centralized access to
 * localized strings. Supports dynamic locale switching.
 * </p>
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class I18n {
    private static final String CORE_BUNDLE_BASE = "org.jscience.ui.i18n.messages_core";
    private static I18n instance;

    private Locale currentLocale = Locale.getDefault();
    private final List<String> bundleBases = new ArrayList<>();
    private final Map<String, ResourceBundle> bundles = new HashMap<>();

    private I18n() {
        // Auto-register all known bundles
        addBundle(CORE_BUNDLE_BASE);
        // Try to load natural and social bundles (they may not be on classpath)
        tryAddBundle("org.jscience.ui.i18n.messages_natural");
        tryAddBundle("org.jscience.ui.i18n.messages_social");
        tryAddBundle("org.jscience.ui.i18n.messages_apps");
    }

    private void tryAddBundle(String bundleBase) {
        try {
            ResourceBundle.getBundle(bundleBase, currentLocale);
            addBundle(bundleBase);
        } catch (MissingResourceException e) {
            // Bundle not available, skip
        }
    }

    public static synchronized I18n getInstance() {
        if (instance == null) {
            instance = new I18n();
        }
        return instance;
    }

    /**
     * Registers an additional resource bundle base name.
     * 
     * @param bundleBase the bundle base name (e.g.,
     *                   "org.jscience.ui.i18n.messages_natural")
     */
    public void addBundle(String bundleBase) {
        if (!bundleBases.contains(bundleBase)) {
            bundleBases.add(bundleBase);
            loadBundle(bundleBase);
        }
    }

    /**
     * Sets the current locale and reloads all bundles.
     * 
     * @param locale the new locale
     */
    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        bundles.clear();
        for (String base : bundleBases) {
            loadBundle(base);
        }
    }

    /**
     * Gets the current locale.
     * 
     * @return current locale
     */
    public Locale getLocale() {
        return currentLocale;
    }

    /**
     * Retrieves a localized string for the given key.
     * Searches all registered bundles in order of registration.
     * 
     * @param key the resource key
     * @return the localized string, or the key itself if not found
     */
    public String get(String key) {
        for (String base : bundleBases) {
            ResourceBundle bundle = bundles.get(base);
            if (bundle != null) {
                try {
                    return bundle.getString(key);
                } catch (MissingResourceException e) {
                    // Try next bundle
                }
            }
        }
        // Return key as fallback
        return key;
    }

    /**
     * Retrieves a localized string for the given key, returning the default value
     * if not found.
     * 
     * @param key          the resource key
     * @param defaultValue the value to return if key is missing
     * @return the localized string or defaultValue
     */
    public String get(String key, String defaultValue) {
        String value = get(key);
        // get(key) returns key if missing, so we check if it returned the key
        if (value.equals(key) && !hasKey(key)) {
            return defaultValue;
        }
        return value;
    }

    private boolean hasKey(String key) {
        for (String base : bundleBases) {
            ResourceBundle bundle = bundles.get(base);
            if (bundle != null && bundle.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a localized string with format arguments.
     * 
     * @param key  the resource key
     * @param args format arguments
     * @return the formatted localized string
     */
    public String get(String key, Object... args) {
        String val = get(key);
        if (args.length > 0) {
            try {
                return String.format(val, args);
            } catch (Exception e) {
                return val + Arrays.toString(args);
            }
        }
        return val;
    }

    private void loadBundle(String bundleBase) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(bundleBase, currentLocale);
            bundles.put(bundleBase, bundle);
        } catch (MissingResourceException e) {
            System.err.println("I18n: Bundle not found: " + bundleBase + " for locale " + currentLocale);
        }
    }
}
