package org.jscience.earth.weather.metar;

import java.io.*;

import java.net.URL;
import java.net.URLConnection;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MetarObservationParser {
    /** DOCUMENT ME! */
    protected String code = null;

/**
     * Creates a new MetarObservationParser object.
     */
    public MetarObservationParser() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param newCode DOCUMENT ME!
     */
    public void setCode(String newCode) {
        code = newCode.toUpperCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public MetarObservation retrieve() throws IOException {
        URL u = new URL(
                "ftp://tgftp.nws.noaa.gov/data/observations/metar/decoded/" +
                code + ".TXT");

        // changed server address to new more reliable one
        URLConnection uconn = u.openConnection();
        uconn.setUseCaches(false);

        StringWriter sout = new StringWriter();
        PrintWriter out = new PrintWriter(sout);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                    uconn.getInputStream()));
        String line = null;

        while ((line = in.readLine()) != null) {
            out.println(line);
        }

        return parseObservation(sout.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected MetarObservation parseObservation(String text) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(text));
            MetarObservation ob = new MetarObservation();
            ob.setLocation(in.readLine());
            ob.setTime(in.readLine());

            String line = null;

            while ((line = in.readLine()) != null) {
                int pos = line.indexOf(":");

                if (pos == -1) {
                    continue;
                }

                String name = line.substring(0, pos);
                String value = line.substring(pos + 2);
                pos = value.lastIndexOf(":0");

                if (pos > -1) {
                    value = value.substring(0, pos);
                }

                if (name.equals("ob")) {
                    ob.setCodedObservation(value);
                } else {
                    ob.setProperty(name, value);
                }
            }

            return ob;
        } catch (IOException ioe) {
            //impossible
            return null;
        }
    }
}
