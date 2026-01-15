/*
 * $Id: PolyhedronDisplayer.java,v 1.3 2007-10-21 21:08:22 virtualcall Exp $
 **********************************************************
 * kaleido
 *
 *  Kaleidoscopic construction of uniform polyhedra
 *  Copyright © 1991-2003 Dr. Zvi Har’El <rl@math.technion.ac.il>
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *  
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *  
 *  3. The end-user documentation included with the redistribution,
 *     if any, must include the following acknowledgment:
 *  	“This product includes software developed by
 *  	 Dr. Zvi Har’El (http://www.math.technion.ac.il/~rl/).”
 *     Alternately, this acknowledgment may appear in the software itself,
 *     if and wherever such third-party acknowledgments normally appear.
 *  
 *  This software is provided ‘as-is’, without any express or implied
 *  warranty.  In no event will the author be held liable for any
 *  damages arising from the use of this software.
 *
 *  Author:
 *	Dr. Zvi Har’El,
 *	Deptartment of Mathematics,
 *	Technion, Israel Institue of Technology,
 *	Haifa 32000, Israel.
 *	E-Mail: rl@math.technion.ac.il
 **********************************************************
 */

package org.jscience.mathematics.geometry.polyhedrons;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * The class <code>Kaleidoscope</code> is loaded by a web browser or by the Java
 * Applet Viewer. It contains the code decode the HTML parameters and start
 * displaying the applet embedded in the web page.
 * <p/>
 * The relevant HTML fragment is:
 * <blockquote>
 * <table class=TableRowColor><tr>
 * <td><code>
 * &lt;APPLET CODE=kaleido.Kaleidoscope ARCHIVE=scope.jar WIDTH=400
 * HEIGHT=480&gt;<br>
 * &lt;PARAM NAME=COLOR VALUE=GR&gt;<br>
 * &lt;IMG SRC=kaleido.gif BORDER=0&gt;<br>
 * &lt;BR&gt;<br>
 * Sorry, you need a Java enabled browser to see this in action...<br>
 * &lt;/APPLET&gt;</code></td>
 * </tr></table>
 * </blockquote>
 * <p/>
 * The case-nonsensitive applet parameters are:
 * <dl>
 * <dt><code>color</code>
 * <dd>One of more of the letters <code>RGBL</code>. The first three represent
 * a color as a combination of the basic colors <i>red</i>, <i>green</i> and
 * <i>blue</i>. This determines the forefround basic color, with the
 * backgroung being black. If the letter <code>L</code>, <i>light</i>, is
 * specified, the background color is determined by the other letters, and the
 * forground is painted in black.
 * The default colors are white on black.
 * <dt><code>input</code>
 * <dd>A list of polyhedron symbols. The symbols are seperated a comma, a
 * period, a colon or a semicolon. A valid symbol is a number sign followed by
 * a decimal number between one and eighty, or a Withoff symbol, i.e, three
 * rational numbers and a vertical bar. The default list is given in {@link
 * UniformPolyhedrons}.
 * <dt><code>azimuth</code>
 * <dt><code>elevation</code>
 * <dd>The direction in space of the rotation axis of the polyhedron. Both are
 * angles, given in degrees. The default values are
 * <small><sup>180</sup>/<sub>7</sub></small> and
 * <small><sup>180</sup>/<sub>17</sub></small>, respectively.
 * </dl>
 * In cases that
 * <code>kaleido</code> is run as a full fledged application, the applet is
 * embedded within a {@link Kaleido}, and the run parameters are explained
 * {@link Kaleido there}.
 * <p/>
 * The applet archive <a href="scope.jar"><code>scope.jar</code></a> includes
 * all the classes in this package which are necessary to display an applet.
 * This includes the classes {@link PolyhedronDisplayer}, {@link Polyhedron}, {@link
 * UniformPolyhedrons}, {@link Vector3D} and {@link Rational}. This excludes the classes
 * {@link Kaleido} and {@link PolyhedronVRMLOutputter}.
 *
 * @author <a href="http://www.math.technion.ac.il/~rl/">Zvi Har’El</a>
 * @version $Id: PolyhedronDisplayer.java,v 1.3 2007-10-21 21:08:22 virtualcall Exp $
 * @see <a href="Kaleidoscope.java">Class source code</a>
 * @see <a href="demo.html">Applet demo</a>
 */
public class PolyhedronDisplayer extends Applet implements Runnable, ActionListener {

    /**
     * Version information.
     */
    static final String version =
            "Kaleidoscopic construction of uniform polyhedra, " +
                    "$Revision: 1.3 $";
    /**
     * Copyright notice.
     */
    static final String copyright =
            "Copyright © 1991-2003 Dr. Zvi Har’El <rl@math.technion.ac.il>";
    /**
     * Input source.
     */
    Object in;
    /**
     * Index of polyhedron in the input.
     */
    int index;
    /**
     * An object reference to the currently showing polyhedron.
     */
    Polyhedron P;
    /**
     * If true, the uniform polyhedron is on display. Otherwise, its dual is
     * showing.
     */
    boolean uniform;
    /**
     * The 16 chosen display colors.
     */
    Color[] color = new Color[16];
    /**
     * Font for titles.
     */
    Font bigFont = new Font("Serif", Font.BOLD, 24);
    /**
     * Font for subtitles.
     */
    Font smallFont = new Font("Serif", Font.BOLD, 12);
    /**
     * An object reference to the clock thread.
     */
    Thread clock;
    /**
     * Boolean flag describing the color setup.
     */
    boolean red, green, blue, light;
    /**
     * Direction in space of rotation axis.
     */
    double azimuth, elevation;
    /**
     * System current millis when rotation stopped.
     */
    private long stoppedAt;
    /**
     * Accumulated time of pausing in millis.
     */
    private long lostTime;
    /**
     * References to buttons. Used for enbling and disabling them.
     */
    private Button next, back;
    /**
     * True if a button was disabeled.
     */
    private boolean disabled;

    /**
     * Initializes the applet. Invoked by the applet browser and by the {@link
     * Kaleido} constructor. Reads the applet parametes, sets the display colors,
     * and build the display buttons.
     *
     * @see Color
     * @see Button
     */
    public void init() {
        /*
        * Get parameters
        */
        String s;
        if ((s = getParameter("input")) != null) {
            in = new Vector();
            StringTokenizer st = new StringTokenizer(s, ".,;:");
            while (st.hasMoreTokens())
                ((Vector) in).addElement(st.nextToken());
        }
        azimuth = (s = getParameter("azimuth")) != null ? Math.PI / 180 *
                Double.valueOf(s).doubleValue() : Math.PI / 7;

        elevation = (s = getParameter("elevation")) != null ? Math.PI / 180 *
                Double.valueOf(s).doubleValue() : Math.PI / 17;
        if ((s = getParameter("color")) != null) {
            s = s.toLowerCase();
            red = s.indexOf('r') >= 0;
            green = s.indexOf('g') >= 0;
            blue = s.indexOf('b') >= 0;
            light = s.indexOf('l') >= 0;
        }
        /*
        * Set colors
        */
        if (!red && !green && !blue) {
            red = true;
            green = true;
            blue = true;
        }
        for (int i = 16, j = 0; --i != 0; j += 12) {
            color[i] = new Color(!red ? 0 : light ? j : 255 - j, !green ? 0 :
                    light ? j : 255 - j, !blue ? 0 : light ? j : 255 - j);
        }
        color[0] = new Color(!red || !light ? 0 : 255, !green || !light ? 0 :
                255, !blue || !light ? 0 : 255);
        setBackground(color[0]);
        setForeground(color[15]);
        /*
        * Add buttons
        */
        back = button("back");
        button("dual");
        button("pause");
        next = button("next");
        uniform = true;
        P = next();
        System.err.println(getAppletInfo());
    }

    public String getAppletInfo() {
        return version + System.getProperty("line.separator") + copyright;
    }

    private Button button(String s) {
        Button b = new Button(s);
        add(b);
        b.addActionListener(this);
        b.setBackground(color[0]);
        b.setForeground(color[15]);
        return b;
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        Polyhedron poly;
        if (s == "pause") {
            if (clock != null)
                stop();
            else
                start();
            return;
        } else if (s == "next") {
            if (disabled) {
                back.setEnabled(true);
                back.setLabel("back");
                disabled = false;
            }
            ++index;
            if ((poly = next()) == null) {
                next.setEnabled(false);
                next.setLabel("");
                disabled = true;
                --index;
                return;
            }
        } else if (s == "back") {
            if (disabled) {
                next.setEnabled(true);
                next.setLabel("next");
                disabled = false;
            }
            --index;
            if ((poly = next()) == null) {
                back.setEnabled(false);
                back.setLabel("");
                disabled = true;
                ++index;
                return;
            }
        } else if (s == "dual") {
            int i = P.index;
            poly = i != -1 ? new Polyhedron(i) : new Polyhedron(P.wythoff);
            uniform = !uniform;
        } else
            return;
        P = poly;
        repaint();
    }

    /**
     * Start the clock thead. Compensating for lost time in pausing periods.
     *
     * @see Thread#start
     */
    public void start() {
        if (clock == null) {
            if (stoppedAt != 0)
                lostTime += System.currentTimeMillis() - stoppedAt;
            clock = new Thread(this);
            clock.start();
        }
    }

    /**
     * Stop the clock thread.
     */
    public void stop() {
        if (clock != null) {
            stoppedAt = System.currentTimeMillis();
            clock = null;
        }
    }

    /**
     * The clock thread activity method. Repaints the screen 10 times in a
     * second.
     */
    public void run() {
        while (clock != null) {
            try {
                clock.sleep(100);
            } catch (Exception e) {
            }
            repaint();
        }
    }

    /**
     * If there is a polylehedron to display, invoke {@link #update}.
     */
    public void paint(Graphics g) {
        if (P == null) return;
        P.drawn = false;
        update(g);
    }

    /**
     * Update the display by computing the new rotation angle and invoking the
     * {@link Polyhedron#paint} method. The angle is conmputed from the system
     * clock, compensating for pausing periods, and a rate of 1 revolution in 5
     * seconds (12 RPM).
     */
    public void update(Graphics g) {
        if (P == null) return;
        double angle = ((clock == null ? stoppedAt :
                System.currentTimeMillis()) - lostTime) / 5000. % (2 *
                Math.PI);
        P.paint(this, g, angle, uniform);
    }

    /**
     * Returns the next polyhedron from the input source.
     */
    private Polyhedron next() {
        String sym = null;
        if (in instanceof Vector) {
            try {
                sym = (String) ((Vector) in).elementAt(index);
            } catch (Exception e) {
            }
        } else {
            sym = "#" + (index + 1);
        }
        try {
            return new Polyhedron(sym);
        } catch (Error e) {
        }
        return null;
    }
}
