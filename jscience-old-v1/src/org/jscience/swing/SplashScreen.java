/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.swing;

import org.jscience.util.MiscellaneousUtils;
import org.jscience.util.Settings;

import java.awt.*;
import java.awt.event.*;

import java.net.URL;


//import javax.swing.*;
//import javax.swing.event.*;
/**
 * for use as a splash screen. sample usage (first line in your
 * application): <code>SplashScreen.splash().disposeAfter(3000)</code>
 *
 * @author Holger Antelmann
 *
 * @since 11/10/2004
 */
public class SplashScreen extends Window {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6542684371587960602L;

    /** DOCUMENT ME! */
    static Toolkit toolkit = Toolkit.getDefaultToolkit();

    /** DOCUMENT ME! */
    static String defaultSplashImageLocation = "org/jscience/dukelogo.gif";

    /** DOCUMENT ME! */
    static Image defaultImage = toolkit.getImage(Settings.getResource(
                defaultSplashImageLocation));

    /** DOCUMENT ME! */
    final static String noFrameName = "org.jscience.swing.SplashScreen.InternalFrame";

    /** DOCUMENT ME! */
    MouseListener ml = new MouseAdapter() {
            public void mouseClicked(MouseEvent ev) {
                SplashScreen.this.dispose();
            }
        };

    /** DOCUMENT ME! */
    Window owner;

    /** DOCUMENT ME! */
    Image image;

    /** DOCUMENT ME! */
    boolean closeOnClick;

    /** DOCUMENT ME! */
    private boolean disposed = false;

/**
     * Creates a new SplashScreen object.
     */
    public SplashScreen() {
        this(defaultImage);
    }

/**
     * Creates a new SplashScreen object.
     *
     * @param owner DOCUMENT ME!
     */
    public SplashScreen(Window owner) {
        this(owner, defaultImage);
    }

/**
     * Creates a new SplashScreen object.
     *
     * @param imageURL DOCUMENT ME!
     */
    public SplashScreen(URL imageURL) {
        this(toolkit.getImage(imageURL));
    }

/**
     * Creates a new SplashScreen object.
     *
     * @param image DOCUMENT ME!
     */
    public SplashScreen(Image image) {
        this(new Frame(noFrameName), image);
    }

/**
     * Creates a new SplashScreen object.
     *
     * @param owner DOCUMENT ME!
     * @param image DOCUMENT ME!
     */
    public SplashScreen(Window owner, Image image) {
        super(owner);
        this.image = image;
        this.closeOnClick = closeOnClick;

        MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);

        try {
            tracker.waitForID(0);
        } catch (InterruptedException ignore) {
        }

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        setSize(width, height);

        Dimension screen = toolkit.getScreenSize();
        setLocation((screen.width - width) / 2, (screen.height - height) / 2);

        if (!defaultOwner()) {
            disposeOnOpen(getOwner());
        }

        setCloseOnClick(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        // avoid flickering
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }

        toFront();
    }

    /**
     * already true by default
     *
     * @param flag DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SplashScreen setCloseOnClick(boolean flag) {
        if (flag) {
            addMouseListener(ml);
        } else {
            removeMouseListener(ml);
        }

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SplashScreen splash() {
        return splash(new Frame(noFrameName));
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static SplashScreen splash(Window owner) {
        final SplashScreen screen = new SplashScreen(owner);

        try {
            EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        screen.setVisible(true);
                    }
                });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return screen;
    }

    /**
     * DOCUMENT ME!
     */
    public void dispose() {
        if (disposed) {
            return;
        }

        super.dispose();
        disposed = true;

        if (defaultOwner()) {
            getOwner().dispose();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private boolean defaultOwner() {
        if (!(getOwner() instanceof Frame)) {
            return false;
        }

        return (noFrameName.equals(((Frame) getOwner()).getTitle()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param millis DOCUMENT ME!
     */
    public void disposeAfter(final int millis) {
        if (disposed) {
            return;
        }

        Thread t = new Thread() {
                public void run() {
                    MiscellaneousUtils.pause(millis);
                    EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                dispose();
                            }
                        });
                }
            };

        t.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param nextScreen DOCUMENT ME!
     */
    public void disposeOnOpen(Window nextScreen) {
        nextScreen.addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent ev) {
                    disposeAfter(2000);
                }
            });
    }
}
