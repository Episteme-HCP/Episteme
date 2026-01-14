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

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a sample class (Quick, Dirty, Ineffective, but easy to understand)
 * descendant of the abstract PassengerProcessor class. It logs some passenger
 * information to files. You don't need to use this class as there is built-in
 * logging in {@code JLESA} from version {@code 0.2.0}.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 *
 * @see LoadDependentKinematicModel
 */
public class LoggerPassengerProcessor extends PassengerProcessor {
    /**
     * DOCUMENT ME!
     */
    private String LGP = null;

    /**
     * DOCUMENT ME!
     */
    private String LPT = null;

    /**
     * DOCUMENT ME!
     */
    private String LQL = null;

/**
     * Constructor ({@code null} parameters disable corresponding logging).
     *
     * @param GeneratedPassengersFile Filename for logging the {@link Passenger}s generated?
     * @param PassengerTimesFile      Filename for logging {@link Passenger} waiting and travel times?
     * @param QueuLengthsFile         Filename for logging the lengths of {@link Passenger} queus?
     */
    public LoggerPassengerProcessor(String GeneratedPassengersFile,
        String PassengerTimesFile, String QueuLengthsFile) {
        super();

        if (GeneratedPassengersFile != null) {
            if (!EntryPoint.goodFileName(GeneratedPassengersFile, "data/jlesa/")) {
                throw new RuntimeException("Illegal filename!");
            }

            LGP = "data/jlesa/" + GeneratedPassengersFile;
            (new File(LGP)).delete();
        }

        if (PassengerTimesFile != null) {
            if (!EntryPoint.goodFileName(PassengerTimesFile, "data/jlesa/")) {
                throw new RuntimeException("Illegal filename!");
            }

            LPT = "data/jlesa/" + PassengerTimesFile;
            (new File(LPT)).delete();
        }

        if (QueuLengthsFile != null) {
            if (!EntryPoint.goodFileName(QueuLengthsFile, "data/jlesa/")) {
                throw new RuntimeException("Illegal filename!");
            }

            LQL = "data/jlesa/" + QueuLengthsFile;

            // (new File(LQL)).delete();
        }
    }

/**
     * Shortcut to {@code this("GeneratedPassengers.log", "GeneratedPassengers.log", "GeneratedPassengers.log")}.
     */
    public LoggerPassengerProcessor() {
        this("GeneratedPassengers.log", "GeneratedPassengers.log",
            "GeneratedPassengers.log");
    }

    /* Tickable */
    public void Tick() {
        if (LQL != null) {
            logQueuLengths();
        }
    }

    /* Tickable */
    public String getName() {
        return ("LoggerPassengerProcessor");
    }

    /* Tickable */
    public String getVersion() {
        return ("0.3.2");
    }

    /* PassengerProcessor */
    public void created(Passenger P) {
        if (LGP != null) {
            logGeneratedPassenger(P);
        }
    }

    /* PassengerProcessor */
    public void process(Passenger P) {
        if (LPT != null) {
            logPassengerTimes(P);
        }
    }

    /**
     * Logs the line {@code LogMessage} to {@code FileName}
     *
     * @param FileName DOCUMENT ME!
     * @param LogMessage DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    private void lineLog(String FileName, String LogMessage) {
        try {
            FileOutputStream FOS = new FileOutputStream(FileName, true);
            PrintWriter PW = new PrintWriter(FOS);

            PW.println(LogMessage);
            PW.close();
            FOS.close();
        } catch (Exception E) {
            throw new RuntimeException(E);
        }
    }

    /* PassengerProcessor */
    public void prepareToDie() {
        ;
    }

    /**
     * Logs a freshly generated {@link Passenger}
     *
     * @param P DOCUMENT ME!
     */
    private void logGeneratedPassenger(Passenger P) {
        lineLog(LGP,
            "" + P.getCreationTime() + " " + P.getCrtF() + ":" + P.getDstF());
    }

    /**
     * Logs a {@link Passenger}'s travel and waiting times
     *
     * @param P DOCUMENT ME!
     */
    private void logPassengerTimes(Passenger P) {
        lineLog(LPT,
            P.getCreationTime() + ":" + World.getTotalTicks() + " " +
            P.getCrtF() + ":" + P.getDstF() + " " + P.getTicksWaited() + ":" +
            P.getTicksTravelled());
    }

    /**
     * Logs the {@link Passenger} queus length on the floors
     */
    private void logQueuLengths() {
        StringBuffer SB = new StringBuffer((World.getNoF() * 3) + 5);

        SB.append(World.getTotalTicks());

        for (int i = 0; i < World.getNoF(); i++) {
            SB.append(':');
            SB.append(World.getNumberOfPassengers(i));
        }

        lineLog(LQL, SB.toString());
    }
}
