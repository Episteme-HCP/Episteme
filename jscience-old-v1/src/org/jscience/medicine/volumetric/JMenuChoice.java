/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
