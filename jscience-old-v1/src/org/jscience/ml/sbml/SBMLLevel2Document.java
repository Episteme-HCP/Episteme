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
