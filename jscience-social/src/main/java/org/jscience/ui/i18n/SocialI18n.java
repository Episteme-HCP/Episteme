package org.jscience.ui.i18n;

import java.util.Locale;

/**
 * Internationalization helper for JScience Social module.
 * Delegates to the core I18n and loads social-specific bundles.
 */
public class SocialI18n {
    private static final String BUNDLE_BASE = "org.jscience.ui.i18n.messages_social";
    private static SocialI18n instance;

    private SocialI18n() {
        // Register social bundle with core I18n
        org.jscience.ui.i18n.I18n.getInstance().addBundle(BUNDLE_BASE);
    }

    public static synchronized SocialI18n getInstance() {
        if (instance == null) {
            instance = new SocialI18n();
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
