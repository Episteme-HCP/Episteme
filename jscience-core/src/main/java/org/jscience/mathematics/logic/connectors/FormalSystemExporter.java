package org.jscience.mathematics.logic.connectors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Interface for exporting a logical system to an external formal verification
 * tool.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface FormalSystemExporter {

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

    /**
     * Returns the file extension typically used by this system.
     * 
     * @return the file extension (e.g., ".v", ".mm")
     */
    String getFileExtension();
}
