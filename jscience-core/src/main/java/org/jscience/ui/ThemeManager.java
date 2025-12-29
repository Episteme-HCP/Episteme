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

package org.jscience.ui;

import javafx.scene.Scene;
import java.util.prefs.Preferences;

/**
 * Global Theme Manager for JScience Applications.
 * Ensures consistent look and feel across all modules (Core, Natural, Social,
 * Killer Apps).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThemeManager {

    private static ThemeManager instance;
    private final Preferences prefs;
    private static final String PREF_THEME = "app_theme";
    private String currentTheme;

    private ThemeManager() {
        prefs = Preferences.userNodeForPackage(ThemeManager.class);
        currentTheme = prefs.get(PREF_THEME, "light");
    }

    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setTheme(String theme) {
        this.currentTheme = theme;
        prefs.put(PREF_THEME, theme);
    }

    public boolean isDarkTheme() {
        return "dark".equalsIgnoreCase(currentTheme);
    }

    public void setDarkTheme(boolean dark) {
        setTheme(dark ? "dark" : "light");
    }

    /**
     * Applies the current theme to the given Scene.
     */
    public void applyTheme(Scene scene) {
        if (scene == null)
            return;

        // Remove old theme styles if any (simplistic approach, ideally we toggle
        // classes)
        // But here we rely on the root style as used in KillerAppBase

        if ("dark".equals(currentTheme)) {
            scene.getRoot().setStyle(
                    "-fx-base: #2b2b2b; " +
                            "-fx-background: #1e1e1e; " +
                            "-fx-control-inner-background: #3c3c3c; " +
                            "-fx-text-fill: #e0e0e0;");
        } else {
            scene.getRoot().setStyle("");
        }
    }
}
