/*
 * $Id: OMDOMWriter.java,v 1.3 2007-10-23 18:21:27 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
 */
package org.jscience.ml.openmath.io;

import org.jscience.ml.openmath.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * An OMDOMWriter.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMDOMWriter {
    /**
     * Stores the namespace prefix.<p></p>
     */
    protected String prefix;

    /**
     * Stores the document reference we use to create elements.<p></p>
     */
    protected Document document;

/**
     * Constructor. <p>
     */
    public OMDOMWriter() {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                                            .newDocumentBuilder();

            document = builder.newDocument();
        } catch (Exception exception) {
        }
    }

    /**
     * Set the namespace prefix.<p></p>
     *
     * @param newPrefix the namespace prefix to generate.
     */
    public void setNamespacePrefix(String newPrefix) {
        prefix = newPrefix;
    }

    /**
     * Get the namespace prefix.<p></p>
     *
     * @return the namespace prefix.
     */
    public String getNamespacePrefix() {
        return prefix;
    }

    /**
     * Write out a DOM document.<p></p>
     *
     * @param object the object to write out.
     *
     * @return the generated DOM document.
     *
     * @throws IOException when a serious error occurs.
     */
    public Document write(OMObject object) throws IOException {
        try {
            if (object instanceof OMRoot) {
                Node node = writeNode(object);
                document.appendChild(node);
            } else {
                Node node = document.createElement("OMOBJ");

                if (prefix != null) {
                    node.setPrefix(prefix);
                }

                node.appendChild(writeNode(object));
                document.appendChild(node);
            }

            return document;
        } catch (Exception exception) {
            exception.printStackTrace(System.err);
            throw new IOException("Unable to write out DOM tree:" +
                exception.getMessage());
        }
    }

    /**
     * Write out a DOM document node.<p></p>
     *
     * @param object the object to write out.
     *
     * @return the generated DOM node.
     *
     * @throws IOException when a serious error occurs.
     */
    public Node writeNode(OMObject object) throws IOException {
        Node node = null;
        Node child = null;

        if (document != null) {
            /*
            * Create the element with basic data.
            */
            if (object instanceof OMApplication) {
                node = document.createElement("OMA");

                OMApplication application = (OMApplication) object;
                Enumeration enumeration = application.getElements().elements();

                for (; enumeration.hasMoreElements();) {
                    child = writeNode((OMObject) enumeration.nextElement());
                    node.appendChild(child);
                }
            }

            if (object instanceof OMAttribution) {
                node = document.createElement("OMATTR");

                OMAttribution attribution = new OMAttribution();
                Hashtable attributions = attribution.getAttributions();
                Enumeration keys = attributions.keys();
                Enumeration values = attributions.elements();
                Element pairs = document.createElement("OMATP");

                for (; keys.hasMoreElements();) {
                    OMObject key = (OMObject) keys.nextElement();
                    OMObject value = (OMObject) values.nextElement();

                    pairs.appendChild(writeNode(key));
                    pairs.appendChild(writeNode(value));
                }

                node.appendChild(pairs);
                node.appendChild(writeNode(attribution.getConstructor()));
            }

            if (object instanceof OMByteArray) {
                node = document.createElement("OMB");

                OMByteArray byteArray = (OMByteArray) object;
                Node text = document.createTextNode(byteArray.getByteArrayAsString());

                node.appendChild(text);
            }

            if (object instanceof OMBinding) {
                node = document.createElement("OMBIND");

                OMBinding binding = new OMBinding();
                Element variables = document.createElement("OMBVAR");
                Enumeration enumeration = binding.getVariables().elements();

                node.appendChild(writeNode(binding.getBinder()));

                for (; enumeration.hasMoreElements();) {
                    OMObject element = (OMObject) enumeration.nextElement();
                    variables.appendChild(writeNode(element));
                }

                node.appendChild(variables);
                node.appendChild(writeNode(binding.getBody()));
            }

            if (object instanceof OMError) {
                node = document.createElement("OME");

                OMError error = (OMError) object;
                Enumeration enumeration = error.getElements().elements();

                node.appendChild(writeNode(error.getSymbol()));

                for (; enumeration.hasMoreElements();) {
                    child = writeNode((OMObject) enumeration.nextElement());
                    node.appendChild(child);
                }
            }

            if (object instanceof OMFloat) {
                node = document.createElement("OMF");
            }

            if (object instanceof OMForeign) {
                node = document.createElement("OMFOREIGN");

                OMForeign foreign = (OMForeign) object;
            }

            if (object instanceof OMInteger) {
                node = document.createElement("OMI");

                OMInteger integer = (OMInteger) object;
                Node text = document.createTextNode(integer.getInteger());

                node.appendChild(text);
            }

            if (object instanceof OMReference) {
                node = document.createElement("OMR");
            }

            if (object instanceof OMRoot) {
                node = document.createElement("OMOBJ");
            }

            if (object instanceof OMString) {
                node = document.createElement("OMSTR");

                OMString string = (OMString) object;
                Node text = document.createTextNode(string.getString());

                node.appendChild(text);
            }

            if (object instanceof OMSymbol) {
                node = document.createElement("OMS");
            }

            if (object instanceof OMVariable) {
                node = document.createElement("OMV");
            }

            if (prefix != null) {
                node.setPrefix(prefix);
            }

            /*
            * Add the attributes.
            */
            Hashtable attributes = object.getAttributes();
            Enumeration keys = attributes.keys();
            Enumeration values = attributes.elements();
            Element element = (Element) node;

            for (; keys.hasMoreElements();) {
                String key = (String) keys.nextElement();
                String value = (String) values.nextElement();
                element.setAttribute(key, value);
            }

            return node;
        }

        throw new IOException("Unable to write out DOM node");
    }
}
