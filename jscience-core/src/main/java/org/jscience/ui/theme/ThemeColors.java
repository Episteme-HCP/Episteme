/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.ui.theme;

import javafx.scene.paint.Color;

/**
 * Shared color constants derived from theme.css for programmatic use in Java.
 */
public final class ThemeColors {

    // Main Theme Colors (Light/Orange)
    public static final Color BACKGROUND = Color.web("#fdfbf7");
    public static final Color ACCENT = Color.web("#ff8c00");
    public static final Color ACCENT_DARK = Color.web("#e65100");
    public static final Color TEXT_BASE = Color.web("#2c2c2c");
    
    // UI Elements
    public static final Color SIDEBAR_BG = Color.web("#f8f8f0");
    public static final Color BORDER = Color.web("#dddddd");
    
    // 3D Scene Defaults
    public static final Color SCENE_BG = Color.web("#2c3e50"); // Midnight Blue
    public static final Color SHAPE_A = Color.CORNFLOWERBLUE;
    public static final Color SHAPE_B = Color.ORANGERED;
    public static final Color SHAPE_SPECULAR = Color.WHITE;
    
    // Materials
    public static final Color COPPER = Color.web("#e67e22");
    public static final Color PLATINUM = Color.web("#7f8c8d");
    public static final Color IRON = Color.web("#34495e");
    public static final Color STEEL = Color.web("#95a5a6");
    
    private ThemeColors() {}
}
