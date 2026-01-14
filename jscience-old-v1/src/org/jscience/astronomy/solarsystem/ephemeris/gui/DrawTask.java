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

import org.jscience.astronomy.solarsystem.ephemeris.*;

import java.awt.*;

import java.util.Hashtable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class DrawTask {
    /** DOCUMENT ME! */
    public CelestialSphere app;

    /** DOCUMENT ME! */
    private CSpherePanel panel;

    /** DOCUMENT ME! */
    private SettingsState state;

    /** DOCUMENT ME! */
    private int height;

    /** DOCUMENT ME! */
    private int width;

    /** DOCUMENT ME! */
    private boolean discarded;

    /** DOCUMENT ME! */
    private Constellations constellations;

    /** DOCUMENT ME! */
    private Orrery orrery;

    /** DOCUMENT ME! */
    public Hashtable links;

    /** DOCUMENT ME! */
    private int moon_linked;

    /** DOCUMENT ME! */
    private int planets_linked;

    /** DOCUMENT ME! */
    private String[] planet_name = {
            "Me", "Ve", "Sol", "Ma", "Ju", "Sa", "Ur", "Ne", "Pl"
        };

    /** DOCUMENT ME! */
    private int messiers_linked;

/**
     * Creates a new DrawTask object.
     *
     * @param cspherepanel DOCUMENT ME!
     */
    DrawTask(CSpherePanel cspherepanel) {
        discarded = false;
        constellations = null;
        orrery = null;
        links = new Hashtable();
        moon_linked = 0;
        planets_linked = 0;
        messiers_linked = 0;
        constellations = new Constellations();
        constellations.link(links);
        orrery = new Orrery();
        panel = cspherepanel;
        app = cspherepanel.app;
        System.out.println("DrawTask initialized.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    void drawObject(Graphics g, int i, int j) {
        g.drawOval(i - 5, j - 3, 10, 6);
    }

    /**
     * DOCUMENT ME!
     */
    private void linkPlanets() {
        String[] as = {
                "mercury", "venus", "sol", "mars", "jupiter", "saturn", "uranus",
                "neptune", "pluto"
            };

        for (int i = 0; i < as.length; i++) {
            LinkBody linkbody = new LinkBody("http://www.seds.org/nineplanets/nineplanets/",
                    as[i] + ".html");
            links.put(planet_name[i], linkbody);
        }

        links.put("Ju", new LinkBody("", "jupiter.html"));
        planets_linked = 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawMoon(Graphics g, Color color, double d, double d1) {
        if (moon_linked == 0) {
            LinkBody linkbody = new LinkBody("", "moon.html");
            links.put("Luna", linkbody);
            moon_linked = 1;
        }

        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);
        double d2 = state.getJulianEphemerisDay();
        g.setColor(color);

        Vector3 vector3 = app.elp2000.findFK5(d2);
        Vector3 vector3_1 = Observer.geocentricEquatorialXYZ(state.getSiderealTime(),
                state.getLatitude());
        vector3_1 = Vector3.neg(vector3_1);
        vector3 = Vector3.add(vector3, vector3_1);
        vector3.normalize();
        vector3.scale(32767D);

        LinkBody linkbody1 = (LinkBody) links.get("Luna");
        linkbody1.coordinate.set(vector3.x, vector3.y, vector3.z);

        if (state.clip_horizon) {
            matrix3d.transform(vector3);

            if (vector3.x > 0.0D) {
                return;
            }

            matrix3d1.transform(vector3);
        } else {
            matrix3d2.transform(vector3);
        }

        if (vector3.x < d) {
            vector3.scale(d1);
            drawBody(g, "Moon", (int) vector3.y, -(int) vector3.z, 12);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param s DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    private void drawBody(Graphics g, String s, int i, int j, int k) {
        i -= (k / 2);
        j -= (k / 2);
        g.fillOval(i, j, k, k);

        if (state.names) {
            g.drawString(s, i + 10, j + 10);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     * @param settingsstate DOCUMENT ME!
     */
    public void draw(Image image, SettingsState settingsstate) {
        height = image.getHeight(null);
        width = image.getWidth(null);
        state = new SettingsState(settingsstate);

        Graphics g = image.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, width, height);
        g.translate(width / 2, height / 2);

        double d1 = state.getZoom();
        double d;

        if (d1 > Math.sqrt(2D)) {
            d = -32767D * Math.sqrt(1.0D - (2D / (d1 * d1)));
        } else {
            d = 0.0D;
        }

        double d2 = panel.scaleFactor();

        if (state.graticule) {
            drawGraticule(g, Color.blue.darker(), d, d2);
        }

        drawZenith(g, Color.yellow, d2);

        if (state.boundaries && app.isBoundariesLoaded()) {
            drawLines(g, Color.red.darker(), app.boundaries, d, d2);
        }

        if (state.names && (state.boundaries || state.constellations)) {
            drawConstelNames(g, Color.red, d, d2);
        }

        Color color = new Color(0, 140, 0);

        if (state.constellations && app.isConstelsLoaded()) {
            drawLines(g, color, app.constels, d, d2);
        }

        if (state.messiers && app.isMessiersLoaded()) {
            drawMessiers(g, app.messier, d, d2);
        }

        if (state.stars && app.isStarsLoaded()) {
            drawStars(g, app.stars, d, d2);
        }

        if (state.planets) {
            if (app.isVSOPLoaded()) {
                drawVSOPplanets(g, Color.cyan, d, d2);
            } else {
                drawPlanets(g, Color.cyan, d, d2);
            }

            if (app.elp2000 != null) {
                drawMoon(g, Color.white, d, d2);
            }
        }

        g.dispose();
    }

    /**
     * DOCUMENT ME!
     *
     * @param ashortpoint DOCUMENT ME!
     */
    private void linkMessiers(ShortPoint[] ashortpoint) {
        String s = "http://www.seds.org/messier/m/";

        for (int i = 0; i < ashortpoint.length; i++) {
            int j = i + 1;
            StringBuffer stringbuffer = new StringBuffer("m");

            if (j < 100) {
                stringbuffer.append("0");
            }

            if (j < 10) {
                stringbuffer.append("0");
            }

            stringbuffer.append(j);

            LinkBody linkbody = new LinkBody("http://www.seds.org/messier/m/",
                    stringbuffer.toString() + ".html");
            linkbody.coordinate.set(ashortpoint[i].x, ashortpoint[i].y,
                ashortpoint[i].z);
            links.put(stringbuffer.toString(), linkbody);
        }

        messiers_linked = 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawGraticule(Graphics g, Color color, double d, double d1) {
        g.setColor(Color.orange.darker());
        lineRA(0.0D, g, d, d1);
        lineDEC(0.0D, g, d, d1, panel.rotAltAz, panel.rotLatTime);
        g.setColor(color);

        for (double d2 = 0.26179938779914941D; d2 < 6.2517693806436885D;
                d2 += 0.26179938779914941D)
            lineRA(d2, g, d, d1);

        for (double d3 = 0.17453292519943295D; d3 < 1.5707963267948966D;
                d3 += 0.17453292519943295D)
            lineDEC(d3, g, d, d1, panel.rotAltAz, panel.rotLatTime);

        for (double d4 = -0.17453292519943295D; d4 > -1.5707963267948966D;
                d4 -= 0.17453292519943295D)
            lineDEC(d4, g, d, d1, panel.rotAltAz, panel.rotLatTime);

        g.setColor(Color.magenta.darker());
        lineDEC(0.0D, g, d, d1, panel.rotAltAz,
            Matrix3D.mul(panel.rotLatTime,
                Matrix3D.rotX(Ecliptic.obliquity(state.getJulianEphemerisDay()))));
        g.setColor(Color.white);
        lineDEC(0.0D, g, d, d1, Matrix3D.identity(),
            Matrix3D.mul(panel.rotAltAz, Matrix3D.rotY(1.5707963267948966D)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawVSOPplanets(Graphics g, Color color, double d, double d1) {
        if (planets_linked == 0) {
            linkPlanets();
        }

        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);
        double d2 = state.getJulianEphemerisDay();
        g.setColor(color);

        Vector3 vector3 = app.VSOPbodies[2].findFK5(d2);
        vector3 = Vector3.neg(vector3);

        int i = 0;

        do {
            byte byte0 = 6;
            String s = planet_name[i];
            Vector3 vector3_1;

            if (i == 2) {
                vector3_1 = new Vector3(vector3);
                g.setColor(Color.yellow);
                byte0 = 12;
            } else {
                if (i == 8) {
                    vector3_1 = orrery.findHREq(8, d2);
                } else {
                    vector3_1 = app.VSOPbodies[i].findFK5(d2);
                }

                g.setColor(color);
                vector3_1 = Vector3.add(vector3_1, vector3);
            }

            vector3_1.normalize();
            vector3_1.scale(32767D);

            LinkBody linkbody = (LinkBody) links.get(s);
            linkbody.coordinate.set(vector3_1.x, vector3_1.y, vector3_1.z);

            if (state.clip_horizon) {
                matrix3d.transform(vector3_1);

                if (vector3_1.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3_1);
            } else {
                matrix3d2.transform(vector3_1);
            }

            if (vector3_1.x < d) {
                vector3_1.scale(d1);
                drawBody(g, planet_name[i], (int) vector3_1.y,
                    -(int) vector3_1.z, byte0);
            }
        } while (++i < 9);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawPlanets(Graphics g, Color color, double d, double d1) {
        if (planets_linked == 0) {
            linkPlanets();
        }

        double d2 = state.getJulianEphemerisDay();
        g.setColor(color);

        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);
        Vector3 vector3 = new Vector3();
        int i = 0;

        do {
            if (i == 2) {
                continue;
            }

            GeocentricCoordinate geocentriccoordinate = orrery.geocentricCoord(i,
                    d2);
            Coordinate.sphereToXYZ(geocentriccoordinate.rightAscension(),
                geocentriccoordinate.declination(), vector3);
            vector3.scale(32767D);

            LinkBody linkbody = (LinkBody) links.get(planet_name[i]);
            linkbody.coordinate.set(vector3.x, vector3.y, vector3.z);

            if (state.clip_horizon) {
                matrix3d.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3);
            } else {
                matrix3d2.transform(vector3);
            }

            if (vector3.x < d) {
                vector3.scale(d1);
                drawBody(g, planet_name[i], (int) vector3.y, -(int) vector3.z, 6);
            }
        } while (++i < 9);

        GeocentricCoordinate geocentriccoordinate1 = orrery.helos(d2);
        Coordinate.sphereToXYZ(geocentriccoordinate1.rightAscension(),
            geocentriccoordinate1.declination(), vector3);
        vector3.scale(32767D);

        LinkBody linkbody1 = (LinkBody) links.get(planet_name[2]);
        linkbody1.coordinate.set(vector3.x, vector3.y, vector3.z);
        matrix3d.transform(vector3);

        if ((vector3.x < 0.0D) || !state.clip_horizon) {
            matrix3d1.transform(vector3);

            if (vector3.x < d) {
                vector3.scale(d1);
                g.setColor(Color.yellow);
                drawBody(g, "Sol", (int) vector3.y, -(int) vector3.z, 12);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param linearray DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawLines(Graphics g, Color color, LineArray linearray,
        double d, double d1) {
        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);
        Vector3 vector3 = new Vector3(0.0D, 0.0D, 0.0D);
        Vector3 vector3_1 = new Vector3(0.0D, 0.0D, 0.0D);
        g.setColor(color);

        int i = linearray.getCount();

        for (int j = 0; j < i; j++) {
            ShortLine shortline = linearray.getLine(j);
            vector3.set(shortline.start.x, shortline.start.y, shortline.start.z);
            vector3_1.set(shortline.end.x, shortline.end.y, shortline.end.z);

            if (state.clip_horizon) {
                matrix3d.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d.transform(vector3_1);

                if (vector3_1.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3);
                matrix3d1.transform(vector3_1);
            } else {
                matrix3d2.transform(vector3);
                matrix3d2.transform(vector3_1);
            }

            if ((vector3.x < d) && (vector3_1.x < d)) {
                vector3.scale(d1);
                vector3_1.scale(d1);
                g.drawLine((int) vector3.y, -(int) vector3.z,
                    (int) vector3_1.y, -(int) vector3_1.z);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     */
    void drawLinePoint(Graphics g, int i, int j) {
        drawStar(g, i, j, 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param d DOCUMENT ME!
     */
    private void drawZenith(Graphics g, Color color, double d) {
        g.setColor(color);

        Matrix3D matrix3d = Matrix3D.rotX(state.getAltitude() -
                1.5707963267948966D);
        Vector3 vector3 = new Vector3(0.0D, 0.0D, -32767D);
        matrix3d.transform(vector3);
        vector3.scale(d);
        g.drawLine((int) vector3.x - 5, (int) vector3.y, (int) vector3.x + 5,
            (int) vector3.y);
        g.drawLine((int) vector3.x, (int) vector3.y - 5, (int) vector3.x,
            (int) vector3.y + 5);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param ashortpoint DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawMessiers(Graphics g, ShortPoint[] ashortpoint, double d,
        double d1) {
        if (messiers_linked == 0) {
            linkMessiers(ashortpoint);
        }

        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);
        Vector3 vector3 = new Vector3(0.0D, 0.0D, 0.0D);

        for (int i = 0; i < ashortpoint.length; i++) {
            vector3.set(ashortpoint[i].x, ashortpoint[i].y, ashortpoint[i].z);

            if (state.clip_horizon) {
                matrix3d.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3);
            } else {
                matrix3d2.transform(vector3);
            }

            if (vector3.x < d) {
                vector3.scale(d1);
                g.setColor(Color.magenta);

                int j = (int) vector3.y;
                int k = -(int) vector3.z;
                drawObject(g, j, k);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     * @param matrix3d DOCUMENT ME!
     * @param matrix3d1 DOCUMENT ME!
     */
    private void lineDEC(double d, Graphics g, double d1, double d2,
        Matrix3D matrix3d, Matrix3D matrix3d1) {
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d, matrix3d1);
        Vector3 vector3 = new Vector3();
        double d3 = 32767D * Math.sin(d);
        double d4 = 32767D * Math.cos(d);

        for (double d5 = 0.0D; d5 < 6.2831853071795862D;
                d5 += 0.017453292519943295D) {
            vector3.x = d4 * Math.cos(d5);
            vector3.y = d4 * Math.sin(d5);
            vector3.z = d3;

            if (state.clip_horizon) {
                matrix3d1.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d.transform(vector3);
            } else {
                matrix3d2.transform(vector3);
            }

            if (vector3.x < d1) {
                vector3.scale(d2);
                drawLinePoint(g, (int) vector3.y, -(int) vector3.z);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void finalize() {
        System.out.println("DrawTask cleaning up.");
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param i DOCUMENT ME!
     * @param j DOCUMENT ME!
     * @param k DOCUMENT ME!
     */
    void drawStar(Graphics g, int i, int j, int k) {
        int l = (k + 1) / 2;

        if (k > 2) {
            g.fillOval(i - l, j - l, k, k);
        } else if (k > 1) {
            g.drawOval(i - 1, j - 1, 1, 1);
        } else {
            g.drawLine(i, j, i, j);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param color DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawConstelNames(Graphics g, Color color, double d, double d1) {
        g.setColor(color);

        int i = 0;
        byte byte0 = 89;
        Vector3 vector3 = new Vector3(0.0D, 0.0D, 0.0D);
        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);

        for (int j = 0; j < byte0; j++) {
            vector3.set(-Constellations.raw_coord[i + 2],
                Constellations.raw_coord[i], Constellations.raw_coord[i + 1]);
            i += 3;

            if (state.clip_horizon) {
                matrix3d.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3);
            } else {
                matrix3d2.transform(vector3);
            }

            if (vector3.x < d) {
                vector3.scale(d1);
                g.drawString(Constellations.name[j], (int) vector3.y,
                    -(int) vector3.z);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param starsarray DOCUMENT ME!
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    private void drawStars(Graphics g, StarsArray starsarray, double d,
        double d1) {
        Matrix3D matrix3d = Matrix3D.mul(panel.rotLatTime, panel.precess);
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);
        double d2 = 1.0D / (1.0D +
            (0.10000000000000001D * Math.log(state.getZoom())));
        Color color = Color.white;
        Color color1 = Color.lightGray;
        Color.lightGray.darker();
        g.setColor(color);

        Vector3 vector3 = new Vector3(0.0D, 0.0D, 0.0D);
        int i = starsarray.getCount();

        for (int j = 0; j < i; j++) {
            StarPoint starpoint = starsarray.getStar(j);
            vector3.set(starpoint.x, starpoint.y, starpoint.z);

            if (state.clip_horizon) {
                matrix3d.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3);
            } else {
                matrix3d2.transform(vector3);
            }

            if (vector3.x < d) {
                vector3.scale(d1);

                int k = (int) vector3.y;
                int l = -(int) vector3.z;
                double d3 = (double) starpoint.magnitude * d2;

                if (state.colour) {
                    g.setColor(starpoint.colour);

                    if (d3 > 80D) {
                        drawStar(g, k, l, 0);
                    } else if (d3 > 48D) {
                        drawStar(g, k, l, 2);
                    } else if (d3 > 32D) {
                        drawStar(g, k, l, 3);
                    } else {
                        drawStar(g, k, l, 4);
                    }
                } else if (d3 > 96D) {
                    g.setColor(color1);
                    drawStar(g, k, l, 1);
                } else if (d3 > 80D) {
                    g.setColor(color);
                    drawStar(g, k, l, 1);
                } else if (d3 > 64D) {
                    g.setColor(color1);
                    drawStar(g, k, l, 2);
                } else if (d3 > 48D) {
                    g.setColor(color);
                    drawStar(g, k, l, 3);
                } else if (d3 > 32D) {
                    g.setColor(color);
                    drawStar(g, k, l, 4);
                } else {
                    g.setColor(color);
                    drawStar(g, k, l, 5);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     */
    private void lineRA(double d, Graphics g, double d1, double d2) {
        Vector3 vector3 = new Vector3();
        Matrix3D matrix3d = panel.rotLatTime;
        Matrix3D matrix3d1 = panel.rotAltAz;
        Matrix3D matrix3d2 = Matrix3D.mul(matrix3d1, matrix3d);

        for (double d3 = -1.5707963267948966D; d3 < 1.5707963267948966D;
                d3 += 0.017453292519943295D) {
            vector3.x = Math.cos(d3) * Math.cos(d);
            vector3.y = Math.cos(d3) * Math.sin(d);
            vector3.z = Math.sin(d3);

            if (state.clip_horizon) {
                matrix3d.transform(vector3);

                if (vector3.x > 0.0D) {
                    continue;
                }

                matrix3d1.transform(vector3);
            } else {
                matrix3d2.transform(vector3);
            }

            vector3.scale(32767D);

            if (vector3.x < d1) {
                vector3.scale(d2);
                drawLinePoint(g, (int) vector3.y, -(int) vector3.z);
            }
        }
    }
}
