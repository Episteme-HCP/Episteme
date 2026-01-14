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

package org.jscience.client.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Utility class for file operations in JavaFX applications.
 */
public class FileHelper {

    /**
     * Shows an open file dialog.
     *
     * @param stage             the parent stage.
     * @param title             the dialog title.
     * @param filterDescription the description of the file filter.
     * @param extensions        the file extensions to filter (e.g., "*.txt", "*.params").
     * @return the selected File, or null if no file was selected.
     */
    public static File showOpenDialog(Stage stage, String title, String filterDescription, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterDescription, extensions));
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Shows a save file dialog.
     *
     * @param stage             the parent stage.
     * @param title             the dialog title.
     * @param filterDescription the description of the file filter.
     * @param extensions        the file extensions to filter.
     * @return the selected File, or null if no file was selected.
     */
    public static File showSaveDialog(Stage stage, String title, String filterDescription, String... extensions) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterDescription, extensions));
        return fileChooser.showSaveDialog(stage);
    }
}
