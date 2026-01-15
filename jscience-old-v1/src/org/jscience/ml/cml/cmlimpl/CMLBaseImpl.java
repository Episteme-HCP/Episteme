package org.jscience.ml.cml.cmlimpl;

import org.jscience.ml.cml.*;
import org.jscience.ml.cml.dom.pmr.PMRDocument;
import org.jscience.ml.cml.dom.pmr.PMRElementImpl;
import org.jscience.ml.cml.util.CMLUtils;
import org.jscience.ml.cml.util.PMRDOMUtils;
import org.w3c.dom.*;
import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.Writer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The base class for all elementObjects mentioned in the Schema
 * <p/>
 * Any CML element may have attributes:titleiddictRefconvention
 * and convenience get/set methods are provided for allAn element (FOO) subclassed from a AbstractBase may be constructed in the
 * following ways:FOO(). Creates a new empty element with null values of the
 * attributes. Required for newInstance(), but use carefully since
 * it has no tagName and no document associated with it.FOO(String tagName, CMLDocument document). Creates an empty
 * element with null attribute names.FOO(org.w3c.dom.Element element). Creates a subclassed Element with
 * the same attribute values as the input Element. Used when a DOM has
 * been created with non-CML-aware software. The routine
 * makeAndProcessSubclass(Element element) will replace the current Element
 * with the appropriate CML subclass.FOO(String title, String id, String dictRef, String convention).
 * Makes subclassed Element and sets attribute values.
 *
 * @author P.Murray-Rust, 1999, 2000, 2003
 */
public class CMLBaseImpl extends PMRElementImpl implements AbstractBase {

    protected String version = CML2;
    protected boolean arraySyntax = false;
    protected BaseTool tool = null;
    protected boolean debug = false;
    protected String tagName;

    static Logger logger = Logger.getLogger("org.jscience.ml.cml.cmlimpl.CMLBaseImpl");

    /**
     * create a Node WITHOUT tagName OR document
     * Use with care
     */
// awful, but there seems to be no no-arg constructor
    public CMLBaseImpl() {
    }

    /**
     * used when creating new nodes in a DOM
     */
// this is the proper method...
    protected CMLBaseImpl(String tagName, AbstractCMLDocument document) {
        super((PMRDocument) document, tagName);
        init();
        this.tagName = tagName;
    }

    public CMLBaseImpl(String tagName, Document document) {
        super((PMRDocument) document, tagName);
        init();
    }

    protected AbstractCMLDocument ownerDocument;

    // communal functionality
    protected void init() {
        ownerDocument = (AbstractCMLDocument) this.getOwnerDocument();
        try {
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("CMLBaseImpl: BUG: " + e);
        }
    }

    public AbstractCMLDocument getCMLDocument() {
        ownerDocument = (AbstractCMLDocument) this.getOwnerDocument();
        return ownerDocument;
    }
// =================================================

    public /*abstract*/ String getClassTagName() {
        return this.tagName;
    }

    /** ====================== ATTRIBUTES ======================= */
    /**
     * content model
     * <p/>
     * adds a child to the element content. The elementName is checked for
     * validity (but not its position in the list of children). It will
     * always be added at the end. Therefore when building a DOM the author
     * must ensure that all elements are added in correct order or must
     * validate after addition. The count will be taken from the sum of the
     * allowed children. Thus foo, bar, foo* will allow the addition of 1
     * bar and many foos.
     *
     * @param elem the element to add (identified by elementName not class)
     * @throws org.jscience.ml.cml.CMLException
     *          element cannot be added (violates content model)
     */
    public void addChild(Element elem) throws org.jscience.ml.cml.CMLException {
        debug("Adding child: " + elem.getNodeName() + " to " + this.getNodeName());
        this.appendChild(elem);
    }

    /**
     * delete an element
     * The element must have been previously created and
     * present in the DOM
     *
     * @param elem element to be deleted.
     * @throws org.jscience.ml.cml.CMLException
     *          element would leave invalid content
     */
    public void deleteChild(Element elem)
            throws org.jscience.ml.cml.CMLException {
        this.removeChild(elem);
    }

    /**
     * gets the first Child of a given type (by elementName)
     * This method
     * will normally be used when there is a single child of this type
     *
     * @param elementName
     * @return null if no elements of this type
     */
    public Element getFirstChild(String elementName) {
        return PMRDOMUtils.getFirstChildWithElementName(this, elementName);
    }

    /**
     * gets a Vector of all child elements with this name
     *
     * @param elementName
     * @return if no children an empty vector (not null) is returned
     */
    public Vector getChildVector(String elementName) {
        return PMRDOMUtils.getChildrenWithElementName(this, elementName);
    }

    /**
     * gets a (typed) list of child elements with a given name
     *
     * @param elementName
     * @return returns a zero length list if none found
     */
    public Element[] getChildList(String elementName) {
        Vector v = this.getChildVector(elementName);
        Element[] vv = new Element[v.size()];
        for (int i = 0; i < vv.length; i++) {
            vv[i] = (Element) v.elementAt(i);
        }
        return vv;
    }

    /**
     * traps the addition of an child to the element content
     * By default this
     * will be routed to addChild() and org.w3c.dom.Node.appendChild(), but
     * other element-specific stuff can be interposed (within the Cont
     *
     * @param elem the element to add (identified by elementName not class)
     * @throws org.jscience.ml.cml.CMLException
     *          element cannot be added (violates content model)
     */
    public Node appendChild(Element elem) throws org.jscience.ml.cml.CMLException {
        return super.appendChild(elem);
    }

    /**
     * get a child of a given name
     * If it doesn't exist, create and add it
     *
     * @param elemName the elementName to get/create (identified by elementName not class)
     * @return the Element
     * @throws org.jscience.ml.cml.CMLException
     *          element cannot be added (violates content model)
     */
    public Element getOrCreateChild(String elemName) throws org.jscience.ml.cml.CMLException {
        Element element = this.getFirstChild(elemName);
        if (element == null) {
            element = ownerDocument.createSubclassedElement(elemName);
            this.appendChild(element);
        }
        return element;
    }

    /**
     * adds text content to an element
     * should only be used for text-only or mixed content.
     *
     * @param value the content
     * @throws CMLException thrown by subclasses
     */
    public void setContentValue(String value) /*throws CMLException*/ {
        this.addTextChild(value);
    }

    /**
     * gets text content from an element
     * should only be used for text-only or mixed content.
     *
     * @return value the content; "" if none
     * @throws CMLException thrown by subclasses
     */
    public String getContentValue() /*throws CMLException*/ {
        NodeList childNodes = this.getChildNodes();
        String s = "";
        if (childNodes.getLength() == 1 &&
                childNodes.item(0) instanceof Text) {
            s = childNodes.item(0).getNodeValue();
        }
        return s;
    }

    /**
     * removes whitespace nodes or throws any Exception for non-whitespace ones
     */
    public void removeTextChildren() throws CMLException {
        NodeList children = this.getChildNodes();
        int nchild = children.getLength();
        int ntext = 0;
        for (int j = nchild - 1; j >= 0; j--) {   // convert to double minus
            Node cc = children.item(j);
            if (cc instanceof Text) {
                if (cc.getNodeValue().trim().equals("")) {
                    this.removeChild(cc);
                    ntext++;
                } else {
                    throw new CMLException("" + this.getClass() + " may not have text children");
                }
            }
        }
    }

    /**
     * SAX2 parsing routine
     * called from characters() callback
     * NOT namespace aware
     *
     * @param saxHandler SaxHandler
     * @param content    throws exception (probably application specific)
     */
    public void characters(SaxHandler saxHandler, String content) throws org.jscience.ml.cml.CMLException {
        if (tool != null && !(tool instanceof BaseToolImpl)) {
            tool.characters(saxHandler, content);
        } else {
            if (!content.trim().equals("")) {
                Text text = this.getOwnerDocument().createTextNode(content);
                this.appendChild(text);
            }
        }
    }

    /**
     * SAX2 parsing routine
     * called from endElement() callback
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     *                   throws exception (probably application specific)
     */
    public void endElement(SaxHandler saxHandler) throws org.jscience.ml.cml.CMLException {
// normal action is same as generic XML
        setDebug(saxHandler.getDebug());
        debug("CMLBase: endElement: ");
        if (tool != null && !(tool instanceof BaseToolImpl)) {
// specialist element
            tool.endElement(saxHandler);
        } else {
            debug("generic...end ");
        }
//        ((SaxHandlerImpl)saxHandler).backtrackParent();
    }

    /**
     * SAX2 parsing routine
     * called from startElement() callback
     * NOT namespace aware
     *
     * @param saxHandler Saxhandler
     * @param attributes the attribute list
     *                   throws exception (probably application specific)
     */
    public void startElement(SaxHandler saxHandler, Attributes attributes) throws org.jscience.ml.cml.CMLException {
        if (tool != null && !(tool instanceof BaseToolImpl)) {
            tool.startElement(saxHandler, attributes);
        } else {
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getLocalName(i);
                String value = attributes.getValue(i);
//               System.out.println("Name: "+name+"/value: "+value);
                this.setAttribute(name, value);
            }
        }
    }

    /**
     * set attribute.
     * do not use.
     * may be called by autogenerated code.
     * reroutes calls to attribute "elementContent" to setTextChild
     */
    public void setAttribute(String name, String value) {
        if (name.equals("elementContent")) {
            this.setTextChild(value);
        } else {
            super.setAttribute(name, value);
        }
    }

    /**
     * addStringContent to Element
     * <p/>
     * if no Text child, creates one
     * if Text child, resets value
     * if existing non-Text children, no action
     *
     * @param value to add
     * @deprecated use setTextChild instead
     */
    public void addTextChild(String value) {
        setTextChild(value);
    }

    /**
     * addStringContent to Element
     * <p/>
     * if no Text child, creates one
     * if Text child, resets value
     * if existing non-Text children, no action
     *
     * @param value to add
     */
    public void setTextChild(String value) {
        NodeList childNodes = this.getChildNodes();
        Text text = null;
        if (childNodes.getLength() == 0) {
            Document doc = this.getOwnerDocument();
            text = doc.createTextNode(value);
            this.appendChild(text);
        } else if (childNodes.getLength() == 1 &&
                childNodes.item(0) instanceof Text) {
            text = (Text) childNodes.item(0);
        }
        if (text != null) text.setNodeValue(value);
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
        if (debug) System.out.println("BASE> " + s);
    }

    /**
     * set CML version
     * <p/>
     * 1 or 2 at present (default 2)
     *
     * @param v version
     * @throws unsupported version
     */
    public void setVersion(String v) throws CMLException {
        if (v.equals(CML1) ||
                v.equals(CML2)) {
            version = v;
        } else {
            throw new CMLException("Unsupported version: " + v);
        }
    }

    /**
     * set array syntax
     * <p/>
     * set CML array syntax (default false)
     *
     * @param syntax
     */
    public void setArraySyntax(boolean syntax) {
        arraySyntax = syntax;
    }

    /**
     * write XML (allows for syntactic variants)
     *
     * @param control - application specific string
     *                for example "CML1 array" or "CML2"
     * @param w       - output
     * @throws IOException
     * @throws org.jscience.ml.cml.CMLException
     *
     */
    public void writeXML(Writer w, String control) throws org.jscience.ml.cml.CMLException, IOException {
        debug("CMLBase writeXML: " + this.getNodeName());
        if (this instanceof AbstractBase) {
            BaseTool tool = this.getTool();
            if (tool != null) {
                debug("AbstractBase routed to tool..." + tool);
                tool.writeXML(w, control);
            } else {
                writeXML0(this, w, control);
            }
        } else {
            writeXML0(this, w, control);
        }
    }

    /**
     * write XML
     * use current control (version and syntax)
     *
     * @param w - output
     */
    public void writeXML(Writer w) throws org.jscience.ml.cml.CMLException, IOException {
        String control = version;
        if (arraySyntax) control += " array";
        writeXML(w, control);
    }

    public static void writeXML0(Element elem, Writer w, String control) throws IOException, org.jscience.ml.cml.CMLException {
        w.write("<" + elem.getNodeName());
        NamedNodeMap atts = elem.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            w.write(" " + atts.item(i).getNodeName() + "=\"" + atts.item(i).getNodeValue() + "\"");
        }
        w.write(">");
        outputChildren(elem, w, control);
        w.write("\n</" + elem.getNodeName() + ">");
    }

    public static void outputChildren(Element elem, Writer w, String control)
            throws IOException, org.jscience.ml.cml.CMLException {
        NodeList children = elem.getChildNodes();
        int nchild = children.getLength();
        for (int i = 0; i < nchild; i++) {
            Node child = children.item(i);
            if (child instanceof Text) {
                w.write(CMLUtils.escape("" + child.getNodeValue()));
            } else if (child instanceof AbstractBase) {
                w.write("\n");
                AbstractBase absChild = (AbstractBase) child;
                BaseTool tool = ((CMLBaseImpl) absChild).getTool();
                if (tool != null) {
                    if (!(tool instanceof BaseToolImpl)) {
                        absChild.writeXML(w, control);
                    } else {
                        tool.writeXML(w, control);
                    }
                } else {
//                    throw new CMLException("Bug: null tool");
                    writeXML0((Element) child, w, control);
                }
            } else if (child instanceof Element) {
                w.write("\n");
                writeXML0((Element) child, w, control);
            } else if (child instanceof Comment) {
                w.write("\n");
                w.write("<!--" + child.getNodeValue() + "-->");
            } else if (child == null) {
                System.err.println("CMLBaseImpl bug: null child");
            } else {
                System.err.println("Cannot output child " + child.getClass());
            }
        }
    }

    /**
     * deep clones an element including subclassing.
     * <p/>
     * uses intermediate XML serialization
     * object is owned by a new document
     * all CML elements have correct subclass
     *
     * @return the copied object
     */
    public AbstractBase deepCopy() {
        AbstractCMLDocument doc = DocumentFactoryImpl.newInstance().createDocument();
        Element clone = (Element) doc.importNode(this, true);
        AbstractBase result = (AbstractBase) ownerDocument.subclass(clone);

        return result;
    }

    /*
      AbstractCMLDocument doc = null;
      StringWriter sw = new StringWriter();
      try {
          this.writeXML(sw);
          sw.close();
          doc = DocumentFactoryImpl.newInstance().parseSAX(new InputSource(new StringReader(sw.toString())));
      } catch (Exception e) {
          e.printStackTrace();
          System.err.println("CMLBaseImpl bug: "+e);
      }
      return (AbstractBase) doc.getDocumentElement();
    */


    /**
     * deep clones an element including subclassing
     * <p/>
     * uses intermediate XML serialization
     * object is owned by document
     *
     * @param document ownerDocument
     * @return the copied object
     */
    public AbstractBase deepCopy(AbstractCMLDocument document) {
        Element clone = (Element) document.importNode(this, true);
        AbstractBase result = (AbstractBase) document.subclass(clone);

        return result;

/*
        AbstractBase newBase = null;
        StringWriter sw = new StringWriter();
        try {
            this.writeXML(sw);
            sw.close();
            boolean debug = false;
            newBase = (AbstractBase) DocumentFactoryImpl.newInstance().parseSAX(new InputSource(new StringReader(sw.toString())), document, debug);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("CMLBaseImpl bug: "+e);
        }
        return newBase;
*/
    }

    /**
     * get tool.
     * <p/>
     * creates one if not already created
     * do not use if subclass is known to compiler
     * (use FooToolImpl.getTool(foo) instead)
     *
     * @return the tool
     */
    public BaseTool getTool() {
        return tool;
    }

    /**
     * get or create tool.
     * <p/>
     * creates one if not already created
     * should only be used when the subclass is not known to the compiler
     * i.e. where an abstractBase is found
     *
     * @return the tool
     */
    public BaseTool getOrCreateTool() {
        if (tool == null) {
            String classx = this.getClass().getName();
            logger.log(Level.FINE, "Class name " + classx);
            if (classx.endsWith("Impl")) {
                classx = classx.substring(0, classx.length() - "Impl".length());
                if (classx.indexOf("org.jscience.ml.cml.cmlimpl.") == 0) {
                    classx = classx.substring("org.jscience.ml.cml.cmlimpl.".length());
                    classx = "org.xmlcml.tools." + classx + "Tool" + "Impl";
                } else {
                    classx = null;
                }
            } else {
                logger.log(Level.SEVERE, "Cannot get class name " + classx);
                classx = null;
            }
            logger.log(Level.FINE, " new Class name " + classx);
            if (classx != null) {
                tool = null;
                Class classz = null;
                try {
                    classz = Class.forName(classx);
                    if (classz != null) {
                        tool = (BaseTool) classz.newInstance();
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Cannot instantiate class " + classz + "/" + e.getCause());
                }
            }
            logger.log(Level.FINE, " new Class  " + tool);
        }
        return tool;
    }

    /**
     * set tool
     * <p/>
     * not user-callable
     *
     * @param t tool
     */

    public void setTool(BaseTool t) {
//		System.out.println ("setting tool for " + this + " to " + t);

        if (t != null) {
            if (tool != t) {
                if (tool != null) {
//					System.out.println ("unsetting ab for tool " + tool);
                    tool.setAbstractBase(null);
                }

//				System.out.println ("setting tool " + tool + " to t " + t);
                tool = t;
//				System.out.println ("tool " + tool);
                tool.setAbstractBase(this);
            }
        } else {
            BaseTool temp = tool;
            tool = null;

            if (temp != null) {
                temp.setAbstractBase(null);
            }
        }
    }

    /**
     * constructs a class name for tool
     * <p/>
     * at present converts some.where.bar.FooImpl
     * to some.where.tool.FooToolImpl
     *
     * @param abstractBase class object
     * @return class name
     */

    public static String createToolClassName(AbstractBase abstractBase) {
        String className = abstractBase.getClass().getName();
        String packageName = abstractBase.getClass().getPackage().getName();
        String name = className.substring(packageName.length() + 1);
        int impl = name.indexOf("Impl");
        int lastDot = packageName.lastIndexOf(".");
        String toolPackageName = packageName.substring(0, lastDot + 1) + "tools";
        name = toolPackageName + "." + name.substring(0, impl) + "ToolImpl";
        return name;
    }

    /**
     * update delegates.
     * updates this delegates (content and attributes)
     * then recurses through children
     */
    public void updateDelegates() {
//        System.out.println("updateDelegates: "+this.getNodeName());
        updateDelegateContent();
        updateDelegateAttributes();
        NodeList childNodes = this.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof AbstractBase) {
                ((AbstractBase) child).updateDelegates();
            }
        }
    }

    /**
     * update DOM.
     * updates this DOM (content and attributes)
     * then recurses through children
     */
    public void updateDOM() {
//        System.out.println("updateDOM: "+this.getNodeName());
        updateDOMContent();
        updateDOMAttributes();
        NodeList childNodes = this.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof AbstractBase) {
                ((AbstractBase) child).updateDOM();
            }
        }
    }

    /**
     * update delegate content.
     */
    public void updateDelegateContent() {
//        System.out.println("updateDelegateContent: "+this.getNodeName());
    }

    /**
     * update delegate attributes.
     */
    public void updateDelegateAttributes() {
//        System.out.println("updateDelegateAttributes: "+this.getNodeName());
    }

    /**
     * update DOM content.
     */
    public void updateDOMContent() {
//        System.out.println("updateDOMContent: "+this.getNodeName());
    }

    /**
     * update DOM attributes.
     */
    public void updateDOMAttributes() {
//        System.out.println("updateDOMAttributes: "+this.getNodeName());
    }
}
