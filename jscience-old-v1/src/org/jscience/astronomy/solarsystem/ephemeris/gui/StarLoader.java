//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import org.jscience.astronomy.solarsystem.ephemeris.Coordinate;
import org.jscience.astronomy.solarsystem.ephemeris.ELP2000.ELP2000;
import org.jscience.astronomy.solarsystem.ephemeris.VSOP87.VSOP87;
import org.jscience.astronomy.solarsystem.ephemeris.Vector3;

import java.awt.*;

import java.io.DataInputStream;
import java.io.IOException;

import java.net.URL;

import java.util.zip.ZipInputStream;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class StarLoader implements Runnable {
    /** DOCUMENT ME! */
    static VSOP87[] VSOPbodies;

    /** DOCUMENT ME! */
    static Boolean VSOP_loaded;

    /** DOCUMENT ME! */
    static StarsArray stars;

    /** DOCUMENT ME! */
    static LineArray constels;

    /** DOCUMENT ME! */
    static LineArray boundaries;

    /** DOCUMENT ME! */
    static ShortPoint[] messier;

    /** DOCUMENT ME! */
    static ELP2000 elp2000;

    /** DOCUMENT ME! */
    static Boolean DataFile_loaded;

    static {
        VSOP_loaded = Boolean.FALSE;
        DataFile_loaded = Boolean.FALSE;
    }

    /** DOCUMENT ME! */
    private CelestialSphere app;

    /** DOCUMENT ME! */
    private boolean suicide;

/**
     * Creates a new StarLoader object.
     *
     * @param celestialsphere DOCUMENT ME!
     */
    StarLoader(CelestialSphere celestialsphere) {
        suicide = false;
        app = celestialsphere;
        (new Thread(this, "StarLoader")).start();
    }

    /**
     * DOCUMENT ME!
     */
    public void kill() {
        suicide = true;
    }

    /**
     * DOCUMENT ME!
     */
    void loadVSOP() {
        synchronized (VSOP_loaded) {
            if (VSOP_loaded.booleanValue()) {
                System.out.println("VSOP already loaded");
                app.VSOPbodies = VSOPbodies;
                app.tryDraw();
            } else {
                try {
                    URL url = new URL(app.getCodeBase(), "VSOP87bin.zip");
                    ZipInputStream zipinputstream = new ZipInputStream(url.openStream());
                    DataInputStream datainputstream = new DataInputStream(zipinputstream);
                    VSOPbodies = new VSOP87[8];

                    int i = 0;

                    do {
                        if (suicide) {
                            break;
                        }

                        zipinputstream.getNextEntry();
                        VSOPbodies[i] = new VSOP87(datainputstream);
                        System.out.println("VSOP " + i);
                    } while (++i < 8);

                    app.VSOPbodies = VSOPbodies;
                    VSOP_loaded = Boolean.TRUE;
                    datainputstream.close();
                } catch (IOException ioexception) {
                    System.out.println("Error reading VSOP data: " +
                        ioexception.getMessage());
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param word0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double extractRA(short word0) {
        double d = word0;

        if (d < 0.0D) {
            d += 65536D;
        }

        d = (d * 2D * 3.1415926535897931D) / 65536D;

        return d;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        loadDataFile();
        loadVSOP();
    }

    /**
     * DOCUMENT ME!
     *
     * @param word0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double extractDEC(short word0) {
        return ((double) word0 * 3.1415926535897931D) / 65534D;
    }

    /**
     * DOCUMENT ME!
     */
    void loadDataFile() {
        synchronized (DataFile_loaded) {
            if (DataFile_loaded.booleanValue()) {
                System.out.println("DataFile already loaded");
                app.stars = stars;
                app.messier = messier;
                app.constels = constels;
                app.boundaries = boundaries;
                app.elp2000 = elp2000;
                app.tryDraw();

                return;
            }

            try {
                URL url = new URL(app.getCodeBase(), "cspheredata.zip");
                ZipInputStream zipinputstream = new ZipInputStream(url.openStream());
                zipinputstream.getNextEntry();

                DataInputStream datainputstream = new DataInputStream(zipinputstream);
                int i = datainputstream.readInt();
                System.out.println("StarsArray gets a count of: " + i);
                stars = new StarsArray(i);

                Vector3 vector3 = new Vector3();

                for (int j = 0; j < i; j++) {
                    double d = extractRA(datainputstream.readShort());
                    double d2 = extractDEC(datainputstream.readShort());
                    byte byte0 = datainputstream.readByte();
                    byte byte1 = datainputstream.readByte();
                    Color color;

                    if (byte0 < 0) {
                        color = new Color(100, 100, 255);
                    } else if (byte0 < 19) {
                        color = new Color(175, 175, 175);
                    } else if (byte0 < 25) {
                        color = Color.yellow.darker();
                    } else if (byte0 < 45) {
                        color = Color.orange;
                    } else {
                        color = new Color(255, 90, 90);
                    }

                    Coordinate.sphereToXYZ(d, d2, vector3);
                    stars.addStar(new StarPoint(
                            (short) (int) (vector3.x * 32767D),
                            (short) (int) (vector3.y * 32767D),
                            (short) (int) (vector3.z * 32767D), color, byte1));

                    if (suicide) {
                        break;
                    }
                }

                app.stars = stars;

                if (suicide) {
                    return;
                }

                app.tryDraw();
                zipinputstream.getNextEntry();
                i = datainputstream.readInt();
                System.out.println("Messier objects: " + i);
                messier = new ShortPoint[i];
                vector3 = new Vector3(0.0D, 0.0D, 0.0D);

                for (int k = 0; k < i; k++) {
                    double d1 = extractRA(datainputstream.readShort());
                    double d3 = extractDEC(datainputstream.readShort());
                    Coordinate.sphereToXYZ(d1, d3, vector3);
                    messier[k] = new ShortPoint((short) (int) (vector3.x * 32767D),
                            (short) (int) (vector3.y * 32767D),
                            (short) (int) (vector3.z * 32767D));

                    if (suicide) {
                        break;
                    }
                }

                app.messier = messier;

                if (suicide) {
                    return;
                }

                app.tryDraw();
                zipinputstream.getNextEntry();
                constels = new LineArray(datainputstream);
                app.constels = constels;

                if (suicide) {
                    return;
                }

                app.tryDraw();
                zipinputstream.getNextEntry();
                boundaries = new LineArray(datainputstream);
                app.boundaries = boundaries;

                if (suicide) {
                    return;
                }

                app.tryDraw();
                System.out.println("Attempting to load ELP2000");
                zipinputstream.getNextEntry();
                elp2000 = new ELP2000(datainputstream);
                System.out.println("Got ELP2000");
                app.elp2000 = elp2000;

                if (suicide) {
                    return;
                }

                app.tryDraw();
                datainputstream.close();
                DataFile_loaded = Boolean.TRUE;
            } catch (IOException ioexception) {
                System.out.println("Error reading data file: " +
                    ioexception.getMessage());
            }
        }
    }
}
