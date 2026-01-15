/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.util.Settings;

import java.awt.*;

import javax.swing.*;


/**
 * a convenient JFrame providing some basic nice stuff
 *
 * @author Holger Antelmann
 */
public class JMainFrame extends JFrame {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4755425435337668079L;

    /**
     * 
     * @see #hasStatusBar()
     */
    protected boolean hasStatusBar;

    /** represents the status text if constructed with a status bar */
    protected JLabel statusLabel = new JLabel(" ");

    /** DOCUMENT ME! */
    protected JPanel statusBar;

    /**
     * if true, the methods say() and complain() write to
     * <code>out</code> and <code>err</code> respectively; if false, calls to
     * those methods are ignored
     *
     * @see #say(String)
     * @see #complain(String)
     */
    protected boolean verbose = true;

/**
     * Creates a new JMainFrame object.
     */
    public JMainFrame() {
        this("JMainFrame");
    }

/**
     * Creates a new JMainFrame object.
     *
     * @param name DOCUMENT ME!
     */
    public JMainFrame(String name) {
        this(name, false, false);
    }

/**
     * Creates a new JMainFrame object.
     *
     * @param content DOCUMENT ME!
     */
    public JMainFrame(Component content) {
        this(content, true, false);
    }

/**
     * if withStatusBar is set, the content pane will be using BorderLayout and
     * places the status bar SOUTH
     *
     * @param name          DOCUMENT ME!
     * @param withMenu      DOCUMENT ME!
     * @param withStatusBar DOCUMENT ME!
     */
    public JMainFrame(String name, boolean withMenu, boolean withStatusBar) {
        super(name);

        if (withStatusBar) {
            getContentPane().setLayout(new BorderLayout());

            FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
            statusBar = new JPanel(layout);
            statusBar.add(statusLabel);
            getContentPane().add(statusBar, BorderLayout.SOUTH);
        }

        init(withMenu);
    }

/**
     * places the given Component in the center of the content pane using
     * BorderLayout
     *
     * @param content         DOCUMENT ME!
     * @param withDefaultMenu DOCUMENT ME!
     * @param withStatusBar   DOCUMENT ME!
     */
    public JMainFrame(Component content, boolean withDefaultMenu,
        boolean withStatusBar) {
        super(content.getName());
        hasStatusBar = withStatusBar;
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(content, BorderLayout.CENTER);

        if (hasStatusBar) {
            statusBar = new JPanel();
            statusBar.add(statusLabel);
            getContentPane().add(statusBar, BorderLayout.SOUTH);
        }

        init(withDefaultMenu);
        pack();
    }

    /**
     * DOCUMENT ME!
     *
     * @param withDefaultMenu DOCUMENT ME!
     */
    void init(boolean withDefaultMenu) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(Settings.getResource(
                    "org/jscience/jscienceicon.png")).getImage());

        if (withDefaultMenu) {
            setJMenuBar(createDefaultMenuBar(this));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JMenuBar createDefaultMenuBar(Frame parent) {
        return Menus.createDefaultMenuBar(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStatusText() {
        return statusLabel.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void updateStatusText(String text) {
        statusLabel.setText(text);
    }

    /**
     * returns true if the object was constructed with a status bar
     * displayed at BorderLayout.SOUTH of the content pane
     *
     * @return DOCUMENT ME!
     */
    public boolean hasStatusBar() {
        return hasStatusBar;
    }

    /**
     * writes the text to <code>out</code> if <code>verbose</code> is
     * true
     *
     * @param text DOCUMENT ME!
     */
    public void say(String text) {
        if (verbose) {
            System.out.println("[JMainFrame Message:] " + text);
        }
    }

    /**
     * writes the text to <code>err</code> if <code>verbose</code> is
     * true
     *
     * @param text DOCUMENT ME!
     */
    public void complain(String text) {
        if (verbose) {
            System.err.println("[JMainFrame Error:] " + text);
        }
    }
}
