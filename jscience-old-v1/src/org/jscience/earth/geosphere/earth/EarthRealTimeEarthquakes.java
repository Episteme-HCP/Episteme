package org.jscience.earth.geosphere.earth;

/**
 *
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Get data about recent earthquakes from the USGS finger sites and
 * display it.
 * <p/>
 * Debugging information is printed when the OpenMap Viewer is launch
 * with -Ddebug.earthquake flag.<P>
 * # Properties for the Earthquake Layer
 * earthquake.sites=<finger site> <finger site> ...
 * # in seconds
 * earthquake.queryinterval=300
 */

//currently redeveloped from class EarthquakeLayer from OpenBBN distributed under the following license http://openmap.bbn.com/license.html

public class EarthRealTimeEarthquakes extends Object {

    public final static transient String fingerSitesProperty = "sites";
    public final static transient String queryIntervalProperty = "queryInterval";

    /**
     * Sites to finger user the user `quake'.
     */
    protected String fingerSites[] = {
            "scec.gps.caltech.edu",
            "geophys.washington.edu",
            "giseis.alaska.edu",
            "mbmgsun.mtech.edu",
            "quake.eas.slu.edu"
    };

    // Old sites
// 	"gldfs.cr.usgs.gov",
// 	"andreas.wr.usgs.gov",
// 	"seismo.unr.edu",
// 	"eqinfo.seis.utah.edu",
// 	"sisyphus.idbsu.edu",
// 	"info.seismo.usbr.gov",
// 	"vtso.geol.vt.edu",
// 	"tako.wr.usgs.gov",
// 	"ldeo.columbia.edu"

    /**
     * Sites that are actively being queried.
     */
    protected boolean activeSites[] = new boolean[fingerSites.length];

    /**
     * Default to 5 minutes.
     */
    private long fetchIntervalMillis = 300 * 1000;

    // lat-lon data of the earthquakes
    protected float llData[] = new float[0];

    // floating information about the earthquakes
    protected String infoData[] = new String[0];
    // floating information about the earthquakes
    protected String drillData[] = new String[0];

    private long lastDataFetchTime = 0;
    protected Color lineColor = Color.red;
    protected boolean showingInfoLine = false;
    protected boolean working = false;
    protected boolean cancelled = false;
    /**
     * The layer GUI.
     */
    protected JPanel gui = null;

    /**
     * Construct an EarthquakeLayer.
     */
    public EarthquakeLayer() {
        activeSites[0] = true;
    }

    /**
     * Set the properties of the EarthquakeLayer.
     *
     * @param prefix String
     * @param props  Properties
     */
    public void setProperties(String prefix, Properties props) {
        super.setProperties(prefix, props);

        prefix = PropUtils.getScopedPropertyPrefix(prefix);

        // list of sites
        String sites = props.getProperty(prefix + fingerSitesProperty);
        if (sites != null) {
            Vector v = new Vector();
            String str;
            StringTokenizer tok = new StringTokenizer(sites);
            while (tok.hasMoreTokens()) {
                str = tok.nextToken();
                v.addElement(str);
            }
            int len = v.size();
            fingerSites = new String[len];
            activeSites = new boolean[len];
            activeSites[0] = true;
            for (int i = 0; i < len; i++) {
                fingerSites[i] = (String) v.elementAt(i);
            }
        }

        fetchIntervalMillis =
                LayerUtils.intFromProperties(props, prefix + queryIntervalProperty, 300) * 1000;
    }

    /**
     * Parse the finger site data.
     *
     * @param data Vector
     */
    protected void parseData(Vector data) {
        int nLines = data.size();
        llData = new float[2 * nLines];
        infoData = new String[nLines];
        drillData = new String[nLines];

        for (int i = 0, j = 0, k = 0; i < nLines; i++) {
            String line = (String) data.elementAt(i);

            // Read a line of input and break it down
            StringTokenizer tokens = new StringTokenizer(line);
            String sdate = tokens.nextToken();
            String stime = tokens.nextToken();
            String slat = tokens.nextToken();
            String slon = tokens.nextToken();
            if (slon.startsWith("NWSE"))// handle ` ' in LatLon data
                slon = tokens.nextToken();
            String sdep = tokens.nextToken();
            if (sdep.startsWith("NWSE"))// handle ` ' in LatLon data
                sdep = tokens.nextToken();
            String smag = tokens.nextToken();
            String q = tokens.nextToken();
            String scomment = tokens.nextToken("\r\n");
            if (q.length() > 1) {
                scomment = q + " " + scomment;
            }

            infoData[j] = smag;
            drillData[j++] = sdate + " " + stime + " (UTC)  " + slat + " " + slon + " " + smag + " " + scomment;

            // Remove NESW from lat and lon before converting to float
            int west = slon.indexOf("W");
            int south = slat.indexOf("S");

            if (west >= 0)
                slon = slon.replace('W', '\0');
            else
                slon = slon.replace('E', '\0');
            if (south >= 0)
                slat = slat.replace('S', '\0');
            else
                slat = slat.replace('N', '\0');
            slon = slon.trim();
            slat = slat.trim();

            float flat = 0, flon = 0;
            try {
                flat = new Float(slat).floatValue();
                flon = new Float(slon).floatValue();
            } catch (NumberFormatException e) {
                Debug.error("EarthquakeLayer.parseData(): " + e +
                        " line: " + line);
            }

            // replace West and South demarcations with minus sign
            if (south >= 0)
                flat = -flat;
            if (west >= 0)
                flon = -flon;

            llData[k++] = flat;
            llData[k++] = flon;
        }
        omgraphics.clear();
    }

    /**
     * Get the earthquake data from the USGS.
     * This is called from the SwingWorker thread.
     *
     * @return Vector
     */
    protected Vector getEarthquakeData() {
        Vector linesOfData = new Vector();
        Socket quakefinger = null;
        PrintWriter output = null;
        BufferedReader input = null;
        String line;

        for (int i = 0; i < activeSites.length; i++) {
            // skip sites which aren't on the active list
            if (!activeSites[i])
                continue;

            try {
                if (Debug.debugging("earthquake")) {
                    Debug.output("Opening socket connection to " + fingerSites[i]);
                }
                quakefinger = new Socket(fingerSites[i], 79);//open connection to finger port
                quakefinger.setSoTimeout(120 * 1000);// 2 minute timeout
                output = new PrintWriter(new OutputStreamWriter(quakefinger.getOutputStream()), true);
                input = new
                        BufferedReader(new InputStreamReader(quakefinger.getInputStream()), 1);
                output.println("/W quake");// use `/W' flag for long output
            } catch (IOException e) {
                Debug.error("EarthquakeLayer.getEarthquakeData(): " +
                        "can't open or write to socket: " + e);
                continue;
            }

            try {
                // add data lines to list
                while ((line = input.readLine()) != null) {
                    if (Debug.debugging("earthquake")) {
                        Debug.output("EarthquakeLayer.getEarthQuakeData(): " + line);
                    }
                    if (line.length() == 0)
                        continue;
                    if (!Character.isDigit(line.charAt(0)))
                        continue;

                    line = hackY2K(line);
                    if (line == null)
                        continue;
                    linesOfData.addElement(line);
                }
            } catch (IOException e) {
                Debug.error("EarthquakeLayer.getEarthquakeData(): " +
                        "can't read from the socket: " + e);
                if (cancelled) {
                    return null;
                }
            }

            try {
                quakefinger.close();
            } catch (IOException e) {
                Debug.error("EarthquakeLayer.getEarthquakeData(): " +
                        "error closing socket: " + e);
            }
        }

//   	int nQuakes = linesOfData.size();
//   	for (int i=0; i<nQuakes; i++) {
//   	    Debug.output((String)linesOfData.elementAt(i));
//   	}
        return linesOfData;
    }

    // This is the USGS's date problem, not ours (of course when they
    // change their format, we'll have to update this).
    // Note that also this could just be a bogus line (not a dataline)
    // beginning with a number, so we've got to deal with it here.
    private String hackY2K(String date) {
        StringTokenizer tok = new StringTokenizer(date, "/");
        String year, month, day;
        try {
            year = tok.nextToken();
            month = tok.nextToken();
            day = tok.nextToken();
        } catch (NoSuchElementException e) {
            Debug.error("EarthquakeLayer: unparsable date: " + date);
            return null;
        }
        if (year.length() == 2) {
            int y;
            try {
                y = Integer.parseInt(year);
            } catch (NumberFormatException e) {
                Debug.error("EarthquakeLayer: invalid year: " + year);
                return null;
            }
            // Sliding window technique...
            if (y > 70) {
                date = "19";
            } else {
                date = "20";
            }
        } else if (year.length() != 4) {
            Debug.error("EarthquakeLayer: unparsable year: " + year);
            return null;
        }

        date = date + year + "/" + month + "/" + day;
        return date;
    }

    /**
     * Get the associated properties object.
     * This method creates a Properties object if necessary
     * and fills it with the relevant data for this layer.
     * Relevant properties for EarthquakeLayers are the
     * sites to retrieve earth quake data from, and the interval
     * in milliseconds (see class description.)
     */
    public Properties getProperties(String prefix, Properties props) {
        props = super.getProperties(props);

        prefix = PropUtils.getScopedPropertyPrefix(prefix);

        StringBuffer sitesToFinger = new StringBuffer("");
        for (int i = 0; i < fingerSites.length; ++i) {
            sitesToFinger.append(fingerSites[i]);
            sitesToFinger.append(" ");
        }

        sitesToFinger.deleteCharAt(sitesToFinger.length() - 1);

        props.put(prefix + fingerSitesProperty, sitesToFinger.toString());
        props.put(prefix + queryIntervalProperty, Long.toString(fetchIntervalMillis));
        return props;
    }

    /**
     * Supplies the propertiesInfo object associated with
     * this EarthquakeLayer object.
     * Contains the human readable describtions of the properties
     * and the <code>initPropertiesProperty</code> (see Inspector
     * class.)
     */
    public Properties getPropertyInfo(Properties info) {
        info = super.getPropertyInfo(info);

        info.put(fingerSitesProperty, "WWW sites to finger");
        info.put(queryIntervalProperty, "Query interval in seconds");
        return info;
    }
}
