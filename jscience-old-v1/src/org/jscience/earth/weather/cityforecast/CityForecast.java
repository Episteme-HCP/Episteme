package org.jscience.earth.weather.cityforecast;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Iterator;
import java.util.Vector;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CityForecast {
    /** DOCUMENT ME! */
    private String code = null;

    /** DOCUMENT ME! */
    private String location = null;

    /** DOCUMENT ME! */
    private String time = null;

    /** DOCUMENT ME! */
    private Vector forecasts = new Vector(8);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCode() {
        return code;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newCode DOCUMENT ME!
     */
    public void setCode(String newCode) {
        code = newCode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLocation() {
        return location;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newLocation DOCUMENT ME!
     */
    public void setLocation(String newLocation) {
        location = newLocation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newTime DOCUMENT ME!
     */
    public void setTime(String newTime) {
        time = newTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator getForecastEntries() {
        return forecasts.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CityForecastEntry getForecastEntry(int index) {
        return (CityForecastEntry) forecasts.elementAt(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     */
    public void addForecastEntry(CityForecastEntry entry) {
        forecasts.add(entry);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringWriter sout = new StringWriter();
        PrintWriter out = new PrintWriter(sout);
        out.println("Location: " + location);
        out.println("Time: " + time);

        for (int i = 0; i < forecasts.size(); i++) {
            out.println(forecasts.elementAt(i));
        }

        return sout.toString();
    }
}
