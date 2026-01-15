package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.*;

import java.util.ArrayList;

public class PMRNodeImpl implements Node {

    // these two do most of the work and are found in every class
    protected Node delegateNode;    // the node to be extended
    protected PMRDocument pmrDocument;
    protected Node parentNode;

    protected ArrayList<Node> childNodes;
    protected ArrayList<Node> siblingNodes;
    /** delegate implementation */

    /**
     * The <code>Node</code> interface is the primary datatype for the entire
     * Document Object Model. It represents a single node in the document tree.
     * While all objects implementing the <code>Node</code> interface expose
     * methods for dealing with children, not all objects implementing the
     * <code>Node</code> interface may have children. For example,
     * <code>Text</code> nodes may not have children, and adding children to such
     * nodes results in a <code>DOMException</code> being raised.
     * <p>The attributes <code>nodeName</code>, <code>nodeValue</code>  and
     * <code>attributes</code> are  included as a mechanism to get at node
     * information without  casting down to the specific derived interface. In
     * cases where  there is no obvious mapping of these attributes for a specific
     *  <code>nodeType</code> (for example, <code>nodeValue</code> for an Element  or
     * <code>attributes</code>  for a Comment), this returns <code>null</code>.
     * Note that the  specialized interfaces may contain additional and more
     * convenient mechanisms to get and set the relevant information.
     */

    /**
     * called by PMRDocumentImpl; should not be used otherwise
     */
    protected PMRNodeImpl() {
        super();
    }

    public PMRNodeImpl(Node node, PMRDocument pmrDocument) {
        this.delegateNode = node;
        this.pmrDocument = pmrDocument;
    }

    public PMRNodeImpl(PMRNodeImpl node, PMRDocument pmrDocument) {
        this.delegateNode = node.delegateNode;
        this.pmrDocument = pmrDocument;
    }

    /**
     * The name of this node, depending on its type; see the table above.
     */
    public String getNodeName() {
        return delegateNode.getNodeName();
    }

    /**
     * The value of this node, depending on its type; see the table above.
     *
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @throws DOMException DOMSTRING_SIZE_ERR: Raised when it would return more characters than
     *                      fit in a <code>DOMString</code> variable on the implementation
     *                      platform.
     */
    public String getNodeValue() throws DOMException {
        return delegateNode.getNodeValue();
    }

    public void setNodeValue(String nodeValue) throws DOMException {
        delegateNode.setNodeValue(nodeValue);
    }

    /**
     * A code representing the type of the underlying object, as defined above.
     */
    public short getNodeType() {
        return delegateNode.getNodeType();
    }

    /**
     * The parent of this node. All nodes, except <code>Document</code>,
     * <code>DocumentFragment</code>, and <code>Attr</code> may have a parent.
     * However, if a node has just been created and not yet added to the tree,
     * or if it has been removed from the tree, this is <code>null</code>.
     */
    public Node getParentNode() {
        return parentNode;
    }

    /**
     * A <code>NodeList</code> that contains all children of this node. If there
     * are no children, this is a <code>NodeList</code> containing no nodes.
     * The content of the returned <code>NodeList</code> is "live" in the sense
     * that, for instance, changes to the children of the node object that
     * it	was created from are immediately reflected in the nodes returned by
     * the <code>NodeList</code> accessors; it is not a static snapshot of the
     * content of the node. This is true for every <code>NodeList</code>,
     * including the ones returned by the <code>getElementsByTagName</code>
     * method.
     */
    public NodeList getChildNodes() {
        return new PMRNodeListImpl(childNodes);
    }

    /**
     * The first child of this node. If there is no such node, this returns
     * <code>null</code>.
     */
    public Node getFirstChild() {
        return (childNodes == null || childNodes.size() == 0) ? null : childNodes.get(0);
    }

    /**
     * The last child of this node. If there is no such node, this returns
     * <code>null</code>.
     */
    public Node getLastChild() {
        return (childNodes == null || childNodes.size() == 0) ? null : childNodes.get(childNodes.size() - 1);
    }

    /**
     * The node immediately preceding this node. If there is no such node, this
     * returns <code>null</code>.
     */
    public Node getPreviousSibling() {
        int pos = getSiblingPosition(this);
        return (pos <= 0) ? null : siblingNodes.get(pos - 1);
    }

    /**
     * The node immediately following this node. If there is no such node, this
     * returns <code>null</code>.
     */
    public Node getNextSibling() {
        int pos = getSiblingPosition(this);
        return (pos < 0 || (pos + 1) >= siblingNodes.size()) ? null : siblingNodes.get(pos + 1);
    }

    /**
     * A <code>NamedNodeMap</code> containing the attributes of this node (if it
     * is an <code>Element</code>) or <code>null</code> otherwise.
     */
    public NamedNodeMap getAttributes() {
        NamedNodeMap nnm = delegateNode.getAttributes();
        return nnm;
    }

    /**
     * The <code>Document</code> object associated with this node. This is also
     * the <code>Document</code> object used to create new nodes. When this
     * node is a <code>Document</code> this is <code>null</code>.
     */
    public Document getOwnerDocument() {
        return pmrDocument;
    }

    /**
     * Inserts the node <code>newChild</code> before the existing child node
     * <code>refChild</code>. If <code>refChild</code> is <code>null</code>,
     * insert <code>newChild</code> at the end of the list of children.
     * <br>If <code>newChild</code> is a <code>DocumentFragment</code> object,
     * all of its children are inserted, in the same order, before
     * <code>refChild</code>. If the <code>newChild</code> is already in the
     * tree, it is first removed.
     *
     * @param newChild The node to insert.
     * @param refChild The reference node, that is , the node before which the new
     *                 node must be inserted.
     * @return The node being inserted.
     * @throws DOMException HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not
     *                      allow children of the type of the <code>newChild</code> node, or if
     *                      the node to insert is one of this node's ancestors.
     *                      <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created
     *                      from a different document than the one that created this node.
     *                      <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if <code>refChild</code> is not a child of
     *                      this node.
     */
    public Node insertBefore(Node newChild, Node refChild) throws DOMException {
        if (newChild == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "insertBefore: Cannot add null newChild");
        }
        if (refChild == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "insertBefore: Cannot insertBefore null refChild");
        }
        Node newChildDelegateNode = ((PMRNodeImpl) newChild).delegateNode;
        if (newChildDelegateNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "insertBefore: Null delegate for new child");
        }
        Node refChildDelegateNode = ((PMRNodeImpl) refChild).delegateNode;
        if (refChildDelegateNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "insertBefore: Null delegate for ref child");
        }
        if (refChildDelegateNode.getParentNode() == null) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "insertBefore: Null refChildDelegate Parent");
        }
        if (refChildDelegateNode.getParentNode() != delegateNode) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "appendChild: refChildDelegate Parent != delegateNode");
        }
// delegate action
        Node node = delegateNode.insertBefore(newChildDelegateNode, refChildDelegateNode);
// parent
        ((PMRNodeImpl) newChild).parentNode = this;

// is refChild in children?
        int pos = getSiblingPosition(refChild);
        if (pos >= 0) {
            siblingNodes.add(pos, newChild);
        } else {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "BUG: cannot find position of refChild");
        }

        return newChild;
    }

    /**
     * Replaces the child node <code>oldChild</code> with <code>newChild</code>
     * in the list of children, and returns the <code>oldChild</code> node. If
     * the <code>newChild</code> is already in the tree, it is first removed.
     *
     * @param newChild The new node to put in the child list.
     * @param oldChild The node being replaced in the list.
     * @return The node replaced.
     * @throws DOMException HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not
     *                      allow children of the type of the <code>newChild</code> node, or it
     *                      the node to put in is one of this node's ancestors.
     *                      <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created
     *                      from a different document than the one that created this node.
     *                      <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of
     *                      this node.
     */
    public Node replaceChild(Node newChild, Node oldChild) throws DOMException {
        if (newChild == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "replaceChild: Cannot add null newChild");
        }
        if (oldChild == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "replaceChild: Cannot replaceChild null oldChild");
        }
        Node newChildDelegateNode = ((PMRNodeImpl) newChild).delegateNode;
        if (newChildDelegateNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "replaceChild: Null delegate for new child");
        }
        Node oldChildDelegateNode = ((PMRNodeImpl) oldChild).delegateNode;
        if (oldChildDelegateNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "replaceChild: Null delegate for old child");
        }
        if (oldChildDelegateNode.getParentNode() == null) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "replaceChild: Null oldChildDelegate Parent");
        }
        if (oldChildDelegateNode.getParentNode() != delegateNode) {
            new Exception().printStackTrace();
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "replaceChild: oldChildDelegate Parent != delegateNode");
        }

// delegate
        Node node = delegateNode.replaceChild(newChildDelegateNode, oldChildDelegateNode);
// parents
        ((PMRNodeImpl) newChild).parentNode = this;
        ((PMRNodeImpl) oldChild).parentNode = null;
// childNodes
        int pos = childNodes.indexOf(oldChild);
        if (pos >= 0) {
            childNodes.set(pos, newChild);
        } else {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "BUG: cannot find position of oldChild");
        }
        return oldChild;
    }

    /**
     * Removes the child node indicated by <code>oldChild</code> from the list
     * of children, and returns it.
     *
     * @param oldChild The node being removed.
     * @return The node removed.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     *                      <br>NOT_FOUND_ERR: Raised if <code>oldChild</code> is not a child of
     *                      this node.
     */
    public Node removeChild(Node oldChild) throws DOMException {
        if (oldChild == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "removeChild: Cannot removeChild null oldChild");
        }
        Node oldChildDelegateNode = ((PMRNodeImpl) oldChild).delegateNode;
        if (oldChildDelegateNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "removeChild: Null delegate for old child");
        }
        if (oldChildDelegateNode.getParentNode() == null) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeChild: Null oldChildDelegate Parent");
        }
        if (oldChildDelegateNode.getParentNode() != delegateNode) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR, "removeChild: oldChildDelegate Parent != delegateNode");
        }

// delegate action
        Node node = delegateNode.removeChild(oldChildDelegateNode);
// parent
        ((PMRNodeImpl) oldChild).parentNode = null;
// childNodes
        int pos = childNodes.indexOf(oldChild);
        if (pos >= 0) {
            childNodes.remove(pos);
        } else {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "BUG: cannot find position of oldChild");
        }

        return oldChild;
    }

    /**
     * Adds the node <code>newChild</code> to the end of the list of children of
     * this node. If the <code>newChild</code> is already in the tree, it is
     * first removed.
     *
     * @param newChild The node to add.If it is a  <code>DocumentFragment</code>
     *                 object, the entire contents of the document fragment are moved into
     *                 the child list of this node
     * @return The node added.
     * @throws DOMException HIERARCHY_REQUEST_ERR: Raised if this node is of a type that does not
     *                      allow children of the type of the <code>newChild</code> node, or if
     *                      the node to append is one of this node's ancestors.
     *                      <br>WRONG_DOCUMENT_ERR: Raised if <code>newChild</code> was created
     *                      from a different document than the one that created this node.
     *                      <br>NO_MODIFICATION_ALLOWED_ERR: Raised if this node is readonly.
     */
    public Node appendChild(Node newChild) throws DOMException {
        if (newChild == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "appendChild: Cannot appendChild null newChild");
        }
        Node newChildDelegateNode = ((PMRNodeImpl) newChild).delegateNode;
        if (newChildDelegateNode == null) {
            throw new DOMException(DOMException.NOT_SUPPORTED_ERR, "appendChild: Null delegate for new child");
        }
// documentFragment is special
        if (newChild instanceof DocumentFragment) {
            int nChild = childNodes.size();
            for (int i = 0; i < nChild; i++) {

                appendChild(childNodes.get(i));
            }
        } else {
// delegate
            Node node = delegateNode.appendChild(newChildDelegateNode);
// parent
            ((PMRNodeImpl) newChild).parentNode = this;
// childNodes
            if (childNodes == null) {
                childNodes = new ArrayList<Node>();
            }
            childNodes.add(newChild);
        }

        return newChild;
    }

    /**
     * This is a convenience method to allow easy determination of whether a
     * node has any children.
     *
     * @return <code>true</code> if the node has any children,
     *         <code>false</code> if the node has no children.
     */
    public boolean hasChildNodes() {
        return delegateNode.hasChildNodes();
    }

    /**
     * Returns a duplicate of this node, that is , serves as a generic copy
     * constructor for nodes. The duplicate node has no parent (
     * <code>parentNode</code> returns <code>null</code>.).
     * <br>Cloning an <code>Element</code> copies all attributes and their
     * values, including those generated by the  XML processor to represent
     * defaulted attributes, but this method does not copy any text it contains
     * unless it is a deep clone, since the text is contained in a child
     * <code>Text</code> node. Cloning any other type of node simply returns a
     * copy of this node.
     *
     * @param deep If <code>true</code>, recursively clone the subtree under the
     *             specified node; if <code>false</code>, clone only the node itself (and
     *             its attributes, if it is an <code>Element</code>).
     * @return The duplicate node.
     */
    public Node cloneNode(boolean deep) {
        PMRNodeImpl newNode = null;

        if (this instanceof Element) {
            newNode = new PMRElementImpl((Element) delegateNode, (PMRDocument) this.getOwnerDocument());
        } else if (this instanceof Text) {
            newNode = new PMRTextImpl((Text) delegateNode, (PMRDocument) this.getOwnerDocument());
        } else if (this instanceof Document) {
            newNode = new PMRDocumentImpl((Document) delegateNode);
        } else if (this instanceof Comment) {
            newNode = new PMRCommentImpl((Comment) delegateNode, (PMRDocument) this.getOwnerDocument());
        } else if (this instanceof ProcessingInstruction) {
            newNode = new PMRProcessingInstructionImpl((ProcessingInstruction) delegateNode, (PMRDocument) this.getOwnerDocument());
        } else {
            System.err.println("NYI: clone does not support this class");
        }

        Node newDelegateNode = delegateNode.cloneNode(false);
        newNode.setDelegateNode(newDelegateNode);

        if (!(this instanceof Document)) {
            newNode.setOwnerDocument((PMRDocument) this.getOwnerDocument());
        }

        if (deep == true) {
            NodeList childNodes = this.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node newChild = childNodes.item(i).cloneNode(true);
                newNode.appendChild(newChild);

                Node childDelegate = ((PMRNodeImpl) newChild).getDelegateNode();
                newDelegateNode.appendChild(childDelegate);
            }
        }

        return newNode;
    }

    /**
     * Tests whether the DOM implementation implements a specific feature and that
     * feature is supported by this node.
     */
    public boolean isSupported(java.lang.String feature, java.lang.String version) {
        return delegateNode.isSupported(feature, version);
    }

    /**
     * Returns whether this node (if it is an element) has any attributes.
     */
    public boolean hasAttributes() {
        return delegateNode.hasAttributes();
    }

    // DOM2
    public void normalize() {
        delegateNode.normalize();
        normalize(childNodes);
    }

    private void normalize(ArrayList<Node> childNodes) {
        int l = childNodes.size();
        ArrayList<Node> newChildren = new ArrayList<Node>();
        boolean start = true;
        for (int i = 0; i < l; i++) {
            Node node = childNodes.get(i);
            if (node instanceof Text) {
                if (start) {
                    start = false;
                    newChildren.add(node);
                } else {
                    ((PMRNodeImpl) node).parentNode = null;
                }
            } else {
                if (node instanceof Element) {
                    normalize(((PMRNodeImpl) node).childNodes);
                }
                start = true;
                newChildren.add(node);
            }
            childNodes = newChildren;
        }
    }

//  public boolean supports(String s, String t) {return delegateNode.supports(s, t);}

    public void setPrefix(String prefix) {
        delegateNode.setPrefix(prefix);
    }

    public String getNamespaceURI() {
        return delegateNode.getNamespaceURI();
    }

    public String getPrefix() {
        return getPrefix();
    }

    public String getLocalName() {
        return delegateNode.getLocalName();
    }

    ///////////////////////////////////////////////////////////////
    // BEGIN DOM LEVEL 3 METHODS //
    ///////////////////////////////////////////////////////////////

    /**
     * Compares the reference node, i.e. the node on which this method is
     * being called, with a node, i.e. the one passed as a parameter, with
     * regard to their position in the document and according to the
     * document order.
     *
     * @param other The node to compare against the reference node.
     * @return Returns how the node is positioned relatively to the reference
     *         node.
     * @throws DOMException NOT_SUPPORTED_ERR: when the compared nodes are from different DOM
     *                      implementations that do not coordinate to return consistent
     *                      implementation-specific results.
     * @since DOM Level 3
     */
    public short compareDocumentPosition(Node other) throws DOMException {
        return (delegateNode.compareDocumentPosition(((PMRNodeImpl) other).delegateNode));
    }

    /**
     * Associate an object to a key on this node. The object can later be
     * retrieved from this node by calling <code>getUserData</code> with the
     * same key.
     *
     * @param key     The key to associate the object to.
     * @param data    The object to associate to the given key, or
     *                <code>null</code> to remove any existing association to that key.
     * @param handler The handler to associate to that key, or
     *                <code>null</code>.
     * @return Returns the <code>DOMUserData</code> previously associated to
     *         the given key on this node, or <code>null</code> if there was none.
     * @since DOM Level 3
     */
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return delegateNode.setUserData(key, data, handler);
    }

    /**
     * Retrieves the object associated to a key on a this node. The object
     * must first have been set to this node by calling
     * <code>setUserData</code> with the same key.
     *
     * @param key The key the object is associated to.
     * @return Returns the <code>DOMUserData</code> associated to the given
     *         key on this node, or <code>null</code> if there was none.
     * @since DOM Level 3
     */
    public Object getUserData(String key) {
        return delegateNode.getUserData(key);
    }

    /**
     * Tests whether two nodes are equal.
     * <br>This method tests for equality of nodes, not sameness (i.e.,
     * whether the two nodes are references to the same object) which can be
     * tested with <code>Node.isSameNode()</code>. All nodes that are the
     * same will also be equal, though the reverse may not be true.
     * <br>Two nodes are equal if and only if the following conditions are
     * satisfied:
     * <ul>
     * <li>The two nodes are of the same type.
     * </li>
     * <li>The following string
     * attributes are equal: <code>nodeName</code>, <code>localName</code>,
     * <code>namespaceURI</code>, <code>prefix</code>, <code>nodeValue</code>
     * . This is: they are both <code>null</code>, or they have the same
     * length and are character for character identical.
     * </li>
     * <li>The
     * <code>attributes</code> <code>NamedNodeMaps</code> are equal. This
     * is: they are both <code>null</code>, or they have the same length and
     * for each node that exists in one map there is a node that exists in
     * the other map and is equal, although not necessarily at the same
     * index.
     * </li>
     * <li>The <code>childNodes</code> <code>NodeLists</code> are equal.
     * This is: they are both <code>null</code>, or they have the same
     * length and contain equal nodes at the same index. Note that
     * normalization can affect equality; to avoid this, nodes should be
     * normalized before being compared.
     * </li>
     * </ul>
     * <br>For two <code>DocumentType</code> nodes to be equal, the following
     * conditions must also be satisfied:
     * <ul>
     * <li>The following string attributes
     * are equal: <code>publicId</code>, <code>systemId</code>,
     * <code>internalSubset</code>.
     * </li>
     * <li>The <code>entities</code>
     * <code>NamedNodeMaps</code> are equal.
     * </li>
     * <li>The <code>notations</code>
     * <code>NamedNodeMaps</code> are equal.
     * </li>
     * </ul>
     * <br>On the other hand, the following do not affect equality: the
     * <code>ownerDocument</code>, <code>baseURI</code>, and
     * <code>parentNode</code> attributes, the <code>specified</code>
     * attribute for <code>Attr</code> nodes, the <code>schemaTypeInfo</code>
     * attribute for <code>Attr</code> and <code>Element</code> nodes, the
     * <code>Text.isElementContentWhitespace</code> attribute for
     * <code>Text</code> nodes, as well as any user data or event listeners
     * registered on the nodes.
     * <p ><b>Note:</b>  As a general rule, anything not mentioned in the
     * description above is not significant in consideration of equality
     * checking. Note that future versions of this specification may take
     * into account more attributes and implementations conform to this
     * specification are expected to be updated accordingly.
     *
     * @param arg The node to compare equality with.
     * @return Returns <code>true</code> if the nodes are equal,
     *         <code>false</code> otherwise.
     * @since DOM Level 3
     */
    public boolean isEqualNode(Node arg) {
        return delegateNode.isEqualNode(((PMRNodeImpl) arg).getDelegateNode());
    }

    /**
     * Returns whether this node is the same node as the given one.
     * <br>This method provides a way to determine whether two
     * <code>Node</code> references returned by the implementation reference
     * the same object. When two <code>Node</code> references are references
     * to the same object, even if through a proxy, the references may be
     * used completely interchangeably, such that all attributes have the
     * same values and calling the same DOM method on either reference
     * always has exactly the same effect.
     *
     * @param other The node to test against.
     * @return Returns <code>true</code> if the nodes are the same,
     *         <code>false</code> otherwise.
     * @since DOM Level 3
     */
    public boolean isSameNode(Node other) {
        return delegateNode.isSameNode(((PMRNodeImpl) other).getDelegateNode());
    }

    /**
     * This attribute returns the text content of this node and its
     * descendants. When it is defined to be <code>null</code>, setting it
     * has no effect. On setting, any possible children this node may have
     * are removed and, if it the new string is not empty or
     * <code>null</code>, replaced by a single <code>Text</code> node
     * containing the string this attribute is set to.
     * <br> On getting, no serialization is performed, the returned string
     * does not contain any markup. No whitespace normalization is performed
     * and the returned string does not contain the white spaces in element
     * content (see the attribute
     * <code>Text.isElementContentWhitespace</code>). Similarly, on setting,
     * no parsing is performed either, the input string is taken as pure
     * textual content.
     * <br>The string returned is made of the text content of this node
     * depending on its type, as defined below:
     * <table border='1' cellpadding='3'>
     * <tr>
     * <th>Node type</th>
     * <th>Content</th>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>
     * ELEMENT_NODE, ATTRIBUTE_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE,
     * DOCUMENT_FRAGMENT_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>concatenation of the <code>textContent</code>
     * attribute value of every child node, excluding COMMENT_NODE and
     * PROCESSING_INSTRUCTION_NODE nodes. This is the empty string if the
     * node has no children.</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE,
     * PROCESSING_INSTRUCTION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'><code>nodeValue</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>DOCUMENT_NODE,
     * DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'><em>null</em></td>
     * </tr>
     * </table>
     *
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised when the node is readonly.
     * @since DOM Level 3
     */
    public void setTextContent(String textContent) throws DOMException {
        delegateNode.setTextContent(textContent);
    }

    /**
     * This attribute returns the text content of this node and its
     * descendants. When it is defined to be <code>null</code>, setting it
     * has no effect. On setting, any possible children this node may have
     * are removed and, if it the new string is not empty or
     * <code>null</code>, replaced by a single <code>Text</code> node
     * containing the string this attribute is set to.
     * <br> On getting, no serialization is performed, the returned string
     * does not contain any markup. No whitespace normalization is performed
     * and the returned string does not contain the white spaces in element
     * content (see the attribute
     * <code>Text.isElementContentWhitespace</code>). Similarly, on setting,
     * no parsing is performed either, the input string is taken as pure
     * textual content.
     * <br>The string returned is made of the text content of this node
     * depending on its type, as defined below:
     * <table border='1' cellpadding='3'>
     * <tr>
     * <th>Node type</th>
     * <th>Content</th>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>
     * ELEMENT_NODE, ATTRIBUTE_NODE, ENTITY_NODE, ENTITY_REFERENCE_NODE,
     * DOCUMENT_FRAGMENT_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'>concatenation of the <code>textContent</code>
     * attribute value of every child node, excluding COMMENT_NODE and
     * PROCESSING_INSTRUCTION_NODE nodes. This is the empty string if the
     * node has no children.</td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>TEXT_NODE, CDATA_SECTION_NODE, COMMENT_NODE,
     * PROCESSING_INSTRUCTION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'><code>nodeValue</code></td>
     * </tr>
     * <tr>
     * <td valign='top' rowspan='1' colspan='1'>DOCUMENT_NODE,
     * DOCUMENT_TYPE_NODE, NOTATION_NODE</td>
     * <td valign='top' rowspan='1' colspan='1'><em>null</em></td>
     * </tr>
     * </table>
     *
     * @throws DOMException DOMSTRING_SIZE_ERR: Raised when it would return more characters than
     *                      fit in a <code>DOMString</code> variable on the implementation
     *                      platform.
     * @since DOM Level 3
     */
    public String getTextContent() throws DOMException {
        return delegateNode.getTextContent();
    }

    /**
     * The absolute base URI of this node or <code>null</code> if the
     * implementation wasn't able to obtain an absolute URI. This value is
     * computed as described in . However, when the <code>Document</code>
     * supports the feature "HTML" [<a href='http://www.w3.org/TR/2003/REC-DOM-Level-2-HTML-20030109'>DOM Level 2 HTML</a>]
     * , the base URI is computed using first the value of the href
     * attribute of the HTML BASE element if any, and the value of the
     * <code>documentURI</code> attribute from the <code>Document</code>
     * interface otherwise.
     *
     * @since DOM Level 3
     */
    public String getBaseURI() {
        return delegateNode.getBaseURI();
    }

    /**
     * Look up the prefix associated to the given namespace URI, starting from
     * this node. The default namespace declarations are ignored by this
     * method.
     * <br>See  for details on the algorithm used by this method.
     *
     * @param nsUri The namespace URI to look for.
     * @return Returns an associated namespace prefix if found or
     *         <code>null</code> if none is found. If more than one prefix are
     *         associated to the namespace prefix, the returned namespace prefix
     *         is implementation dependent.
     * @since DOM Level 3
     */
    public String lookupPrefix(String nsUri) {
        return delegateNode.lookupPrefix(nsUri);
    }

    /**
     * Look up the namespace URI associated to the given prefix, starting from
     * this node.
     * <br>See  for details on the algorithm used by this method.
     *
     * @param pre The prefix to look for. If this parameter is
     *            <code>null</code>, the method will return the default namespace URI
     *            if any.
     * @return Returns the associated namespace URI or <code>null</code> if
     *         none is found.
     * @since DOM Level 3
     */
    public String lookupNamespaceURI(String pre) {
        return delegateNode.lookupNamespaceURI(pre);
    }

    /**
     * This method returns a specialized object which implements the
     * specialized APIs of the specified feature and version, as specified
     * in . The specialized object may also be obtained by using
     * binding-specific casting methods but is not necessarily expected to,
     * as discussed in . This method also allow the implementation to
     * provide specialized objects which do not support the <code>Node</code>
     * interface.
     *
     * @param feature The name of the feature requested. Note that any plus
     *                sign "+" prepended to the name of the feature will be ignored since
     *                it is not significant in the context of this method.
     * @param version This is the version number of the feature to test.
     * @return Returns an object which implements the specialized APIs of
     *         the specified feature and version, if any, or <code>null</code> if
     *         there is no object which implements interfaces associated with that
     *         feature. If the <code>DOMObject</code> returned by this method
     *         implements the <code>Node</code> interface, it must delegate to the
     *         primary core <code>Node</code> and not return results inconsistent
     *         with the primary core <code>Node</code> such as attributes,
     *         childNodes, etc.
     * @since DOM Level 3
     */
    public Object getFeature(String feature, String version) {
        return delegateNode.getFeature(feature, version);
    }

    /**
     * This method checks if the specified <code>namespaceURI</code> is the
     * default namespace or not.
     *
     * @param namespaceURI The namespace URI to look for.
     * @return Returns <code>true</code> if the specified
     *         <code>namespaceURI</code> is the default namespace,
     *         <code>false</code> otherwise.
     * @since DOM Level 3
     */
    public boolean isDefaultNamespace(String namespaceURI) {
        return delegateNode.isDefaultNamespace(namespaceURI);
    }

    /////////////////////////////////////////////////////////////
    // END DOM LEVEL 3 METHODS //
    /////////////////////////////////////////////////////////////


    public String toString() {
        String s = "PMRNode: " + this.getClass() + "/" + this.hashCode();
        s += "[DelNode: " + delegateNode.getNodeName() + "=" + delegateNode.getNodeValue() + "]";
        s += "PMRDoc: ";
        s += ((pmrDocument == null) ? "NULL" : "" + pmrDocument.hashCode());
        return s;
    }

    /**
     * for use by subclasses to remove PMRDocument hierarchy
     */
    protected void setOwnerDocument(PMRDocument doc) {
        this.pmrDocument = doc;
    }

    protected void setOwnerDocumentRecursively(PMRDocument doc) {
        this.setOwnerDocument(doc);
        for (int i = 0; i < this.getChildNodes().getLength(); i++) {
            ((PMRNodeImpl) this.getChildNodes().item(i)).setOwnerDocumentRecursively(doc);
        }
    }

    /**
     * for use by subclasses to remove delegateNode hierarchy
     */
    protected void setDelegateNode(Node delegateNode) {
        this.delegateNode = delegateNode;
    }

    protected void wrapRecursively(Node delegateNode) {
        this.setDelegateNode(delegateNode);
        NodeList thisChildren = this.getChildNodes();
        NodeList delegateChildren = delegateNode.getChildNodes();

        for (int i = 0; i < thisChildren.getLength(); i++) {
            ((PMRNodeImpl) thisChildren.item(i)).wrapRecursively(delegateChildren.item(i));
        }
    }

    // ======================================================

    /**
     * get siblingNodes.
     * empty vector if none
     */
    ArrayList<Node> getSiblingNodes() {
        siblingNodes = (parentNode == null) ? null : ((PMRNodeImpl) parentNode).childNodes;
        return siblingNodes;
    }

    // if not found returns -1
    int getSiblingPosition(Node node) {
        getSiblingNodes();
        return (siblingNodes == null) ? -1 : siblingNodes.indexOf(this);
    }

    // for debugging only
    public Node getDelegateParentNode() {
        return (delegateNode == null) ? null : delegateNode.getParentNode();
    }

    public Node getDelegateNode() {
        return delegateNode;
    }

    ArrayList<Node> getAllElementDescendants() {
        ArrayList<Node> descend = new ArrayList<Node>();
        if (childNodes != null) {
            int l = childNodes.size();
            for (int i = 0; i < l; i++) {
                getAllElementDescendants(descend, childNodes.get(i));
            }
        }
        return descend;
    }

    void getAllElementDescendants(ArrayList<Node> descend, Node node) {
        if (node instanceof Element) {
            descend.add(node);
            ArrayList<Node> childNodes = ((PMRNodeImpl) node).childNodes;
            if (childNodes != null) {
                int l = childNodes.size();
                for (int i = 0; i < l; i++) {
                    getAllElementDescendants(descend, childNodes.get(i));
                }
            }
        }
    }
}
