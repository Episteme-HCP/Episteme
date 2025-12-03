package org.jscience.mathematics.logic.connector;

import java.io.IOException;
import java.io.Writer;
import org.jscience.mathematics.logic.Logic;

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
     * @param name   the name of the module/theory
     * @param writer the writer to output to
     * @throws IOException if an I/O error occurs
     */
    void export(String name, Writer writer) throws IOException;

    /**
     * Returns the file extension typically used by this system.
     * 
     * @return the file extension (e.g., ".v", ".mm")
     */
    String getFileExtension();
}
