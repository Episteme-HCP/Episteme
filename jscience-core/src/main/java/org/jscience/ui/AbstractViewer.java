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

package org.jscience.ui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for all JScience Viewers.
 * Viewers are JavaFX Nodes that can be embedded in Demos or launched independently.
 * 
 * Subclasses MUST override: getCategory(), getName(), getDescription(), getLongDescription()
 * with proper I18n support.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public abstract class AbstractViewer extends BorderPane implements Viewer {

    /**
     * Returns the category for grouping. MUST be overridden with I18n.
     * @return the category name
     */
    @Override
    public abstract String getCategory();

    /**
     * Returns the display name. MUST be overridden with I18n.
     * @return the display name
     */
    @Override
    public abstract String getName();

    /**
     * Returns a short description (1-2 lines). MUST be overridden with I18n.
     * @return the short description
     */
    @Override
    public abstract String getDescription();

    /**
     * Returns a long description. MUST be overridden with I18n.
     * @return the long description
     */
    @Override
    public abstract String getLongDescription();

    /**
     * Returns the list of configurable parameters.
     * Override this to expose viewer-specific parameters.
     * @return list of parameters (empty by default)
     */
    @Override
    public List<Parameter<?>> getViewerParameters() {
        return new ArrayList<>();
    }

    @Override
    public void show(Stage stage) {
        Scene scene = new Scene(this, 1000, 700);
        ThemeManager.getInstance().applyTheme(scene);
        stage.setTitle(getName());
        stage.setScene(scene);
        stage.show();
    }
}
