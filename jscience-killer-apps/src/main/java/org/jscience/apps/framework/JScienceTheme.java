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

package org.jscience.apps.framework;

import javafx.scene.paint.Color;

/**
 * Shared color theme for JScience Killer Apps.
 * Provides consistent colors across all applications.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class JScienceTheme {

    // Primary colors
    public static final Color PRIMARY = Color.web("#2980b9");
    public static final Color PRIMARY_DARK = Color.web("#1a5276");
    public static final Color PRIMARY_LIGHT = Color.web("#5dade2");

    // Accent colors
    public static final Color ACCENT = Color.web("#e74c3c");
    public static final Color ACCENT_SUCCESS = Color.web("#27ae60");
    public static final Color ACCENT_WARNING = Color.web("#f39c12");

    // Background colors
    public static final Color BACKGROUND = Color.web("#ecf0f1");
    public static final Color BACKGROUND_DARK = Color.web("#2c3e50");
    public static final Color BACKGROUND_CARD = Color.web("#ffffff");

    // Text colors
    public static final Color TEXT_PRIMARY = Color.web("#2c3e50");
    public static final Color TEXT_SECONDARY = Color.web("#7f8c8d");
    public static final Color TEXT_LIGHT = Color.web("#bdc3c7");

    // Domain-specific colors
    public static final Color PHYSICS_BLUE = Color.web("#3498db");
    public static final Color CHEMISTRY_GREEN = Color.web("#27ae60");
    public static final Color BIOLOGY_PURPLE = Color.web("#9b59b6");
    public static final Color MATH_ORANGE = Color.web("#e67e22");
    public static final Color ENGINEERING_GRAY = Color.web("#95a5a6");
    public static final Color ECONOMICS_GOLD = Color.web("#f1c40f");

    // Chart series colors
    public static final Color[] CHART_SERIES = {
            Color.web("#3498db"), // Blue
            Color.web("#e74c3c"), // Red
            Color.web("#2ecc71"), // Green
            Color.web("#f39c12"), // Orange
            Color.web("#9b59b6"), // Purple
            Color.web("#1abc9c"), // Teal
            Color.web("#e91e63"), // Pink
            Color.web("#00bcd4") // Cyan
    };

    // CSS style strings
    public static final String CARD_STYLE = "-fx-background-color: white; -fx-background-radius: 8; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);";

    public static final String BUTTON_PRIMARY_STYLE = "-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; "
            +
            "-fx-padding: 10 20; -fx-background-radius: 5;";

    public static final String BUTTON_SECONDARY_STYLE = "-fx-background-color: #ecf0f1; -fx-text-fill: #2c3e50; " +
            "-fx-padding: 8 16; -fx-background-radius: 5; -fx-border-color: #bdc3c7; -fx-border-radius: 5;";

    public static final String HEADER_STYLE = "-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;";

    public static final String SUBHEADER_STYLE = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #7f8c8d;";

    private JScienceTheme() {
        // Prevent instantiation
    }

    /**
     * Get chart color for series index (cycles through palette).
     */
    public static Color getChartColor(int index) {
        return CHART_SERIES[index % CHART_SERIES.length];
    }

    /**
     * Get color for domain category.
     */
    public static Color getDomainColor(String domain) {
        return switch (domain.toLowerCase()) {
            case "physics" -> PHYSICS_BLUE;
            case "chemistry" -> CHEMISTRY_GREEN;
            case "biology" -> BIOLOGY_PURPLE;
            case "mathematics", "math" -> MATH_ORANGE;
            case "engineering" -> ENGINEERING_GRAY;
            case "economics" -> ECONOMICS_GOLD;
            default -> PRIMARY;
        };
    }
}
