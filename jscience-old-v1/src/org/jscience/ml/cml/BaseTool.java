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
import java.lang.reflect.Method;
import java.util.List;

/**
 * Performs editing functions on a generic CMLObject
 */
public interface BaseTool {

    final static String DOUBLE = "Double";
    final static String INTEGER = "Integer";
    final static String STRING = "String";

    final static String XSD_DOUBLE = "xsd:double";
    final static String XSD_INTEGER = "xsd:integer";
    final static String XSD_STRING = "xsd:string";

    /**
     * get abstract base.
     * <p/>
     * only defined for implemented tools (default => null)
     *
     * @return the element associated with this tool (null for default)
     */
    AbstractBase getAbstractBase();

    /** get Tool.
     *
     * normally subclassed
     *
     * @param base element to get Tool for
     */
//    BaseTool getToolX(AbstractBase base);

    /**
     * set abstract base
     * <p/>
     * only defined for implemented tools (default => null)
     * should not be required in user code
     *
     * @param ab the element associated with this tool
     */
    void setAbstractBase(AbstractBase ab);

    /**
     * ignore an attribute
     *
     * @param name    attribute to ignore
     * @param descend carry over to descendants
     */
    void setIgnoreAttribute(String name, boolean descend);

    /**
     * is the contained object equal to the other contained object.
     * <p/>
     * The two objects contained in the elements are compared. The default
     * is to use an element-by-element and attribute-by-attribute comparison.
     * This can be overridden for subclasses. For example the order of children
     * in some elements matters while in others (e.g. molecules) it does not. The
     * compared object must be of the same class but may have different namespace
     * prefix, etc. By default ownerDocuments are ignored (thus molecules in different
     * documents can be compared.)
     * <p/>
     * The tools may have sepcial methods to affect of control equality. Thus
     * it is possible to ignore Ids.
     * <p/>
     * If the reason for non-equality is required, the mustEqual() method should
     * be used.
     *
     * @param baseTool the other element tool (must be of same class)
     */
    boolean equals(BaseTool baseTool);

    /**
     * throws exception if the contained object is not equal to the other contained object.
     * <p/>
     * Essentially an assert() mechanism; see equals() for strategy.
     *
     * @param baseTool the other element tool (must be of same class)
     * @throws CMLException the reason for non-equality
     */
    void mustEqual(BaseTool baseTool) throws CMLException;

    /**
     * removes the first element by tagName
     * <p/>
     * finds first element with that name and removes it from its parent.
     * mainly for debugging
     *
     * @param elementName
     * @return the element removed
     */

    Element removeElement(String elementName);

    /**
     * SAX2 parsing routine - called from characters() callback
     * <p/>
     * NOT namespace aware
     *
     * @param saxHandler SaxHandler
     * @param content    throws exception (probably application specific)
     */
    void characters(SaxHandler saxHandler, String content) throws CMLException;

    /**
     * SAX2 parsing routine - called from endElement() callback
     * <p/>
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     *                   throws exception (probably application specific)
     */
    void endElement(SaxHandler saxHandler) throws CMLException;

    /**
     * SAX2 parsing routine - called from startElement() callback
     * <p/>
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     * @param attributes the attribute list
     *                   throws exception (probably application specific)
     */
    void startElement(SaxHandler saxHandler, Attributes attributes) throws CMLException;

    /**
     * write XML, using class-specific information
     * <p/>
     * NOT namespace aware
     *
     * @param w       the writer
     * @param control (concatenation of CML version and array)
     * @throws exception (probably application specific)
     */
    void writeXML(Writer w, String control) throws CMLException, IOException;

    // utilities
    /**
     * copy attributes from one AbstractBase to another.
     * <p/>
     * overwrites existing atts
     *
     * @param from element to copy from
     * @param to   element to copy to
     */
    void copyAttributesFromTo(AbstractBase from, AbstractBase to);

    /**
     * transfer children from one AbstractBase to another.
     *
     * @param from element to copy from
     * @param to   element to copy to
     */
    void transferChildrenFromTo(AbstractBase from, AbstractBase to);

    /**
     * run workflow script or object.
     * <p/>
     * sets workflow to the tool and runs the current command
     * all information (current command, symbolTables can be accessed from workflow
     *
     * @param workflow to process.
     */
    void processCurrentCommand(Workflow workflow) throws CMLException;

    /**
     * get the workflow.
     *
     * @return workflow to use with tool
     */
    Workflow getWorkflow();

    // =========================== introspection ========================
    /**
     * run any public method.
     * introspects the above methods
     * Example:  CMLVector3 projectOnto(CMLVector3 v)
     * is called with v as arg v1, v2 and floatArg as dontCare
     * and returns Object of type CMLVector3
     *
     * @param methodS   case-sensitive?. Returns without action if duplicate method name
     * @param argVector arguments in order. First arg is instance of this
     * @return any return value (see Method.invoke for classes)
     */
    Object runMethod(String methodS, List argVector);

    /**
     * get all public methods belonging just to this class.
     *
     * @return array of methods
     */
    public Method[] getDeclaredPublicMethods();

    // ============== parse commandline args =======================
    /**
     * process the commandline from main routines.
     * normally subclassed
     *
     * @param args      the arguments
     * @param offset    current position in args
     * @param argVector vector to accumulate arguments
     * @param doc       owner document
     * @return position in args after parsing
     */
    int processCommandLine(String[] args, int offset, List argVector, AbstractCMLDocument doc);

    /**
     * a standard instance for testing.
     * returns null unless subclassed
     *
     * @param doc    the owner document
     * @param serial the instance (1,2,3)
     * @return the new instance (null unless subclassed)
     */
    BaseTool getTestInstance(AbstractCMLDocument doc, int serial);

}
