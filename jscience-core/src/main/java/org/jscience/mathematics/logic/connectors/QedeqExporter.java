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

package org.jscience.mathematics.logic.connectors;

import java.io.IOException;

/**
 * Exporter for QEDEQ (Hilbert II).
 * <p>
 * Generates QEDEQ XML modules.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class QedeqExporter implements FormalSystemExporter {

    @Override
    public void exportSystem(java.util.Map<String, Object> system, java.io.Writer writer) throws IOException {
        String name = (String) system.getOrDefault("module", "GeneratedModule");
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        writer.write("<qedeq xmlns=\"http://www.qedeq.org/0_04_06/xml/qedeq\">\n");
        writer.write("  <header>\n");
        writer.write("    <specification>\n");
        writer.write("      <name>" + name + "</name>\n");
        writer.write("      <title>JScience Export: " + name + "</title>\n");
        writer.write("    </specification>\n");
        writer.write("  </header>\n");
        writer.write("  <chapter>\n");
        writer.write("    <title>Exported Axioms</title>\n");
        writer.write("    <section>\n");
        writer.write("      <title>Definitions</title>\n");

        if (system.containsKey("axioms")) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> axioms = (java.util.Map<String, String>) system.get("axioms");
            for (java.util.Map.Entry<String, String> entry : axioms.entrySet()) {
                writer.write("      <!-- " + entry.getKey() + ": " + entry.getValue() + " -->\n");
            }
        }

        writer.write("    </section>\n");
        writer.write("  </chapter>\n");
        writer.write("</qedeq>\n");
    }

    @Override
    public String getFileExtension() {
        return ".xml";
    }
}
