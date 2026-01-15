package org.jscience.chemistry.gui.extended.beans;

import java.util.EventObject;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MolViewerEvent extends EventObject {
    /** DOCUMENT ME! */
    public static final int REPLACE_MOLECULE = 0;

    /** DOCUMENT ME! */
    protected int eventType;

    /** DOCUMENT ME! */
    protected Object param = null;

/**
     * Creates a new MolViewerEvent object.
     *
     * @param source DOCUMENT ME!
     * @param what   DOCUMENT ME!
     * @param param  DOCUMENT ME!
     */
    public MolViewerEvent(Object source, int what, Object param) {
        this(source, what);
        this.param = param;
    }

/**
     * Creates a new MolViewerEvent object.
     *
     * @param source DOCUMENT ME!
     * @param what   DOCUMENT ME!
     */
    public MolViewerEvent(Object source, int what) {
        super(source);
        eventType = what;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return eventType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getParam() {
        return param;
    }
}
