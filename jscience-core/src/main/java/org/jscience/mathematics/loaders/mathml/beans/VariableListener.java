package org.jscience.ml.mathml.beans;

import java.util.EventListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface VariableListener extends EventListener {
    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    void variableChanged(VariableEvent evt);
}
