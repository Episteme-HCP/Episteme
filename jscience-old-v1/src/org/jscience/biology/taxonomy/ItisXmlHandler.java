/**
 *  '$RCSfile: ItisXmlHandler.java,v $'
 *  Copyright: 2000 Regents of the University of California and the
 *              National Center for Ecological Analysis and Synthesis
 *    Authors: @authors@
 *    Release: @release@
 *
 *   '$Author: virtualcall $'
 *     '$Date: 2007-10-23 18:15:17 $'
 * '$Revision: 1.3 $'
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.jscience.biology.taxonomy;

import org.apache.xerces.dom.DOMImplementationImpl;

import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import java.util.Stack;
import java.util.Vector;


/**
 * XML parsing routines that handle conversion of XML documents that are
 * retrieved from ITIS into Java objects.  See: "http://sis.agr.gc.ca/itis"
 * for details.
 */

//code from http://knb.ecoinformatics.org/software/
public class ItisXmlHandler extends DefaultHandler {
    // data structures used temporarily during XML parsing
    /**
     * DOCUMENT ME!
     */
    private Document xmlDocument;

    /**
     * DOCUMENT ME!
     */
    private Stack elementStack;

    /**
     * DOCUMENT ME!
     */
    private Vector taxaList;

    /**
     * DOCUMENT ME!
     */
    private Stack taxaStack;

    /**
     * DOCUMENT ME!
     */
    private String currentValue;

    /**
     * DOCUMENT ME!
     */
    private String currentPathexpr;

    /**
     * DOCUMENT ME!
     */
    private String parserName = null;

/**
     * construct an instance of the Taxon class, manually setting the
     * attributes instead of getting them from an XML stream
     */
    public ItisXmlHandler() {
        // retrieve any configuration parameters we need
        loadConfigurationParameters();
    }

    /**
     * construct an instance of the Taxon class from an XML Stream
     * (generally retrieved from ITIS Canada)
     *
     * @param xmlReader the XML representation of the taxon as a Reader
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Vector parseTaxa(Reader xmlReader) throws Exception {
        // Initialize temporary variables
        try {
            DOMImplementation dom = DOMImplementationImpl.getDOMImplementation();
            xmlDocument = dom.createDocument("taxondoc", "taxondoc", null);
        } catch (DOMException de) {
            //Utility.debug(1, "Couldn't build taxon document.");
        }

        elementStack = new Stack();
        taxaList = new Vector();
        taxaStack = new Stack();

        // Initialize the parser and read the xml
        XMLReader parser = initializeParser();

        if (parser == null) {
            //Utility.debug(1, "SAX parser not instantiated properly.");
        }

        try {
            parser.parse(new InputSource(xmlReader));
        } catch (IOException ioe) {
            //Utility.debug(4, "Error reading the xml during parsing.");
            throw (new Exception(ioe.getMessage()));
        } catch (SAXException e) {
            //Utility.debug(4, "Error parsing taxon xml (" + e.getClass().getName() +").");
            /*
            Utility.debug(4, e.getMessage());
            Utility.debug(4, e.toString());
            e.printStackTrace(System.err);
            */
            throw (new Exception(e.getMessage()));
        }

        // Return the first taxon found.  Note this potentially ignores some others
        //Utility.debug(20, "Found " + taxaList.size() + " taxa.");
        return taxaList;
    }

    /**
     * construct an instance of the Taxon class from an XML String
     * (generally retrieved from ITIS Canada)
     *
     * @param xmlString the XML representation of the taxon as a String
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Vector parseTaxa(String xmlString) throws Exception {
        return parseTaxa(new StringReader(xmlString));
    }

    /**
     * Set up the SAX parser for reading the XML serialized taxa
     *
     * @return DOCUMENT ME!
     */
    private XMLReader initializeParser() {
        XMLReader parser = null;

        // Set up the SAX document handlers for parsing
        try {
            // Get an instance of the parser
            parser = XMLReaderFactory.createXMLReader(parserName);

            // Set the ContentHandler to this instance
            parser.setContentHandler(this);

            // Set the error Handler to this instance
            parser.setErrorHandler(this);
        } catch (Exception e) {
            //Utility.debug(1, "Error in initializeParser " + e.toString());
        }

        return parser;
    }

    /**
     * callback method used by the SAX Parser when the start tag of an
     * element is detected. Used in this context to parse and store the taxon
     * information in class variables.
     *
     * @param uri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     * @param atts DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void startElement(String uri, String localName, String qName,
        Attributes atts) throws SAXException {
        //Utility.debug(20, "Processing start element: " + localName);
        Element currentNode = null;

        try {
            currentNode = xmlDocument.createElement(localName);
        } catch (DOMException de) {
            //Utility.debug(1, "Error creating element while processing taxon.");
        }

        // add attributes to Element here
        if (atts != null) {
            int len = atts.getLength();

            for (int i = 0; i < len; i++) {
                currentNode.setAttribute(atts.getLocalName(i), atts.getValue(i));
            }
        }

        elementStack.push(currentNode);

        if (currentNode.getTagName().equals("taxon") ||
                currentNode.getTagName().equals("synonym") ||
                currentNode.getTagName().equals("parent") ||
                currentNode.getTagName().equals("child")) {
            ItisTaxon newTaxon = new ItisTaxon();

            if (currentNode.getTagName().equals("taxon")) {
                newTaxon.setDataComplete(true);
            } else if (currentNode.getTagName().equals("child")) {
                ItisTaxon parentTaxon = (ItisTaxon) taxaStack.peek();
                parentTaxon.setHasChildList(true);
            }

            //Utility.debug(20, "Adding taxon to stack. ");
            taxaStack.push(newTaxon);
        }
    }

    /**
     * callback method used by the SAX Parser when the end tag of an
     * element is detected. Used in this context to parse and store the taxon
     * information in class variables.
     *
     * @param uri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param qName DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        //Utility.debug(20, "Processing end element: " + localName);
        Element leaving = (Element) elementStack.pop();

        if (leaving.getTagName().equals("taxon") ||
                leaving.getTagName().equals("synonym") ||
                leaving.getTagName().equals("parent") ||
                leaving.getTagName().equals("child")) {
            //Utility.debug(20, "Adding taxon to list. ");
            ItisTaxon newTaxon = (ItisTaxon) taxaStack.pop();
            taxaList.add(newTaxon);
        }
    }

    /**
     * callback method used by the SAX Parser when the text sequences
     * of an xml stream are detected. Used in this context to parse and store
     * the taxon information in class variables.
     *
     * @param ch DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param length DOCUMENT ME!
     */
    public void characters(char[] ch, int start, int length) {
        //Utility.debug(20, "Processing characters.");
        String inputString = new String(ch, start, length);
        Element currentNode = (Element) elementStack.peek();
        String currentTag = currentNode.getTagName();
        ItisTaxon currentTaxon = null;

        if (!taxaStack.empty()) {
            currentTaxon = (ItisTaxon) taxaStack.pop();
        }

        if (currentTag.equals("tsn")) {
            long tsn = (new Long(inputString)).longValue();
            currentTaxon.setTsn(tsn);

            //Utility.debug(15, "Setting tsn to: " + tsn);
            Element parentNode = (Element) elementStack.get(elementStack.size() -
                    2);

            if (parentNode.getTagName().equals("parent")) {
                ItisTaxon previousTaxon = (ItisTaxon) taxaStack.peek();
                previousTaxon.setParentTsn(tsn);

                //Utility.debug(15, "Setting parent tsn to: " + tsn);
            } else if (parentNode.getTagName().equals("synonym")) {
                ItisTaxon previousTaxon = (ItisTaxon) taxaStack.peek();
                previousTaxon.addSynonymTsn(tsn);

                //Utility.debug(15, "Adding child tsn: " + tsn);
            } else if (parentNode.getTagName().equals("child")) {
                ItisTaxon previousTaxon = (ItisTaxon) taxaStack.peek();
                previousTaxon.addChildTsn(tsn);

                //Utility.debug(15, "Adding child tsn: " + tsn);
            }
        } else if (currentTag.equals("concatenatedname")) {
            currentTaxon.setScientificName(inputString);

            //Utility.debug(15, "Setting name to: " + inputString);
        } else if (currentTag.equals("synonymname")) {
            currentTaxon.setScientificName(inputString);

            //Utility.debug(15, "Setting name to: " + inputString);
        } else if (currentTag.equals("rank")) {
            currentTaxon.setTaxonRank(inputString);

            //Utility.debug(15, "Setting rank to: " + inputString);
        } else if (currentTag.equals("taxonauthor")) {
            currentTaxon.setTaxonAuthor(inputString);

            //Utility.debug(15, "Setting author to: " + inputString);
        } else if (currentTag.equals("commonname")) {
            currentTaxon.addVernacularName(inputString);

            //Utility.debug(15, "Adding common name: " + inputString);
        }

        if (currentTaxon != null) {
            taxaStack.push(currentTaxon);
        }
    }

    /**
     * Load the configuration parameters that we need
     */
    private void loadConfigurationParameters() {
        parserName = "org.apache.xerces.parsers.SAXParser";
    }
}
