/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
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
package org.jscience.social.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Internationalization helper for JScience Social module.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class I18n {

    private static I18n instance;
    private static final List<ResourceBundle> bundles = new ArrayList<>();
    private Locale currentLocale = Locale.getDefault();
    private final List<String> bundleNames = new ArrayList<>();

    private I18n() {
        // Social bundle
        addBundle("org.jscience.social.i18n.messages");
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

    public Locale getLocale() {
        return currentLocale;
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
