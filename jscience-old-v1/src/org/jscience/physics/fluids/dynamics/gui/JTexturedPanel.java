/*
 * Program : ADFC�
 * Class : org.jscience.fluids.gui2.JTexturedPanel
 *         Componente embellecedor Swing for dar textura of fondo.
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
 * DOCUMENT ME!
 *
 * @author Alejandro "balrog" Rodriguez Gallego
 * @version 0.1
 */
public class JTexturedPanel extends javax.swing.JPanel {
    /** DOCUMENT ME! */
    private Image texture;

    /** DOCUMENT ME! */
    private int xt;

    /** DOCUMENT ME! */
    private int yt;

    /** DOCUMENT ME! */
    private int xc;

    /** DOCUMENT ME! */
    private int yc;

/**
     * Crea a nuevo JTexturedPanel
     */
    public JTexturedPanel() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param resourceName DOCUMENT ME!
     */
    public void setTextura(String resourceName) {
        // Load of the imagen mediante a media Tracker
        URL imagen = getClass().getResource(resourceName);

        Toolkit t = getToolkit();
        MediaTracker tracker = new MediaTracker(this);
        texture = t.getImage(imagen);
        tracker.addImage(texture, 0);

        try {
            tracker.waitForID(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        xt = texture.getWidth(this);
        yt = texture.getHeight(this);

        //System.out.println(""+xt+" "+textura);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        // Size of the component
        xc = getWidth();
        yc = getHeight();

        //System.out.println("No " +texture);
        if (texture != null) {
            // number of times that the mosaic is reapeated
            //System.out.println("Ok");
            int xr = (xc / xt) + 1;
            int yr = (yc / yt) + 1;

            for (int i = 0; i < xr; i++)
                for (int j = 0; j < yr; j++)
                    g.drawImage(texture, i * xt, j * yt, this);
        }
    }
}
