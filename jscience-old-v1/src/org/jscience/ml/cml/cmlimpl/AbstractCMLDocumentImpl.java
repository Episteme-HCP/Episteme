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

import org.jscience.ml.cml.AbstractBase;
import org.jscience.ml.cml.AbstractCMLDocument;
import org.jscience.ml.cml.CMLException;
import org.jscience.ml.cml.dom.pmr.PMRDocumentImpl;
import org.jscience.ml.cml.util.PMRDOMUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * The base class for any CML Document objects
 *
 * @author P.Murray-Rust, 1999, 2000, 2003
 */
public class AbstractCMLDocumentImpl extends PMRDocumentImpl
        implements AbstractCMLDocument {

    /**
     * Description of the Field
     */
    public final static String DEFAULT_OUTPUT_ENCODING = "ISO-8859-1";
    /**
     * Description of the Field
     */
    public final static String DEFAULT_OUTPUT_DTD = "cml.dtd";

    protected String version = CML2;
    protected boolean arraySyntax = false;
    protected boolean debug = false;

    /**
     * create an Empty Document.
     * probably should only be used when we need an instance of this class for introspection
     */
    public AbstractCMLDocumentImpl() {
        super();
        init();
    }

    /**
     * Description of the Method
     */
    void init() {

    }

    /**
     * Constructor for the AbstractCMLDocumentImpl object
     *
     * @param doc Description of the Parameter
     */
    protected AbstractCMLDocumentImpl(Document doc) {
        super(doc);
    }

    static Hashtable tagTable = new Hashtable();

    public static AbstractBase transferOwnerDocument(AbstractCMLDocument doc, AbstractBase elem) {
        /**- PMR
         boolean deepCopy = true;
         Node node = null;
         try {
         node = doc.importNode(elem, deepCopy);
         } catch (DOMException de) {
         System.err.println("NOT_SUPPORTED_ERR: Raised if the type of node being imported is not supported.");
         de.printStackTrace();
         }

         return (AbstractBase) node;
         }
         --*/
///*
// create root element if there isn't one.
        AbstractBase ab = null;
        if (doc.getDocumentElement() == null) {
            Element el = doc.createElement("dummyRootElement");
            doc.appendChild(el);
        }
        try {
            StringWriter sw = new StringWriter();
            PMRDOMUtils.outputEventStream(elem, sw, 0, 0);
            sw.close();
            String s = sw.toString();

            StringReader sr = new StringReader(s);
            InputSource is = new InputSource(sr);
            ab = (AbstractBase) DocumentFactoryImpl.newInstance().parseSAX(is, doc);
            if (doc.getDocumentElement() == null) {
                ab = (AbstractBase) doc.getDocumentElement();
            }
            if (ab == null) {
                System.out.println("Input document has no root Element...");
                System.out.println("From string: " + s);
            }

        } catch (IOException ioe) {
            System.err.println("BUG");
            ioe.printStackTrace();
        } catch (CMLException cmle) {
            System.err.println("BUG");
            cmle.printStackTrace();
        }
        return ab;
    }
//*/


    /**
     * Description of the Method
     *
     * @param tagName Description of the Parameter
     * @return Description of the Return Value
     */
    public Element createSubclassedElement(String tagName) {
        return (Element) this.createElement(tagName);
    }

    public Element subclass(Element oldEl) {
        String tagName = oldEl.getNodeName();
        Element newEl = this.createSubclassedElement(tagName);
        NamedNodeMap attributes = oldEl.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Attr attribute = (Attr) attributes.item(i);
            newEl.setAttribute(attribute.getName(), attribute.getValue());
        }
        NodeList childNodes = oldEl.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Text) {
                String text = child.getNodeValue();
                newEl.appendChild(this.createTextNode(text));
            } else if (child instanceof Comment) {
            } else if (child instanceof Element) {
                Element newChild = subclass((Element) child);
                newEl.appendChild(newChild);
            } else {
            }
        }
        return newEl;
    }

    /**
     * creates new AbstractBase
     *
     * @return the new element
     */
    public AbstractBase createAbstractBase() {
        return new CMLBaseImpl("base", this);
    }

    /**
     * getFirstChild (cf AbstractBase)
     *
     * @param elementName Description of the Parameter
     * @return the Element (null if none)
     */
    public Element getFirstElement(String elementName) {
        NodeList nodes = this.getElementsByTagName(elementName);
        return (nodes.getLength() == 0) ? null : (Element) nodes.item(0);
    }

    /**
     * getElementList (cf AbstractBase) NOTE this will get subelements as well
     * as toplevel ones (for example Molecules which have Molecule children)
     *
     * @param elementName Description of the Parameter
     * @return the Elements (null if none)
     */
    public Element[] getElementList(String elementName) {
        NodeList nodes = this.getElementsByTagName(elementName);
        if (nodes.getLength() == 0) {
            return null;
        }
        Element[] elements = new Element[nodes.getLength()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = (Element) nodes.item(i);
        }
        return elements;
    }

    /**
     * getChildVector (cf AbstractBase)
     *
     * @param elementName Description of the Parameter
     * @return the Elements (empty list if none)
     */
    public Vector getElementVector(String elementName) {
        NodeList nodes = this.getElementsByTagName(elementName);
        if (nodes.getLength() == 0) {
            return null;
        }
        Vector elementVector = new Vector();
        for (int i = 0; i < nodes.getLength(); i++) {
            elementVector.addElement(nodes.item(i));
        }
        return elementVector;
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
        if (debug) System.out.println("DOC> " + s);
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

    /**
     * write the document in XML, using node-specific routines
     *
     * @param w       the writer
     * @param control (cannot remember what this does!)
     * @throws IOException  Description of the Exception
     * @throws CMLException Description of the Exception
     */
    public void writeXML(Writer w, String control)
            throws IOException, CMLException {
        debug("AbstractDoc CONTROL..." + control);
        Element elem = this.getDocumentElement();
        if (elem instanceof AbstractBase) {
//			System.out.println ("saving as AbstractBase");
            ((AbstractBase) elem).writeXML(w, control);
        } else {
//			System.out.println ("saving as CMLBaseImpl");
            CMLBaseImpl.writeXML0(elem, w, control);
        }
    }

    /**
     * main
     *
     * @param args command line
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Usage: java org.jscience.ml.cml.cmlimpl.CMLDocumentImpl -IN url [-OUT file]");
            System.exit(0);
        }

        String inFile = "";
        String outFile = "";
        int i = 0;

        try {

            while (i < args.length) {
                if (false) {
                    ;
                } else if (args[i].equals("-IN")) {
                    i++;
                    inFile = args[i++];
                } else if (args[i].equals("-OUT")) {
                    i++;
                    outFile = args[i++];
                } else {
                    System.err.println("Bad arg: " + args[i - 1]);
                }
            }
            AbstractCMLDocument doc = null;
            doc = DocumentFactoryImpl.newInstance().parseSAX(new InputSource(inFile));
            if (doc == null) {
                System.err.println("No doc generated by reader");
            } else if (!outFile.equals("")) {
                FileWriter fw = new FileWriter(outFile);
                String control = " ";
                doc.writeXML(fw, control);
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clone element in context of document.
     *
     * @param element to clone (may or may not be owned by this)
     * @return the cloned element (owner is this)
     * @throws CMLException cannot clone non-AbstractBase
     */
    public AbstractBase clone(AbstractBase element) throws CMLException {
        boolean deepCopy = true;
        return (AbstractBase) element.cloneNode(deepCopy);

/*
        if (!(element instanceof AbstractBase)) {
            throw new CMLException("Cannot clone non-AbstractBase");
        }
        AbstractBase newBase = null;
        try {
            StringWriter sw = new StringWriter();
            PMRDOMUtils.output(element, sw);
            sw.close();
            String s = sw.toString();
            newBase = (AbstractBase)  DocumentFactoryImpl.newInstance().parseSAX(new InputSource(new StringReader(s)), this);
        } catch (IOException ioe) {
            System.out.println("BUG");
            ioe.printStackTrace();
        }
        return newBase;
*/
    }

    /**
     * update delegates.
     * updates documentElement delegates (content and attributes)
     * then recurses through children
     */
    public void updateDelegates() {
//        System.out.println("updateDelegates "+this.getNodeName());
        Element documentElement = this.getDocumentElement();
        if (documentElement != null && documentElement instanceof AbstractBase) {
            ((AbstractBase) documentElement).updateDelegates();
        }
    }

    /**
     * update DOM.
     * updates documentElement DOM (content and attributes)
     * then recurses through children
     */
    public void updateDOM() {
        Element documentElement = this.getDocumentElement();
        if (documentElement != null && documentElement instanceof AbstractBase) {
            ((AbstractBase) documentElement).updateDOM();
        }
    }
}

