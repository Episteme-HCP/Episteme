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

package org.jscience.mathematics.loaders.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.jscience.io.ResourceWriter;

/**
 * Interface for exporting a logical system to an external formal verification
 * tool.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FormalSystemExporter extends ResourceWriter<Map<String, Object>> {

    /**
     * Exports the given logical context/system to the specified writer.
     * 
     * @param system the system components to export
     * @param writer the writer to output to
     * @throws IOException if an I/O error occurs
     */
    void exportSystem(Map<String, Object> system, Writer writer) throws IOException;

    /**
     * Exports a system to a file.
     * 
     * @param system the system components to export
     * @param file   the file to write to
     * @throws IOException if an I/O error occurs
     */
    default void exportSystem(Map<String, Object> system, File file) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            exportSystem(system, writer);
        }
    }

    /**
     * Exports a system to a file path.
     * 
     * @param system   the system components to export
     * @param filePath the path to the file
     * @throws IOException if an I/O error occurs
     */
    default void exportSystem(Map<String, Object> system, String filePath) throws IOException {
        exportSystem(system, new File(filePath));
    }

    @Override
    default void save(Map<String, Object> resource, String destination) throws Exception {
        exportSystem(resource, destination);
    }

    @Override
    default String getName() {
        return getClass().getSimpleName();
    }

    @Override
    default String getDescription() {
        return "Formal System Exporter";
    }

    @Override
    default String getLongDescription() {
        return "Exports logical definitions to external formal systems.";
    }

    @Override
    default String getCategory() {
        return "Mathematics/Logic";
    }

    @Override
    default String getResourcePath() {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    default Class<Map<String, Object>> getResourceType() {
        return (Class<Map<String, Object>>) (Class<?>) Map.class;
    }

    /**
     * Returns the file extension typically used by this system.
     * 
     * @return the file extension (e.g., ".v", ".mm")
     */
    String getFileExtension();
}


