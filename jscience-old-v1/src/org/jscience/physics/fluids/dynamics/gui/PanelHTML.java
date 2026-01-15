/*
 * Program : ADFC�
 * Class : org.jscience.fluids.gui2.PanelHTML
 *         Consola HTML
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 29/07/2000
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
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
