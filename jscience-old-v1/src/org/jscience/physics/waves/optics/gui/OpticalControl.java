/**
 * Title:        SlimApp2
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright:    Copyright (c) imt
 * </p>
 *
 * <p>
 * Company:      imt
 * </p>
 *
 * <p></p>
 */
package org.jscience.physics.waves.optics.gui;

import java.awt.*;
import java.awt.event.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class OpticalControl extends Panel implements ItemListener,
    AdjustmentListener, ActionListener {
    /** DOCUMENT ME! */
    private RespondToEvents resp;

/**
     * Creates a new OpticalControl object.
     *
     * @param a DOCUMENT ME!
     */
    public OpticalControl(RespondToEvents a) {
        resp = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void adjustmentValueChanged(AdjustmentEvent e) {
        if (e.getAdjustable() instanceof Scrollbar) {
            resp.scrollbar(((Scrollbar) e.getAdjustable()).getName(),
                e.getValue());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        if (e.getItemSelectable() instanceof Checkbox) {
            resp.checkbox(((Checkbox) e.getItemSelectable()).getName(),
                (e.getStateChange() == ItemEvent.SELECTED));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Button) {
            resp.button(((Button) e.getSource()).getLabel());
        }
    }
}
