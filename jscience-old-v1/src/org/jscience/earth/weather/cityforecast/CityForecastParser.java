package org.jscience.earth.weather.cityforecast;

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
public class CityForecastParser {
    /** DOCUMENT ME! */
    protected String state = null;

    /** DOCUMENT ME! */
    protected String city = null;

/**
     * Creates a new CityForecastParser object.
     */
    public CityForecastParser() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param newState DOCUMENT ME!
     */
    public void setState(String newState) {
        state = newState.toLowerCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param newCity DOCUMENT ME!
     */
    public void setCity(String newCity) {
        city = newCity.toLowerCase().replace(' ', '_');
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public CityForecast retrieve() throws IOException {
        URL u = new URL("ftp://weather.noaa.gov/data/forecasts/city/" + state +
                "/" + city + ".txt");
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

        return parseForecast(sout.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected CityForecast parseForecast(String text) {
        try {
            BufferedReader in = new BufferedReader(new StringReader(text));
            CityForecast fc = new CityForecast();
            fc.setCode(in.readLine());

            //skip blank line
            in.readLine();
            fc.setLocation(in.readLine());
            fc.setTime(in.readLine());
            in.readLine();

            String line = null;

            while ((line = in.readLine()) != null) {
                line = line.trim();

                if (line.length() == 0) {
                    continue;
                }

                CityForecastEntry entry = parseForecastEntry(line);
                fc.addForecastEntry(entry);
            }

            return fc;
        } catch (IOException ioe) {
            //impossible
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected CityForecastEntry parseForecastEntry(String text) {
        CityForecastEntry entry = new CityForecastEntry();

        if (!text.startsWith(".")) {
            return null;
        }

        text = text.substring(1);

        int pos = text.indexOf("...");

        if (pos == -1) {
            return null;
        }

        entry.setTime(text.substring(0, pos));
        entry.setPrediction(text.substring(pos + 4));

        return entry;
    }
}
