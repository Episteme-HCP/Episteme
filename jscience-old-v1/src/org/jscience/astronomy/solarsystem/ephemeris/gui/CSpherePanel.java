//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import netscape.javascript.JSObject;

import org.jscience.astronomy.solarsystem.ephemeris.CalendarDate;
import org.jscience.astronomy.solarsystem.ephemeris.JulianDay;
import org.jscience.astronomy.solarsystem.ephemeris.Matrix3D;
import org.jscience.astronomy.solarsystem.ephemeris.Vector3;

import java.awt.*;
import java.awt.event.*;

import java.text.DecimalFormat;

import java.util.Enumeration;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CSpherePanel extends Panel implements ActionListener,
    MouseMotionListener, MouseListener, ComponentListener {
    /** DOCUMENT ME! */
    private static int TIME_FWRD = 0;

    /** DOCUMENT ME! */
    private static int TIME_BACK = 1;

    /** DOCUMENT ME! */
    private static int TIME_NOW = 2;

    /** DOCUMENT ME! */
    private static int AZIM_SUB = 3;

    /** DOCUMENT ME! */
    private static int AZIM_ADD = 4;

    /** DOCUMENT ME! */
    private static int ALT_DWN = 5;

    /** DOCUMENT ME! */
    private static int ALT_UP = 6;

    /** DOCUMENT ME! */
    private static int ZOOM_IN = 7;

    /** DOCUMENT ME! */
    private static int ZOOM_OUT = 8;

    /** DOCUMENT ME! */
    private static int SETTINGS = 9;

    /** DOCUMENT ME! */
    private static double[] timemap = {
            0.020833333333333332D, 1.0D, 7D, 30D, 365D, 36525D
        };

    /** DOCUMENT ME! */
    private static String[] timelabel = { "30m", "24h", "7d", "30d", "1y", "100y" };

    /** DOCUMENT ME! */
    public static final String[] monthstr = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
            "Nov", "Dec"
        };

    /** DOCUMENT ME! */
    public CelestialSphere app;

    /** DOCUMENT ME! */
    private int mousex;

    /** DOCUMENT ME! */
    private int mousey;

    /** DOCUMENT ME! */
    private Image shadow_image;

    /** DOCUMENT ME! */
    private Image scratch_image;

    /** DOCUMENT ME! */
    protected Matrix3D precess;

    /** DOCUMENT ME! */
    protected Matrix3D rotLatTime;

    /** DOCUMENT ME! */
    protected Matrix3D rotAltAz;

    /** DOCUMENT ME! */
    private Matrix3D inv_pr_rot;

    /** DOCUMENT ME! */
    private Matrix3D inv_rot;

    /** DOCUMENT ME! */
    private SettingsState state;

    /** DOCUMENT ME! */
    private DrawTask draw_task;

    /** DOCUMENT ME! */
    private SettingsDialog settings_dlg;

    /** DOCUMENT ME! */
    private UpdateTimer updatetimer;

    /** DOCUMENT ME! */
    private boolean update_shadow;

    /** DOCUMENT ME! */
    public Color buttcolor;

    /** DOCUMENT ME! */
    private String[] buttlabel = {
            ">", "<", "Now!", "-5d Azim", "+5d Azim", "-5d Alt", "+5d Alt", "In",
            "Out", "Setup"
        };

    /** DOCUMENT ME! */
    private Choice timechooser;

    /** DOCUMENT ME! */
    DecimalFormat format4;

    /** DOCUMENT ME! */
    Dimension mouseover;

/**
     * Creates a new CSpherePanel object.
     *
     * @param celestialsphere DOCUMENT ME!
     */
    public CSpherePanel(CelestialSphere celestialsphere) {
        shadow_image = null;
        scratch_image = null;
        precess = null;
        rotLatTime = null;
        rotAltAz = null;
        inv_pr_rot = null;
        inv_rot = null;
        state = null;
        draw_task = null;
        settings_dlg = null;
        updatetimer = null;
        update_shadow = false;
        buttcolor = Color.white;
        format4 = new DecimalFormat("00.0000");
        mouseover = new Dimension(100, 40);
        app = celestialsphere;
        setBackground(Color.black);
        addMouseMotionListener(this);
        addMouseListener(this);
        addComponentListener(this);
        shadow_image = app.getImage(app.getCodeBase(), "stars.gif");
        addbutt(new Button(buttlabel[TIME_BACK]));
        timechooser = new Choice();

        for (int i = 0; i < timelabel.length; i++)
            timechooser.add(timelabel[i]);

        add(timechooser);
        addbutt(new Button(buttlabel[TIME_FWRD]));

        for (int j = TIME_NOW; j <= SETTINGS; j++)
            addbutt(new Button(buttlabel[j]));

        state = new SettingsState();
        state.setLatitude((getCookieLatitude() * 3.1415926535897931D) / 180D);
        state.setLongitude((getCookieLongitude() * 3.1415926535897931D) / 180D);
        rotateView();
        settings_dlg = new SettingsDialog(this, state);
        updatetimer = new UpdateTimer(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param componentevent DOCUMENT ME!
     */
    public void componentShown(ComponentEvent componentevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param settingsstate DOCUMENT ME!
     */
    public void setState(SettingsState settingsstate) {
        state = settingsstate;
        settingsHaveChanged();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mouseDragged(MouseEvent mouseevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent mouseevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mousePressed(MouseEvent mouseevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param componentevent DOCUMENT ME!
     */
    public void componentResized(ComponentEvent componentevent) {
        System.out.println("Setting new size.");

        Rectangle rectangle = getBounds();

        if (draw_task != null) {
            scratch_image = createImage(rectangle.width, rectangle.height);
            shadow_image = createImage(rectangle.width, rectangle.height);
        }

        tryDraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @param componentevent DOCUMENT ME!
     */
    public void componentHidden(ComponentEvent componentevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mouseMoved(MouseEvent mouseevent) {
        int i = getBounds().width;
        int j = getBounds().height;
        double d = scaleFactor();
        mousex = (int) ((double) (mouseevent.getX() - (i / 2)) / d);
        mousey = (int) ((double) (-(mouseevent.getY() - (j / 2))) / d);

        if (state.info) {
            repaint(i - mouseover.width, j - mouseover.height, mouseover.width,
                mouseover.height);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent mouseevent) {
        int i = getBounds().width;
        int j = getBounds().height;
        double d = scaleFactor();
        int k = (int) ((double) (mouseevent.getX() - (i / 2)) / d);
        int l = (int) ((double) (-(mouseevent.getY() - (j / 2))) / d);
        int i1 = -(int) Math.sqrt(Math.max(0x3fff0001 - (k * k) - (l * l), 0));
        Vector3 vector3 = new Vector3(i1, k, l);
        Vector3 vector3_1 = new Vector3(0.0D, 0.0D, 0.0D);
        double d1 = 0.99950000000000006D;
        inv_pr_rot.transform(vector3);
        vector3.normalize();

        LinkBody linkbody = null;

        for (Enumeration enumeration = draw_task.links.elements();
                enumeration.hasMoreElements();) {
            LinkBody linkbody1 = (LinkBody) enumeration.nextElement();
            Vector3 vector3_2 = linkbody1.coordinate;
            vector3_1.set(vector3_2.x, vector3_2.y, vector3_2.z);
            vector3_1.normalize();

            double d2 = Vector3.dot(vector3_1, vector3);

            if (d2 > d1) {
                linkbody = linkbody1;
                d1 = d2;
            }
        }

        if (mouseevent.isControlDown()) {
            linkto(vector3, mouseevent);
        } else if (linkbody != null) {
            linkto(linkbody);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param button DOCUMENT ME!
     */
    private void addbutt(Button button) {
        add(button);
        button.addActionListener(this);
        button.setBackground(buttcolor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCookieLongitude() {
        return app.getCookieLongitude();
    }

    /**
     * DOCUMENT ME!
     *
     * @param linkbody DOCUMENT ME!
     */
    private void linkto(LinkBody linkbody) {
        String[] as = {
                linkbody.link_base + linkbody.link_extension, "seds",
                "toolbar,status,resizable,scrollbars"
            };

        try {
            JSObject jsobject = (JSObject) app.JScall("open", as);
            app.JScall(jsobject, "focus", null);
        } catch (Exception exception) {
            System.out.println("Javascript threw the following Exception");

            String s = exception.getMessage();

            if (s != null) {
                System.out.println(s);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param vector3 DOCUMENT ME!
     * @param mouseevent DOCUMENT ME!
     */
    private void linkto(Vector3 vector3, MouseEvent mouseevent) {
        vector3.normalize();

        double d = (Math.asin(vector3.z) * 180D) / 3.1415926535897931D;
        double d1 = (Math.atan2(vector3.y, vector3.x) * 180D) / 3.1415926535897931D;

        if (d1 < 0.0D) {
            d1 += 360D;
        }

        String s = format4.format(d1) + "%2C" + format4.format(d);
        SkyViewFrame skyviewframe = new SkyViewFrame(app, s);
        Point point = getLocationOnScreen();
        point.translate(mouseevent.getX() - 50, mouseevent.getY() - 50);
        skyviewframe.setLocation(point);
        skyviewframe.show();
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        settings_dlg.destroy();
        updatetimer.destroy();
        removeAll();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        super.paint(g);

        if ((draw_task != null) && update_shadow) {
            Image image = scratch_image;
            scratch_image = shadow_image;
            shadow_image = image;
            update_shadow = false;
        }

        g.drawImage(shadow_image, 0, 0, this);

        if (state.info) {
            paintInfo(g);
        }

        if (!app.isStarsLoaded() || !app.isConstelsLoaded()) {
            g.setColor(Color.white);
            g.drawString("Loading star and constellation data, please wait.",
                20, getBounds().height - 20);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param d1 DOCUMENT ME!
     */
    public void saveCookie(double d, double d1) {
        app.saveCookie(d, d1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionevent DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent actionevent) {
        CalendarDate calendardate = state.getDate();
        String s = actionevent.getActionCommand();

        if (s == buttlabel[TIME_FWRD]) {
            double d = timemap[timechooser.getSelectedIndex()];

            if (d == 365D) {
                int i = calendardate.year;

                if (calendardate.month > 2) {
                    i++;
                }

                if ((i % 4) == 0) {
                    d = 366D;
                }
            }

            state.incTimeOffset(d);
            settingsHaveChanged();
        } else if (s == buttlabel[TIME_BACK]) {
            double d1 = timemap[timechooser.getSelectedIndex()];

            if (d1 == 365D) {
                int j = calendardate.year;

                if (calendardate.month <= 2) {
                    j--;
                }

                if ((j % 4) == 0) {
                    d1 = 366D;
                }
            }

            state.decTimeOffset(d1);
            settingsHaveChanged();
        } else if (s == buttlabel[TIME_NOW]) {
            state.setTimeOffset(0.0D);
            state.setJulianDay(JulianDay.now());
            settingsHaveChanged();
        } else if (s == buttlabel[AZIM_ADD]) {
            state.incAz(0.087266462599716474D);
            settingsHaveChanged();
        } else if (s == buttlabel[AZIM_SUB]) {
            state.decAz(0.087266462599716474D);
            settingsHaveChanged();
        } else if (s == buttlabel[ALT_UP]) {
            state.incAlt(0.087266462599716474D);
            settingsHaveChanged();
        } else if (s == buttlabel[ALT_DWN]) {
            state.decAlt(0.087266462599716474D);
            settingsHaveChanged();
        } else if (s == buttlabel[ZOOM_IN]) {
            state.multZoom(1.1000000000000001D);
            settingsHaveChanged();
        } else if (s == buttlabel[ZOOM_OUT]) {
            state.multZoom(0.90909090909090906D);
            settingsHaveChanged();
        } else if (s == buttlabel[SETTINGS]) {
            Point point = getLocationOnScreen();
            settings_dlg.show(state, point.x + 100, point.y + 100);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mouseEntered(MouseEvent mouseevent) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param mouseevent DOCUMENT ME!
     */
    public void mouseExited(MouseEvent mouseevent) {
    }

    /**
     * DOCUMENT ME!
     */
    public synchronized void tryDraw() {
        if (app.isStarsLoaded()) {
            if (draw_task == null) {
                Rectangle rectangle = getBounds();

                if ((rectangle.width == 0) || (rectangle.height == 0)) {
                    return;
                }

                Image image = createImage(rectangle.width, rectangle.height);

                if (image == null) {
                    return;
                }

                Graphics g = image.getGraphics();
                g.drawImage(shadow_image, 0, 0, null);
                g.dispose();
                shadow_image = image;
                scratch_image = createImage(rectangle.width, rectangle.height);
                draw_task = new DrawTask(this);
            } else {
                draw_task.draw(scratch_image, state);
                update_shadow = true;
                repaint();
            }
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
    public void settingsHaveChanged() {
        rotateView();
        tryDraw();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double scaleFactor() {
        int i = getBounds().width;
        int j = getBounds().height;

        return ((double) Math.max(j, i) * state.getZoom()) / 65535D;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    private void paintInfo(Graphics g) {
        int i = (int) Math.sqrt(Math.max(0x3fff0001 - (mousex * mousex) -
                    (mousey * mousey), 0));
        Vector3 vector3 = new Vector3(-i, mousex, mousey);
        inv_rot.transform(vector3);
        g.setColor(Color.white);

        double d = (Math.asin(vector3.z / 32767D) * 180D) / 3.1415926535897931D;
        double d1 = (Math.atan2(vector3.y, vector3.x) * 12D) / 3.1415926535897931D;

        if (d1 < 0.0D) {
            d1 += 24D;
        }

        CalendarDate calendardate = state.getDate();
        g.drawString("" + calendardate.year + " " +
            monthstr[calendardate.month - 1] + " " +
            format4.format(calendardate.day), 20, getBounds().height - 20);

        FontMetrics fontmetrics = g.getFontMetrics();
        int j = (fontmetrics.getAscent() * 3) / 2;
        g.drawString("RA:" + format4.format(d1),
            getBounds().width - mouseover.width,
            (getBounds().height - mouseover.height) + j);
        g.drawString("DEC:" + format4.format(d),
            getBounds().width - mouseover.width,
            (getBounds().height - mouseover.height) + (j * 2));
    }

    /**
     * DOCUMENT ME!
     */
    public void updateTime() {
        if (state.animate && app.isConstelsLoaded()) {
            state.setJulianDay(JulianDay.now());
            settingsHaveChanged();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param componentevent DOCUMENT ME!
     */
    public void componentMoved(ComponentEvent componentevent) {
    }

    /**
     * DOCUMENT ME!
     */
    public void rotateView() {
        double d = state.getJulianEphemerisDay();
        double d1 = 2451545D;
        precess = JulianDay.equaPrecessMatrix(d1, d);
        rotAltAz = Matrix3D.mul(Matrix3D.rotY(1.5707963267948966D -
                    state.getAltitude()), Matrix3D.rotX(state.getAzimuth()));
        rotLatTime = Matrix3D.mul(Matrix3D.rotY(-state.getLatitude()),
                Matrix3D.rotZ(3.1415926535897931D - state.getSiderealTime()));

        Matrix3D[] amatrix3d = {
                Matrix3D.rotZ(state.getSiderealTime() - 3.1415926535897931D),
                Matrix3D.rotY(state.getLatitude()),
                Matrix3D.rotX(-state.getAzimuth()),
                Matrix3D.rotY(state.getAltitude() - 1.5707963267948966D)
            };
        inv_rot = Matrix3D.mul(amatrix3d);
        inv_pr_rot = Matrix3D.mul(JulianDay.equaPrecessMatrix(d, d1), inv_rot);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getCookieLatitude() {
        return app.getCookieLatitude();
    }
}
