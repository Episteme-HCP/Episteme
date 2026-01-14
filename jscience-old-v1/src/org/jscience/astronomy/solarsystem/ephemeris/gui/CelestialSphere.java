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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import netscape.javascript.JSObject;

import org.jscience.astronomy.solarsystem.ephemeris.ELP2000.ELP2000;
import org.jscience.astronomy.solarsystem.ephemeris.VSOP87.VSOP87;

import java.applet.Applet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CelestialSphere extends Applet implements ActionListener {
    /** DOCUMENT ME! */
    private CSphereFrame frame;

    /** DOCUMENT ME! */
    private CSpherePanel panel;

    /** DOCUMENT ME! */
    private Image shadow_image;

    /** DOCUMENT ME! */
    private Image scratch_image;

    /** DOCUMENT ME! */
    public StarsArray stars;

    /** DOCUMENT ME! */
    public LineArray constels;

    /** DOCUMENT ME! */
    public LineArray boundaries;

    /** DOCUMENT ME! */
    public ShortPoint[] messier;

    /** DOCUMENT ME! */
    public VSOP87[] VSOPbodies;

    /** DOCUMENT ME! */
    public ELP2000 elp2000;

    /** DOCUMENT ME! */
    private StarLoader starloader;

    /** DOCUMENT ME! */
    private boolean update_shadow;

    /** DOCUMENT ME! */
    private Button butt;

    /** DOCUMENT ME! */
    private double latitude;

    /** DOCUMENT ME! */
    private double longitude;

    /** DOCUMENT ME! */
    JSObject JSwin;

/**
     * Creates a new CelestialSphere object.
     */
    public CelestialSphere() {
        frame = null;
        panel = null;
        shadow_image = null;
        scratch_image = null;
        stars = null;
        constels = null;
        boundaries = null;
        messier = null;
        VSOPbodies = null;
        elp2000 = null;
        update_shadow = false;
        JSwin = null;
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        System.out.println("Applet stopping.");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isStarsLoaded() {
        return stars != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isVSOPLoaded() {
        return VSOPbodies != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCookieLongitude() {
        return longitude;
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        System.out.println("Applet cleaning up.");

        if (starloader != null) {
            starloader.kill();
        }

        stars = null;
        constels = null;
        boundaries = null;
        messier = null;
        panel.destroy();
        frame.destroy();
        removeAll();
    }

    /**
     * DOCUMENT ME!
     */
    private void getCookie() {
        try {
            String[] as = { "CelestialSphere" };
            String s = (String) JScall("getCookie", as);

            if (s.equals("")) {
                System.out.println("Cookie not set.");
            } else {
                System.out.println("Cookie is set to " + s);
                s = s.substring(4);

                String s1 = s.substring(0, s.indexOf('L'));
                latitude = Double.valueOf(s1).doubleValue();

                String s2 = s.substring(s.indexOf('=') + 1, s.length());
                longitude = Double.valueOf(s2).doubleValue();
            }
        } catch (Exception _ex) {
            System.out.println("Cookie Monster: No coookiees!!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionevent DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent actionevent) {
        String s = actionevent.getActionCommand();

        if (s == butt.getLabel()) {
            frame.show();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    public void saveCookie(double d, double d1) {
        String s = new String("LAT=" + d + "LONG=" + d1);
        System.out.println("Saving cookie: " + s);

        try {
            String[] as = new String[1];
            as[0] = s;
            JScall("register", as);
        } catch (Exception _ex) {
            System.out.println("Cookie Monster: Can't save coookiee!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * DOCUMENT ME!
     */
    public void tryDraw() {
        panel.tryDraw();
        frame.tryDraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBoundariesLoaded() {
        return boundaries != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConstelsLoaded() {
        return constels != null;
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        System.out.println("Applet started.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param aobj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object JScall(String s, Object[] aobj) throws Exception {
        return JScall(JSwin, s, aobj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param jsobject DOCUMENT ME!
     * @param s DOCUMENT ME!
     * @param aobj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object JScall(JSObject jsobject, String s, Object[] aobj)
        throws Exception {
        StringBuffer stringbuffer = new StringBuffer(s + "(");

        if (aobj != null) {
            for (int i = 0; i < aobj.length; i++) {
                stringbuffer.append(aobj[i].toString());

                if (i < (aobj.length - 1)) {
                    stringbuffer.append(", ");
                }
            }
        }

        stringbuffer.append(")");
        System.out.println(stringbuffer);

        if (jsobject == null) {
            throw new Exception(stringbuffer.toString() +
                "\n   Javascript not available.");
        }

        Object obj;

        try {
            obj = jsobject.call(s, aobj);
        } catch (Exception exception) {
            String s1 = exception.getMessage();

            if (s1 == null) {
                s1 = "No Description";
            }

            System.out.println("Caught Javascript Exception:\n   " + s1);
            throw exception;
        }

        return obj;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAppletInfo() {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("Celestial Sphere\n");
        stringbuffer.append("Copyright 1998-2002 Peter Csapo\n");
        stringbuffer.append("Contact: peter@pcsapo.com\n");
        stringbuffer.append("Requires Java 1.1 or better.\n");
        stringbuffer.append("Tested under:\n");
        stringbuffer.append("  Sun Java Plugin 1.4,\n");
        stringbuffer.append("  Microsoft JVM 5.0,\n");
        stringbuffer.append("  Netscape 4.78: Symantec Java Bytecode Compiler");

        return stringbuffer.toString();
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        System.out.println("Applet initializing.");

        try {
            JSwin = JSObject.getWindow(this);
            getCookie();
        } catch (Exception _ex) {
            System.out.println(
                "No javascript available. Cookies not retrieved.");
        }

        super.init();
        setBackground(Color.black);
        setLayout(new BorderLayout());
        panel = new CSpherePanel(this);
        add("Center", panel);
        butt = new Button("Detach");
        butt.setBackground(panel.buttcolor);
        panel.add(butt);
        butt.addActionListener(this);
        frame = new CSphereFrame("Celestial Sphere", this);
        starloader = new StarLoader(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMessiersLoaded() {
        return messier != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCookieLatitude() {
        return latitude;
    }
}
