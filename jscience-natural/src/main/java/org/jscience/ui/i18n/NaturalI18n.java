package org.jscience.ui.i18n;

import java.util.Locale;

/**
 * Internationalization helper for JScience Natural module.
 * Delegates to the core I18n and loads natural-specific bundles.
 */
public class NaturalI18n {
    private static final String BUNDLE_BASE = "org.jscience.ui.i18n.messages_natural";
    private static NaturalI18n instance;

    private NaturalI18n() {
        org.jscience.ui.i18n.I18n.getInstance().addBundle(BUNDLE_BASE);
    }

    public static synchronized NaturalI18n getInstance() {
        if (instance == null) {
            instance = new NaturalI18n();
        }
        return instance;
    }

    public void setLocale(Locale locale) {
        org.jscience.ui.i18n.I18n.getInstance().setLocale(locale);
    }

    public String get(String key) {
        return org.jscience.ui.i18n.I18n.getInstance().get(key);
    }
}
