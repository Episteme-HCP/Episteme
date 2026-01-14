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

package org.jscience.ui.viewers.history;

import javafx.scene.layout.BorderPane;

/**
 * Generic Viewer for Timeline data.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class TimelineViewer extends BorderPane implements org.jscience.ui.Viewer {

    public TimelineViewer() {
        // Initial stub logic for timeline
        // In a full implementation, this would layout events on a time axis.
        this.setStyle("-fx-background-color: white;");
    }

    @Override
    public String getCategory() {
        return "History";
    }

    @Override
    public String getName() {
        return "Timeline Viewer";
    }
    
    @Override
    public String getDescription() {
        return "Visualizes events on a timeline.";
    }

    @Override
    public void show(javafx.stage.Stage stage) {
         javafx.scene.Scene scene = new javafx.scene.Scene(this);
         org.jscience.ui.ThemeManager.getInstance().applyTheme(scene);
         stage.setTitle(getName());
         stage.setScene(scene);
         stage.show();
    }
    
    @Override
    public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() {
        return java.util.Collections.emptyList();
    }
}
