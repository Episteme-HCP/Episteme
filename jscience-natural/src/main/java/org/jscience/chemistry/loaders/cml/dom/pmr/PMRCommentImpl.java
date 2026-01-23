package org.jscience.chemistry.loaders.cml.dom.pmr;

import org.w3c.dom.Comment;


/**
 * 
 */
public class PMRCommentImpl extends PMRCharacterDataImpl implements Comment {
/**
     * Creates a new PMRCommentImpl object.
     */
    public PMRCommentImpl() {
        super();
    }

/**
     * Creates a new PMRCommentImpl object.
     *
     * @param comment DOCUMENT ME!
     * @param doc     DOCUMENT ME!
     */
    public PMRCommentImpl(Comment comment, PMRDocument doc) {
        super(comment, doc);
    }
}
