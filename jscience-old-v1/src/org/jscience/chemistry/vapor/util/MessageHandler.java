/*
 * Localized message handler for VLE.
 *
 * Author: Samir Vaidya (mailto: syvaidya@yahoo.com)
 * Copyright (c) Samir Vaidya
 */
package org.jscience.chemistry.vapor.util;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Localized message handler for VLE.
 */
public class MessageHandler {
    /**
     * DOCUMENT ME!
     */
    static ResourceBundle messages = null;

    static {
        messages = ResourceBundle.getBundle("org.jscience.chemistry.vapor.MessagesBundle",
                Locale.getDefault());
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getString(String key) {
        return messages.getString(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param parameters DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getString(String key, Object[] parameters) {
        return MessageFormat.format(messages.getString(key), parameters);
    }
}
