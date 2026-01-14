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

package org.jscience.astronomy.solarsystem.artificialsatellites;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.SortedSet;

/**
 * DRIVER                                                 3 NOV 80
 * <p/>
 * WGS-72 PHYSICAL AND GEOPOTENTIAL CONSTANTS
 * CK2= .5*J2*AE**2     CK4=-.375*J4*AE**4
 */
public class DRIVER {

    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static PrintStream out = System.out;

    public static void main(String[] args) throws Exception {

        // SELECT EPHEMERIS TYPE AND OUTPUT TIMES

        while (true) {

            String controlLine = in.readLine();
            String[] ABUF = new String[]{in.readLine(), in.readLine()};
            int IEPT = Integer.parseInt(controlLine.substring(0, 1));
            if (IEPT <= 0) return;
            if (IEPT > 5) {
                out.println("EPHEMERIS NUMBER " + IEPT + " NOT LEGAL, WILL SKIP THIS CASE");
                continue;
            }

            ElementSet es = new ElementSet(ABUF[0], ABUF[1]);

            // INPUT CHECK FOR PERIOD VS EPHEMERIS SELECTED
            // PERIOD GE 225 MINUTES  IS DEEP SPACE

            PropagatorFactory propagatorFactory = new PropagatorFactory();
            Propagator p = propagatorFactory.newInstance(String.valueOf(IEPT), es);

            if (es.isDeep() && !p.isDeep()) {
                out.println("SHOULD USE DEEP SPACE EPHEMERIS");
            }
            if (!es.isDeep() && p.isDeep()) {
                out.println("SHOULD USE NEAR EARTH EPHEMERIS");
            }

            out.println("1" + ABUF[0] + "\n" + " " + ABUF[1] + "\n\n" + " " + p.getName() +
                    " TSINCE" + "              " + "X" + "                " + "Y" + "                " +
                    "Z" + "              " + "XDOT" + "             " + "YDOT" + "             " + "ZDOT" + "\n");

            double TS = Double.parseDouble(controlLine.substring(1, 11));
            double TF = Double.parseDouble(controlLine.substring(11, 21));
            double DELT = Double.parseDouble(controlLine.substring(21, 31));
            SortedSet ephemeris = p.generateEphemeris(TS, TF, DELT);
            Iterator iter = ephemeris.iterator();
            while (iter.hasNext()) {
                out.println(format_705((OrbitalState) iter.next()));
            }
        }

    }

    private static String format_705(OrbitalState s) {

        StringBuffer buffer = new StringBuffer();
        NumberFormat f = new DecimalFormat("0.00000000");
        double[] values = new double[]{s.TSINCE, s.X, s.Y, s.Z, s.XDOT, s.YDOT, s.ZDOT};
        for (int i = 0; i < values.length; i++) {
            String str = "                 " + f.format(values[i]);
            int strLength = str.length();
            str = str.substring(strLength - 17, strLength);
            buffer.append(str);
        }

        return buffer.toString();

    }

    /**
     * Deny the ability to construct an instance of this class.
     */
    private DRIVER() {
    }

}
