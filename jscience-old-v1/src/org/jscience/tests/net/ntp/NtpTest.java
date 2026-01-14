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

package org.jscience.tests.net.ntp;

import org.jscience.net.ntp.NtpConnection;
import org.jscience.net.ntp.TimeManager;

import java.net.InetAddress;

import java.util.Date;
import java.util.Enumeration;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class NtpTest {
    /** DOCUMENT ME! */
    public static String defaultNtpServer = "ntp.belnet.be";

    /** DOCUMENT ME! */
    private static String helpMessage = "Options :\n" +
        "-s [<server>] : sets the local time according to <server>.\n" +
        "-g [<server>] : prints the time according to <server>.\n" +
        "-o [<server>] : prints the offset in ms of the local time compared to\n" +
        "                the time given by <server>. (default)\n" +
        "-i [<server>] : prints info on <server>.\n" +
        "-t [<server>] : traces <server> back to the primary server.\n" +
        "-h            : prints this message.\n\n" +
        "If <server> is omitted then " + defaultNtpServer + " is used.";

    /** DOCUMENT ME! */
    private static final int MODE_SETTIME = 0;

    /** DOCUMENT ME! */
    private static final int MODE_GETTIME = 1;

    /** DOCUMENT ME! */
    private static final int MODE_GETOFFSET = 2;

    /** DOCUMENT ME! */
    private static final int MODE_GETINFO = 3;

    /** DOCUMENT ME! */
    private static final int MODE_GETTRACE = 4;

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(String[] argv) throws Exception {
        String ntpServer = defaultNtpServer;
        int argc = argv.length;
        int mode = MODE_GETOFFSET;

        if ((argc == 1) && argv[0].equals("-h")) {
            System.out.println(helpMessage);
            System.exit(0);
        }

        if (argc != 0) {
            String firstArgument = argv[0];

            if (firstArgument.equals("-s")) {
                mode = MODE_SETTIME;
            } else if (firstArgument.equals("-g")) {
                mode = MODE_GETTIME;
            } else if (firstArgument.equals("-o")) {
                mode = MODE_GETOFFSET;
            } else if (firstArgument.equals("-i")) {
                mode = MODE_GETINFO;
            } else if (firstArgument.equals("-t")) {
                mode = MODE_GETTRACE;
            } else if (argc == 1) {
                ntpServer = firstArgument;
            } else {
                System.out.println("Illegal option : " + firstArgument);
                System.exit(-1);
            }
        }

        if (argc == 2) {
            ntpServer = argv[1];
        }

        if (argc > 2) {
            System.out.println("Too many arguments");
            System.exit(-1);
        }

        System.out.println("Server=" + ntpServer + "\n");

        NtpConnection ntpConnection = new NtpConnection(InetAddress.getByName(
                    ntpServer));

        switch (mode) {
        case MODE_GETOFFSET:

            long offset = ntpConnection.getInfo().offset;
            System.out.println("Offset=" + offset);

            break;

        case MODE_GETINFO:
            System.out.println(ntpConnection.getInfo());

            break;

        case MODE_GETTIME:
            System.out.println(ntpConnection.getTime());

            break;

        case MODE_GETTRACE:

            Enumeration trace = ntpConnection.getTrace().elements();

            while (trace.hasMoreElements()) {
                System.out.println(trace.nextElement() + "\n\n");
            }

            break;

        case MODE_SETTIME:

            try {
                TimeManager s = TimeManager.getInstance();
                Date now = ntpConnection.getTime();
                s.setTime(now);
            } catch (Exception e) {
                String message = "Unable to set the system date\n" +
                    "Make sure that \"LocalTimeManager.class\" exists\n" +
                    "and that \"settime.dll\" is in the library loadpath";
                System.out.println(message);
                ntpConnection.close();
                System.exit(-1);
            }

            break;
        }

        ntpConnection.close();
    }
}
