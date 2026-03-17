package org.episteme.core.technical.i18n;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility for internationalization in Episteme.
 * Provides access to localized strings for UI and metadata.
 */
public class I18N {
    private static final Logger logger = LoggerFactory.getLogger(I18N.class);
    private static final String BUNDLE_NAME = "org.episteme.core.i18n.messages";
    private static ResourceBundle bundle;
    private static Locale currentLocale = Locale.getDefault();

    static {
        loadBundle();
    }

    private static void loadBundle() {
        try {
            bundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
        } catch (MissingResourceException e) {
            logger.warn("Could not find I18N bundle: {}. Falling back to empty bundle.", BUNDLE_NAME);
            // Provide an empty bundle to avoid NPEs
            bundle = new ResourceBundle() {
                @Override
                protected Object handleGetObject(String key) { return null; }
                @Override
                public java.util.Enumeration<String> getKeys() { return java.util.Collections.emptyEnumeration(); }
            };
        }
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        loadBundle();
    }

    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    public static String get(String key, Object... args) {
        String msg = get(key);
        return String.format(msg, args);
    }
}
