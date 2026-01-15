package org.jscience.ml.cml;

import org.jscience.ml.cml.dom.pmr.PMRDocument;

import org.w3c.dom.Element;

import java.util.Vector;


/**
 * CORE InterfaceDTD ClassThe interface for any Document objects There are NO
 * Core Methods.
 *
 * @author P.Murray-Rust, 1999, 2000, 2003
 */
public interface AbstractCMLDocument extends PMRDocument, CMLNode {
    /** the package name */
    static String PACKAGE_NAME = "org.jscience.ml.cml";

    /** DOCUMENT ME! */
    static String NULL_DTD = "";

    /** DOCUMENT ME! */
    static String NULL_NAMESPACE = "null";

    /** DOCUMENT ME! */
    static String DEFAULT_OUTPUT_ENCODING = "ISO-8859-1";

    /** DOCUMENT ME! */
    static String DEFAULT_OUTPUT_DTD = "cml.dtd";

    /** DOCUMENT ME! */
    static String DEFAULT_OUTPUT_NAMESPACE = org.jscience.ml.cml.AbstractBase.NAMESPACE_URI;

    /**
     * gets first element of given type
     *
     * @param name element to return
     *
     * @return Element the element (null if none)
     */
    Element getFirstElement(String name);

    /**
     * gets all element of given type (included nested children)
     *
     * @param name elements to return
     *
     * @return Vector of elements (empty if none)
     */
    Vector getElementVector(String name);

    /**
     * gets all element of given type (included nested children)
     *
     * @param name elements to return
     *
     * @return array of elements (empty if none)
     */
    Element[] getElementList(String name);

    /**
     * creates new Element of given subclass
     *
     * @param tagName determines the class
     *
     * @return the new element
     */
    Element createSubclassedElement(String tagName);

    /**
     * DOCUMENT ME!
     *
     * @param oldEl DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Element subclass(Element oldEl);

    /**
     * creates new AbstractBase
     *
     * @return the new element
     */
    AbstractBase createAbstractBase();

    /**
     * toggle debugging
     *
     * @param d debug
     */
    void setDebug(boolean d);

    /**
     * debug status
     *
     * @return is debug set
     */
    boolean getDebug();

    /**
     * clone element in context of document.
     *
     * @param element to clone (may or may not be owned by this)
     *
     * @return the cloned element (owner is this)
     *
     * @throws CMLException cannot clone non-AbstractBase
     */
    AbstractBase clone(AbstractBase element) throws CMLException;

    /**
     * update delegates. updates documentElement delegates (content and
     * attributes) then recurses through children
     */
    void updateDelegates();

    /**
     * update DOM. updates documentElement DOM (content and attributes)
     * then recurses through children
     */
    void updateDOM();
}
