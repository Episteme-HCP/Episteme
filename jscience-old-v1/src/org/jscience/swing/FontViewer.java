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

package org.jscience.swing;

import org.jscience.util.Settings;
import org.jscience.util.license.Licensed;

import java.awt.*;

import java.io.File;


/**
 * A nice little Font Viewer GUI application.
 *
 * @author Holger Antelmann
 *
 * @see JFontChooser
 */
public class FontViewer extends JMainFrame implements FontSelectionListener,
    Licensed {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 3206964082921688184L;

    /** DOCUMENT ME! */
    JFontChooser fc;

    /** DOCUMENT ME! */
    JFontFileChooser ffc;

/**
     * uses a JFontChooser
     *
     * @throws SecurityException DOCUMENT ME!
     */
    public FontViewer() throws SecurityException {
        super("FontViewer", true, true);
        Settings.checkLicense(this);
        fc = new JFontChooser();
        fc.addFontSelectionListener(this);
        getContentPane().add(fc);
        updateStatusText(format(fc.getSelectedFont()));
        pack();
    }

/**
     * uses a JFontFileChooser
     *
     * @param rootDirectory DOCUMENT ME!
     * @throws SecurityException DOCUMENT ME!
     */
    public FontViewer(File rootDirectory) throws SecurityException {
        super("FontViewer", true, true);
        Settings.checkLicense(this);
        ffc = new JFontFileChooser(rootDirectory);
        ffc.addFontSelectionListener(this);
        getContentPane().add(ffc);
        updateStatusText(format(ffc.getSelectedFont()));
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JFontChooser getJFontChooser() {
        return fc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JFontFileChooser getJFontFileChooser() {
        return ffc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ev DOCUMENT ME!
     */
    public void fontSelectionChanged(FontSelectionEvent ev) {
        updateStatusText(format(ev.getFont()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String format(Font font) {
        if (font == null) {
            return "no font selected";
        }

        String s = font.getName() + " (";

        switch (font.getStyle()) {
        case Font.PLAIN:
            s += "plain";

            break;

        case Font.ITALIC:
            s += "italic";

            break;

        case Font.BOLD:
            s += "bold";

            break;

        default:
            s += "bold, italic";

            break;
        }

        s += (", size: " + font.getSize() + ")");

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            new FontViewer().setVisible(true);
        } else {
            new FontViewer(new File(args[0])).setVisible(true);
        }
    }
}
