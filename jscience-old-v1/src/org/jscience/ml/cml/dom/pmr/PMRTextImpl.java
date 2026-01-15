package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;


/**
 * DOCUMENT ME!
 *
 * @author Administrator
 * @author 19 August 2003
 */
public class PMRTextImpl extends PMRCharacterDataImpl implements Text {
    /**
     * Constructor for the PMRTextImpl object.
     */
    public PMRTextImpl() {
        super();
    }

    /**
     * Constructor for the PMRTextImpl object.
     *
     * @param text Description of the Parameter
     * @param doc  DOCUMENT ME!
     */
    public PMRTextImpl(Text text, PMRDocument doc) {
        super(text, doc);
    }

    /**
     * Breaks this node into two nodes at the specified <code>offset</code>,
     * keeping both in the tree as siblings. After being split, this node will
     * contain all the content up to the <code>offset</code> point. A new node
     * of the same type, which contains all the content at and after the
     * <code>offset</code> point, is returned. If the original node had a
     * parent node, the new node is inserted as the next sibling of the
     * original node. When the <code>offset</code> is equal to the length of
     * this node, the new node has no data.
     *
     * @param i The 16-bit unit offset at which to split, starting from
     *          <code>0</code>.
     * @return The new node, of the same type as this node.
     */
    public Text splitText(int i) {
        Text text = ((Text) delegateNode).splitText(i);

        return new PMRTextImpl(text, pmrDocument);
    }

    /**
     * Returns all text of <code>Text</code> nodes logically-adjacent text
     * nodes to this node, concatenated in document order. <br>
     * For instance, in the example below <code>wholeText</code> on the
     * <code>Text</code> node that contains "bar" returns "barfoo", while on
     * the <code>Text</code> node that contains "foo" it returns "barfoo".
     *
     * @return DOCUMENT ME!
     * @since DOM Level 3
     */
    public String getWholeText() {
        return ((Text) delegateNode).getWholeText();
    }

    /**
     * Returns whether this text node contains <a
     * href='http://www.w3.org/TR/2004/REC-xml-infoset-20040204#infoitem.character'>
     * element content whitespace</a>, often abusively called "ignorable
     * whitespace". The text node is determined to contain whitespace in
     * element content during the load of the document or if validation occurs
     * while using <code>Document.normalizeDocument()</code>.
     *
     * @return DOCUMENT ME!
     * @since DOM Level 3
     */
    public boolean isElementContentWhitespace() {
        return ((Text) delegateNode).isElementContentWhitespace();
    }

    /**
     * Replaces the text of the current node and all logically-adjacent text
     * nodes with the specified text. All logically-adjacent text nodes are
     * removed including the current node unless it was the recipient of the
     * replacement text. <br>
     * This method returns the node which received the replacement text. The
     * returned node is:
     * <p/>
     * <ul>
     * <li>
     * <code>null</code>, when the replacement text is the empty string;
     * </li>
     * <li>
     * the current node, except when the current node is read-only;
     * </li>
     * <li>
     * a new <code>Text</code> node of the same type ( <code>Text</code> or
     * <code>CDATASection</code>) as the current node inserted at the location
     * of the replacement.
     * </li>
     * </ul>
     * <p/>
     * <br>For instance, in the above example calling
     * <code>replaceWholeText</code> on the <code>Text</code> node that
     * contains "bar" with "yo" in argument results in the following: <br>
     * Where the nodes to be removed are read-only descendants of an
     * <code>EntityReference</code>, the <code>EntityReference</code> must be
     * removed instead of the read-only nodes. If any
     * <code>EntityReference</code> to be removed has descendants that are not
     * <code>EntityReference</code>, <code>Text</code>, or
     * <code>CDATASection</code> nodes, the <code>replaceWholeText</code>
     * method must fail before performing any modification of the document,
     * raising a <code>DOMException</code> with the code
     * <code>NO_MODIFICATION_ALLOWED_ERR</code>. <br>
     * For instance, in the example below calling <code>replaceWholeText</code>
     * on the <code>Text</code> node that contains "bar" fails, because the
     * <code>EntityReference</code> node "ent" contains an
     * <code>Element</code> node which cannot be removed.
     *
     * @param content The content of the replacing <code>Text</code> node.
     * @return The <code>Text</code> node created with the specified content.
     * @throws DOMException NO_MODIFICATION_ALLOWED_ERR: Raised if one of the
     *                      <code>Text</code> nodes being replaced is readonly.
     * @since DOM Level 3
     */
    public Text replaceWholeText(String content) throws DOMException {
        return ((Text) delegateNode).replaceWholeText(content);
    }
}
