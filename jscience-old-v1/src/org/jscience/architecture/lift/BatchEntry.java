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

import java.io.InputStream;


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is the main file of JLESA. It executes a series of {@link EntryPoint}s
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.3 $ $Date: 2007-10-23 18:13:51 $
 */
public class BatchEntry {
    /**
     * DOCUMENT ME!
     */
    private final static String PackageName = (new BatchEntry()).getClass()
                                               .getPackage().getName();

    /**
     * DOCUMENT ME!
     *
     * @param Reason DOCUMENT ME!
     */
    private static void printUsageAndExit(String Reason) {
        System.err.println("Cannot start batch beacuse of \"" + Reason + "\"!");
        System.err.println("Usage1: java " +
            (new BatchEntry()).getClass().getName() +
            " Config1.xml Config2.xml ...");
        System.err.println("Usage2: java " +
            (new BatchEntry()).getClass().getName() +
            " --advanced X=1-20 EntryPoint ConfigX.xml");
        System.err.println("Usage3: java " +
            (new BatchEntry()).getClass().getName() +
            " --advanced Z=1-20 ResultAnalyzer ConfigZ.xml");
        System.exit(-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param Args DOCUMENT ME!
     */
    public static void main(String[] Args) {
        if (Args.length == 0) {
            printUsageAndExit("Not enough arguments");
        } else {
            String Arg0 = Args[0].toLowerCase().trim();

            if (Arg0.equals("--advanced")) {
                if (Args.length != 4) {
                    printUsageAndExit("Advanced mode requires 3+1 arguments!");
                }

                String[] ArgParts = Args[1].split("=");

                if (ArgParts.length != 2) {
                    printUsageAndExit("Cannot split \"" + Args[1] + "\"");
                }

                String[] Limits = ArgParts[1].split("-");

                try {
                    int From = Integer.parseInt(Limits[0]);
                    int To = Integer.parseInt(Limits[1]);

                    if (From > To) {
                        printUsageAndExit("From > To");
                    }

                    for (int i = From; i <= To; i++) {
                        String ConfigFile = Args[3].replaceAll(ArgParts[0],
                                ("" + i));
                        execute(Args[2], ConfigFile);
                    }
                } catch (Exception E) {
                    throw new RuntimeException(E);
                }
            } else {
                for (int i = 0; i < Args.length; i++) {
                    execute("EntryPoint", Args[i]);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ClassName DOCUMENT ME!
     * @param ConfigName DOCUMENT ME!
     */
    private static void execute(String ClassName, String ConfigName) {
        String CommandLine = "java " + PackageName + "." + ClassName + " " +
            ConfigName;

        try {
            Process P = Runtime.getRuntime().exec(CommandLine);

            System.out.println("Executing \"" + CommandLine + "\".");

            int RetVal = P.waitFor();
            InputStream ES = P.getErrorStream();

            if ((ES.available() > 0) || (RetVal != 0)) {
                //				System.err.println(ES.available()+" "+RetVal);
                System.err.println("Error executing \"" + CommandLine +
                    "\", exiting...");
                System.err.println(
                    "-----ERROR-----ERROR-----ERROR-----ERROR-----ERROR-----ERROR-----");

                byte[] ESB = new byte[ES.available()];
                ES.read(ESB);
                System.err.println(new String(ESB));
                System.err.println(
                    "-----ERROR-----ERROR-----ERROR-----ERROR-----ERROR-----ERROR-----");
                System.exit(-1);
            } else {
                System.out.println(
                    "---------------------------------------------------------------------");

                InputStream OS = P.getInputStream();
                byte[] OSB = new byte[OS.available()];
                OS.read(OSB);
                System.out.println(new String(OSB));
                System.out.println(
                    "---------------------------------------------------------------------");
            }
        } catch (Exception E) {
            throw new RuntimeException(E);
        }
    }
}
