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

package org.jscience.architecture.lift;

import org.jscience.architecture.lift.util.Converter;

import java.io.File;

import java.util.ArrayList;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * A simple {@link PassengerProcessor} that writes the travel and waiting
 * times of passengers to a file. Obsoleted since {@code JLESA} contains
 * logging.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see Passenger
 * @see PassengerGenerator
 */
public class SimplePassengerProcessor extends PassengerProcessor {
    /**
     * DOCUMENT ME!
     */
    private ArrayList Completed = new ArrayList();

    /**
     * DOCUMENT ME!
     */
    private String LogFileName;

/**
     * Constructor
     */
    public SimplePassengerProcessor(String LogFileName) {
        super();

        try {
            if (!((new File(LogFileName)).getCanonicalPath()
                       .startsWith((new File(".")).getCanonicalPath()))) {
                throw new RuntimeException("ERROR! Illegal filename");
            }
        } catch (Exception E) {
            throw new RuntimeException(E);
        }

        this.LogFileName = LogFileName;
    }

    /* PassengerProcessor */
    public void prepareToDie() {
        long TT = World.getTotalTicks();
        int NoP = Completed.size();
        int[] Waits = new int[NoP];
        int[] Travels = new int[NoP];
        int MaxWait = 0;
        int MaxTravel = 0;

        for (int i = 0; i < NoP; i++) {
            Passenger CP = (Passenger) Completed.get(i);
            Waits[i] = CP.getTicksWaited();
            Travels[i] = CP.getTicksTravelled();

            if (MaxWait < Waits[i]) {
                MaxWait = Waits[i];
            }

            if (MaxTravel < Travels[i]) {
                MaxTravel = Travels[i];
            }
        }

        int[] Ts = new int[MaxTravel + 1];
        int[] Ws = new int[MaxWait + 1];
        int[] Ss = new int[MaxTravel + MaxWait + 1];

        for (int i = 0; i < NoP; i++) {
            Ts[Travels[i]]++;
            Ws[Waits[i]]++;
            Ss[Waits[i] + Travels[i]]++;
        }

        String[][] Data = new String[Ss.length][4];

        for (int i = 0; i < Ss.length; i++) {
            Data[i][0] = "" + i;
            Data[i][1] = ((Ws.length <= i) ? "" : ("" + Ws[i]));
            Data[i][2] = ((Ts.length <= i) ? "" : ("" + Ts[i]));
            Data[i][3] = "" + Ss[i];
        }

        try {
            Converter.toCSV("data/jlesa/" + LogFileName, Data,
                new String[] {
                    "Number of ticks", "Passenger Wait Time (PWT)",
                    "Passenger Travel Time (PTT)", "PWT+PTT",
                    ("" + NoP + " Passengers in " + TT + " Ticks.")
                }, "NULL", ',', '"');
        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    /* Tickable */
    public String getName() {
        return ("SimplePassengerProcessor");
    }

    /* Tickable */
    public String getVersion() {
        return ("0.3.0");
    }

    /* Tickable */
    public void Tick() {
        ;
    }

    /* PassengerProcessor */
    public void created(Passenger P) {
        ;
    }

    /* PassengerProcessor */
    public void process(Passenger P) {
        Completed.add(P);
    }
}
