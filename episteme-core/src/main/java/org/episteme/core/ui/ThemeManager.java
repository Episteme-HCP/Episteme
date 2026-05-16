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

package org.episteme.core.ui;

import javafx.scene.Scene;
import java.util.prefs.Preferences;

/**
 * Manages the application-wide visual theme.
 * Standardizes the use of CSS files and provides high-performance theme switching.
 */
public class ThemeManager {
    private static final Preferences PREFS = Preferences.userNodeForPackage(ThemeManager.class);
    private static final ThemeManager INSTANCE = new ThemeManager();
    private static final String PREF_THEME = "ui_theme";

    private String currentTheme;

    private ThemeManager() {
        // Default to Dark mode for professional aesthetics
        currentTheme = PREFS.get(PREF_THEME, "Dark");
    }

    public static ThemeManager getInstance() {
        return INSTANCE;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setTheme(String theme) {
        this.currentTheme = theme;
        PREFS.put(PREF_THEME, theme);
    }

    /**
     * Applies the current theme to the specified Scene.
     */
    public void applyTheme(Scene scene) {
        if (scene == null) return;

        // Clear existing stylesheets to avoid stacking and performance degradation
        scene.getStylesheets().clear();

        // 1. Base styling (Always loaded)
        java.net.URL mainCss = ThemeManager.class.getResource("/org/episteme/core/ui/main.css");
        if (mainCss != null) scene.getStylesheets().add(mainCss.toExternalForm());

        // 2. Global Component Layout (Shared across all themes)
        java.net.URL themeCss = ThemeManager.class.getResource("/org/episteme/core/ui/theme.css");
        if (themeCss != null) scene.getStylesheets().add(themeCss.toExternalForm());

        // 3. Theme-specific Colors
        String themeFile = null;
        if ("High Contrast".equalsIgnoreCase(currentTheme) || "HighContrast".equalsIgnoreCase(currentTheme)) {
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
            themeFile = "high-contrast.css";
        } else if ("Dark".equalsIgnoreCase(currentTheme)) {
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
            themeFile = "dark.css";
        } else if ("Caspian".equalsIgnoreCase(currentTheme)) {
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_CASPIAN);
        } else {
            // Default: Light
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
            themeFile = "light.css";
        }

        if (themeFile != null) {
            java.net.URL specificCss = ThemeManager.class.getResource("/org/episteme/core/ui/" + themeFile);
            if (specificCss != null) scene.getStylesheets().add(specificCss.toExternalForm());
        }
    }

    private boolean isHighContrast() {
        return "High Contrast".equalsIgnoreCase(currentTheme) || "HighContrast".equalsIgnoreCase(currentTheme);
    }

    /**
     * Called when the locale changes.
     */
    public void notifyLocaleChange(java.util.Locale locale) {
        // Implementation can be added if locale affects theme (e.g. text direction)
    }
}
