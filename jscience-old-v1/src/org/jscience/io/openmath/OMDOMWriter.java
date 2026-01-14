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

package org.jscience.io.openmath;

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
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Version$
 */
public class OMDOMWriter {
    /**
     * Stores the namespace prefix.<p></p>
     */
    protected String mPrefix;

    /**
     * Stores the document reference we use to create elements.<p></p>
     */
    protected Document mDocument;

/**
     * Constructor. <p>
     */
    public OMDOMWriter() {
        try {
            DocumentBuilder tBuilder = DocumentBuilderFactory.newInstance()
                                                             .newDocumentBuilder();

            mDocument = tBuilder.newDocument();
        } catch (Exception tException) {
        }
    }

    /**
     * Set the namespace prefix.<p></p>
     *
     * @param fPrefix the namespace prefix to generate.
     */
    public void setNamespacePrefix(String fPrefix) {
        mPrefix = fPrefix;
    }

    /**
     * Get the namespace prefix.<p></p>
     *
     * @return the namespace prefix.
     */
    public String getNamespacePrefix() {
        return mPrefix;
    }

    /**
     * Write out a DOM document.<p></p>
     *
     * @param fObject the object to write out.
     *
     * @return the generated DOM document.
     *
     * @throws IOException when a serious error occurs.
     */
    public Document write(OMObject fObject) throws IOException {
        try {
            Node tNode = mDocument.createElement("OMOBJ");
            tNode.appendChild(writeNode(fObject));
            mDocument.appendChild(tNode);

            return mDocument;
        } catch (Exception tException) {
            tException.printStackTrace();

            throw new IOException("Unable to write out DOM tree:" +
                tException.getMessage());
        }
    }

    /**
     * Write out a DOM document node.<p></p>
     *
     * @param fObject the object to write out.
     *
     * @return the generated DOM node.
     *
     * @throws IOException when a serious error occurs.
     */
    public Node writeNode(OMObject fObject) throws IOException {
        Node tNode = null;

        if (mDocument != null) {
            if (fObject instanceof OMApplication) {
                OMApplication tApplication = (OMApplication) fObject;
                Node tChild;

                Enumeration tEnum = tApplication.getElements().elements();
                tNode = mDocument.createElement("OMA");

                for (; tEnum.hasMoreElements();) {
                    tChild = writeNode((OMObject) tEnum.nextElement());
                    tNode.appendChild(tChild);
                }
            }

            if (fObject instanceof OMAttribution) {
                OMAttribution tAttribution = new OMAttribution();

                tNode = mDocument.createElement("OMATTR");

                Hashtable tAttributions = tAttribution.getAttributions();
                Enumeration tKeys = tAttributions.keys();
                Enumeration tValues = tAttributions.elements();
                Element tPairs = mDocument.createElement("OMATP");

                for (; tKeys.hasMoreElements();) {
                    OMObject tKey = (OMObject) tKeys.nextElement();
                    OMObject tValue = (OMObject) tValues.nextElement();

                    tPairs.appendChild(writeNode(tKey));
                    tPairs.appendChild(writeNode(tValue));
                }

                tNode.appendChild(tPairs);
                tNode.appendChild(writeNode(tAttribution.getConstructor()));
            }

            if (fObject instanceof OMByteArray) {
                OMByteArray tByteArray = (OMByteArray) fObject;
                Node tText = mDocument.createTextNode(tByteArray.getByteArrayAsString());

                tNode = mDocument.createElement("OMB");
                tNode.appendChild(tText);
            }

            if (fObject instanceof OMBinding) {
                OMBinding tBinding = new OMBinding();

                tNode = mDocument.createElement("OMBIND");
                tNode.appendChild(writeNode(tBinding.getBinder()));

                Element tVariables = mDocument.createElement("OMBVAR");
                Enumeration tEnum = tBinding.getVariables().elements();

                for (; tEnum.hasMoreElements();) {
                    OMObject tChild = (OMObject) tEnum.nextElement();
                    tVariables.appendChild(writeNode(tChild));
                }

                tNode.appendChild(tVariables);
                tNode.appendChild(writeNode(tBinding.getBody()));
            }

            if (fObject instanceof OMError) {
                OMError tError = (OMError) fObject;
                Node tChild;

                Enumeration tEnum = tError.getElements().elements();
                ;

                tNode = mDocument.createElement("OME");
                tNode.appendChild(writeNode(tError.getSymbol()));

                for (; tEnum.hasMoreElements();) {
                    tChild = writeNode((OMObject) tEnum.nextElement());
                    tNode.appendChild(tChild);
                }
            }

            if (fObject instanceof OMFloat) {
                tNode = mDocument.createElement("OMF");
            }

            if (fObject instanceof OMInteger) {
                OMInteger tInteger = (OMInteger) fObject;
                Node tText = mDocument.createTextNode(tInteger.getInteger());

                tNode = mDocument.createElement("OMI");
                tNode.appendChild(tText);
            }

            if (fObject instanceof OMString) {
                OMString tString = (OMString) fObject;
                Node tText = mDocument.createTextNode(tString.getString());

                tNode = mDocument.createElement("OMSTR");
                tNode.appendChild(tText);
            }

            if (fObject instanceof OMSymbol) {
                tNode = mDocument.createElement("OMS");
            }

            if (fObject instanceof OMVariable) {
                tNode = mDocument.createElement("OMV");
            }

            if (mPrefix != null) {
                tNode.setPrefix(mPrefix);
            }

            /*
            * Add the attributes.
            */
            Hashtable tAttributes = fObject.getAttributes();
            Enumeration tKeys = tAttributes.keys();
            Enumeration tValues = tAttributes.elements();
            Element tElement = (Element) tNode;

            for (; tKeys.hasMoreElements();) {
                String tKey = (String) tKeys.nextElement();
                String tValue = (String) tValues.nextElement();
                tElement.setAttribute(tKey, tValue);
            }

            return tNode;
        }

        throw new IOException("Unable to write out DOM node");
    }
}
