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

package org.jscience.ml.cml.cmlimpl;

import org.jscience.ml.cml.*;
import org.jscience.ml.cml.logger.JumboLogger;
import org.jscience.ml.cml.util.PMRDOMUtils;
import org.w3c.dom.*;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Performs editing functions on a generic CMLObject
 */
public class BaseToolImpl implements BaseTool {

    protected static JumboLogger jumboLogger = (JumboLogger) JumboLogger.getLogger("");

    protected static String editorPackageName = "org.xmlcml.tools.editor";
    protected static String CONSTRUCT = "_CONSTRUCT";
    protected static String TEST = "_TEST";

    protected static Hashtable baseToolTable;

    // the objects being edited
    protected AbstractBase abstractBase = null;
    // the ownerDocument
    protected AbstractCMLDocument document;

    protected boolean debug = false;

    protected Hashtable ignoreAttributeTable;
    protected Workflow workflow;
    protected Method[] declaredPublicMethods = null;
    protected Constructor[] declaredPublicConstructors = null;

    /**
     * new BaseTool.
     * <p/>
     * only for use by subclasses
     */
    protected BaseToolImpl() {
        abstractBase = null;
    }

    /**
     * new BaseTool containing a new AbstractBase.
     * <p/>
     * uses document as owner
     *
     * @param document the owner document
     */
    public BaseToolImpl(AbstractCMLDocument document) {
        this(document.createAbstractBase());
    }

    protected BaseToolImpl(AbstractBase base) {
        superInit(base);
    }

    protected void superInit(AbstractBase base) {
        setAbstractBase(base);
    }

    /**
     * gets tool or creates new one.
     *
     * @param base
     * @return the tool (null if molecule is null)
     */
    public static BaseTool getTool(AbstractBase base) {
        if (base == null) {
            return null;
        } else {
            BaseTool baseTool = ((CMLBaseImpl) base).getTool();
            if (baseTool == null) {
                baseTool = new BaseToolImpl(base);
            }
            return baseTool;
        }
    }

    /**
     * gets existing tool for <code>base</code> or
     * returns <code>tool</code> after initialising
     * it with <code>base</code>.
     *
     * @param base the item that you want a tool for
     * @param tool the tool to use should base not already have one
     * @return the tool (null if molecule is null)
     */
    public static BaseTool getTool(AbstractBase base, BaseTool tool) {
        if (base == null) {
            return null;
        } else {
            BaseTool baseTool = ((CMLBaseImpl) base).getTool();
            if (baseTool == null) {
                tool.setAbstractBase(base);

                return tool;
            } else {
                return baseTool;
            }
        }
    }

    public AbstractCMLDocument getCMLDocument() {
//		System.out.println ("base " +  abstractBase + " for " + this);
        document = abstractBase.getCMLDocument();
        return document;
    }

    /**
     * get abstract base
     * <p/>
     * only defined for implemented editors (default => null)
     *
     * @return the element associated with this editor (null for default)
     */
    public AbstractBase getAbstractBase() {
        return abstractBase;
    }

    /**
     * set abstract base
     * <p/>
     * only defined for implemented editors (default => null)
     * should not be required in user code
     *
     * @param ab the element associated with this editor
     */
    public void setAbstractBase(AbstractBase ab) /*throws CMLException*/ {

//		System.out.println ("setting abstractBase for " + this + " to " + ab);

        if (ab != null) {
            if (ab != abstractBase) {
//				System.out.println ("abstractBase " + abstractBase + " ab " + ab);

                if (abstractBase != null) {
                    BaseTool oldTool = abstractBase.getTool();

                    if (oldTool != null) {
//						System.out.println ("oldTool " + oldTool + " for " + ab);
                        oldTool.setAbstractBase(null);
                    }
                }

                abstractBase = ab;
                abstractBase.setTool(this);
                document = getCMLDocument();
            }
        } else {
            AbstractBase temp = abstractBase;
            abstractBase = null;

            if (temp != null) {
                temp.setTool(null);
            }
            document = null;
        }

    }

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
     * childElements are compared using their equals() methods
     * <p/>
     * The editors may have special methods to affect of control equality. Thus
     * it is possible to ignore Ids.
     * <p/>
     * If the reason for non-equality is required, the mustEqual() method should
     * be used.
     *
     * @param baseTool the other element editor (must be of same class)
     */
    public boolean equals(BaseTool baseTool) {
        boolean equals = true;
        try {
            mustEqual(baseTool);
        } catch (CMLException e) {
            equals = false;
        }
        return equals;
    }

    /**
     * throws exception if the contained object is not equal to the other contained object.
     * <p/>
     * see equals() for strategy.
     *
     * @param otherTool the other element (must be of same class)
     * @throws CMLException the reason for non-equality
     */
    public void mustEqual(BaseTool otherTool) throws CMLException {
        if (!otherTool.getClass().equals(this.getClass())) {
            throw new CMLException("Elements are different classes: " + this.getClass() + "/" + otherTool.getClass());
        }
        mustEqualAttributes(otherTool);
        mustEqualChildNodes(otherTool);
    }

    public void mustEqualAttributes(BaseTool otherTool) throws CMLException {
        Vector attributeVector0 = PMRDOMUtils.createVector(abstractBase.getAttributes());
        NamedNodeMap otherAttributes = otherTool.getAbstractBase().getAttributes();
        Vector attributeVector1 = PMRDOMUtils.createVector(otherAttributes);
        int n0 = attributeVector0.size();
        for (int i = 0; i < n0; i++) {
            Attr attr0 = (Attr) attributeVector0.elementAt(0);
            String name0 = attr0.getNodeName();
            String value0 = attr0.getNodeValue();
            Attr attr1 = (Attr) otherAttributes.getNamedItem(name0);
            String value1 = (attr1 == null) ? null : attr1.getNodeValue();
            if (!canIgnoreAttribute(name0)) {
                if (value0.equals("")) {
                    if (!PMRDOMUtils.isEmptyAttribute(value1)) {
                        throw new CMLException("empty attribute: " + name0 + "is non-empty in other: " + value1);
                    }
                } else {
                    if (PMRDOMUtils.isEmptyAttribute(value1)) {
                        throw new CMLException("attribute : " + name0 + "/" + value0 + " is empty in other element");
                    } else if (!value0.equals(value1)) {
                        throw new CMLException("unequal attribute values for: " + name0 + ": " + value0 + "..." + value1);
                    }
                }
            }
            attributeVector0.remove(attr0);
            attributeVector1.remove(attr1);
        }
        int n1 = attributeVector1.size();
        for (int i = 0; i < n1; i++) {
            Attr attr1 = (Attr) attributeVector1.elementAt(i);
            String name1 = attr1.getNodeName();
            if (!canIgnoreAttribute(name1)) {
                String value1 = attr1.getNodeValue();
                if (!PMRDOMUtils.isEmptyAttribute(value1)) {
                    throw new CMLException("non-empty in other: " + name1 + "/" + value1);
                }
            }
        }
    }

    public void mustEqualChildNodes(BaseTool otherTool) throws CMLException {
        NodeList childList0 = this.getAbstractBase().getChildNodes();
        NodeList childList1 = otherTool.getAbstractBase().getChildNodes();
        int nc0 = childList0.getLength();
        int nc1 = childList1.getLength();
        if (nc0 != nc1) {
            throw new CMLException("this (" + this.getAbstractBase() + ") and other (" + otherTool.getAbstractBase() + ") have different childNode counts: " + nc0 + "/" + nc1);
        }
        for (int i = 0; i < nc0; i++) {
            Node child0 = childList0.item(i);
            String value0 = child0.getNodeValue();
            Node child1 = childList1.item(i);
            String value1 = child1.getNodeValue();
            if (child0 instanceof Text && child1 instanceof Text) {
                if (!value0.equals(value1)) {
                    throw new CMLException("Text for this and other have different values: " + value0 + "/" + value1);
                }
            } else if (child0 instanceof AbstractBase && child1 instanceof AbstractBase) {
                BaseTool newBaseTool = new BaseToolImpl();
                BaseTool baseTool0 = ((CMLBaseImpl) child0).getTool();
                BaseTool baseTool1 = ((CMLBaseImpl) child1).getTool();
                if (baseTool0 == null || baseTool1 == null) {
                    throw new CMLException("Unexpected null editors in mustEqual()");
                }
                setIgnoreAttributes(baseTool0);
                setIgnoreAttributes(baseTool1);
                baseTool0.mustEqual(baseTool1);
            } else {
                throw new CMLException("Cannot equate: " + child0 + "/" + child1);
            }
        }
    }

    private void setIgnoreAttributes(BaseTool be) {
        Enumeration keys = ignoreAttributeTable.keys();
        while (keys.hasMoreElements()) {
            String attName = (String) keys.nextElement();
            if (((Boolean) ignoreAttributeTable.get(attName)).booleanValue()) {
                be.setIgnoreAttribute(attName, true);
            }
        }
    }

    /**
     * ignore an attribute
     *
     * @param name    attribute to ignore
     * @param descend carry over to descendants
     */
    public void setIgnoreAttribute(String name, boolean descend) {
        if (ignoreAttributeTable == null) {
            ignoreAttributeTable = new Hashtable();
        }
        ignoreAttributeTable.put(name, new Boolean(descend));
    }

    private boolean canIgnoreAttribute(String name) {
        return (ignoreAttributeTable == null) ? false : ignoreAttributeTable.containsKey(name);
    }

    /**
     * removes the first element by tagName
     * <p/>
     * finds first element with that name and removes it from its parent.
     * mainly for debugging
     *
     * @return the element removed
     */
//*--
    public Element removeElement(String elementName) {
        NodeList nodes = abstractBase.getElementsByTagName(elementName);
        if (nodes.getLength() > 0) {
            PMRDOMUtils.removeNode(nodes.item(0));
        }
        return (Element) nodes.item(0);
    }

    /**
     * SAX2 parsing routine - called from characters() callback
     * <p/>
     * NOT namespace aware
     *
     * @param saxHandler SaxHandler
     * @param content    throws exception (probably application specific)
     */
    public void characters(SaxHandler saxHandler, String content) throws CMLException {
        debug("BaseToolImpl characters ...");
    }

    /**
     * SAX2 parsing routine - called from endElement() callback
     * <p/>
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     *                   throws exception (probably application specific)
     */
    public void endElement(SaxHandler saxHandler) throws CMLException {
        debug("BaseToolImpl endElement ...");
    }

    /**
     * SAX2 parsing routine - called from startElement() callback
     * <p/>
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     * @param attributes the attribute list
     *                   throws exception (probably application specific)
     */
    public void startElement(SaxHandler saxHandler, Attributes attributes) throws CMLException {
        debug("super startElement: " + abstractBase.getNodeName() + ", attributes: " + attributes.getLength());
        for (int i = 0; i < attributes.getLength(); i++) {
            debug("att: " + attributes.getLocalName(i) + "=" + attributes.getValue(i));
            abstractBase.setAttribute(attributes.getLocalName(i), attributes.getValue(i));
        }
    }

    /**
     * write XML, using class-specific information
     * <p/>
     * NOT namespace aware
     *
     * @param w       the writer
     * @param control (concatenation of CML version and array)
     * @throws exception (probably application specific)
     */
    public void writeXML(Writer w, String control) throws CMLException, IOException {
        debug("BaseTool WriteXML " + control + "/" + this);
        CMLBaseImpl.writeXML0(abstractBase, w, control);
    }

    /**
     * toggle debugging
     *
     * @param d debug
     */
    public void setDebug(boolean d) {
        this.debug = d;
    }

    /**
     * debug status
     *
     * @return is debug set
     */
    public boolean getDebug() {
        return this.debug;
    }

    /**
     * output string if debug set
     *
     * @param s string to output
     */
    protected void debug(String s) {
        if (debug) System.out.println("ED> " + s);
    }

    /**
     * copy attributes from one AbstractBase to another.
     * <p/>
     * overwrites existing atts
     *
     * @param from element to copy from
     * @param to   element to copy to
     */
    public void copyAttributesFromTo(AbstractBase from, AbstractBase to) {
        NamedNodeMap nnm = from.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String name = attribute.getNodeName();
            String value = attribute.getNodeValue();
            if (!PMRDOMUtils.isEmptyAttribute(value)) {
                to.setAttribute(name, value);
            }
        }
    }

    /**
     * transfer children from one AbstractBase to another.
     *
     * @param from element to copy from
     * @param to   element to copy to
     */
    public void transferChildrenFromTo(AbstractBase from, AbstractBase to) {
        NodeList childNodes = from.getChildNodes();
        int l = childNodes.getLength();
        for (int i = 0; i < l; i++) {
            Node child = (Node) childNodes.item(0);
            from.removeChild(child);
            to.appendChild(child);
        }
    }

    /**
     * clears an editorTable.
     *
     * @param editorTable the table to clear
     */
    public static void clear(Hashtable editorTable) {
        editorTable.clear();
    }

    /**
     * set Tool package.
     *
     * @param pName the package in which Tools will be found
     */
    public static void setToolPackage(String pName) {
        editorPackageName = pName;
    }

    /**
     * constructs a tools class object.
     * <p/>
     * at present converts "moleculeTool"
     * to [currentPackage].MoleculeToolImpl
     * Does NOT use or create a DOM, so only use for accesing class methods
     * e.g. in  workflow
     *
     * @return object (or null if cannot create)
     */
    public static BaseTool createToolForName(String baseName) {
        String packageName = new CMLBaseImpl().getClass().getPackage().getName();
        int lastDot = packageName.lastIndexOf(".");
        String name = packageName.substring(0, lastDot + 1) + "tools" + "." + baseName.substring(0, 1).toString().toUpperCase() + baseName.substring(1) + "Impl";
        BaseTool baseTool = null;
        try {
            baseTool = (BaseTool) Class.forName(name).newInstance();
        } catch (Exception e) {
        }
        return baseTool;
    }

    /**
     * run workflow script or object.
     * <p/>
     * sets workflow to the tool and runs the current command
     * all information (current command, symbolTables can be accessed from workflow
     *
     * @param workflow to process.
     */
    public void processCurrentCommand(Workflow workflow) throws CMLException {
        System.out.println("No workflow for: " + this.getClass().toString());
    }

    /**
     * get the workflow.
     *
     * @return workflow
     */
    public Workflow getWorkflow() {
        return this.workflow;
    }

    public String formatNumber(String s) {
        Double dbl = null;

        try {
            dbl = new Double(s);
        } catch (NumberFormatException nfe) {
            return s;
        }

        return null;
    }

    /**
     * get all public constructors belonging just to this class.
     *
     * @return array of constructors
     */
    public Constructor[] getDeclaredPublicConstructors() {
        if (declaredPublicConstructors == null) {
            Class classx = this.getClass();
            Constructor[] declared = classx.getDeclaredConstructors();
            HashSet set = new HashSet();
            for (int i = 0; i < declared.length; i++) {
                set.add(declared[i]);
            }
            Constructor[] publicc = classx.getConstructors();
            ArrayList dcc = new ArrayList();
            for (int i = 0; i < publicc.length; i++) {
                if (set.contains(publicc[i])) {
                    dcc.add(publicc[i]);
                }
            }
            declaredPublicConstructors = (Constructor[]) dcc.toArray(new Constructor[0]);
        }
        return declaredPublicConstructors;
    }

    /**
     * get all public methods belonging just to this class.
     *
     * @return array of methods
     */
    public Method[] getDeclaredPublicMethods() {
        if (declaredPublicMethods == null) {
            Class classx = this.getClass();
            Method[] declared = classx.getDeclaredMethods();
            HashSet set = new HashSet();
            for (int i = 0; i < declared.length; i++) {
                set.add(declared[i]);
            }
            Method[] publicm = classx.getMethods();
            ArrayList dcm = new ArrayList();
            for (int i = 0; i < publicm.length; i++) {
                if (set.contains(publicm[i])) {
                    dcm.add(publicm[i]);
                }
            }
            declaredPublicMethods = (Method[]) dcm.toArray(new Method[0]);
        }
        return declaredPublicMethods;
    }

    // =========================== introspection ========================
    /**
     * run any public constructor
     * introspects the above constructors
     * Example:  CMLVector3 projectOnto(CMLVector3 v)
     * is called with v as arg v1, v2 and floatArg as dontCare
     * and returns Object of type CMLVector3
     *
     * @param argVector arguments in order.
     * @return the constructor value
     */
    public Object runConstructor(List argVector) {
        Object[] args = new Object[0];
        Object result = null;
        Constructor theConstructor = getConstructor(argVector);
        if (theConstructor == null) {
            jumboLogger.info("Cannot find constructor (" + argsString(argVector) + ")");
        } else {
            Class[] params = theConstructor.getParameterTypes();
            args = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                args[i] = argVector.get(i);
            }
            try {
                result = theConstructor.newInstance(args);
            } catch (Exception e) {
                jumboLogger.severe("Cannot construct: " + e);
            }
        }
        return result;
    }

    /**
     * run any public method
     * introspects the above methods
     * Example:  CMLVector3 projectOnto(CMLVector3 v)
     * is called with v as arg v1, v2 and floatArg as dontCare
     * and returns Object of type CMLVector3
     * Returns without action if duplicate method name
     *
     * @param methodS   case-sensitive method.
     * @param argVector arguments in order. First arg is instance of this
     * @return any return value (see Method.invoke for classes)
     */
    public Object runMethod(String methodS, List argVector) {
        Object[] args = new Object[0];
        Object result = null;
        Method theMethod = getMethod(methodS, argVector);
        if (theMethod == null) {
            jumboLogger.info("Cannot find unique method: " + methodS);
        } else {
            Class[] params = theMethod.getParameterTypes();
            args = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                args[i] = argVector.get(i + 1);
            }
            this.setAbstractBase((AbstractBase) argVector.get(0));
            try {
                result = theMethod.invoke(this, args);
            } catch (java.lang.reflect.InvocationTargetException ite) {
                Throwable cause = ite.getCause();
                jumboLogger.severe("Invocation exception in " + this.getAbstractBase() + "." + theMethod.getName() + "() / : " + cause);
            } catch (Exception e) {
                jumboLogger.severe("Cannot invoke: " + e);
            }
        }
        return result;
    }

    protected Method getMethod(String methodS, List argVector) {
        getDeclaredPublicMethods();
        Method theMethod = null;
        for (int i = 0; i < declaredPublicMethods.length; i++) {
            Method dcp = declaredPublicMethods[i];
            if (dcp.getName().equals(methodS)) {
                Class[] argTypes = dcp.getParameterTypes();
                if (argTypes.length + 1 != argVector.size()) {
                    continue;
                }
                boolean found = true;
                for (int j = 0; j < argTypes.length; j++) {
                    Object arg = argVector.get(j + 1);
                    if (!(argMatchesType(arg, argTypes[j]))) {
                        found = false;
                        break;
                    }
                }
                if (!found) {
                    break;
                } else if (theMethod == null) {
                    theMethod = dcp;
                } else {
                    System.err.println("Duplicate method for these args (maybe problems with primitives)");
                    theMethod = null;
                    break;
                }
            }
        }
        return theMethod;
    }

    protected Constructor getConstructor(List argVector) {
        getDeclaredPublicConstructors();
        Constructor theConstructor = null;
        for (int i = 0; i < declaredPublicConstructors.length; i++) {
            Constructor dcp = declaredPublicConstructors[i];
            Class[] argTypes = dcp.getParameterTypes();
            if (argTypes.length != argVector.size()) {
                continue;
            }
            boolean found = true;
            for (int j = 0; j < argTypes.length; j++) {
                Object arg = argVector.get(j);
                if (!(argMatchesType(arg, argTypes[j]))) {
                    found = false;
                    break;
                }
            }
            if (found) {
                theConstructor = dcp;
                break;
            }
        }
        return theConstructor;
    }

    boolean argMatchesType(Object arg, Class argType) {
        boolean result =
                argType.isAssignableFrom(arg.getClass()) ||
                        argType.equals(void.class) && arg instanceof Void ||
                        argType.equals(boolean.class) && arg instanceof Boolean ||
                        argType.equals(byte.class) && arg instanceof Byte ||
                        argType.equals(char.class) && arg instanceof Character ||
                        argType.equals(short.class) && arg instanceof Short ||
                        argType.equals(int.class) && arg instanceof Integer ||
                        argType.equals(long.class) && arg instanceof Long ||
                        argType.equals(float.class) && arg instanceof Float ||
                        argType.equals(double.class) && arg instanceof Double;
        return result;
    }

    /**
     * gets unique method if possible
     * does not use args at present
     *
     * @param methodS method name
     * @return unique method for that name
     */
    protected Method getUniqueMethod(String methodS) {
        getDeclaredPublicMethods();
        Method theMethod = null;
        for (int i = 0; i < declaredPublicMethods.length; i++) {
            if (declaredPublicMethods[i].getName().equals(methodS)) {
                if (theMethod != null) {
                    jumboLogger.severe("Duplicate method name: cannot process at present: " + methodS);
                    theMethod = null;
                    break;
                } else {
                    theMethod = declaredPublicMethods[i];
                }
            }
        }
        return theMethod;
    }

    protected static double[] getDoubleArgs(int start, int nargs, String[] args) {
        double xyz[] = new double[nargs];
        for (int i = 0; i < nargs; i++) {
            xyz[i] = new Double(args[start + i]).doubleValue();
        }
        return xyz;
    }

// ======================= usage ========================

    protected void usage(String className) {
        System.out.println("-HELP         // list of methods for " + className);
        System.out.println("    or");
        System.out.println("" + className + " -METHOD  foo  [arg ]");
        System.out.println("    and foo() is one of its methods ");
        System.out.println("    which takes 0 or more args of the form ");
        System.out.println("-Foo    fooArgs // where Foo is an AbstractBase class");
        System.out.println("-float  value  // floating point arg");
        System.out.println("-int    value  // integer arg");
        System.out.println("-string value  // string arg");
        System.out.println("    the first arg is the value of the object");
        System.out.println("    (throws an error if not of correct class)");
        System.out.println("");
        System.out.println("Examples:");
        System.out.println("  java org.xmlcml.tools.Vector3ToolImpl -METHOD getLength CMLVector3 1. 1. 1.");
        System.out.println("  java org.xmlcml.tools.Vector3ToolImpl -METHOD cross CMLVector3 1. 1. 1. CMLVector3 1. 1. 0.");
        System.out.println("  java org.xmlcml.tools.Vector3ToolImpl -METHOD multiplyBy CMLVector3 1. 1. 1. -float 2.0");
    }

    protected void constructorsUsage() {
        Constructor[] constructors = this.getDeclaredPublicConstructors();
        for (int i = 0; i < constructors.length; i++) {
            String constructorName = getShortClassName(this.getClass());
            String s = "";
            s += " " + constructorName + "(";
            s += argsString(constructors[i].getParameterTypes());
            System.out.println(s);
        }
    }

    String argsString(Class[] classes) {
        String s = "";
        for (int j = 0; j < classes.length; j++) {
            if (j > 0) {
                s += ", ";
            }
            s += getShortClassName(classes[j]);
        }
        s += ")";
        return s;
    }

    String argsString(List argVector) {
        String s = "";
        for (int j = 0; j < argVector.size(); j++) {
            if (j > 0) {
                s += ", ";
            }
            s += getShortClassName(argVector.get(j).getClass());
        }
        s += ")";
        return s;
    }

    static String[] excludedMethods = {
            "main",
            "processCommandLine",
    };

    protected void methodsUsage() {
        Method[] methods = this.getDeclaredPublicMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            boolean exclude = false;
            for (int j = 0; j < excludedMethods.length; j++) {
                if (excludedMethods[j].equals(methodName)) {
                    exclude = true;
                    break;
                }
            }
            if (exclude) {
                continue;
            }
            String s = "";
            Class returnx = methods[i].getReturnType();
            s += " " + getShortClassName(returnx);
            s += " " + methodName + "(";
            s += argsString(methods[i].getParameterTypes());
            s += ")";
            System.out.println(s);
        }
    }

    static String getShortClassName(Class classx) {
        String className = classx.getName();
        int idx = -1;
        if (classx.isArray()) {
            idx = className.lastIndexOf("[");
            className = className.substring(idx + 1);
            String abbrev = className.substring(0, 1);
            if (abbrev.equals("L")) {
                className = className.substring(1);
// remove trailing colon
                int colon = className.lastIndexOf(";");
                if (colon != -1) {
                    className = className.substring(0, colon);
                }
            } else if (abbrev.equals("B")) {
                className = "byte";
            } else if (abbrev.equals("C")) {
                className = "char";
            } else if (abbrev.equals("D")) {
                className = "double";
            } else if (abbrev.equals("F")) {
                className = "float";
            } else if (abbrev.equals("I")) {
                className = "int";
            } else if (abbrev.equals("J")) {
                className = "long";
            } else if (abbrev.equals("S")) {
                className = "short";
            } else if (abbrev.equals("Z")) {
                className = "boolean";
            } else if (abbrev.equals("V")) {
                className = "void";
            } else {
                jumboLogger.severe("Cannot find abbreviation " + abbrev);
            }
        }
        Package packagex = classx.getPackage();
        if (packagex != null) {
            className = className.substring(packagex.getName().length() + 1);
        }
        for (int i = 0; i <= idx; i++) {
            className += "[]";
        }
        return className;
    }

    static Set excludeMethodSet = new HashSet();

    static {
        excludeMethodSet.add("main");
        excludeMethodSet.add("getAbstractBase");
        excludeMethodSet.add("setAbstractBase");
        excludeMethodSet.add("getTestInstance");
        excludeMethodSet.add("processCommandLine");
    }

    ;

    /**
     * test all declared public methods.
     * introspects args and creates test instances from primitives or getTestInstance();
     * cannot create arg which require arrays so skips method
     * also may skip some superclasses
     */
    public void testMethods() {
        AbstractCMLDocument doc = new AbstractCMLDocumentImpl();
// object itself is first testInstance of its class
        BaseTool theObjectTool = this.getTestInstance(doc, 1);
        if (theObjectTool == null) {
            jumboLogger.severe("Cannot get test instance: " + this.getClass());
            return;
        }
        AbstractBase theObject = theObjectTool.getAbstractBase();
        String theClassName = theObject.getClass().getName();
        System.out.println("\n==================== " + theClassName + " =====================\n");
        String interfaceName = "";
        Class interfaceClass = null;
        try {
            interfaceName = convertClassToInterface(theClassName);
            interfaceClass = Class.forName(interfaceName);
        } catch (Exception ex) {
            jumboLogger.severe("BUG Cannot create interface for " + theClassName + "/" + ex);
        }
        // get all public methods for this class (not superclasses)
        getDeclaredPublicMethods();
        //
        for (int i = 0; i < declaredPublicMethods.length; i++) {
            Method dpm = declaredPublicMethods[i];
            if (excludeMethodSet.contains(dpm.getName())) {
                continue;
            }
            System.out.println("\n.................. " + dpm.getName() + " ......................");
            // classes of args
            Class[] argTypes = dpm.getParameterTypes();
            // instances of args to be invoked
            Object[] args = new Object[argTypes.length];
            Map argTypeMap = new HashMap();
// this is instance of interface and uses first test instance so index it in map
            argTypeMap.put(interfaceClass, new Integer(1));
            boolean ok = true;
            for (int j = 0; j < argTypes.length; j++) {
                Class argType = argTypes[j];
                if (argType.isArray()) {
                    System.err.println("Cannot process array arg");
                    ok = false;
                    break;
                }
                Integer argTypeCount = (Integer) argTypeMap.get(argType);
                if (argTypeCount == null) {
                    argTypeCount = new Integer(0);
                }
                int nargType = argTypeCount.intValue() + 1;
                argTypeCount = new Integer(nargType);
                argTypeMap.put(argType, argTypeCount);
                jumboLogger.fine("narg " + nargType + "/" + argType.getName());
                if (AbstractBase.class.isAssignableFrom(argType)) {
                    BaseTool baseTool = null;
                    String className = "";
                    try {
                        className = argType.getName();
                        className = convertInterfaceToClass(className);
                        AbstractBase ab = (AbstractBase) Class.forName(className).newInstance();
                        baseTool = ab.getOrCreateTool();
                    } catch (Exception ee) {
                        jumboLogger.severe("Cannot create tool: " + ee);
                    }
                    if (baseTool == null) {
                        jumboLogger.severe("Cannot get tool instance: " + argType.getName() + "/" + className + "/" + nargType);
                        ok = false;
                        break;
                    }
                    BaseTool instanceTool = ((BaseToolImpl) baseTool).getTestInstance(doc, nargType);
                    if (instanceTool == null) {
                        jumboLogger.severe("No test instance for : " + this.getClass().getName() + "/" + nargType);
                        ok = false;
                        break;
                    }
                    args[j] = instanceTool.getAbstractBase();
                } else if (argType.equals(double.class)) {
                    args[j] = new Double(nargType + 0.5);
                } else if (argType.equals(int.class)) {
                    args[j] = new Integer(nargType);
                } else if (argType.equals(String.class)) {
                    args[j] = "s" + nargType;
                } else {
                    ok = false;
                    jumboLogger.severe("Cannot process argType: " + argType);
                }
            }
            if (ok) {
// get the object each time as it may be contaminated by previous runs
                theObjectTool = this.getTestInstance(doc, 1);
                if (theObjectTool == null) {
                    jumboLogger.severe("Cannot get test instance: " + this.getClass());
                    return;
                }
                theObject = theObjectTool.getAbstractBase();
                System.out.print("This: ");
                printObject(theObject);
                for (int j = 0; j < args.length; j++) {
                    System.out.print("Arg: ");
                    printObject(args[j]);
                }
                try {
                    Object result = dpm.invoke(theObjectTool, args);
                    System.out.print("Result:. ");
                    printObject(((result == null) ? theObject : result));
                    System.out.println();
                } catch (java.lang.reflect.InvocationTargetException ite) {
                    Throwable cause = ite.getCause();
                    jumboLogger.severe("Invocation exception in " + this.getAbstractBase() + "." + dpm.getName() + "() / : " + cause);
                } catch (Exception e) {
                    jumboLogger.severe("BUG " + e + "/" + theObject.getClass() + "/" + dpm.getDeclaringClass());
                }
            }
        }

    }

    /**
     * converts CMLInterface to class.
     * e.g. converts org.jscience.ml.cml.CMLVector3 to org.jscience.ml.cml.cmlimpl.Vector3Impl
     * and AbstractBase to CMLBaseImpl
     *
     * @param interfaceName the interface
     * @return the class name
     */
    public static String convertInterfaceToClass(String interfaceName) {
        String className = interfaceName;
        if (interfaceName.startsWith("org.jscience.ml.cml.")) {
            className = interfaceName.substring("org.jscience.ml.cml.".length());
        }
        if (className.equals("AbstractBase")) {
            className = "CMLBaseImpl";
        } else if (className.startsWith("CML")) {
            className = className.substring(3) + "Impl";
        }
        if (interfaceName.startsWith("org.jscience.ml.cml.")) {
            className = "org.jscience.ml.cml.cmlimpl." + className;
        }
        return className;
    }

    /**
     * converts CML classname to interface.
     * e.g. converts org.jscience.ml.cml.cmlimpl.Vector3Impl to org.jscience.ml.cml.CMLVector3
     * and  CMLBaseImpl to AbstractBase
     *
     * @param className
     * @return the interface name
     */
    public static String convertClassToInterface(String className) {
        String interfaceName = className;
        if (className.startsWith("org.jscience.ml.cml.cmlimpl.")) {
            interfaceName = interfaceName.substring("org.jscience.ml.cml.cmlimpl.".length());
        }
        if (interfaceName.equals("CMLBaseImpl")) {
            interfaceName = "AbstractBase";
        } else if (interfaceName.endsWith("Impl")) {
            interfaceName = "CML" + interfaceName.substring(0, interfaceName.length() - "Impl".length());
        }
        if (className.startsWith("org.jscience.ml.cml.cmlimpl.")) {
            interfaceName = "org.jscience.ml.cml." + interfaceName;
        }
        return interfaceName;
    }

    // ============== parse commandline args =======================
    /**
     * process args.
     *
     * @param args
     */
    public void processArgs(String[] args) {
        String className = this.getClass().getName();
        BaseTool theTool = null;
        if (args.length == 0) {
            usage(className);
        } else if ("-help".equalsIgnoreCase(args[0])) {
            System.out.println("Class: " + getShortClassName(this.getClass()) + "\n");
            constructorsUsage();
            System.out.println();
            methodsUsage();
        } else {
            AbstractCMLDocument doc = (AbstractCMLDocument) DocumentFactoryImpl.newInstance().createDocument();

            Vector argVector = new Vector();
            boolean ok = true;
// parse the args, grab the method and leave the other args as object in argVector
            String methodS = parseArgs(args, argVector, doc);
// run through the args
            for (int j = 0; j < argVector.size(); j++) {
                Object arg = argVector.elementAt(j);
// print for the record
                System.out.print(((j > 0) ? "Arg: " : "Object: "));
                printObject(arg);
            }
            Object result = null;
            if (methodS == null) {
// cannot find method
                System.out.println("Cannot find method");
            } else if (methodS.equals(TEST)) {
                testMethods();
// run tests
            } else if (methodS.equals(CONSTRUCT)) {
// constructor
                System.out.println("Constructor: ");
                result = runConstructor(argVector);
            } else {
// must have a 'this' object
                if (argVector.size() == 0 ||
                        !(argVector.elementAt(0) instanceof AbstractBase)
                        ) {
                    jumboLogger.severe("Must start args with the object to be tested");
// get the method
                } else {
                    System.out.println("Method: " + methodS);
                    theTool = ((AbstractBase) argVector.get(0)).getOrCreateTool();
                    if (theTool != null) {
                        result = theTool.runMethod(methodS, argVector);
                    } else {
                        System.out.println("Null tool (perhaps doesn't support method())");
                    }
                }
            }
            System.out.print("Result:.. ");
            printObject(((result == null) ? ((theTool == null) ? null : theTool.getAbstractBase()) : result));
        }
    }

    String parseArgs(String[] args, List argVector, AbstractCMLDocument doc) {
        String methodS = null;
        String constructorS = null;
        int i = 0;
        boolean ok = true;
        while (i < args.length) {
            if (false) {
                ;
            } else if (args[i].equalsIgnoreCase("-float")) {
                Double floatArg = new Double(args[++i]);
                i++;
                argVector.add(floatArg);
            } else if (args[i].equalsIgnoreCase("-int")) {
                Integer intArg = new Integer(args[++i]);
                i++;
                argVector.add(intArg);
            } else if (args[i].equalsIgnoreCase("-string")) {
                String stringArg = args[++i];
                i++;
                argVector.add(stringArg);
            } else if (args[i].equalsIgnoreCase("-method")) {
                methodS = args[++i];
                i++;
            } else if (args[i].equalsIgnoreCase("-test")) {
                methodS = TEST;
                i++;
            } else if (args[i].equalsIgnoreCase("-constructor")) {
                ++i;
                methodS = CONSTRUCT;
            }/* else if (args[i].equalsIgnoreCase("-document")) {
          ++i;
          argVector.add(new CMLDocumentImpl());
      }*/
            else if (args[i].startsWith("-CML")) {
                String toolClassName = "org.xmlcml.tools." + args[i].substring(4) + "ToolImpl";
                BaseTool tool = null;
                try {
                    Class toolClass = Class.forName(toolClassName);
                    tool = (BaseTool) toolClass.newInstance();
                } catch (Exception e) {
                    System.out.println("Unknown args/class: " + args[i] + "/" + toolClassName);
                    ok = false;
                }
                if (tool != null) {
                    ++i;
                    i = tool.processCommandLine(args, i, argVector, doc);
                } else {
                    i++;            // just to keep going
                }
            } else {
                jumboLogger.severe("Unknown arg: " + args[i]);
                i++;
                ok = false;
            }
        }
        return (ok) ? methodS : null;
    }

    void printObject(Object obj) {
        if (obj instanceof Node) {
            PMRDOMUtils.debug((Node) obj);
        } else if (obj instanceof double[]) {
            double[] arr = (double[]) obj;
            for (int k = 0; k < arr.length; k++) {
                System.out.print(" " + arr[k]);
            }
            System.out.println();
        } else if (obj instanceof Object[]) {
            Object[] arr = (Object[]) obj;
            for (int k = 0; k < arr.length; k++) {
                printObject(arr[k]);
            }
            System.out.println();
        } else if (obj instanceof BaseTool) {
            printObject(((BaseTool) obj).getAbstractBase());
        } else {
            System.out.println("" + obj);
        }
    }

    /**
     * a standard instance for testing.
     * returns null unless subclassed
     *
     * @param doc    the owner document
     * @param serial the instance (1,2,3)
     * @return null
     */
    public BaseTool getTestInstance(AbstractCMLDocument doc, int serial) {
        return null;
    }

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
    public int processCommandLine(String[] args, int offset, List argVector, AbstractCMLDocument doc) {
        System.out.println("No commandline processor for: " + this.getClass().getName());
        System.out.println("If required should create a subclassed processCommandLine: ");
        return offset;
    }
}

