package org.jscience.social.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalization helper for jscience-social.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class I18n {

    private static final String BUNDLE_BASE = "org.jscience.social.i18n.messages";
    private static I18n instance;
    private ResourceBundle bundle;

    private I18n() {
        org.jscience.ui.i18n.I18n.getInstance().addBundle(BUNDLE_BASE);
    }

    public static synchronized I18n getInstance() {
        if (instance == null) {
            instance = new I18n();
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
