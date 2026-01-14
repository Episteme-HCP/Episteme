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

package org.jscience.ml.sbml;

import java.io.IOException;
import java.io.Writer;


/**
 * This class represents a SBML Level 2 Document This code is licensed
 * under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */
public class SBMLLevel2Document {
    /** DOCUMENT ME! */
    private final static String SBML2HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<sbml xmlns=\"http://www.sbml.org/sbml/level2\" xmlns:sbml=\"http://www.sbml.org/sbml/level2\" version=\"1\" level=\"2\"" +
        " xmlns:math=\"http://www.w3.org/1998/Math/MathML\"\nxmlns:html=\"http://www.w3.org/1999/xhtml\">\n";

    /** DOCUMENT ME! */
    private Model model;

/**
     * Creates a new SBMLLevel2Document object.
     */
    public SBMLLevel2Document() {
    }

/**
     * Creates a new instance of SBMLLevel2Document
     *
     * @param model DOCUMENT ME!
     */
    public SBMLLevel2Document(Model model) {
        this.model = model;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Model getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @param m DOCUMENT ME!
     */
    public void setModel(Model m) {
        model = m;
    }

    /**
     * Writes an SBML document and closes the writer.
     *
     * @param writer DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeDocument(Writer writer) throws IOException {
        writer.write(SBML2HEADER + model.toString() + "</sbml>\n");
        writer.close();
    }
}
