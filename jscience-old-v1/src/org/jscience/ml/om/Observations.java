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

/* ====================================================================
 * /Observations.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;


import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.jscience.ml.om.util.SchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * The Observations element is the root element of a
 * schema element. All other schema elements are
 * grouped below Observations.<br>
 * The object itself contains no astronomical data but
 * provides XML namespaces, and schema element containers.
 * A schema element container groups multiple
 * schema elements of one and the same type.<br>
 * E.g.<br>
 * <observers><br>
 * <observer><br>
 * <name>Foo</name><br>
 * <i>More observer stuff goes here</i><br>
 * </observer><br>
 * <observer><br>
 * <name>Foo</name><br>
 * <i>More observer stuff goes here</i><br>
 * </observer><br>
 * </observers><br>
 * In this example <observers> is the container
 * element of multiple <observer> elements.<br>
 * Also the Observations object contains the
 * serializeToSchema() method, that will create
 * a schema valid XML file.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public class Observations {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    // XML Namespace of schema Key
    public static final String XML_NS_KEY = "xmlns:fgca";

    // XML Namespace of schema
    public static final String XML_NS = "http://www.hobby-astronomie.net/comast";


    // XML SI Schema Key 
    public static final String XML_SI_KEY = "xmlns:xsi";

    // XML SI Schema
    public static final String XML_SI = "http://www.w3.org/2001/XMLSchema-instance";


    // XML Schema Location Key
    public static final String XML_SCHEMA_LOCATION_KEY = "xsi:schemaLocation";

    // XML Schema Location
    public static final String XML_SCHEMA_LOCATION = "http://www.hobby-astronomie.net/comast et_logbook.xsd";

    // XML Schema Version Key
    public static final String XML_SCHEMA_VERSION_KEY = "version";

    // XML Schema Location 
    public static final String XML_SCHEMA_VERSION = "1.4";


    // Schema container for <observation> objects
    public static final String XML_OBSERVATION_CONTAINER = "fgca:observations";

    // Schema container for <session> objects
    public static final String XML_SESSION_CONTAINER = "sessions";

    // Schema container for <target> objects
    public static final String XML_TARGET_CONTAINER = "targets";

    // Schema container for <observer> objects
    public static final String XML_OBSERVER_CONTAINER = "observers";

    // Schema container for <site> objects
    public static final String XML_SITE_CONTAINER = "sites";

    // Schema container for <scope> objects
    public static final String XML_SCOPE_CONTAINER = "scopes";

    // Schema container for <eyepiece> objects
    public static final String XML_EYEPIECE_CONTAINER = "eyepieces";

    // ------------------
    // Instance Variables ------------------------------------------------
    // ------------------

    // All obervation objects belonging to this Observations group
    private ArrayList observationsList = new ArrayList();

    // --------------
    // Public methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------  

    public Collection getObservations() {

        return this.observationsList;

    }

    // -------------------------------------------------------------------  
    public void addObservations(Observations observations) throws SchemaException {

        if (observations != null) {
            observationsList.addAll(observations.observationsList);
        } else {
            throw new SchemaException("Observations cannot be null. ");
        }

    }

    // -------------------------------------------------------------------
    public void addObservation(IObservation observation) throws SchemaException {

        if (observation != null) {
            observationsList.add(observation);
        } else {
            throw new SchemaException("Observation cannot be null. ");
        }

    }

    // -------------------------------------------------------------------
    public void serializeAsXml(File xmlFile) throws SchemaException {

        if (xmlFile == null) {
            throw new SchemaException("File cannot be null. ");
        }

        Document newSchema = null;
        try {
            newSchema = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException pce) {
            throw new SchemaException("Unable to create new XML document. ", pce);
        }

        Iterator iterator = observationsList.iterator();
        IObservation current = null;
        Element root = newSchema.createElement(Observations.XML_OBSERVATION_CONTAINER);
        root.setAttribute(XML_NS_KEY, XML_NS);
        root.setAttribute(XML_SI_KEY, XML_SI);
        root.setAttribute(XML_SCHEMA_LOCATION_KEY, XML_SCHEMA_LOCATION);
        root.setAttribute(XML_SCHEMA_VERSION_KEY, XML_SCHEMA_VERSION);

        newSchema.appendChild(root);

        while (iterator.hasNext()) {
            current = (IObservation) iterator.next();
            current.addToXmlElement(root);
        }

        // Create OutputFormat for document, with default encoding (UTF-8) and pretty print (intent)
        OutputFormat outputFormat = new OutputFormat(newSchema,
                "ISO-8859-1",
                true);
        XMLSerializer serializer = new XMLSerializer(outputFormat);
        try {
            serializer.setOutputByteStream(new FileOutputStream(xmlFile));
            serializer.serialize(newSchema);
        } catch (FileNotFoundException fnfe) {
            throw new SchemaException("File not found: " + xmlFile.getAbsolutePath());
        } catch (IOException ioe) {
            throw new SchemaException("Error while serializing. Nested Exception is: ", ioe);
        }

    }

}
