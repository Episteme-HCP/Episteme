package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.ProcessingInstruction;


/**
 * 
 */
public class PMRProcessingInstructionImpl extends PMRNodeImpl
    implements ProcessingInstruction {
/**
     * Creates a new PMRProcessingInstructionImpl object.
     */
    public PMRProcessingInstructionImpl() {
        super();
    }

/**
     * Creates a new PMRProcessingInstructionImpl object.
     *
     * @param processingInstruction DOCUMENT ME!
     * @param doc                   DOCUMENT ME!
     */
    public PMRProcessingInstructionImpl(
        ProcessingInstruction processingInstruction, PMRDocument doc) {
        super(processingInstruction, doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTarget() {
        return ((ProcessingInstruction) delegateNode).getTarget();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getData() {
        return ((ProcessingInstruction) delegateNode).getData();
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     */
    public void setData(String data) {
        ((ProcessingInstruction) delegateNode).setData(data);
    }
}
