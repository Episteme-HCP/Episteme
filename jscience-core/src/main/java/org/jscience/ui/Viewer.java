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

import javafx.stage.Stage;
import java.util.List;

/**
 * Base provider interface for all UI components (Viewers, Demos, Apps).
 * All methods are abstract and must be overridden by implementations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Viewer {
    
    /**
     * Returns the category for grouping (e.g., "Chemistry", "Physics").
     * Must be internationalized via I18n.
     * @return the category name
     */
    String getCategory();

    /**
     * Returns the display name of the viewer/demo.
     * Must be internationalized via I18n.
     * @return the display name
     */
    String getName();

    /**
     * Returns a short description (1-2 lines).
     * Must be internationalized via I18n.
     * @return the short description
     */
    String getDescription();

    /**
     * Returns a long description (multi-line, detailed explanation).
     * Must be internationalized via I18n.
     * @return the long description
     */
    String getLongDescription();

    /**
     * Launches the component in the given stage.
     * @param stage the JavaFX stage to display the component
     */
    void show(Stage stage);

    /**
     * Returns a list of parameters exposed by this viewer.
     * Parameters allow external control and configuration.
     * @return list of configurable parameters
     */
    List<Parameter<?>> getViewerParameters();

    /**
     * Sets a parameter value by name.
     * This allows external control of viewer parameters (e.g., from a Demo).
     * @param parameterName the parameter name/key
     * @param value the new value
     * @return true if the parameter was found and set, false otherwise
     */
    default boolean setParameter(String parameterName, Object value) {
        for (Parameter<?> param : getViewerParameters()) {
            if (param.getName().equals(parameterName) || 
                param.getName().endsWith("." + parameterName)) {
                @SuppressWarnings("unchecked")
                Parameter<Object> p = (Parameter<Object>) param;
                p.setValue(value);
                return true;
            }
        }
        return false;
    }

    /**
     * Gets a parameter by name.
     * @param parameterName the parameter name/key
     * @return the parameter, or null if not found
     */
    default Parameter<?> getParameter(String parameterName) {
        for (Parameter<?> param : getViewerParameters()) {
            if (param.getName().equals(parameterName) || 
                param.getName().endsWith("." + parameterName)) {
                return param;
            }
        }
        return null;
    }
}

