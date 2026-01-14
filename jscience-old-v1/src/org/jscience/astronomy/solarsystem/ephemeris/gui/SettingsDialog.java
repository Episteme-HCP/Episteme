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

import org.jscience.astronomy.solarsystem.ephemeris.JulianDay;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class SettingsDialog implements ActionListener, WindowListener {
    /** DOCUMENT ME! */
    private final int frame_width = 400;

    /** DOCUMENT ME! */
    private final int frame_height = 350;

    /** DOCUMENT ME! */
    private final int twidth = 20;

    /** DOCUMENT ME! */
    private CSpherePanel app;

    /** DOCUMENT ME! */
    private SettingsState state;

    /** DOCUMENT ME! */
    private Frame set_frame;

    /** DOCUMENT ME! */
    Checkbox star_box;

    /** DOCUMENT ME! */
    Checkbox grat_box;

    /** DOCUMENT ME! */
    Checkbox const_box;

    /** DOCUMENT ME! */
    Checkbox names_box;

    /** DOCUMENT ME! */
    Checkbox bound_box;

    /** DOCUMENT ME! */
    Checkbox colour_box;

    /** DOCUMENT ME! */
    Checkbox pos_box;

    /** DOCUMENT ME! */
    Checkbox planets_box;

    /** DOCUMENT ME! */
    Checkbox anim_box;

    /** DOCUMENT ME! */
    Checkbox mess_box;

    /** DOCUMENT ME! */
    Checkbox horiz_box;

    /** DOCUMENT ME! */
    Checkbox brighter_box;

    /** DOCUMENT ME! */
    TextField long_field;

    /** DOCUMENT ME! */
    TextField lat_field;

    /** DOCUMENT ME! */
    TextField ra_field;

    /** DOCUMENT ME! */
    TextField dec_field;

    /** DOCUMENT ME! */
    Button apply_butt;

/**
     * Creates a new SettingsDialog object.
     *
     * @param cspherepanel  DOCUMENT ME!
     * @param settingsstate DOCUMENT ME!
     */
    SettingsDialog(CSpherePanel cspherepanel, SettingsState settingsstate) {
        set_frame = null;
        app = cspherepanel;
        state = new SettingsState(settingsstate);

        Panel panel = null;
        Panel panel1 = null;
        panel = new Panel(new GridLayout(9, 1));
        panel.add(star_box = new Checkbox("Stars"));
        panel.add(grat_box = new Checkbox("Graticule"));
        panel.add(const_box = new Checkbox("Constellations"));
        panel.add(names_box = new Checkbox("Names"));
        panel.add(bound_box = new Checkbox("Boundaries"));
        panel.add(colour_box = new Checkbox("Colour"));
        panel.add(pos_box = new Checkbox("Position Info"));
        panel.add(planets_box = new Checkbox("Planets"));
        panel.add(mess_box = new Checkbox("Messier Objects"));
        panel.add(anim_box = new Checkbox("Animate"));
        panel.add(horiz_box = new Checkbox("Clip Horizon"));
        panel.add(brighter_box = new Checkbox("Brighter"));
        reset_checkboxes();
        panel1 = new Panel();
        panel1.add(new Label("Longitude"));
        panel1.add(long_field = new TextField(String.valueOf(
                        cspherepanel.getCookieLongitude()), 20));
        panel1.add(new Label("Latitude"));
        panel1.add(lat_field = new TextField(String.valueOf(
                        cspherepanel.getCookieLatitude()), 20));
        panel1.add(new Label("Right Ascension"));
        panel1.add(ra_field = new TextField("RA", 20));
        panel1.add(new Label("Declination"));
        panel1.add(dec_field = new TextField("Dec", 20));
        ra_field.setEnabled(false);
        dec_field.setEnabled(false);
        apply_butt = new Button("Apply");
        apply_butt.addActionListener(this);
        set_frame = new Frame("Celestial Sphere: Settings");
        set_frame.setLayout(new BorderLayout());
        set_frame.add("West", panel);
        set_frame.add("Center", panel1);
        set_frame.add("South", apply_butt);
        set_frame.setSize(400, 350);
        set_frame.setBackground(Color.lightGray);
        set_frame.addWindowListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowDeactivated(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowClosing(WindowEvent windowevent) {
        cancel();
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        if (set_frame != null) {
            System.out.println("SettingsDialog: disposing frame.");
            set_frame.dispose();
            set_frame = null;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void reset_checkboxes() {
        star_box.setState(state.stars);
        grat_box.setState(state.graticule);
        const_box.setState(state.constellations);
        names_box.setState(state.names);
        bound_box.setState(state.boundaries);
        colour_box.setState(state.colour);
        pos_box.setState(state.info);
        planets_box.setState(state.planets);
        anim_box.setState(state.animate);
        mess_box.setState(state.messiers);
        horiz_box.setState(state.clip_horizon);
        brighter_box.setState(state.brighter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionevent DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent actionevent) {
        try {
            if (actionevent.getActionCommand() == apply_butt.getLabel()) {
                System.out.println("Apply settings.");
                state.stars = star_box.getState();
                state.graticule = grat_box.getState();
                state.constellations = const_box.getState();
                state.names = names_box.getState();
                state.boundaries = bound_box.getState();
                state.colour = colour_box.getState();
                state.info = pos_box.getState();
                state.planets = planets_box.getState();
                state.animate = anim_box.getState();
                state.messiers = mess_box.getState();
                state.clip_horizon = horiz_box.getState();
                state.brighter = brighter_box.getState();

                double d = Double.valueOf(lat_field.getText()).doubleValue();
                double d1 = Double.valueOf(long_field.getText()).doubleValue();

                if ((d > 90D) || (d < -90D)) {
                    throw new NumberFormatException("Latitude out of range.");
                }

                if ((d1 > 180D) || (d1 < -180D)) {
                    throw new NumberFormatException("Longitude out of range.");
                }

                state.setLatitude((d * 3.1415926535897931D) / 180D);
                state.setLongitude((d1 * 3.1415926535897931D) / 180D);
                state.setJulianDay(JulianDay.now());
                app.saveCookie(d, d1);
                app.setState(state);
                set_frame.setVisible(false);
            }
        } catch (NumberFormatException numberformatexception) {
            new AlertBox(set_frame,
                "Please enter valid numbers: " +
                numberformatexception.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowOpened(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowClosed(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowDeiconified(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowActivated(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param settingsstate DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    public void show(SettingsState settingsstate, int i, int j) {
        if (!set_frame.isVisible()) {
            state.set(settingsstate);
            set_frame.setLocation(i, j);
        }

        set_frame.show();
    }

    /**
     * DOCUMENT ME!
     */
    protected void finalize() {
        destroy();
    }

    /**
     * DOCUMENT ME!
     *
     * @param windowevent DOCUMENT ME!
     */
    public void windowIconified(WindowEvent windowevent) {
    }

    /**
     * DOCUMENT ME!
     */
    public void cancel() {
        reset_checkboxes();
        set_frame.setVisible(false);
    }
}
