package org.jscience.chemistry.gui.extended.beans;

import java.util.EventListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface MolViewerEventListener extends EventListener {
    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     */
    public void centralDisplayChange(MolViewerEvent event);
}
