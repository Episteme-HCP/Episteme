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

package org.jscience.tests.astronomy.milkyway.hipparcos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Iterator;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
/**
 * A window to lis all star info in should read stars from iterator
 */
public class StarList extends Frame implements ActionListener {
    /** DOCUMENT ME! */
    protected TextArea text;

    /** DOCUMENT ME! */
    protected StarStore sky;

/**
     * Constructor - must orovide star source
     *
     * @param sky DOCUMENT ME!
     */
    public StarList(StarStore sky) {
        super(" Stars in Current Field ");
        this.sky = sky;
        setSize(600, 700);
        text = new TextArea("", 50, 100, TextArea.SCROLLBARS_BOTH);

        Button b = new Button("Close");
        b.addActionListener(this);

        Button r = new Button("Refresh");
        r.addActionListener(this);

        Panel p = new Panel();
        p.add(b);
        p.add(r);
        add("Center", text);
        add("South", p);
        text.setFont(new Font("Monospaced", Font.PLAIN, 11));
    }

    /**
     * close or refresh
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.startsWith("Close")) {
            setVisible(false);

            return;
        }

        if (cmd.startsWith("Refr")) {
            refresh();

            return;
        }
    }

    /* load all data again from the start store */
    public void refresh() {
        Iterator stars = sky.getStars();
        text.setText(Star.header() + "\n");

        while (stars.hasNext()) {
            Object star = stars.next();
            text.append("" + star + "\n");
        }
    }
}
