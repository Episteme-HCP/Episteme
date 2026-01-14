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

package org.jscience.architecture.lift.util;

import java.io.*;

import java.net.NetworkInterface;

import java.util.ArrayList;


/**
 * Description of the Class
 *
 * @author Administrator
 */
public class Converter {
    /**
     * Set this to true if you need bug-compatibility with the old
     * version. There was a bug in the previous release that did handled
     * malformed csv files differently. Whenever a line contained whitespaces
     * after the last escape char or before the first one, the Converter
     * converted it in a wrong way, to a field that contained this escape
     * char. It should have prodocued an error as the escape char was not
     * properly escaped. Now, it trims the line and still beheaves
     * incorrectly, yet effectively.
     */
    public final static boolean CompatibilityMode = false;

    /**
     * Packs a records (0,1, or more fields) into a CSV line. NOTE:
     * Slow and dumb.
     *
     * @param In The fields of the record.
     * @param Delim The delimeter character, '\t', ';' and ',' are the most
     *        frequent ones.
     * @param Esc The escape character, '"' and '\'' are the most frequent
     *        ones.
     *
     * @return The CSV-packed record.
     */
    public static String CSV_pack(String[] In, char Delim, char Esc) {
        String RetVal = "";
        String OneEsc = new String("" + Esc);
        String TwoEscs = new String("" + Esc + Esc);

        if (In != null) {
            for (int i = 0; i < In.length; i++) {
                if (i != 0) {
                    RetVal += Delim;
                }

                if (In[i] != null) {
                    RetVal += (OneEsc + In[i].replaceAll(OneEsc, TwoEscs) +
                    OneEsc);
                } else {
                    RetVal += (OneEsc + "NULL" + OneEsc);
                }
            }
        } else {
            RetVal = "NULLNULLNULL!";
        }

        return (RetVal);
    }

    /**
     * Description of the Method
     *
     * @param Filename Description of the Parameter
     * @param Delim Description of the Parameter
     * @param Esc Description of the Parameter
     * @param SkipHeader Description of the Parameter
     *
     * @return Description of the Return Value
     */
    public static String[][] CSV_unpack(String Filename, char Delim, char Esc,
        boolean SkipHeader) {
        String[][] RetVal = new String[0][];

        try {
            BufferedReader BR = new BufferedReader(new InputStreamReader(
                        new FileInputStream(Filename), "ISO-8859-2"));
            String AL = BR.readLine();
            ArrayList ArLi = new ArrayList();

            if (SkipHeader) {
                AL = BR.readLine();
            }

            while (AL != null) {
                if (AL.trim().length() > 0) {
                    ArLi.add(Converter.CSV_unpack(AL, ',', '"'));
                }

                AL = BR.readLine();
            }

            BR.close();
            RetVal = new String[ArLi.size()][];

            for (int i = 0; i < RetVal.length; i++) {
                RetVal[i] = (String[]) ArLi.get(i);
            }
        } catch (Exception E) {
            new RuntimeException(E);
        }

        return (RetVal);
    }

    /**
     * Unpacks a records (0,1, or more fields) from a CSV line. NOTE:
     * Slow and dumb.
     *
     * @param In The CSV-packed line.
     * @param Delim The delimeter character, '\t', ';' and ',' are the most
     *        frequent ones.
     * @param Esc The escape character, '"' and '\'' are the most frequent
     *        ones.
     *
     * @return The fields of the record.
     */
    public static String[] CSV_unpack(String In, char Delim, char Esc) {
        ArrayList AL = new ArrayList();

        AL = _unpack(In, Delim, Esc, AL);

        String[] RetVal = (String[]) (AL.toArray(new String[0]));

        for (int i = 0; i < RetVal.length; i++) {
            RetVal[i] = RetVal[i].replaceAll(new String("" + Esc + Esc),
                    new String("" + Esc));
        }

        return (RetVal);
    }

    /**
     * Description of the Method
     *
     * @param Name Description of the Parameter
     * @param Line Description of the Parameter
     * @param Link Description of the Parameter
     *
     * @return Description of the Return Value
     */
    public static String HTMLTableLine(String Name, String[] Line, String Link) {
        StringBuffer RetVal = new StringBuffer(128 + (Line.length * 64));

        RetVal.append("<tr><td>");

        if (Link == null) {
            RetVal.append("<tr><td>");
            RetVal.append(Name);
            RetVal.append("</td>");
        } else {
            RetVal.append("<a href=\"");
            RetVal.append(Link);
            RetVal.append("\">");
            RetVal.append(Name);
            RetVal.append("</a>");
        }

        for (int i = 0; i < Line.length; i++) {
            RetVal.append("<td>");
            RetVal.append(Line[i]);
            RetVal.append("</td>");
        }

        RetVal.append("</tr>");

        return (RetVal.toString());
    }

    /**
     * The main program for the Converter class
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        try {
            java.util.Enumeration E = NetworkInterface.getNetworkInterfaces();

            while (E.hasMoreElements()) {
                System.err.println("" + E.nextElement());
            }
        } catch (Exception E) {
            System.err.println("" + E);
        }
    }

    /**
     * Description of the Method
     *
     * @param FileName Description of the Parameter
     * @param Data Description of the Parameter
     * @param Header Description of the Parameter
     * @param NullString Description of the Parameter
     * @param Delimeter Description of the Parameter
     * @param Escape Description of the Parameter
     *
     * @throws IOException Description of the Exception
     */
    public static void toCSV(String FileName, String[][] Data, String[] Header,
        String NullString, char Delimeter, char Escape)
        throws IOException {
        String[] Buffer = Converter.toCSV(Data, Header, NullString, Delimeter,
                Escape);

        FileOutputStream FOS = new FileOutputStream(FileName);
        OutputStreamWriter OSW = new OutputStreamWriter(FOS, "ISO-8859-2");
        PrintWriter PW = new PrintWriter(OSW);

        for (int i = 0; i < Buffer.length; i++) {
            PW.println(Buffer[i]);
        }

        PW.close();
        OSW.close();
        FOS.close();
    }

    /**
     * Converts some strings into a CSV-like array.
     *
     * @param Data The input data, first index is row index, second is column
     *        index
     * @param Header The header strings for the data
     * @param NullString All nulls are replaced by this in the Data. "N/A",
     *        "&nbsp;" and "" are very common.
     * @param Delimeter Description of the Parameter
     * @param Escape Description of the Parameter
     *
     * @return The HTML-like encoded lines of the table.
     */
    public static String[] toCSV(String[][] Data, String[] Header,
        String NullString, char Delimeter, char Escape) {
        ArrayList Lines = new ArrayList();

        if (NullString == null) {
            return new String[] { "NullString is null!" };
        }

        if (Data == null) {
            return new String[] { "Data is null!" };
        }

        if (Header != null) {
            String[] Header2 = new String[Header.length];

            for (int j = 0; j < Header.length; j++) {
                if (Header[j] == null) {
                    Header2[j] = NullString;
                } else {
                    Header2[j] = Header[j];
                }
            }

            Lines.add(CSV_pack(Header2, Delimeter, Escape));
        }

        for (int i = 0; i < Data.length; i++) {
            if (Data[i] == null) {
                Lines.add("Data[" + i + "] is null!!!!");
            } else {
                String[] Data2 = new String[Data[i].length];

                for (int j = 0; j < Data[i].length; j++) {
                    if (Data[i][j] == null) {
                        Data2[j] = NullString;
                    } else {
                        Data2[j] = Data[i][j];
                    }
                }

                Lines.add(CSV_pack(Data2, Delimeter, Escape));
            }
        }

        return ((String[]) (Lines.toArray(new String[0])));
    }

    /**
     * Converts some strings into a HTML-like table.
     *
     * @param Data The input data, first index is row index, second is column
     *        index
     * @param Header The header strings for the data
     * @param NullString All nulls are replaced by this in the Data. "N/A",
     *        "&nbsp;" and "" are very common.
     * @param Caption The caption of the table
     * @param TableParams The params of the table tag.
     *
     * @return The HTML-like encoded lines of the table.
     */
    public static String[] toHTML(String[][] Data, String[] Header,
        String NullString, String Caption, String TableParams) {
        ArrayList Lines = new ArrayList();

        if (NullString == null) {
            return new String[] { "NullString is null!" };
        }

        if (Data == null) {
            return new String[] { "Data is null!" };
        }

        if (Header == null) {
            return new String[] { "Header is null!" };
        }

        if (TableParams != null) {
            Lines.add("<table " + TableParams + ">");
        } else {
            Lines.add("<table>");
        }

        if (Caption != null) {
            Lines.add("<caption>" + Caption + "</caption>");
        }

        if (Header != null) {
            Lines.add("<thead>");

            for (int i = 0; i < Header.length; i++) {
                if (Header[i] != null) {
                    Lines.add("<th>" + Header[i] + "</th>");
                } else {
                    Lines.add("<th>" + NullString + "</th>");
                }
            }

            Lines.add("</thead>");
        }

        for (int i = 0; i < Data.length; i++) {
            if (Data[i] == null) {
                Lines.add("Data[" + i + "] is null!!!!");
            } else {
                Lines.add("<tr>");

                for (int j = 0; j < Data[i].length; j++) {
                    if (Data[i][j] != null) {
                        Lines.add("<td>" + Data[i][j] + "</td>");
                    } else {
                        Lines.add("<td>" + NullString + "</td>");
                    }
                }
            }

            Lines.add("</tr>");
        }

        Lines.add("</table>");

        return ((String[]) (Lines.toArray(new String[0])));
    }

    /**
     * NOTE: Slow and dumb.
     *
     * @param In Description of the Parameter
     * @param Delim Description of the Parameter
     * @param Esc Description of the Parameter
     * @param AL Description of the Parameter
     *
     * @return Description of the Return Value
     */
    private static ArrayList _unpack(String In, char Delim, char Esc,
        ArrayList AL) {
        if (In.length() > 0) {
            if (In.charAt(0) != Esc) {
                int nextDelim = In.indexOf(Delim);

                if (nextDelim != -1) {
                    AL.add(new String(In.substring(0, nextDelim)));

                    return (_unpack(In.substring(nextDelim + 1), Delim, Esc, AL));
                } else {
                    AL.add(new String(In.substring(0)));

                    return (AL);
                }
            } else {
                if (!CompatibilityMode) {
                    In = In.trim();
                }

                String AltIn = In.replaceAll("\"\"", "##");
                int nextDelim = In.indexOf("" + Esc + Delim);
                int nextDelimAlt = AltIn.indexOf("" + Esc + Delim);

                if (nextDelim == 1) {
                    AL.add(new String(""));

                    return (_unpack(In.substring(nextDelim + 2), Delim, Esc, AL));
                } else {
                    if (nextDelimAlt > 0) {
                        AL.add(new String(In.substring(1, nextDelimAlt)));
                    } else {
                        AL.add(new String(In.substring(1, In.length() - 1)));

                        return (AL);
                    }

                    return (_unpack(In.substring(nextDelimAlt + 2), Delim, Esc,
                        AL));
                }
            }
        } else {
            return (AL);
        }
    }
}
