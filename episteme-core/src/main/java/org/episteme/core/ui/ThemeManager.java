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
 * Global Theme Manager for Episteme Applications.
 * Ensures consistent look and feel across all modules (Core, Natural, Social,
 * Killer Apps).
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class ThemeManager {

    private static ThemeManager instance;
    private final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
    private String currentTheme = "Dark";

    private ThemeManager() {
        currentTheme = prefs.get("visual_theme", "Dark");
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
        prefs.put("visual_theme", theme);
    }

    public boolean isDarkTheme() {
        return "Dark".equalsIgnoreCase(currentTheme);
    }

    /**
     * Applies the current theme to the given Scene.
     */
    public void applyTheme(Scene scene) {
        if (scene == null)
            return;

        scene.getStylesheets().clear();
        // Always load main.css as base if available
        java.net.URL mainCss = ThemeManager.class.getResource("/org/episteme/core/ui/main.css");
        if (mainCss != null) {
            scene.getStylesheets().add(mainCss.toExternalForm());
        }

        // Load theme.css (Custom styles including the Orange Banner)
        java.net.URL themeCss = ThemeManager.class.getResource("/org/episteme/core/ui/theme.css");
        if (themeCss != null) {
            scene.getStylesheets().add(themeCss.toExternalForm());
        }

        if ("High Contrast".equalsIgnoreCase(currentTheme) || "HighContrast".equalsIgnoreCase(currentTheme)) {
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
            java.net.URL hcCss = ThemeManager.class.getResource("/org/episteme/core/ui/high-contrast.css");
            if (hcCss != null) {
                scene.getStylesheets().add(hcCss.toExternalForm());
            }
        } else if ("Dark".equalsIgnoreCase(currentTheme)) {
             javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
             java.net.URL darkCss = ThemeManager.class.getResource("/org/episteme/core/ui/dark.css");
             if (darkCss != null) {
                 scene.getStylesheets().add(darkCss.toExternalForm());
             }
        } else if ("Caspian".equalsIgnoreCase(currentTheme)) {
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_CASPIAN);
        } else {
            javafx.application.Application.setUserAgentStylesheet(javafx.application.Application.STYLESHEET_MODENA);
        }
        
        // Ensure theme.css is always loaded last for shared overrides
        scene.getStylesheets().remove(themeCss.toExternalForm()); // Avoid duplicates
        scene.getStylesheets().add(themeCss.toExternalForm());
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

