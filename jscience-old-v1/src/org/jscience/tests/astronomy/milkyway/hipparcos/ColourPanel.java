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

// Written by William O'Mullane for the
// Astrophysics Division of ESTEC  - part of the European Space Agency.
/**
 * Simple panel to Display the range of colours
 */
package org.jscience.tests.astronomy.milkyway.hipparcos;

import org.jscience.astronomy.catalogs.hipparcos.Star3D;

import java.awt.*;


//this package is rebundled from Hipparcos Java package from
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ColourPanel extends Panel {
/**
     * Creates a new ColourPanel object.
     */
    public ColourPanel() {
        init();
    }

    /**
     * initialiser
     */
    protected void init() {
        GridBagLayout gridbag = new GridBagLayout();
        setLayout(new GridLayout(6, 2, 0, 3));

        //setLayout(gridbag);
        GridBagConstraints lab = new GridBagConstraints();
        lab.fill = GridBagConstraints.HORIZONTAL;
        lab.gridy = GridBagConstraints.RELATIVE;
        lab.ipady = 0;
        lab.gridx = 0;

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = GridBagConstraints.RELATIVE;
        c.ipady = 25;
        c.gridx = 0;

        //setLayout(new GridLayout(Constants.colours.length,1));
        Label title = new Label("Colours", Label.CENTER);
        title.setForeground(Color.cyan);
        title.setBackground(Color.black);

        Label range = new Label("B-V range", Label.CENTER);
        range.setForeground(Color.cyan);
        range.setBackground(Color.black);
        gridbag.setConstraints(title, lab);
        add(title);
        add(range);

        for (int col = 0; col < Constants.colours.length; col++) {
            ColourCell cell = new ColourCell();
            gridbag.setConstraints(cell, c);
            add(cell);

            String txt = "";

            if (col < Constants.colBands.length) {
                txt = "" + Constants.colBands[col];

                if (col == 0) {
                    txt = "<" + txt;
                } else {
                    txt = Constants.colBands[col - 1] + " - " + txt;
                }
            } else {
                txt = ">" + Constants.colBands[col - 1];
            }

            cell.showCol(Star3D.getStarAppearance(col));

            Label l = new Label(txt, Label.CENTER);
            l.setForeground(Color.cyan);
            l.setBackground(Color.black);
            gridbag.setConstraints(l, lab);
            add(l);
        }
    }
}
;
