/*
 *        @(#)JMenuChoice.java 1.4 99/09/15 13:44:12
 *
 * Copyright (c) 1996-1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.jscience.medicine.volumetric;

import java.awt.event.ActionListener;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class JMenuChoice extends AttrComponent {
    /** DOCUMENT ME! */
    ButtonGroup bg = new ButtonGroup();

    /** DOCUMENT ME! */
    JRadioButtonMenuItem[] items;

/**
     * Creates a new JMenuChoice object.
     *
     * @param al   DOCUMENT ME!
     * @param menu DOCUMENT ME!
     * @param attr DOCUMENT ME!
     */
    JMenuChoice(ActionListener al, JMenu menu, ChoiceAttr attr) {
        super(attr);
        items = new JRadioButtonMenuItem[attr.valueLabels.length];

        for (int i = 0; i < attr.valueLabels.length; i++) {
            items[i] = new JRadioButtonMenuItem(attr.valueLabels[i]);
            items[i].setName(attr.getName());
            items[i].setActionCommand(attr.valueNames[i]);
            items[i].addActionListener(al);
            menu.add(items[i]);
            bg.add(items[i]);
        }

        update();
    }

    /**
     * DOCUMENT ME!
     */
    public void update() {
        items[((ChoiceAttr) attr).getValue()].setSelected(true);
    }
}
