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

package org.jscience.astronomy.solarsystem;

import java.io.*;

import java.net.URL;
import java.net.URLConnection;

import java.util.Vector;


/**
 * The KnownArtificialSatellitesFactory class provides support for
 * artificial satellites of the solar system.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//uses the data like liftoff.msfc.nasa.gov/RealTime/Jtrack/3d/JTrack3D.html

//this code is reverse engineered after NASA
//http://science.nasa.gov/Realtime/JTrack/3D/
//this is illegal and you should not read this
public class KnownArtificialSatellitesFactory extends Object {
    /**
     * DOCUMENT ME!
     */
    static Vector jm296 = new Vector();

    /**
     * DOCUMENT ME!
     */
    static Vector jm297 = new Vector();

    /**
     * DOCUMENT ME!
     */
    static Vector jm298 = new Vector();

    /**
     * DOCUMENT ME!
     */
    static int jm299; //Num sats

    /**
     * DOCUMENT ME!
     */
    static URL jm300;

    /**
     * DOCUMENT ME!
     */
    static long jm301;

    /**
     * DOCUMENT ME!
     */
    String jm302;

    /**
     * DOCUMENT ME!
     */
    String jm303;

    /**
     * DOCUMENT ME!
     */
    int jm304;

    /**
     * DOCUMENT ME!
     */
    double jm305;

    /**
     * DOCUMENT ME!
     */
    float jm306;

    /**
     * DOCUMENT ME!
     */
    float jm307;

    /**
     * DOCUMENT ME!
     */
    float jm308;

    /**
     * DOCUMENT ME!
     */
    float jm309;

    /**
     * DOCUMENT ME!
     */
    float jm310;

    /**
     * DOCUMENT ME!
     */
    float jm311;

    /**
     * DOCUMENT ME!
     */
    float jm312;

    /**
     * DOCUMENT ME!
     */
    float jm313;

    /**
     * DOCUMENT ME!
     */
    float jm314;

    /**
     * DOCUMENT ME!
     */
    float jm315;

    /**
     * DOCUMENT ME!
     */
    double jm316;

    /**
     * DOCUMENT ME!
     */
    int jm7;

    /**
     * DOCUMENT ME!
     */
    int jm6;

    /**
     * DOCUMENT ME!
     */
    float jm317;

    // jm285
    /**
     * Creates a new KnownArtificialSatellitesFactory object.
     */
    public KnownArtificialSatellitesFactory() {
    }

    /**
     * DOCUMENT ME!
     */
    public void getArtificialSatellites() {
        try {
            long l = System.currentTimeMillis();
            URL url = new URL(jm383(), "artificialsatellites.txt");
            URL url1 = new URL(jm383(), "artificialsatelliteslaunchers.txt");
            jm285.jm294(url, url1);
            System.out.println("Download time = " +
                (System.currentTimeMillis() - l));

            return;
        } catch (Exception _ex) {
            return;
        }

        //public void run() {
        jm384();
        jm392.jm341();
        jm342.jm236();
        jm342.jm233(jm318.jm327());

        if (jm393 == null) {
            int i = jm285.jm292("Shuttle");

            if (i != -1) {
                jm342.jm239(i);
            }
        } else {
            System.out.println("Looking up = " + jm393);

            int j = jm285.jm292(jm393);
            System.out.println("select = " + j);

            if (j != -1) {
                jm342.jm239(j);
            }
        }

        //  }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inputstream DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static void jm286(InputStream inputstream) throws IOException {
        DataInputStream datainputstream = new DataInputStream(inputstream);
        String s;

        while (((s = datainputstream.readLine()) != null) && (s.length() > 1)) {
            String s1 = datainputstream.readLine();
            String s2 = datainputstream.readLine();
            jm296.addElement(jm48(s, s1, s2));
            jm299++;
        }

        inputstream.close();
        datainputstream.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataoutputstream DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void jm287(DataOutputStream dataoutputstream)
        throws IOException {
        String s = "                        ";
        byte[] abyte0 = new byte[24];
        byte[] abyte1 = new byte[8];
        dataoutputstream.writeFloat(jm314);
        dataoutputstream.writeFloat(jm312);
        dataoutputstream.writeFloat(jm309);
        dataoutputstream.writeFloat(jm310);
        dataoutputstream.writeFloat(jm313);
        dataoutputstream.writeFloat(jm317);
        dataoutputstream.writeFloat(jm315);
        dataoutputstream.writeFloat(jm306);
        dataoutputstream.writeFloat(jm307);
        dataoutputstream.writeFloat(jm308);
        dataoutputstream.writeDouble(jm305);
        dataoutputstream.writeDouble(jm316);
        dataoutputstream.writeInt(jm6);
        dataoutputstream.writeInt(jm7);
        dataoutputstream.writeInt(jm304);
        s.getBytes(0, 24, abyte0, 0);
        jm302.getBytes(0, jm302.length(), abyte0, 0);
        dataoutputstream.write(abyte0, 0, 24);
        s.getBytes(0, 8, abyte1, 0);
        jm303.getBytes(0, jm303.length(), abyte1, 0);
        dataoutputstream.write(abyte1, 0, 8);
    }

    /**
     * DOCUMENT ME!
     *
     * @param datainputstream DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void jm288(DataInputStream datainputstream)
        throws IOException {
        byte[] abyte0 = new byte[24];
        byte[] abyte1 = new byte[8];
        jm314 = datainputstream.readFloat();
        jm312 = datainputstream.readFloat();
        jm309 = datainputstream.readFloat();
        jm310 = datainputstream.readFloat();
        jm313 = datainputstream.readFloat();
        jm317 = datainputstream.readFloat();
        jm315 = datainputstream.readFloat();
        jm306 = datainputstream.readFloat();
        jm307 = datainputstream.readFloat();
        jm308 = datainputstream.readFloat();
        jm305 = datainputstream.readDouble();
        jm316 = datainputstream.readDouble();
        jm6 = datainputstream.readInt();
        jm7 = datainputstream.readInt();
        jm304 = datainputstream.readInt();
        datainputstream.read(abyte0, 0, 24);
        jm302 = (new String(abyte0, 0)).trim();
        datainputstream.read(abyte1, 0, 8);
        jm303 = (new String(abyte1, 0)).trim();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static int jm292(String s) {
        boolean flag = false;
        int i = -1;

        for (int j = 0; (j < jm299) && !flag; j++)
            if (((jm285) jm296.elementAt(j)).jm302.equalsIgnoreCase(s)) {
                flag = true;
                i = j;
            }

        return i;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inputstream DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static void jm293(InputStream inputstream)
        throws IOException {
        DataInputStream datainputstream = new DataInputStream(inputstream);
        boolean flag = true;
        String s;

        while ((s = datainputstream.readLine()) != null)

            if (s.equals("*")) {
                flag = false;
            } else if (flag) {
                jm297.addElement(s);
            } else {
                jm298.addElement(s);
            }

        inputstream.close();
        datainputstream.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @param url1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static String jm294(URL url, URL url1) throws IOException {
        if (url != null) {
            jm300 = url;
            jm301 = 0L;
        }

        if (url1 != null) {
            BufferedInputStream bufferedinputstream = new BufferedInputStream(url1.openStream());
            jm293(bufferedinputstream);
        }

        return jm294();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static String jm294() throws IOException {
        if (jm300 == null) {
            return "No address given for vector database";
        }

        URLConnection urlconnection = jm300.openConnection();
        urlconnection.connect();

        long l = urlconnection.getLastModified();

        if ((l > jm301) || (l == 0L)) {
            jm291();

            BufferedInputStream bufferedinputstream = new BufferedInputStream(jm300.openStream());
            jm286(bufferedinputstream);
            jm301 = l;

            return "Database downloaded";
        } else {
            return "Current database already downloaded.";
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param inputstream DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static void jm295(InputStream inputstream) throws IOException {
        DataInputStream datainputstream = new DataInputStream(inputstream);

        for (boolean flag = false; !flag;)
            try {
                jm285 jm285_1 = new jm285();
                jm285_1.jm288(datainputstream);
                jm296.addElement(jm285_1);
                jm299++;
            } catch (EOFException _ex) {
                flag = true;
            }

        inputstream.close();
        datainputstream.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static jm285 jm48(String s, String s1, String s2) {
        jm285 jm285_1 = new jm285();
        jm285_1.jm302 = s.trim();

        if (s != null) {
            jm285_1.jm302 = s.substring(0, 23).trim();
            jm285_1.jm7 = Integer.parseInt(s.substring(24, 35));
            jm285_1.jm6 = Integer.parseInt(s.substring(35, 46));
        }

        if (s1 != null) {
            jm285_1.jm303 = new String(s1.substring(9, 15));
            jm285_1.jm304 = Integer.parseInt(s1.substring(18, 20));

            if (jm285_1.jm304 < 50) {
                jm285_1.jm304 += 100;
            }

            jm285_1.jm305 = Double.valueOf(s1.substring(20, 32)).doubleValue();
            jm285_1.jm306 = Float.valueOf(s1.substring(33, 43)).floatValue();
            jm285_1.jm307 = Float.valueOf("0." + s1.substring(45, 50))
                                 .floatValue();
            jm285_1.jm308 = Float.valueOf(s1.substring(50, 52)).floatValue();

            if (s1.substring(53, 54) != "-") {
                jm285_1.jm309 = Float.valueOf("0." +
                        s1.substring(54, 59).trim()).floatValue();
            } else {
                jm285_1.jm309 = Float.valueOf("-0." +
                        s1.substring(54, 59).trim()).floatValue();
            }

            jm285_1.jm310 = Float.valueOf("-" + s1.substring(60, 61))
                                 .floatValue();
        }

        if (s2 != null) {
            jm285_1.jm311 = Float.valueOf(s2.substring(8, 16)).floatValue();
            jm285_1.jm312 = Float.valueOf(s2.substring(17, 25)).floatValue();
            jm285_1.jm313 = Float.valueOf("0." + s2.substring(26, 33))
                                 .floatValue();
            jm285_1.jm314 = Float.valueOf(s2.substring(34, 42)).floatValue();
            jm285_1.jm315 = Float.valueOf(s2.substring(43, 51)).floatValue();
            jm285_1.jm316 = Double.valueOf(s2.substring(52, 63)).doubleValue();
        }

        return jm285_1;
    }

    /**
     * DOCUMENT ME!
     */
    public void getInfo() {
        try {
            int j = jm285.jm292(jm9.getSelectedItem());
            jm285 jm285_1 = jm285.jm289(j);
            String s = jm285_1.jm303.substring(0, 2) + "-" +
                jm285_1.jm303.substring(2);
            int l = Integer.parseInt(jm285_1.jm303.substring(0, 2));

            if (l < 20) {
                s = "20" + s;
            } else {
                s = "19" + s;
            }

            String s1 = "http://liftoff.msfc.nasa.gov/realtime/satlookup.aspx?sc=" +
                s;
            URL url = new URL(s1);
            jm11.showDocument(url, "main");
        } catch (Exception _ex) {
        }
    }
}
