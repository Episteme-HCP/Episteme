/*
 * Program : ADFC�
 * Class : org.jscience.fluids.gui2.Portada
 *         Muestra a imagen como portada del program
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 7/08/2000
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.gui;

import java.awt.*;

import java.net.URL;


/**
 * Shows an image centered on the screen.
 */
public class Facade extends Window // implements Runnable
 {
    /** DOCUMENT ME! */
    Image img = null;

/**
     * Creates a new Facade object.
     *
     * @param resourceName DOCUMENT ME!
     */
    public Facade(String resourceName) {
        super(new Frame());

        // Load the image
        URL imagen = getClass().getResource(resourceName);
        Toolkit t = getToolkit();
        MediaTracker tracker = new MediaTracker(this);
        img = t.getImage(imagen);
        tracker.addImage(img, 0);

        try {
            tracker.waitForID(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Center on the screen
        Dimension ds = getToolkit().getScreenSize();
        int xt = img.getWidth(null);
        int yt = img.getHeight(null);
        setLocation((ds.width - xt) / 2, (ds.height - yt) / 2);
        setSize(xt, yt);

        show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        if (img != null) {
            g.drawImage(img, 0, 0, null);
        }
    }
}
