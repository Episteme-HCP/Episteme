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

/*
 * BasisReader.java
 *
 * Created on July 22, 2004, 7:06 AM
 */

package org.jscience.chemistry.quantum.basis;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.ref.WeakReference;

/**
 * This class provides the means to read a basis set stored in XML format
 * and convert it into appropriate datastructure representation.
 * <p/>
 * Follows a singleton pattern.
 *
 * @author V.Ganesh
 * @version 1.0
 */
public class BasisReader {

    private static WeakReference _basisReader = null;

    private BasisSet basisSet;
    private AtomicBasis atomicBasis;
    private Orbital orbital;

    private String element = new String("element");

    /**
     * Creates a new instance of BasisReader
     */
    private BasisReader() {
    }

    /**
     * Get an instance (and the only one) of BasisReader
     *
     * @return BasisReader instance
     */
    public static BasisReader getInstance() {
        if (_basisReader == null) {
            _basisReader = new WeakReference(new BasisReader());
        } // end if

        BasisReader basisReader = (BasisReader) _basisReader.get();

        if (basisReader == null) {
            basisReader = new BasisReader();
            _basisReader = new WeakReference(basisReader);
        } // end if

        return basisReader;
    }

    /**
     * Read a particular basis for the basis library (XML)
     *
     * @param basisName the name of basis
     * @return BasisSet object, representing the requested basis set
     */
    public BasisSet readBasis(String basisName) throws Exception {
        // read the XML config file
        // we use the JAXP DOM parser for the job!
        // get an instance of the parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        // set the error handler .. to detect errors(?) in xml
        // in normal situations, error should never occur because
        // the xml files parsed here are automatically generated ones
        db.setErrorHandler(new ErrorHandler() {
            public void warning(SAXParseException e) throws SAXException {
                System.err.println("Warning : " + e);
                e.printStackTrace();
            }

            public void error(SAXParseException e) throws SAXException {
                System.err.println("Error : " + e);
                e.printStackTrace();
            }

            public void fatalError(SAXParseException e) throws SAXException {
                System.err.println("Error : " + e);
                e.printStackTrace();
            }
        }); // setErrorHandler

        // and now, parse the input file:
        Document basisDoc = db.parse(getClass().getResourceAsStream("/org.jscience.chemistry.quantum/basis/basis_" + basisName + ".xml"));

        // and save the basis info. properly
        saveIt(basisDoc);

        return basisSet;
    }

    /**
     * Recursive routine save DOM tree nodes
     * ... and hoping that it reads the basis properly
     */
    private void saveIt(Node n) {
        int type = n.getNodeType();   // get node type

        switch (type) {
            case Node.ATTRIBUTE_NODE:
                String nodeName = n.getNodeName();

                if (nodeName.equals("name")) {
                    // instance of a new basis set
                    basisSet = new BasisSet(nodeName);
                } // end if

                break;
            case Node.ELEMENT_NODE:
                element = n.getNodeName();

                NamedNodeMap atts = n.getAttributes();

                if (element.equals("atom")) {
                    // a new atomic basis
                    atomicBasis = new AtomicBasis(atts.getNamedItem("symbol").getNodeValue(),
                            Integer.parseInt(atts.getNamedItem("atomicNumber").getNodeValue()));
                    basisSet.addAtomicBasis(atomicBasis);
                } else if (element.equals("orbital")) {
                    // a orbital entry for atomic basis
                    orbital = new Orbital(atts.getNamedItem("type").getNodeValue());
                    atomicBasis.addOrbital(orbital);
                } else if (element.equals("entry")) {
                    // a orbital (coefficient, exponent) entry
                    orbital.addEntry(Double.parseDouble(atts.getNamedItem("coeff").getNodeValue()),
                            Double.parseDouble(atts.getNamedItem("exp").getNodeValue()));
                } else {
                    if (atts == null) return;

                    for (int i = 0; i < atts.getLength(); i++) {
                        Node att = atts.item(i);
                        saveIt(att);
                    } // end for
                } // end if

                break;
            default:
                break;
        } // end switch..case

        // save children if any
        for (Node child = n.getFirstChild(); child != null;
             child = child.getNextSibling()) {
            saveIt(child);
        } // end for
    }
} // end of class BasisReader
