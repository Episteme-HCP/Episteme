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

package org.jscience.architecture.traffic;

import java.awt.*;

import java.io.File;

import java.net.URL;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


/**
 * Class used to view and browse through online help.
 *
 * @author Group GUI
 * @version 1.0
 */
public class HelpViewer extends JFrame {
    /**
     * Constant for use with <code>showHelp(int index)</code>,
     * indicating to show the help index.
     */
    public static final int HELP_INDEX = 0;

    /**
     * Constant for use with <code>showHelp(int index)</code>,
     * indicating to show the specifications.
     */
    public static final int HELP_SPECS = 1;

    /**
     * Constant for use with <code>showHelp(int index)</code>,
     * indicating to show the license for this application.
     */
    public static final int HELP_LICENSE = 2;

    /**
     * Constant for use with <code>showHelp(int index)</code>,
     * indicating to show the website of this application.
     */
    public static final int HELP_WEBSITE = 3;

    /**
     * Constant for use with <code>showHelp(int index)</code>,
     * indicating to show the about page.
     */
    public static final int HELP_ABOUT = 4;

    /** The base URL for all online help files. */
    protected static String base = determineBase();

    /** The Controller that created this HelpViewer. */
    protected Controller controller;

    /** The pane viewing the help pages. */
    protected JEditorPane viewPane;

/**
     * Creates a HelpViewer showing nothing.
     *
     * @param _controller The Controller to be used
     */
    public HelpViewer(Controller _controller) {
        super("GLD Online Help");
        controller = _controller;
        this.setSize(820, 625);

        // add view
        Container c = this.getContentPane();
        viewPane = new JEditorPane();
        viewPane.setEditable(false);
        viewPane.addHyperlinkListener(new HyperListener());

        JScrollPane scroller = new JScrollPane(viewPane);
        c.add(scroller);
    }

    /**
     * Determines the base URL for all local help files.
     *
     * @return DOCUMENT ME!
     */
    public static String determineBase() {
        // determine base URL
        File file = new File("");
        String base = "file:/" + file.getAbsolutePath();

        if (!base.endsWith("/org/jscience/architecture/traffic")) {
            base += "/org/jscience/architecture/traffic";
        }

        base += "/docs/";

        return base;
    }

    /**
     * Returns the URL for the helpItem specified.
     *
     * @param helpItem One of constants HELP_INDEX, HELP_SPECS, HELP_LICENSE,
     *        HELP_WEBSITE and HELP_ABOUT
     *
     * @return DOCUMENT ME!
     */
    public static String getHelpItem(int helpItem) {
        switch (helpItem) {
        case HELP_INDEX:
            return base + "index.html";

        case HELP_SPECS:
            return base + "specs/index.html";

        case HELP_LICENSE:
            return base + "license.html";

        case HELP_WEBSITE:
            return "http://www.students.cs.uu.nl/swp/2001/isg";

        case HELP_ABOUT:
            return base + "about.html";
        }

        return "";
    }

    /**
     * Shows the help page belonging to the specified HELP_xxx
     * constant.
     *
     * @param helpItem One of constants HELP_INDEX, HELP_SPECS, HELP_LICENSE,
     *        HELP_WEBSITE and HELP_ABOUT
     */
    public void showHelp(int helpItem) {
        toURL(getHelpItem(helpItem));
        setVisible(true);
        requestFocus();
    }

    /**
     * Sets the view pane to a given URL.
     *
     * @param s String representing the URL to show.
     */
    protected void toURL(String s) {
        try {
            URL url = new URL(s);
            toURL(url);
        } catch (Exception exc) {
            controller.showError("Couldn't view help item \"" + s + "\" : " +
                exc);
        }
    }

    /**
     * Sets the view pane to a given URL.
     *
     * @param url URL to view.
     */
    protected void toURL(URL url) {
        try {
            viewPane.setPage(url);
        } catch (Exception exc) {
            controller.showError("Couldn't view help item \"" + url + "\" : " +
                exc);
        }
    }

    /**
     * Simple inner class which listens to hyperlink events.
     */
    public class HyperListener implements HyperlinkListener {
        /**
         * Refer to a link's destination when activated
         *
         * @param e The HyperlinkEvent to be checked
         */
        public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                toURL(e.getURL());
            }
        }
    }
}
