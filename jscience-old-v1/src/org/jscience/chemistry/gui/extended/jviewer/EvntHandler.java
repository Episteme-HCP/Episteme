package org.jscience.chemistry.gui.extended.jviewer;

import java.awt.*;

import java.util.Vector;


/**
 * An implementation of a JViewer's event handler interface which allows
 * the contents of one or more JViewers to be manipulated in response to mouse
 * movements.
 *
 * @author Mike Brusati (brusati
 *
 * @see IEvntHandler
 */
public class EvntHandler implements IEvntHandler {
    /** DOCUMENT ME! */
    protected Vector viewers = new Vector();

    /** DOCUMENT ME! */
    protected int prevX = 0;

    /** DOCUMENT ME! */
    protected int prevY = 0;

/**
     * Default constructor.
     */
    public EvntHandler() {
    }

/**
     * Convenience constructor.  Useful for applications requiring one JViewer.
     *
     * @param v a JViewer whose contents will be manipulated by this event
     *          handler.
     */
    public EvntHandler(JViewer v) {
        addJViewer(v);
    }

    /**
     * Add another JViewer to the list of JViewers manipulated by this
     * event handler.
     *
     * @param v a JViewer whose contents will be manipulated by this event
     *        handler.
     */
    public void addJViewer(JViewer v) {
        viewers.addElement(v);
    }

    /**
     * The event handler for all known JViewers.
     *
     * @param e a mouse event
     *
     * @return DOCUMENT ME!
     */
    public boolean handleEvnt(Event e) {
        switch (e.id) {
        case Event.MOUSE_DOWN:
            prevX = e.x;
            prevY = e.y;

            return true;

        case Event.MOUSE_UP:
            return true;

        case Event.MOUSE_DRAG:

            JViewer v;
            int dx = prevX - e.x;
            int dy = prevY - e.y;

            for (int i = 0; i < viewers.size(); i++) {
                v = (JViewer) viewers.elementAt(i);

                if (v.transform(dx, dy)) {
                    v.repaint();
                }
            }

            prevX = e.x;
            prevY = e.y;

            return true;
        }

        return false;
    }
}
