package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.*;

import java.util.ArrayList;

/**
 * Delegation-based implementation
 */
public class PMRElementImpl extends PMRNodeImpl implements Element {

    /**
     * By far the vast majority of objects (apart from text) that authors
     * encounter when traversing a document are <code>Element</code> nodes.
     * Assume the following XML document:&lt;elementExample id="demo"&gt;
     * &lt;subelement1/&gt;
     * &lt;subelement2&gt;&lt;subsubelement/&gt;&lt;/subelement2&gt;
     * &lt;/elementExample&gt;
     * <p>When represented using DOM, the top node is an <code>Element</code> node
     * for "elementExample", which contains two child <code>Element</code> nodes,
     * one for "subelement1" and one for "subelement2". "subelement1" contains no
     * child nodes.
     * <p>Elements may have attributes associated with them; since the
     * <code>Element</code> interface inherits from <code>Node</code>, the generic
     *  <code>Node</code> interface method <code>getAttributes</code> may be used
     * to retrieve the set of all attributes for an element.  There are methods on
     *  the <code>Element</code> interface to retrieve either an <code>Attr</code>
     *  object by name or an attribute value by name. In XML, where an attribute
     * value may contain entity references, an <code>Attr</code> object should be
     * retrieved to examine the possibly fairly complex sub-tree representing the
     * attribute value. On the other hand, in HTML, where all attributes have
     * simple string values, methods to directly access an attribute value can
     * safely be used as a convenience.
     */

    /**
     * only for use by subclass constructors
     */
    protected PMRElementImpl() {
        super();
    }

    protected PMRElementImpl(PMRDocument pmrDoc, String tagName) {
        super((PMRNodeImpl) pmrDoc.createElement(tagName), pmrDoc);
    }

    protected PMRElementImpl(Element element, PMRDocument doc) {
        super(element, doc);
    }

    /**
     * The name of the element. For example, in: &lt;elementExample
     * id="demo"&gt;  ... &lt;/elementExample&gt; , <code>tagName</code> has
     * the value <code>"elementExample"</code>. Note that this is
     * case-preserving in XML, as are all of the operations of the DOM. The
     * HTML DOM returns the <code>tagName</code> of an HTML element in the
     * canonical uppercase form, regardless of the case in the  source HTML
     * document.
     */
    public String getTagName() {
        return ((Element) delegateNode).getTagName();
    }

    /**
     * Retrieves an attribute value by name.
     *
     * @param name The name of the attribute to retrieve.
     * @return The <code>Attr</code> value as a string, or the empty  string if
     *         that attribute does not have a specified or default value.
     */
    public String getAttribute(String name) {
        return ((Element) delegateNode).getAttribute(name);
    }

    /**
     * Adds a new attribute. If an attribute with that name is already present
     * in the element, its value is changed to be that of the value parameter.
     * This value is a simple string, it is not parsed as it is being set. So
     * any markup (such as syntax to be recognized as an entity reference) is
     * treated as literal text, and needs to be appropriately escaped by the
     * implementation when it is written out. In order to assign an attribute
     * value that contains entity references, the user must create an
     * <code>Attr</code> node plus any <code>Text</code> and
     * <code>EntityReference</code> nodes, build the appropriate subtree, and
     * use <code>setAttributeNode</code> to assign it as the value of an
     * attribute.
     *
     * @param name  The name of the attribute to create or alter.
     * @param value Value to set in string form.
     * @throws DOMException INVALID_CHARACTER_ERR: Raised if the specified name contains an
     *                      invalid character.
     *                      <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     */
    public void setAttribute(String name, String value) throws DOMException {
        ((Element) delegateNode).setAttribute(name, value);
    }

    /**
     * Removes an attribute by name. If the removed attribute has a default
     * value it is immediately replaced.
     *
     * @param name The name of the attribute to remove.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     */
    public void removeAttribute(String name) throws DOMException {
        ((Element) delegateNode).removeAttribute(name);
    }

    /**
     * Retrieves an <code>Attr</code> node by name.
     *
     * @param name The name of the attribute to retrieve.
     * @return The <code>Attr</code> node with the specified attribute name or
     *         <code>null</code> if there is no such attribute.
     */
    public Attr getAttributeNode(String name) {
        Attr attr = ((Element) delegateNode).getAttributeNode(name);
        return attr;
    }

    /**
     * Adds a new attribute. If an attribute with that name is already present
     * in the element, it is replaced by the new one.
     *
     * @param newAttr The <code>Attr</code> node to add to the attribute list.
     * @return If the <code>newAttr</code> attribute replaces an existing
     *         attribute with the same name, the  previously existing
     *         <code>Attr</code> node is returned, otherwise <code>null</code> is
     *         returned.
     * @throws DOMException WRONG_DOCUMENT_ERR: Raised if <code>newAttr</code> was created from a
     *                      different document than the one that created the element.
     *                      <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>INUSE_ATTRIBUTE_ERR: Raised if <code>newAttr</code> is already an
     *                      attribute of another <code>Element</code> object. The DOM user must
     *                      explicitly clone <code>Attr</code> nodes to re-use them in other
     *                      elements.
     */
    public Attr setAttributeNode(Attr newAttr) throws DOMException {
//	  System.out.println("NYI");
        return ((Element) delegateNode).setAttributeNode(newAttr);
    }

    /**
     * Removes the specified attribute.
     *
     * @param oldAttr The <code>Attr</code> node to remove from the attribute
     *                list. If the removed <code>Attr</code> has a default value it is
     *                immediately replaced.
     * @return The <code>Attr</code> node that was removed.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if <code>oldAttr</code> is not an attribute
     *                      of the element.
     */
    public Attr removeAttributeNode(Attr oldAttr) throws DOMException {
        return ((Element) delegateNode).removeAttributeNode(oldAttr);
    }

    /**
     * Returns a <code>NodeList</code> of all descendant elements with a given
     * tag name, in the order in which they would be encountered in a preorder
     * traversal of the <code>Element</code> tree.
     *
     * @param name The name of the tag to match on. The special value "*"
     *             matches all tags.
     * @return A list of matching <code>Element</code> nodes.
     */
    public NodeList getElementsByTagName(String name) {
        ArrayList<Node> descendentNodes = this.getAllElementDescendants();
        int l = descendentNodes.size();
        ArrayList<Node> elems = new ArrayList<Node>();
        for (int i = 0; i < l; i++) {
            Node node = descendentNodes.get(i);
            if (node.getNodeName().equals(name)) {
                elems.add(node);
            }
        }

        return new PMRNodeListImpl(elems);

    }

    /**
     * Puts all <code>Text</code> nodes in the full depth of the sub-tree
     * underneath this <code>Element</code> into a "normal" form where only
     * markup (for example, tags, comments, processing instructions, CDATA sections,
     * and entity references) separates <code>Text</code> nodes, that is , there
     * are no adjacent <code>Text</code> nodes.  This can be used to ensure
     * that the DOM view of a document is the same as if it were saved and
     * re-loaded, and is useful when operations (such as XPointer lookups) that
     * depend on a particular document tree structure are to be used.
     */
    public void normalize() {
        ((Element) delegateNode).normalize();
    }

    // DOM2

    public void removeAttributeNS(String s, String t) {
        ((Element) delegateNode).removeAttributeNS(s, t);
    }

    public Attr getAttributeNodeNS(String s, String t) {
        Attr attr = ((Element) delegateNode).getAttributeNodeNS(s, t);
        return attr;
    }

    public Attr setAttributeNodeNS(Attr attr) {
        Attr attr1 = ((Element) delegateNode).setAttributeNodeNS(attr);
        return (Attr) attr1;
    }

    public boolean hasAttributeNS(String s, String t) {
        return ((Element) delegateNode).hasAttributeNS(s, t);
    }

    public boolean hasAttribute(String s) {
        return ((Element) delegateNode).hasAttribute(s);
    }

    public void setAttributeNS(String s, String t, String v) {
        ((Element) delegateNode).setAttributeNS(s, t, v);
    }

    public String getAttributeNS(String s, String t) {
        return ((Element) delegateNode).getAttributeNS(s, t);
    }

    ///////////////////////////////////////////////////////////////
    // BEGIN DOM LEVEL 3 METHODS //
    ///////////////////////////////////////////////////////////////

    /**
     * The type information associated with this element.
     *
     * @since DOM Level 3
     */
    public TypeInfo getSchemaTypeInfo() {
        return ((Element) delegateNode).getSchemaTypeInfo();
    }

    /**
     * If the parameter <code>isId</code> is <code>true</code>, this method
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior
     * of <code>Document.getElementById</code>, but does not change any
     * schema that may be in use, in particular this does not affect the
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code>
     * node. Use the value <code>false</code> for the parameter
     * <code>isId</code> to undeclare an attribute for being a
     * user-determined ID attribute.
     * <br> To specify an attribute by local name and namespace URI, use the
     * <code>setIdAttributeNS</code> method.
     *
     * @param name The name of the attribute.
     * @param isId Whether the attribute is a of type ID.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute
     *                      of this element.
     * @since DOM Level 3
     */
    public void setIdAttribute(String name, boolean isId) throws DOMException {
        ((Element) delegateNode).setIdAttribute(name, isId);
    }

    /**
     * If the parameter <code>isId</code> is <code>true</code>, this method
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior
     * of <code>Document.getElementById</code>, but does not change any
     * schema that may be in use, in particular this does not affect the
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code>
     * node. Use the value <code>false</code> for the parameter
     * <code>isId</code> to undeclare an attribute for being a
     * user-determined ID attribute.
     *
     * @param namespaceURI The namespace URI of the attribute.
     * @param localName    The local name of the attribute.
     * @param isId         Whether the attribute is a of type ID.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute
     *                      of this element.
     * @since DOM Level 3
     */
    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId) throws DOMException {
        ((Element) delegateNode).setIdAttributeNS(namespaceURI, localName, isId);
    }

    /**
     * If the parameter <code>isId</code> is <code>true</code>, this method
     * declares the specified attribute to be a user-determined ID attribute
     * . This affects the value of <code>Attr.isId</code> and the behavior
     * of <code>Document.getElementById</code>, but does not change any
     * schema that may be in use, in particular this does not affect the
     * <code>Attr.schemaTypeInfo</code> of the specified <code>Attr</code>
     * node. Use the value <code>false</code> for the parameter
     * <code>isId</code> to undeclare an attribute for being a
     * user-determined ID attribute.
     *
     * @param idAttr The attribute node.
     * @param isId   Whether the attribute is a of type ID.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if the specified node is not an attribute
     *                      of this element.
     * @since DOM Level 3
     */
    public void setIdAttributeNode(Attr idAttr, boolean isId) throws DOMException {
        ((Element) delegateNode).setIdAttributeNode(idAttr, isId);
    }

    /////////////////////////////////////////////////////////////
    // END DOM LEVEL 3 METHODS //
    /////////////////////////////////////////////////////////////

    /**
     * NOT YET IMPLEMENTED
     */
    public NodeList getElementsByTagNameNS(String s, String t) {
//		return new PMRNodeListImpl(((Element) delegateNode).getElementsByTagNameNS(s, t));
        System.out.println("getElementsByTagNameNS not implemented");
        return null;
    }

/*
	public String toString() {
	  String s = "[ELEM: "+this.getNodeName()+"]";
	  NamedNodeMap atts = (NamedNodeMap) this.getAttributes();
	  for (int i = 0; i < atts.getLength(); i++) {
		  Attr attr = (Attr) atts.item(i);
		  s += " "+attr.getNodeName()+"=\""+attr.getNodeValue()+"\"";
	  }
	  return s;
	}
*/
}
