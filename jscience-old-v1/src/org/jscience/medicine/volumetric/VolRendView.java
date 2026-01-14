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

/* Revision history:

Original Code by Doug Gehringer.
Version of 20000121 appletized by LMDorman, but app usage is broken.
Version of 20000409 generalized file selection to allow changing directories.lmd
Version of 20000429 Changed VolFile.java to use serial read instead of RandomAccessFile.  lmd
Version of 20000914 ddg: Changed to split out applet case into separate class
*/
package org.jscience.medicine.volumetric;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.media.j3d.Canvas3D;

import javax.swing.*;
import javax.swing.border.BevelBorder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class VolRendView extends JApplet implements ItemListener,
    ActionListener {
    // parameters settable by setting corresponding property
    /** DOCUMENT ME! */
    public boolean timing = false;

    /** DOCUMENT ME! */
    public boolean debug = true;

    /** DOCUMENT ME! */
    Canvas3D canvas;

    /** DOCUMENT ME! */
    VolRend volRend;

    //dummy for use if called as an applet
    /** DOCUMENT ME! */
    String[] args;

    /**
     * DOCUMENT ME!
     */
    public void start() {
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        String timingstr = getParameter("TIMING");
        System.out.println("main: timingstr = " + timingstr);
        timing = Boolean.valueOf(timingstr).booleanValue();
        debug = Boolean.valueOf(getParameter("DEBUG")).booleanValue();
        System.out.println("main: debug = " + debug);

        volRend = new VolRend(timing, debug);

        volRend.initContext(getCodeBase()); // initializes the renderer

        // setup the inital data file or settings
        // get filename from applet PARAM
        String filename = getParameter("VRSFILENAME");
        System.out.println("VolRendView: VRSFILENAME = " + filename);
        volRend.restoreContext(filename);

        // Setup the frame
        getContentPane().setLayout(new BorderLayout());

        canvas = volRend.getCanvas();
        canvas.setSize(600, 600);

        getContentPane().add(canvas, BorderLayout.CENTER);

        if (debug == true) {
            System.out.println(" setting up GUI ");
        }

        getContentPane().add(setupPanelGUI(), BorderLayout.SOUTH);

        validate();

        volRend.update();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JPanel setupPanelGUI() {
        if (debug) {
            System.out.println(" in setupPanelGUI()");
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new BevelBorder(BevelBorder.RAISED));

        panel.add(new JLabel(
                "Press the mouse button and drag to move the model"));

        Colormap cmap = volRend.colorModeAttr.getColormap();

        if (cmap instanceof SegyColormap) {
            SegyColormap segyCmap = (SegyColormap) cmap;
            panel.add(new SegyCmapEditor(volRend, segyCmap));
        }

        panel.validate();

        return panel;
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

        //System.out.println("action:  set attr " + name  + " to value " +
        //	value);
        volRend.context.getAttr(name).set(value);
        volRend.update();
    }
}
