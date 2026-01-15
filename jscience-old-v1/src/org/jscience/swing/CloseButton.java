/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


//import java.awt.event.KeyEvent;
/**
 * A simple JButton that will call <code>parent.dispose()</code> when the
 * button is pressed. The label of the button reads "Close".
 *
 * @author Holger Antelmann
 */
public class CloseButton extends JButton {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5312761625303410350L;

/**
     * The parent is the Window to be 'closed' by calling
     * <code>dispose()</code> when the button is pressed.
     *
     * @param parent DOCUMENT ME!
     */
    public CloseButton(final Window parent) {
        super(Menus.language.getString("button_close"));
        setMnemonic(((Integer) Menus.language.getObject("button_close key")).intValue());
        addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    //parent.setVisible(false);
                    parent.dispose();
                }
            });
    }
}
