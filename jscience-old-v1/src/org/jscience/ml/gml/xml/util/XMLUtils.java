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

/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.xml.util;

import org.jaxen.jdom.JDOMXPath;

import org.jaxen.saxpath.SAXPathException;

import org.jdom.*;

import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Provides some utility methods.
 *
 * @author Milan Trninic, revised by Aleksandar Milanovic
 * @version 1.1
 */
public class XMLUtils {
    /**
     * Converts XML text to a JDOM document.
     *
     * @param sourceString DOCUMENT ME!
     *
     * @return Null if the conversion fails.
     */
    public static Document string2jdom(String sourceString) {
        SAXBuilder builder = new SAXBuilder();
        StringReader stringReader = new StringReader(sourceString);
        Document result = null;

        try {
            result = builder.build(stringReader);
        } catch (JDOMException e) {
            // do nothing
        } catch (IOException e) {
            // do nothing
        }

        return result;
    }

    /**
     * Executes an XPath query using namespaces.
     *
     * @param parent The query context.
     * @param path The XPath query.
     *
     * @return Returns a list of XML nodes matching the query.
     *         java.util.Collections.EMPTY_LIST is returned if no match was
     *         found.
     *
     * @throws SAXPathException DOCUMENT ME!
     */
    public static List executeXPathQuery(Element parent, String path)
        throws SAXPathException {
        // Create a new XPath
        JDOMXPath xpath = new JDOMXPath(path);
        List result = xpath.selectNodes(parent);

        return result;
    }

    /**
     * Finds global elements with the given attribute value.
     *
     * @param root DOCUMENT ME!
     * @param elemName DOCUMENT ME!
     * @param elemNamespace DOCUMENT ME!
     * @param attrName DOCUMENT ME!
     * @param attrValue DOCUMENT ME!
     *
     * @return List of global elements. Empty list if none found.
     */
    public static List getGlobalElementsWithAttribute(Element root,
        String elemName, // local
        Namespace elemNamespace, String attrName, String attrValue) {
        List complexTypes = root.getChildren(elemName, elemNamespace);
        List result = new LinkedList();
        Iterator complexTypeIter = complexTypes.iterator();

        while (complexTypeIter.hasNext()) {
            Element nextComplexType = (Element) complexTypeIter.next();
            Attribute nameAttribute = nextComplexType.getAttribute(attrName);

            if ((nameAttribute != null) &&
                    nameAttribute.getValue().equals(attrValue)) {
                result.add(nextComplexType);
            }
        }

        if (result.size() == 0) {
            result = Collections.EMPTY_LIST;
        }

        return result;
    }
}
