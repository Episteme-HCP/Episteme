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


/**
 * This file is licensed under the GNU Public Licens (GPL).<br>
 * This is a utility class that generates minimal statistical information from
 * the results logged by {@link LoggerPassengerProcessor}. The results are
 * displayed in  CSV on {@link java.lang.System}.out.
 *
 * @author Nagy Elemer Karoly (eknagy@users.sourceforge.net)
 * @version $Revision: 1.4 $ $Date: 2007-10-23 18:13:52 $
 */
public class ResultAnalyzer {
    /**
     * Analyzes the {@code Data} matrix (which is produced by {@link
     * SimplePassengerProcessor}). Prints some info ({@code average}s and
     * {@code variance}s of the waiting and travel times.
     *
     * @param Data DOCUMENT ME!
     * @param FileName DOCUMENT ME!
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static void Analyze(String[][] Data, String FileName) {
        String[] Names = new String[] { "Waiting", "Travel", "Total" };
        int[] PersonCounts = new int[3];
        int[] PersonTotals = new int[3];
        double[] Averages = new double[3];
        double[] VarianceTotals = new double[3];
        double[] Variances = new double[3];

        for (int j = 0; j < 3; j++) {
            PersonCounts[j] = 0;
            PersonTotals[j] = 0;
            Averages[j] = 0.0;
            VarianceTotals[j] = 0.0;
            Variances[j] = 0.0;
        }

        for (int i = 0; i < Data.length; i++) {
            for (int j = 1; j <= 3; j++) {
                int Time = Integer.parseInt(Data[i][0]);

                if ((!Data[i][j].equals("0")) && (!Data[i][j].equals(""))) {
                    int NOP = Integer.parseInt(Data[i][j]);
                    PersonCounts[j - 1] += NOP;
                    PersonTotals[j - 1] += (NOP * Time);
                }
            }
        }

        if ((PersonCounts[0] != PersonCounts[1]) ||
                (PersonCounts[2] != PersonCounts[1])) {
            throw new RuntimeException("Corrupted Data[][]!");
        }

        if (PersonCounts[0] == 0) {
            System.out.println(Converter.CSV_pack(
                    new String[] {
                        ("Results of " + FileName), "ERROR: 0 Passengers!"
                    }, ',', '"'));

            return;
        }

        for (int j = 0; j < 3; j++) {
            Averages[j] = ((double) PersonTotals[j]) / ((double) PersonCounts[j]);
        }

        for (int i = 0; i < Data.length; i++) {
            for (int j = 1; j <= 3; j++) {
                double Time = Double.parseDouble(Data[i][0]);

                if ((!Data[i][j].equals("0")) && (!Data[i][j].equals(""))) {
                    int NOP = Integer.parseInt(Data[i][j]);
                    double ERROR2 = (Averages[j - 1] - Time) * (Averages[j - 1] -
                        Time);

                    VarianceTotals[j - 1] += (((double) NOP) * ERROR2);
                }
            }
        }

        for (int j = 0; j < 3; j++) {
            Variances[j] = Math.sqrt(VarianceTotals[j] / ((double) PersonCounts[j]));
        }

        String[] Results = new String[] {
                ("Results of " + FileName), DTS(Averages[0]), DTS(Variances[0]),
                DTS(Averages[1]), DTS(Variances[1]), ("" + PersonCounts[0])
            };

        System.out.println(Converter.CSV_pack(Results, ',', '"'));
    }

    /**
     * A handy util to transfrom doubles to Strings with exactly 3
     * decimal places after the dot.
     *
     * @param In DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String DTS(double In) {
        int X = ((int) (In * 1000));
        String XX = (X / 1000) + ".";
        X = X % 1000;

        if (X < 10) {
            return (XX + "00" + X);
        } else if (X < 100) {
            return (XX + "0" + X);
        } else {
            return (XX + X);
        }
    }

    /**
     * The main program for the ResultAnalyzer class that uses only the
     * {@code Args[0]} as the name of the input file.
     *
     * @param Args DOCUMENT ME!
     */
    public static void main(String[] Args) {
        if (Args.length != 1) {
            System.err.println("Usage: java" +
                (new ResultAnalyzer()).getClass().getPackage().getName() +
                " FILE");
            System.exit(-1);
        } else {
            if (EntryPoint.goodFileName(Args[0], "data/jlesa/")) {
                ResultAnalyzer RA = new ResultAnalyzer();
                Analyze(Converter.CSV_unpack(("data/jlesa/" + Args[0]), ',',
                        '"', true), Args[0]);
            } else {
                System.err.println("ERROR! File error.");
                System.exit(-1);
            }
        }
    }
}
