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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class AnnotationsEditor extends JPanel implements ItemListener,
    ActionListener {
    /** DOCUMENT ME! */
    VolRend volRend;

/**
     * Creates a new AnnotationsEditor object.
     *
     * @param volRend DOCUMENT ME!
     */
    AnnotationsEditor(VolRend volRend) {
        this.volRend = volRend;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

        boxPanel.add(new JLabel("Outline Boxes"));

        AttrComponent px = new JPanelToggle(this, boxPanel, volRend.plusXBoxAttr);
        AttrComponent py = new JPanelToggle(this, boxPanel, volRend.plusYBoxAttr);
        AttrComponent pz = new JPanelToggle(this, boxPanel, volRend.plusZBoxAttr);
        AttrComponent mx = new JPanelToggle(this, boxPanel,
                volRend.minusXBoxAttr);
        AttrComponent my = new JPanelToggle(this, boxPanel,
                volRend.minusYBoxAttr);
        AttrComponent mz = new JPanelToggle(this, boxPanel,
                volRend.minusZBoxAttr);
        add(boxPanel);

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        imagePanel.add(new JLabel("Face Images"));

        AttrComponent pxi = new JPanelString(this, imagePanel,
                volRend.plusXImageAttr);
        AttrComponent pyi = new JPanelString(this, imagePanel,
                volRend.plusYImageAttr);
        AttrComponent pzi = new JPanelString(this, imagePanel,
                volRend.plusZImageAttr);
        AttrComponent mxi = new JPanelString(this, imagePanel,
                volRend.minusXImageAttr);
        AttrComponent myi = new JPanelString(this, imagePanel,
                volRend.minusYImageAttr);
        AttrComponent mzi = new JPanelString(this, imagePanel,
                volRend.minusZImageAttr);
        add(imagePanel);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void itemStateChanged(ItemEvent e) {
        String name = ((Component) e.getItemSelectable()).getName();
        boolean value = (e.getStateChange() == ItemEvent.SELECTED);
        ToggleAttr attr = (ToggleAttr) volRend.context.getAttr(name);
        attr.set(value);
        volRend.update();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        String name = ((Component) e.getSource()).getName();
        String value = e.getActionCommand();
        StringAttr attr = (StringAttr) volRend.context.getAttr(name);
        attr.set(value);
        volRend.update();
    }
}
