package org.jscience.chemistry.gui.basic;

import java.awt.*;

import java.util.Vector;


/**
 * A class representing an Atom in 3D.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//this code is adapted from Eric Harlow
//http://www.netbrain.com/~brain/molecule/
//bigbrain@netbrain.com
public class Atom extends Object {
    /** DOCUMENT ME! */
    public String m_sName;

    /** DOCUMENT ME! */
    public double m_x;

    /** DOCUMENT ME! */
    public double m_y;

    /** DOCUMENT ME! */
    public double m_z;

    /** DOCUMENT ME! */
    public double m_tx;

    /** DOCUMENT ME! */
    public double m_ty;

    /** DOCUMENT ME! */
    public double m_tz;

    /** DOCUMENT ME! */
    Vector bondlist;

    /** DOCUMENT ME! */
    public int m_nFog;

    /*
     * Constructor
     */
    public Atom(String s, double x, double y, double z) {
        m_sName = s;
        m_x = x;
        m_y = y;
        m_z = z;
        m_nFog = 100;
    }

    /*
     * AddBond
     *
     * Add a bond to the list of bonds that this atom is a part of.
     */
    public void addBond(Bond bond) {
        // --- Make sure there's a list ready
        if (bondlist == null) {
            bondlist = new Vector();
        }

        // --- Add the bond to the list.
        bondlist.addElement(bond);
    }

    /*
     * CalculateFog
     */
    public void calculateFog(double min_range, double max_range) {
        double dCurr = max_range - m_tz;
        double dRange = max_range - min_range;

        m_nFog = (int) ((dCurr * 100.0) / dRange);

        if (m_nFog < 0) {
            m_nFog = 0;
        }

        if (m_nFog > 100) {
            m_nFog = 100;
        }
    }

    /*
     * Set the color of the molecule based on the char.
     */
    public void setColor(Graphics g, double min_range, double max_range) {
        char cName = m_sName.charAt(0);

        switch (cName) {
        case 'H':
            g.setColor(adjustColor(Color.white));

            break;

        case 'C':
            g.setColor(adjustColor(Color.black));

            break;

        case 'N':
            g.setColor(adjustColor(Color.blue));

            break;

        case 'O':
            g.setColor(adjustColor(Color.red));

            break;

        case 'S':
            g.setColor(adjustColor(Color.yellow));

            break;

        case 'F':
            g.setColor(adjustColor(Color.green));

            break;

        default:
            g.setColor(adjustColor(Color.pink));

            break;
        }
    }

    //http://info.bio.cmu.edu/Courses/BiochemMols/RasFrames/CPKCLRS.HTM
    /**
     * Carbon light grey [200,200,200] C8C8C8 Oxygen red [240,0,0]
     * F00000 Hydrogen white [255,255,255] FFFFFF Nitrogen light blue
     * [143,143,255] 8F8FFF Sulfur yellow [255,200,50] FFC832 Phosphorus
     * orange [255,165,0] FFA500 Chlorine green [0,255,0] 00FF00 Bromine, Zinc
     * brown [165,42,42] A52A2A Sodium blue [0,0,255] 0000FF Iron orange
     * [255,165,0] FFA500 Magnesium dark green [42,128,42] 2A802A Calcium dark
     * grey [128,128,128] 808090 Unknown deep pink [255,20,147] FF1493
     *
     * @param orig DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color adjustColor(Color orig) {
        int green;
        int red;
        int blue;

        if (MoleculeViewer.m_bFog) {
            red = adjustComponent(orig.getRed());
            green = adjustComponent(orig.getGreen());
            blue = adjustComponent(orig.getBlue());

            return (new Color(red, green, blue));
        } else {
            return (orig);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int adjustComponent(int nValue) {
        int nChange;

        if (nValue > 128) {
            nChange = (((nValue - 128) * m_nFog) / 100);
            nValue -= nChange;
        } else {
            nChange = (((128 - nValue) * m_nFog) / 100);
            nValue += nChange;
        }

        if (nValue < 0) {
            nValue = 0;
        }

        if (nValue > 255) {
            nValue = 255;
        }

        return (nValue);
    }
}
