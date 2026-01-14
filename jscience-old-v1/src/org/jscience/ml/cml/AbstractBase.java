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

package org.jscience.ml.cml;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.Writer;

/**
 * The base class for all elementObjects in the Schema
 * <p/>
 * Any element may have attributes:titleiddictRefconventionand convenience get/set methods are provided for all, through
 * the interfaces AttributeTitle, AttributeId and AttributeConvention.An element (FOO) subclassed from a AbstractBase may be constructed in the
 * following ways:FOO(). Creates a new empty element with null values of the
 * attributes. Required for newInstance(), but use carefully since
 * it has no tagName and no document associated with it.FOO(String tagName, Document document). Creates an empty
 * element with null attribute names.FOO(org.w3c.dom.Element element). Creates a subclassed Element with
 * the same attribute values as the input Element. Used when a DOM has
 * been created with non-aware software. The routine
 * makeAndProcessSubclass(Element element) will replace the current Element
 * with the appropriate subclass.
 *
 * @author P.Murray-Rust, 1999, 2000, 2003
 */
public interface AbstractBase extends Element, CMLNode {

    public final static String NAMESPACE_URI = "http://www.xml-cml.org/schema/cml2/core";
    public final static String PACKAGE_NAME = "org.jscience.ml.cml";

    public final static String[] CONTENT_MODEL = {"#ANY"};

    // fundamental data types might as well go here
    public final static int UNKNOWN = -1;
    public final static int STRING_TYPE = UNKNOWN + 1;
    public final static int FLOAT_TYPE = STRING_TYPE + 1;
    public final static int INTEGER_TYPE = FLOAT_TYPE + 1;

    public final static String CONVENTION = "convention";
    public final static String DICTREF = "dictRef";
    public final static String ID = "id";
    public final static String REF = "ref";
    public final static String TITLE = "title";

    /**
     * output CML variants
     */
    public final static String CML1 = "CML1";
    public final static String CML2 = "CML2";
    public final static String CMLCDK = "CMLCDK";

    /** ====================== ATTRIBUTES ======================= */

    /** ========================================================= */

// ------------------ content model stuff ----------------

    /**
     * sets attribute, changing data type if required
     *
     * @param attName
     * @param attVal
     */
    void /*setUntypedAttribute*/setAttribute(String attName, String attVal)/* throws org.jscience.ml.cml.CMLException*/;

    /**
     * gets attribute, changing data type if required
     *
     * @param attName
     */
    String /*getUntypedAttribute*/getAttribute(String attName)/* throws org.jscience.ml.cml.CMLException*/;

    /**
     * removes whitespace nodes or throws any Exception for non-whitespace ones
     *
     * @throws CMLException non-whitespace Text node
     */
    void removeTextChildren() throws org.jscience.ml.cml.CMLException;

    /**
     * get a child of a given name
     * If it doesn't exist, create and add it
     *
     * @param elemName the elementName to get/create (identified by elementName not class)
     * @return the Element
     * @throws org.jscience.ml.cml.CMLException
     *          element cannot be added (violates content model)
     */
    Element getOrCreateChild(String elemName) throws org.jscience.ml.cml.CMLException;

    /**
     * SAX2 parsing routine
     * called from characters() callback
     * NOT namespace aware
     *
     * @param saxHandler SaxHandler
     * @param content    throws exception (probably application specific)
     */

    void characters(SaxHandler saxHandler, String content) throws org.jscience.ml.cml.CMLException;

    /**
     * SAX2 parsing routine
     * called from endElement() callback
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     *                   throws exception (probably application specific)
     */
    void endElement(SaxHandler saxHandler) throws org.jscience.ml.cml.CMLException;

    /**
     * SAX2 parsing routine
     * called from startElement() callback
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     * @param attributes the attribute list
     *                   throws exception (probably application specific)
     */
    void startElement(SaxHandler saxHandler, Attributes attributes) throws org.jscience.ml.cml.CMLException;

    /**
     * adds text content to an element
     * should only be used for text-only or mixed content.
     *
     * @param value the content
     * @throws CMLException thrown by subclasses
     */
    void setContentValue(String value) /*throws CMLException*/;

    /**
     * gets text content from an element
     * should only be used for text-only or mixed content.
     *
     * @throws CMLException thrown by subclasses
     */
    String getContentValue() /*throws CMLException*/;

    /**
     * deep clones an element including subclassing
     * <p/>
     * uses intermediate XML serialization
     * object is owned by a new document
     *
     * @return the copied object
     */
    AbstractBase deepCopy();

    /**
     * deep clones an element including subclassing
     * <p/>
     * uses intermediate XML serialization
     * object is owned by document
     *
     * @param document ownerDocument
     * @return the copied object
     */
    AbstractBase deepCopy(AbstractCMLDocument document);

    /**
     * write XML
     * use current control (version and syntax)
     *
     * @param w - output
     */
    void writeXML(Writer w) throws org.jscience.ml.cml.CMLException, IOException;

    /**
     * get owner document as CMLDocument.
     *
     * @return the document
     */
    AbstractCMLDocument getCMLDocument();

    /**
     * get tool.
     * <p/>
     * creates one if not already created
     * do not use if subclass is known to compiler
     * (use FooToolImpl.getTool(foo) instead)
     *
     * @return the tool
     */
    BaseTool getTool();

    /**
     * get or create tool.
     * <p/>
     * creates one if not already created
     * should only be used when the subclass is not known to the compiler
     * i.e. where an abstractBase is found
     *
     * @return the tool
     */
    public BaseTool getOrCreateTool();

    /**
     * set tool
     * <p/>
     * sets the tool. NOT user-callable
     *
     * @param tool
     */
    void setTool(BaseTool tool);

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
     * addStringContent to Element
     * <p/>
     * if no Text child, creates one
     * if Text child, resets value
     * if existing non-Text children, no action
     *
     * @param value to add
     */
    void setTextChild(String value);

    /**
     * update delegates.
     * updates this delegates (content and attributes)
     * then recurses through children
     */
    void updateDelegates();

    /**
     * update DOM.
     * updates this DOM (content and attributes)
     * then recurses through children
     */
    void updateDOM();

    /**
     * update delegate content.
     */
    void updateDelegateContent();

    /**
     * update delegate attributes.
     */
    void updateDelegateAttributes();

    /**
     * update DOM content.
     */
    void updateDOMContent();

    /**
     * update DOM attributes.
     */
    void updateDOMAttributes();
}
