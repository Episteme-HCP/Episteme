package org.jscience.ui.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalization helper for JScience Core UI.
 */
public class I18n {

    private static I18n instance;
    private static final java.util.List<ResourceBundle> bundles = new java.util.ArrayList<>();
    private java.util.Locale currentLocale = Locale.getDefault();

    private final java.util.List<String> bundleNames = new java.util.ArrayList<>();

    private I18n() {
        // Core bundle
        addBundle("org.jscience.ui.i18n.messages");
    }

    public static synchronized I18n getInstance() {
        if (instance == null) {
            instance = new I18n();
        }
        return instance;
    }

    public void addBundle(String baseName) {
        if (!bundleNames.contains(baseName)) {
            bundleNames.add(baseName);
            loadBundle(baseName);
        }
    }

    private void loadBundle(String baseName) {
        try {
            ResourceBundle b = ResourceBundle.getBundle(baseName, currentLocale);
            bundles.add(b);
        } catch (Exception e) {
            System.err.println("Could not load bundle: " + baseName);
        }
    }

    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        bundles.clear();
        for (String baseName : bundleNames) {
            loadBundle(baseName);
        }
    }

    public String get(String key) {
        for (ResourceBundle b : bundles) {
            if (b.containsKey(key)) {
                return b.getString(key);
            }
        }
        return "!" + key + "!";
    }
}
