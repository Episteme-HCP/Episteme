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


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
//author: Doug Gehringer
//email:Doug.Gehringer@sun.com
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SegyCmapEditor extends JPanel implements ChangeListener {
    /** DOCUMENT ME! */
    VolRend volRend;

    /** DOCUMENT ME! */
    SegyColormap cmap;

    /** DOCUMENT ME! */
    JSlider minAlpha;

    /** DOCUMENT ME! */
    JSlider maxAlpha;

/**
     * Creates a new SegyCmapEditor object.
     *
     * @param volRend DOCUMENT ME!
     * @param cmap    DOCUMENT ME!
     */
    SegyCmapEditor(VolRend volRend, SegyColormap cmap) {
        this.volRend = volRend;
        this.cmap = cmap;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(new JLabel("Min Alpha Cutoff"));
        minAlpha = new JSlider(0, 255, cmap.minAlphaAttr.getValue());
        minAlpha.addChangeListener(this);
        add(minAlpha);
        add(new JLabel("Max Alpha Cutoff"));
        maxAlpha = new JSlider(0, 255, cmap.maxAlphaAttr.getValue());
        maxAlpha.addChangeListener(this);
        add(maxAlpha);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();

        if (!source.getValueIsAdjusting()) {
            int value = (int) source.getValue();

            if (source == minAlpha) {
                cmap.minAlphaAttr.set(value);
            } else if (source == maxAlpha) {
                cmap.maxAlphaAttr.set(value);
            }

            volRend.update();
        }
    }
}
