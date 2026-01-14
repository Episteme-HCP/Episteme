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

package org.jscience.physics.fluids.dynamics.gui;

import java.awt.*;

import java.io.IOException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class PanelHTML extends JEditorPane {
    /** DOCUMENT ME! */
    private String text;

    /** DOCUMENT ME! */
    HTMLEditorKit hek;

    /** DOCUMENT ME! */
    HTMLDocument hdoc;

/**
     * Creates a new PanelHTML object.
     */
    public PanelHTML() {
        super();

        text = new String();
        hek = new HTMLEditorKit();
        setEditorKit(hek);
        setEditable(false);
        hdoc = (HTMLDocument) getDocument();

        // setDoubleBuffered(true);
        setText("");

        // setToolTipText("This HTML console shows messages from the solver");
        addHyperlinkListener(createHyperLinkListener());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HyperlinkListener createHyperLinkListener() {
        return new HyperlinkListener() {
                public void hyperlinkUpdate(HyperlinkEvent e) {
                    if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        if (e instanceof HTMLFrameHyperlinkEvent) {
                            ((HTMLDocument) getDocument()).processHTMLFrameHyperlinkEvent((HTMLFrameHyperlinkEvent) e);
                        } else {
                            try {
                                setPage(e.getURL());
                            } catch (IOException ioe) {
                                System.err.println("IOException: " + ioe);
                            }
                        }
                    }
                }
            };
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    public void out(java.lang.String html) {
        try {
            hek.insertHTML(hdoc, hdoc.getLength(), html, 0, 0, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        scrollRectToVisible(new Rectangle(0, getHeight() - 5, 5, getHeight()));
        Thread.yield();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HTMLDocument getHTMLDocument() {
        return hdoc;
    }
}
