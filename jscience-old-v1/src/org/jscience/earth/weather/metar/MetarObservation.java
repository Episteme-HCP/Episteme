package org.jscience.earth.weather.metar;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.util.Properties;


//this code is rebundled after:
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class MetarObservation {
    /** DOCUMENT ME! */
    private String location = null;

    /** DOCUMENT ME! */
    private String time = null;

    /** DOCUMENT ME! */
    private String codedObservation = null;

    /** DOCUMENT ME! */
    private Properties props = new Properties();

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
    public String getCodedObservation() {
        return codedObservation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param newCodedObservation DOCUMENT ME!
     */
    public void setCodedObservation(String newCodedObservation) {
        codedObservation = newCodedObservation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getProperty(String name) {
        return props.getProperty(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setProperty(String name, String value) {
        props.put(name, value);
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
        out.println("Obs: " + codedObservation);
        props.list(out);

        return sout.toString();
    }
}
