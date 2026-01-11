/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.i18n;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * Internationalization Manager for JScience Killer Apps.
 * Delegates to the core {@link I18n} class to ensure shared state.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class I18nManager {

    private static I18nManager instance;
    // We strictly wrap I18n singleton
    private final I18n delegate;

    private I18nManager() {
        this.delegate = I18n.getInstance();
    }

    public static synchronized I18nManager getInstance() {
        if (instance == null) {
            instance = new I18nManager();
        }
        return instance;
    }

    public void addBundle(String baseName) {
        delegate.addBundle(baseName);
    }

    public void addListener(Consumer<Locale> listener) {
        delegate.addListener(listener);
    }

    public void setLocale(Locale locale) {
        delegate.setLocale(locale);
    }

    public String get(String key) {
        return delegate.get(key);
    }

    public String get(String key, Object... args) {
        return delegate.get(key, args);
    }

    public Locale getCurrentLocale() {
        return delegate.getLocale();
    }

    public static Locale[] getSupportedLocales() {
        return I18n.getInstance().getSupportedLocales();
    }
}
