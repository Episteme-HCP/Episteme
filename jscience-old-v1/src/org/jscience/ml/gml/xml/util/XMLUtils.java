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
