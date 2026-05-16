/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.episteme.social.ui.i18n;

import java.util.Locale;

/**
 * Internationalization helper for Episteme Social module.
 * Delegates to the core I18N and loads social-specific bundles.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SocialI18N {
    private static final String BUNDLE_BASE = "org.episteme.core.ui.i18n.messages_social";
    private static SocialI18N instance;

    private SocialI18N() {
        // Register social bundle with core I18N
        org.episteme.core.ui.i18n.I18N.getInstance().addBundle(BUNDLE_BASE);
    }

    public static synchronized SocialI18N getInstance() {
        if (instance == null) {
            instance = new SocialI18N();
        }
        return instance;
    }

    public void setLocale(Locale locale) {
        org.episteme.core.ui.i18n.I18N.getInstance().setLocale(locale);
    }

    public String get(String key) {
        return org.episteme.core.ui.i18n.I18N.getInstance().get(key);
    }

    public String get(String key, String defaultValue) {
        return org.episteme.core.ui.i18n.I18N.getInstance().get(key, defaultValue);
    }
}

