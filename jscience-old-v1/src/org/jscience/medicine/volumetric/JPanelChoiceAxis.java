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
public class JPanelChoiceAxis extends AttrComponent {
    /** DOCUMENT ME! */
    JPanel block = new JPanel();

    /** DOCUMENT ME! */
    JPanel left = new JPanel();

    /** DOCUMENT ME! */
    JPanel right = new JPanel();

    /** DOCUMENT ME! */
    JPanel bottom = new JPanel();

    /** DOCUMENT ME! */
    ButtonGroup bg = new ButtonGroup();

    /** DOCUMENT ME! */
    JRadioButton[] items;

/**
     * Creates a new JPanelChoiceAxis object.
     *
     * @param al    DOCUMENT ME!
     * @param panel DOCUMENT ME!
     * @param attr  DOCUMENT ME!
     */
    JPanelChoiceAxis(ActionListener al, JPanel panel, ChoiceAttr attr) {
        super(attr);
        block.setLayout(new BoxLayout(block, BoxLayout.Y_AXIS));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));

        JLabel label = new JLabel(attr.getLabel());
        block.add(label);
        items = new JRadioButton[attr.valueNames.length];

        for (int i = 0; i < attr.valueNames.length; i++) {
            items[i] = new JRadioButton(attr.valueLabels[i]);
            items[i].setActionCommand(attr.valueNames[i]);
            items[i].setName(attr.getName());
            items[i].addActionListener(al);

            if (i == 0) {
                block.add(items[i]);
            } else if ((i % 2) == 1) {
                left.add(items[i]);
            } else {
                right.add(items[i]);
            }

            bg.add(items[i]);
        }

        bottom.add(left);
        bottom.add(right);
        bottom.setAlignmentX(0.0f);
        block.add(bottom);
        panel.add(block);
        update();
    }

    /**
     * DOCUMENT ME!
     */
    public void update() {
        items[((ChoiceAttr) attr).getValue()].setSelected(true);
    }
}
