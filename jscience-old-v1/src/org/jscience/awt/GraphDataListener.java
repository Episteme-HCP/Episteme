package org.jscience.awt;

import java.util.EventListener;


/**
 * GraphDataListener.
 *
 * @author Mark Hale
 * @version 1.0
 */
public interface GraphDataListener extends EventListener {
    /**
     * Sent when the contents of the model has changed.
     *
     * @param e DOCUMENT ME!
     */
    void dataChanged(GraphDataEvent e);
}
