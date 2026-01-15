package org.jscience.ml.mathml.beans;

import java.util.EventObject;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public final class VariableEvent extends EventObject {
    /** DOCUMENT ME! */
    private String variable;

    /** DOCUMENT ME! */
    private Object value;

/**
     * Creates a new VariableEvent object.
     *
     * @param src DOCUMENT ME!
     * @param var DOCUMENT ME!
     * @param val DOCUMENT ME!
     */
    public VariableEvent(Object src, String var, Object val) {
        super(src);
        variable = var;
        value = val;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }
}
