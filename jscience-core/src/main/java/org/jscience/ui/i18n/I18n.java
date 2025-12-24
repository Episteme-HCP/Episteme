package org.jscience.ui.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalization helper for JScience Core UI.
 */
public class I18n {
    private static final String BUNDLE_BASE = "org.jscience.ui.i18n.messages";
    private static I18n instance;
    private ResourceBundle bundle;

    private I18n() {
        setLocale(Locale.getDefault());
    }

    public static synchronized I18n getInstance() {
        if (instance == null) {
            instance = new I18n();
        }
        return instance;
    }

    public void setLocale(Locale locale) {
        try {
            this.bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);
        } catch (Exception e) {
            this.bundle = ResourceBundle.getBundle(BUNDLE_BASE, Locale.ENGLISH);
        }
    }

    public String get(String key) {
        try {
            return bundle.getString(key);
        } catch (Exception e) {
            return "!" + key + "!";
        }
    }
}
