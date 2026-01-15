package org.jscience.ml.cml.util;

import org.w3c.dom.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * re-usable routines to support PMR* interfaces
 *
 * @author Peter Murray-Rust, 1997, 2003
 */

public class PMRDOMUtils {

    /////
    /**
     * Description of the Field
     */
    public final static int PRETTY = 1;

    /**
     * recursively deletes all whitespace descendants
     *
     * @param element Description of the Parameter
     */
    public static void deleteWhitespaceDescendants(Element element) {
        if (element == null) {
            return;
        }
        NodeList childNodes = element.getChildNodes();
        Vector deleteVector = new Vector();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Element) {
                deleteWhitespaceDescendants((Element) child);
            } else if (child instanceof Text) {
                if (child.getNodeValue().trim().equals("")) {
// save nodes to be deleted
                    deleteVector.addElement(child);
                }
            }
        }
// delete nodes
        for (Enumeration e = deleteVector.elements(); e.hasMoreElements();) {
            element.removeChild((Node) e.nextElement());
        }
    }

    /**
     * remove a node from its parent
     *
     * @param node to be removed
     */
    public static void removeNode(Node node) {
        Node parentNode = node.getParentNode();
        if (parentNode != null) {
            parentNode.removeChild(node);
        }
    }

    /**
     * remove all child nodes
     *
     * @param element to remove child nodes from
     */
    public static void removeChildNodes(Element element) {
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            element.removeChild(childNodes.item(i));
        }
    }

    /**
     *  transfers complete fromDocument, changing ownership and subsclassing
     *
     *@param  element  Description of the Parameter
     *@return Description of the Return Value
     */

    /**
     * return all descendants in document order
     *
     * @param element Description of the Parameter
     * @return Description of the Return Value
     */
    public static Enumeration depthFirstEnumeration(Element element) {
        Vector outputVector = new Vector();
        if (element.hasChildNodes()) {
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node childNode = nodeList.item(i);
                outputVector.addElement(childNode);
                if (childNode instanceof Element) {
                    Enumeration e = PMRDOMUtils.depthFirstEnumeration((Element) childNode);
                    while (e.hasMoreElements()) {
                        outputVector.addElement(e.nextElement());
                    }
                }
            }
        }
        return outputVector.elements();
    }

    /**
     * get all Element children If none, returns empty Vector
     *
     * @param element Description of the Parameter
     * @return The childElements value
     */
    public static Vector getChildElements(Element element) {
        Vector vector = new Vector();
        if (element == null) {
            return vector;
        }
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                vector.addElement(child);
            }
        }
        return vector;
    }

    /**
     * get the index'th Element child If none returns null Count ignores
     * non-Element nodes
     *
     * @param element Description of the Parameter
     * @param index   Description of the Parameter
     * @return The childElement value
     */
    public static Element getChildElement(Element element, int index) {
        Vector vector = getChildElements(element);
        return (vector.size() == 0 || index >= vector.size()) ? null :
                (Element) vector.elementAt(index);
    }

    /**
     * gets all ancestors of 'element' including the document Element but not
     * Document. [element is not its own ancestor]
     *
     * @param element Description of the Parameter
     * @return Element the ancestor (null = none)
     */
    public static Vector getAncestors(Element element) {
        Node parent = element;
        Vector vector = new Vector();
        while (true) {
            parent = parent.getParentNode();
            if (parent == null || !(parent instanceof Element)) {
                return vector;
            }
            vector.addElement(parent);
        }
    }

    /**
     * gets first ancestor of 'element' with tagName; [element is not its own
     * ancestor]
     *
     * @param element Description of the Parameter
     * @param tagName Description of the Parameter
     * @return Element the ancestor (null = none)
     */
    public static Element getAncestor(Element element, String tagName) {
        Node parent = element;
        String prefix = element.getPrefix();
        String tagName0 = tagName;
        if (prefix != null && tagName0 != null) {
            tagName0 = prefix + ":" + tagName0;
        }
        while (true) {
            parent = parent.getParentNode();
            if (parent == null || !(parent instanceof Element)) {
                return null;
            }
            if (((Element) parent).getTagName().equals(tagName0)) {
                return (Element) parent;
            }
        }
    }

    /**
     * converts a NodeList to a vector
     * <p/>
     * the vector (but not its elements) is safely mutable.
     * shallowCopies the items in the node list
     *
     * @param nodes NodeList to copy
     * @return the equiavlent vector
     */
    public static Vector createVector(NodeList nodes) {
        int n = nodes.getLength();
        Vector v = new Vector(n);
        for (int i = 0; i < n; i++) {
            v.setElementAt(nodes.item(i), i);
        }
        return v;
    }

    /**
     * converts a NamedNodeMap to a vector
     * <p/>
     * the vector (but not its elements) is safely mutable.
     * shallowCopies the items in the nodemap
     *
     * @param nodeMap NodeMap to copy
     * @return the equiavlent vector
     */
    public static Vector createVector(NamedNodeMap nodeMap) {
        int n = nodeMap.getLength();
        Vector v = new Vector();
        for (int i = 0; i < n; i++) {
            v.addElement(nodeMap.item(i));
        }
        return v;
    }

    /*
    *  ======================================================================
    */
    /**
     * Kludge to overcome cases where getAttribute(attName) has been subverted
     *
     * @param element Description of the Parameter
     * @param attName Description of the Parameter
     * @return String the attribute String; null if none
     */
    public static String getAttribute(Element element, String attName) {
        if (attName == null) {
            return null;
        }
        NamedNodeMap nnm = element.getAttributes();
        if (nnm == null) {
            return null;
        }
        Node node = nnm.getNamedItem(attName);
        if (node == null) {
            return null;
        }
        return node.getNodeValue();
    }

    /**
     * returns the attributes in concatenated text form (suitable for insertion
     * to start tags). If none, returns "". Example:<BR>
     * xml:link="simple" href="http://www.some/where"<BR>
     *
     * @param element Description of the Parameter
     * @return String the attribute String
     */
    public static String getAttributeString(Element element) {
        NamedNodeMap nnm = element.getAttributes();
        String s = "";
        if (nnm != null) {
            for (int i = 0; i < nnm.getLength(); i++) {
                Attr attr = (Attr) nnm.item(i);
                if (attr == null) {
                    System.err.println("NULL Attr");
// omit anything that looks like a namespace
                } else if (attr.getName().startsWith("xmlns")) {
                } else {
                    s += PMRDOMUtils.getAttributeString(attr);
                }
            }
        }
        return s;
    }

    /**
     * returns a string representing a single attribute in a start tag, for example
     * foo=bar plugh"; String starts with space and delimiters are QUOT, not
     * APOS
     *
     * @param att Description of the Parameter
     * @return The attributeString value
     */
    public static String getAttributeString(Attr att) {
        return " " + att.getNodeName() + CMLUtils.EQUALS + CMLUtils.QUOT + CMLUtils.escape(att.getNodeValue()) + CMLUtils.QUOT;
    }

    /**
     * normalizes all whitespace in a string to single spaces
     *
     * @param s the string
     * @return normalized string; null if s is null
     */
    public static String normalizeString(String s) {
        if (s == null) return null;
        String ss = s.trim();
        StringBuffer sb = new StringBuffer();
        boolean inWhite = true;
        for (int i = 0; i < ss.length(); i++) {
            char c = ss.charAt(i);
            if (inWhite) {
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                    inWhite = false;
                }
            } else {
                if (Character.isWhitespace(c)) {
                    inWhite = true;
                    sb.append(' ');
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    /**
     * gets vector of Element children with given element name (ignored if name
     * is null or bad value of sensitivity).
     *
     * @param element Description of the Parameter
     * @param name    Description of the Parameter
     * @return Vector the vector of children (empty if none)
     */
    public static Vector getChildrenWithElementName(Element element, String name) {
        if (element == null || name == null || !element.hasChildNodes()) {
            return null;
        }
        NodeList childNodes = element.getChildNodes();
        Vector vector = new Vector();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (!(child instanceof Element)) {
                continue;
            }
            if (((Element) child).getTagName().equals(name)) {
                vector.addElement(child);
            }
        }
        return vector;
    }

//

    //

    /**
     * gets first child with given element name (ignored if name is null).
     *
     * @param element Description of the Parameter
     * @param name    Description of the Parameter
     * @return Element the first child or null
     */
    public static Element getFirstChildWithElementName(Element element, String name) {
        Vector childVector = PMRDOMUtils.getChildrenWithElementName(element, name);
        return (childVector == null || childVector.size() == 0) ? null :
                (Element) childVector.elementAt(0);
    }

    /**
     * if this has a single PCDATA child, returns its string value, else null
     *
     * @param element Description of the Parameter
     * @return String String content of single PCDATA child
     */
    public static String getPCDATAContent(Element element) {
        String ss = "";
        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child instanceof Text) {
                ss += child.getNodeValue();
            } else if (child instanceof Element) {
                ss = null;
                break;
            }
        }
        return ss;
    }

    /**
     * if this has a single PCDATA child, returns it, else null
     *
     * @param element Description of the Parameter
     * @return PCDATANode single PCDATA child
     */
    public static Text getPCDATAChildNode(Element element) {
        if (!element.hasChildNodes()) {
            return null;
        }
        if (element.getChildNodes().getLength() != 1) {
            return null;
        }
        Node child = element.getFirstChild();
        return (!(child instanceof Text)) ? null : (Text) child;
    }

///** interpret the value of the given attribute as a number */
//	public static Double getNumericAttribute(Element element, String attName) {
//		Double n = null;
//		String val = element.getAttribute(attName);
//		if (val == null || val.equals("")) return n;
//		try {
//			n = new Double(val);
//		} catch (NumberFormatException e) {}
//		return n;
//	}

    //

    /**
     * Sets a string to be the content of an existing single PCDATANode If
     * there are no children, create a new PCDATANode. If there are non-PCDATA
     * children or more than one PCDATANode child, return without action.
     *
     * @param element The new pCDATAContent value
     * @param s       The new pCDATAContent value
     */
    public static void setPCDATAContent(Element element, String s) {
        if (s == null || element == null) {
            return;
        }
        if (!element.hasChildNodes()) {
            Text text = element.getOwnerDocument().createTextNode(s);
            element.appendChild(text);
        } else {
            Text pcdNode = PMRDOMUtils.getPCDATAChildNode(element);
            if (pcdNode == null) {
                return;
            }
            pcdNode.setData(s);
        }
    }

//

//	public static String write(Writer w, Document document, int type, int level, String encoding,
//		boolean xmlDecl, String dtd/*, String prefix, String namespace*/)
//		throws IOException {
//		if (document == null) {
//			w.write("NULL document");
//			return w.toString();
//		}
//		return outputEventStream(document.getDocumentElement(), w, type, level, encoding,
//			xmlDecl, dtd/*, prefix, namespace*/);
//	}
//
///** gets the event stream (as XML or text)
// Options for <TT>type</TT><BR>
//<UL>
//<LI>XMLCONTENT rendering of content as XML</LI>
//<LI>TAGGED	precise rendering of XML (at present == XML)</LI>
//<LI>UNTAGGED all tags removed</LI>
//<LI>PRETTY pretty printed (indented tags)</LI>
//<LI>SPLITFILES pretty printed and split into entities</LI>
//<LI>WHITESPACE with whitespace elements highlighted by comments</LI>
//</UL>
//** This is a mess and needs recoding into subclasses **
//@param Writer w outputs to this stream.
//@param int type	type chosen from list above
//@param int level of indentation (normally set to 0 when called, and expanded
//recursively internally);
//@return String output event stream if w is  StringWriter

    //*/

    /**
     * Description of the Method
     *
     * @param node  Description of the Parameter
     * @param w     Description of the Parameter
     * @param type  Description of the Parameter
     * @param level Description of the Parameter
     * @return Description of the Return Value
     * @throws IOException Description of the Exception
     */
    public static String outputEventStream(Node node, Writer w, int type, int level
                                           /*
                                           *  , String prefix, String namespace
                                           */) throws IOException {
        if (w == null) {
            throw new IOException("Null Writer");
        }
        if (node == null) {
            return "NULL";
        }
        if (node instanceof Element) {
            Element element = (Element) node;
            //This should pick up specialised nodes
            if (PMRDOMUtils.isEmptyElement(element)) {
                PMRDOMUtils.outputEmptyTag(element, w, type, level
                        /*
                        *  , prefix, namespace
                        */);
            } else {
                PMRDOMUtils.outputStartTag(element, w, type, level
                        /*
                        *  , prefix, namespace
                        */);
                PMRDOMUtils.outputChildContent(element, w, type, level
                        /*
                        *  , prefix, namespace
                        */);
            }
            if (!PMRDOMUtils.isEmptyElement(element)) {
                PMRDOMUtils.outputEndTag(element, w, type, level);
            }
        } else if (node instanceof Comment) {
            String s = "<!--" + node.getNodeValue() + "-->";
            w.write(s);
        } else if (node instanceof Document) {
            Element child = ((Document) node).getDocumentElement();
            if (child == null) {
                w.write("document has no child element");
            } else {
                NodeList nl = node.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    outputEventStream(nl.item(i), w, type, level);
                }
            }
        } else if (node instanceof ProcessingInstruction) {
            String s = "<?" + ((ProcessingInstruction) node).getTarget() + " " + ((ProcessingInstruction) node).getData() + "?>";
            w.write(s);
        } else if (node instanceof Text) {
            String s = node.getNodeValue().toString();
            //omit whitespace
            if (type == PMRDOMUtils.PRETTY) {
                String s1 = s.trim();
                if (s1.equals("")) {
                    s = s1;
                }
            }
            w.write(CMLUtils.escape(s));
        } else if (node instanceof DocumentType) {
        } else {
            String s = node.getNodeValue();
            w.write("<!--Output event Stream: untreated node" + node.getClass() + "-->");
        }

        return ((level == 0) ? w.toString() : null);
    }

    /**
     * convenience routine to output a Node as a String
     *
     * @param node Description of the Parameter
     * @return Description of the Return Value
     */
    public static String debug(Node node) {
        String s = output(node);
        System.out.println(s);
        return s;
    }

    /**
     * convenience routine to output a Node as a String
     *
     * @param node Description of the Parameter
     * @return Description of the Return Value
     */
    public static String output(Node node) {
        String s = "";
        try {
            StringWriter sw = new StringWriter();
            output(node, sw);
            sw.close();
            s = sw.toString();
        } catch (IOException e) {
            ;
        }
        return s;
    }

    /**
     * convenience routine
     *
     * @param document Description of the Parameter
     * @param w        Description of the Parameter
     * @return Description of the Return Value
     * @throws IOException Description of the Exception
     */
    public static String output(Document document, Writer w)
            throws IOException {
        if (document == null) {
            return null;
        } else {
            return outputEventStream(document, w, PRETTY, 0);
        }
    }

    /**
     * convenience routine
     *
     * @param node Description of the Parameter
     * @param w    Description of the Parameter
     * @return Description of the Return Value
     * @throws IOException Description of the Exception
     */
    public static String output(Node node, Writer w)
            throws IOException {
        return outputEventStream(node, w, PRETTY, 0);
    }

    /**
     * same as above but also outputs XMLDeclaration and DOCTYPE
     *
     * @param node     Description of the Parameter
     * @param w        Description of the Parameter
     * @param type     Description of the Parameter
     * @param level    Description of the Parameter
     * @param encoding Description of the Parameter
     * @param xmlDecl  Description of the Parameter
     * @param dtd      Description of the Parameter
     * @return Description of the Return Value
     * @throws IOException Description of the Exception
     */
    public static String outputEventStream(Node node, Writer w, int type, int level, String encoding,
                                           boolean xmlDecl, String dtd
                                           /*
                                           *  , String prefix, String namespace
                                           */) throws IOException {
        if (xmlDecl) {
            w.write("<?xml version=\"1.0\"" +
                    ((encoding != null) ? " encoding=\"" + encoding + "\"" : "") + "?>\n");
        }
        if (dtd != null && level == 0 && node instanceof Element) {
            String rootName = ((Element) node).getTagName();
            w.write("<!DOCTYPE " + rootName + " SYSTEM \"" + dtd + "\">\n");
        }
        return outputEventStream(node, w, type, level
                /*
                *  , prefix, namespace
                */);
    }

    /**
     * Gets the emptyElement attribute of the PMRDOMUtils class
     *
     * @param element Description of the Parameter
     * @return The emptyElement value
     */
    public static boolean isEmptyElement(Element element) {
        NodeList childs = element.getChildNodes();
        return (childs == null || childs.getLength() == 0);
    }

    /**
     * subclassed by specialist classes
     *
     * @param element Description of the Parameter
     * @param w       Description of the Parameter
     * @param type    Description of the Parameter
     * @param level   Description of the Parameter
     * @throws IOException Description of the Exception
     */
    public static void outputStartTag(Element element, Writer w, int type,
                                      int level
                                      /*
                                      *  , String prefix, String namespace
                                      */)
            throws IOException {
        //if only a single PCDATANode, omit returns in prettyprinting
        int nspace = level * 2;
        if (type == PMRDOMUtils.PRETTY) {
            w.write(CMLUtils.spaces(nspace));
        }
        if (false) {
        } else {
            outputStartTag0(w, element, level, ">");
        }
        if ((type == PMRDOMUtils.PRETTY) && isPrettyCR(element, type)) {
            w.write("\n");
        }
    }

    /**
     * Description of the Method
     *
     * @param w       Description of the Parameter
     * @param element Description of the Parameter
     * @param level   Description of the Parameter
     * @param sTagC   Description of the Parameter
     * @throws IOException Description of the Exception
     */
    static void outputStartTag0(Writer w, Element element, int level, String sTagC) throws IOException {
//		String prefix = element.getPrefix();
        String qName = element.getTagName();
        int idx = qName.indexOf(":");
        String prefix = (idx == -1) ? null : qName.substring(0, idx);
        String namespace = null;
//		if (level == 0) namespace = element.getNamespaceURI();
//		if (level == 0) namespace = "http://www.xmlcml.org/dtd/cml1_0_1";
        w.write("<" + element.getTagName());
        if (!PMRDOMUtils.isEmptyAttribute(namespace)) {
            w.write(" xmlns");
            if (prefix != null) {
                w.write(":" + prefix);
            }
            w.write("=\"" + namespace + "\"");
        }
        w.write(PMRDOMUtils.getAttributeString(element) + sTagC);
    }

    /**
     * subclassed by specialist classes
     *
     * @param element Description of the Parameter
     * @param w       Description of the Parameter
     * @param type    Description of the Parameter
     * @param level   Description of the Parameter
     * @throws IOException Description of the Exception
     */
    public static void outputEmptyTag(Element element, Writer w, int type, int level
                                      /*
                                      *  , String prefix, String namespace
                                      */)
            throws IOException {
        //if only a single PCDATANode, omit returns in prettyprinting
        int nspace = level * 2;
        if (type == PMRDOMUtils.PRETTY) {
            w.write(CMLUtils.spaces(nspace));
        }
        if (false) {
        } else {
            outputStartTag0(w, element, level, "/>");
        }
        if ((type == PMRDOMUtils.PRETTY) && isPrettyCR(element, type)) {
            w.write("\n");
        }
    }

    /**
     * only a single PCDATANode, omit CRs
     *
     * @param element Description of the Parameter
     * @param type    Description of the Parameter
     * @return The prettyCR value
     */
    private static boolean isPrettyCR(Element element, int type) {
        if (type != PMRDOMUtils.PRETTY) {
            return false;
        }
        if (element.getChildNodes().getLength() == 1 && element.getFirstChild() instanceof Text) {
            return false;
        }
        return true;
    }

    /**
     * for building the tree If writer is null, builds a tree by adding this to
     * parentNode, else outputs a start tag.
     *
     * @param element    Description of the Parameter
     * @param w          Description of the Parameter
     * @param parentNode Description of the Parameter
     * @param level      Description of the Parameter
     * @throws IOException Description of the Exception
     */
    public static void outputStartElement(Element element, Writer w, Element parentNode, int level
                                          /*
                                          *  , String prefix, String namespace
                                          */) throws IOException {
        if (w == null) {
            parentNode.appendChild(element);
        } else {
            level = Math.max(0, level);
            //if (level > 0) namespace = null;
            PMRDOMUtils.outputStartTag(element, w, PMRDOMUtils.PRETTY, level
                    /*
                    *  , prefix, namespace
                    */);
        }
    }

    /**
     * subclassed by specialist classes
     *
     * @param element Description of the Parameter
     * @param w       Description of the Parameter
     * @param type    Description of the Parameter
     * @param level   Description of the Parameter
     * @throws IOException Description of the Exception
     */
    public static void outputChildContent(Element element, Writer w, int type, int level
                                          /*
                                          *  , String prefix, String namespace
                                          */) throws IOException {
        int count = 0;
        NodeList childs = element.getChildNodes();
        for (int i = 0; i < childs.getLength(); i++) {
            PMRDOMUtils.outputEventStream(childs.item(i), w, type, level + 1
                    /*
                    *  , prefix, null
                    */);
        }
    }

    /**
     * subclassed by specialist classes
     *
     * @param element Description of the Parameter
     * @param w       Description of the Parameter
     * @param type    Description of the Parameter
     * @param level   Description of the Parameter
     * @throws IOException Description of the Exception
     */
    public static void outputEndTag(Element element, Writer w, int type, int level)
            throws IOException {
        int nspace = level * 2;
        if ((type == PMRDOMUtils.PRETTY) && isPrettyCR(element, type)) {
            w.write(CMLUtils.spaces(nspace));
        }
        if (false) {
        } else {
            w.write("</" + element.getTagName() + ">");
        }
        if (type == PMRDOMUtils.PRETTY) {
            w.write("\n");
        }
    }

    /**
     * for building the tree If writer is null, noop; else outputs an end tag
     *
     * @param element Description of the Parameter
     * @param w       Description of the Parameter
     * @param level   Description of the Parameter
     * @throws IOException Description of the Exception
     */
    public static void outputEndElement(Element element, Writer w, int level) throws IOException {
        if (w != null) {
            PMRDOMUtils.outputEndTag(element, w, PMRDOMUtils.PRETTY, level);
        }
    }


    ///*------------------------------------------------------------------------------------*/
    /**
     * recursively removes all whitespace nodes
     *
     * @param document Description of the Parameter
     */
    public static void deleteWhitespaceDescendants(Document document) {
        Element rootNode = document.getDocumentElement();
        PMRDOMUtils.deleteWhitespaceDescendants(rootNode);
    }

    ///** UTILITIES */
    /**
     * Gets the emptyAttribute attribute of the PMRDOMUtils class
     *
     * @param attributeValue Description of the Parameter
     * @return The emptyAttribute value
     */
    public static boolean isEmptyAttribute(String attributeValue) {
        return (attributeValue == null || attributeValue.equals(""));
    }

    /**
     * clones node in context of new document.
     * <p/>
     * provides workaraound for setDocument(newDocument) which is DOM3
     * uses document.createFoo()
     * does not maintain namespaces
     * only does Element, Text, Comment, ProcessingInstruction at present
     * creates W3C classes, not subclasses
     * cannot clone Document node and returns null
     * cloned node will have no parent
     *
     * @param node     to clone
     * @param document context document
     * @return new node
     */
    public static Node cloneNode(Node node, Document document) {
        if (node instanceof Document) {
            return null;
        } else {
            return cloneNode(node, document, null);
        }
    }

    static Node cloneNode(Node node, Document document, Node parent) {
        Node newNode = null;
        if (node instanceof Element) {
            Element element = (Element) node;
            newNode = document.createElement(element.getNodeName());
            NamedNodeMap nnm = element.getAttributes();
            if (nnm != null) {
                for (int i = 0; i < nnm.getLength(); i++) {
                    Attr attr = (Attr) nnm.item(i);
                    ((Element) newNode).setAttribute(attr.getNodeName(), attr.getNodeValue());
                }
            }
        } else if (node instanceof Text) {
            newNode = document.createTextNode(node.getNodeValue());
        } else if (node instanceof Comment) {
            newNode = document.createComment(node.getNodeValue());
        } else if (node instanceof ProcessingInstruction) {
            ProcessingInstruction pi = (ProcessingInstruction) node;
            newNode = document.createProcessingInstruction(pi.getTarget(), pi.getData());
        } else {
        }

        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            cloneNode(childNodes.item(i), document, newNode);
        }
        if (parent != null) {
            parent.appendChild(newNode);
        }

        return newNode;
    }

    /**
     * match  decendent elements.
     * <p/>
     * Finds all decendent elements of rootElement that match the criteria defined by p.
     *
     * @param rootElement the element who along with its children will be processed.
     * @param p           the <code>Picker</code>  used to match the elements
     * @return a List of the matching elements, or null if none matched.
     */
    public static List pickElements(Element rootElement, final Picker p) {
        NodeList childNodes = rootElement.getChildNodes();
        int i = childNodes.getLength();

        if (i > 0) {
            LinkedList nodesToConsider = new LinkedList();
            Node node = null;
            ArrayList chosenNodes = new ArrayList();

            --i;
            for (; i >= 0; --i) {
                nodesToConsider.add(childNodes.item(i));
            }

            while (nodesToConsider.size() > 0) {
                node = (Node) nodesToConsider.removeFirst();
                childNodes = node.getChildNodes();

                if ((i = childNodes.getLength()) > 0) {
                    --i;
                    for (; i >= 0; --i) {
                        nodesToConsider.add(childNodes.item(i));
                    }
                }

                if (p.pick(node)) {
                    chosenNodes.add(node);
                }
            }

            if (chosenNodes.size() > 0) {
                return chosenNodes;
            }
        }

        // no children
        return null;
    }

    /**
     * copies attributes chosen via a <code>Picker</code> callback.
     * <p/>
     * Using the Picker, choose a set of Attributes to be copied from the Element.
     *
     * @param e the element whose Attributes are to be processed
     * @param p the picker that determines whether to choose the attribute
     * @return an array of the matching Attributes, or null if none matched.
     */
    public static Attr[] copyAttributes(Element e, final Picker p) {
        ArrayList l = new ArrayList();

        NamedNodeMap nnm = e.getAttributes();
        if (nnm != null) {
            for (int i = nnm.getLength() - 1; i >= 0; --i) {
                Attr attr = (Attr) nnm.item(i);
                if (p.pick(attr)) {
                    l.add(attr);
                }
            }
        }

        if (l.size() > 0) {
            return ((Attr[]) l.toArray(new Attr[0]));
        } else {
            return null;
        }
    }

    /**
     * copies and removes attributes chosen via a <code>Picker</code> callback.
     * <p/>
     * Using the Picker, choose a set of Attributes to be removed  from the Element.
     * These Attributes are then returned
     *
     * @param e the element whose attributes are to be processed
     * @param p the <code>Picker</code> that determines whether to choose the attribute
     * @return an array of the matching Attributes, or null if none matched.
     * @throws DOMException propagated from the call to remove the Attribute node from the Element.
     */
    public static Attr[] cutAttributes(Element e, final Picker p) throws DOMException {
        ArrayList l = new ArrayList();

        NamedNodeMap nnm = e.getAttributes();
        if (nnm != null) {
            for (int i = nnm.getLength() - 1; i >= 0; --i) {
                Attr attr = (Attr) nnm.item(i);
                if (p.pick(attr)) {
                    l.add(attr);
                    e.removeAttributeNode(attr);
                }
            }
        }

        if (l.size() > 0) {
            return ((Attr[]) l.toArray(new Attr[0]));
        } else {
            return null;
        }
    }

    /**
     * adds Attributes to a given Element.
     * <p/>
     * Adds  a set of Attributes to an Element.
     *
     * @param e     the element whose attributes are to be processed
     * @param attrs an array of the Attributes to be added
     * @throws DOMException propagated from the call to add the Attribute node to the Element.
     */
    public static void pasteAttributes(Element e, final Attr[] attrs) throws DOMException {
        if (attrs != null) {
            for (int i = attrs.length - 1; i >= 0; --i) {
                e.setAttributeNode(attrs[i]);
            }
        }
    }
}
