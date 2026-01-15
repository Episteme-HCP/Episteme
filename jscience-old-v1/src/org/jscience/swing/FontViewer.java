/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
