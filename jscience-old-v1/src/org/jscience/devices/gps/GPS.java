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

package org.jscience.devices.gps;

import java.util.HashMap;
import java.util.Vector;


/**
 * This is the abstract base-class that encapsulates the functionality of a
 * generic GPS-unit.
 */
public abstract class GPS {
    /**
     * DOCUMENT ME!
     */
    protected HashMap zListeners;

    /**
     * Creates a new GPS object.
     */
    protected GPS() {
        zListeners = new HashMap();
    }

    /**
     * Adds the specified IGPSlistener to receive data from the GPS.
     *
     * @param l DOCUMENT ME!
     */
    public void addGPSlistener(IGPSlistener l) {
        if (zListeners.get("GPS") == null) {
            zListeners.put("GPS", new Vector());
        }

        // Only allow a listener to be registered once.
        if (((Vector) zListeners.get("GPS")).contains(l)) {
            return;
        }

        ((Vector) zListeners.get("GPS")).add(l);

        return;
    }

    /**
     * Removes the the GPS-listener l from the list of GPS-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void removeGPSListener(IGPSlistener l) {
        while (((Vector) zListeners.get("GPS")).removeElement(l)) {
        }

        return;
    }

    /**
     * Adds l to the list of listeners interested in transfer-events. Members
     * of this list can't be directly added, but have to be added through
     * addition of other listeners.
     *
     * @param l DOCUMENT ME!
     */
    protected void addTransferListener(ITransferListener l) {
        if (zListeners.get("TRANSFER") == null) {
            zListeners.put("TRANSFER", new Vector());
        }

        // Only allow a listener to be registered once.
        if (((Vector) zListeners.get("TRANSFER")).contains(l)) {
            return;
        }

        ((Vector) zListeners.get("TRANSFER")).add(l);

        return;
    }

    /**
     * Removes the the transfer-listener l from the list of transfer-listeners.
     *
     * @param l DOCUMENT ME!
     */
    protected void removeTransferListener(ITransferListener l) {
        while (((Vector) zListeners.get("TRANSFER")).removeElement(l)) {
        }

        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void addWaypointListener(IWaypointListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("WAYPOINT") == null) {
            zListeners.put("WAYPOINT", new Vector());
        }

        if (((Vector) zListeners.get("WAYPOINT")).contains(l)) {
            return;
        }

        addTransferListener(l);

        ((Vector) zListeners.get("WAYPOINT")).add(l);

        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void removeWaypointListener(IWaypointListener l) {
        while (((Vector) zListeners.get("WAYPOINT")).removeElement(l)) {
        }

        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void addTrackListener(ITrackpointListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("TRACK") == null) {
            zListeners.put("TRACK", new Vector());
        }

        if (((Vector) zListeners.get("TRACK")).contains(l)) {
            return;
        }

        addTransferListener(l);

        ((Vector) zListeners.get("TRACK")).add(l);

        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void removeTrackListener(ITrackpointListener l) {
        while (((Vector) zListeners.get("TRACK")).removeElement(l)) {
        }

        return;
    }

    /**
     * Adds l to the list of listeners interested in waypoint-data. Also adds l
     * to the list of transfer-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void addLapListener(ILapListener l) {
        // Only allow a listener to be registered once.
        if (zListeners.get("LAP") == null) {
            zListeners.put("LAP", new Vector());
        }

        if (((Vector) zListeners.get("LAP")).contains(l)) {
            return;
        }

        addTransferListener(l);

        ((Vector) zListeners.get("LAP")).add(l);

        return;
    }

    /**
     * Removes the the Waypoint-listener l from the list of Waypoint-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void removeLapListener(ILapListener l) {
        while (((Vector) zListeners.get("LAP")).removeElement(l)) {
        }

        return;
    }

    /**
     * Notifies listeners of the beginning of a stream of data. Tells listeners
     * of the number of data-units in the transfer.
     *
     * @param number DOCUMENT ME!
     */
    public void fireTransferStart(int number) {
        for (int i = 0; i < ((Vector) zListeners.get("TRANSFER")).size();
             i++) {
            ((ITransferListener) ((Vector) zListeners.get("TRANSFER")).elementAt(i)).transferStarted(number);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     *
     * @param wp DOCUMENT ME!
     */
    public void fireWaypointData(IWaypoint wp) {
        if (((Vector) zListeners.get("WAYPOINT")) == null) {
            zListeners.put("WAYPOINT", new Vector());
        }

        for (int i = 0; i < ((Vector) zListeners.get("WAYPOINT")).size();
             i++) {
            ((IWaypointListener) ((Vector) zListeners.get("WAYPOINT")).elementAt(i)).waypointReceived(wp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     *
     * @param tp DOCUMENT ME!
     */
    public void fireTrackpointData(ITrackpoint tp) {
        if (((Vector) zListeners.get("TRACK")) == null) {
            zListeners.put("TRACK", new Vector());
        }

        for (int i = 0; i < ((Vector) zListeners.get("TRACK")).size(); i++) {
            ((ITrackpointListener) ((Vector) zListeners.get("TRACK")).elementAt(i)).trackpointReceived(tp);
        }
    }

    /**
     * Goes through the list of Waypoint-listeners and distributes the waypoint
     * wp.
     *
     * @param lp DOCUMENT ME!
     */
    public void fireLapData(ILap lp) {
        if (((Vector) zListeners.get("LAP")) == null) {
            zListeners.put("LAP", new Vector());
        }

        for (int i = 0; i < ((Vector) zListeners.get("LAP")).size(); i++) {
            ((ILapListener) ((Vector) zListeners.get("LAP")).elementAt(i)).lapReceived(lp);
        }
    }

    /**
     * Notifies listeners of the end of a stream of data.
     */
    public void fireTransferComplete() {
        if (((Vector) zListeners.get("TRANSFER")) == null) {
            zListeners.put("TRANSFER", new Vector());
        }

        for (int i = 0; i < ((Vector) zListeners.get("TRANSFER")).size();
             i++) {
            ((ITransferListener) ((Vector) zListeners.get("TRANSFER")).elementAt(i)).transferComplete();
        }
    }

    /**
     * Goes through the list of GPSlisteners and distributes the new position
     * data.
     *
     * @param pos DOCUMENT ME!
     */
    protected void firePositionData(IPosition pos) {
        if (((Vector) zListeners.get("GPS")) == null) {
            zListeners.put("GPS", new Vector());
        }

        for (int i = 0; i < ((Vector) zListeners.get("GPS")).size(); i++) {
            ((IGPSlistener) ((Vector) zListeners.get("GPS")).elementAt(i)).positionReceived(pos);
        }
    }

    /**
     * Goes through the list of GPSlisteners and distributes the new date data.
     *
     * @param dat DOCUMENT ME!
     */
    protected void fireDateData(IDate dat) {
        if (((Vector) zListeners.get("DATE")) == null) {
            zListeners.put("DATE", new Vector());
        }

        for (int i = 0; i < ((Vector) zListeners.get("DATE")).size(); i++) {
            ((IGPSlistener) ((Vector) zListeners.get("DATE")).elementAt(i)).dateReceived(dat);
        }
    }

    /**
     * Goes through the list of GPSlisteners and distributes the new time data.
     *
     * @param time DOCUMENT ME!
     */
    protected void fireTimeData(ITime time) {
        for (int i = 0; i < ((Vector) zListeners.get("GPS")).size(); i++) {
            ((IGPSlistener) ((Vector) zListeners.get("GPS")).elementAt(i)).timeReceived(time);
        }
    }

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public abstract void requestPosition();

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public abstract void requestTime();

    /**
     * Makes a request for the specified data to the GPS. Data will be returned
     * to all listeners through the IGPSlistener-interface.
     */
    public abstract void requestDate();

    /**
     * Requests a descriptive string from the GPS. Should be formatted for
     * human-reading. The string should be constructed by every
     * GPS-implementation upon startup.
     *
     * @return DOCUMENT ME!
     */
    public abstract String getDescription();

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     */
    public abstract void requestWaypoints();

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     */
    public abstract void requestTracks();

    /**
     * Asks the GPS to transmit all the waypoints in it's memory. The result
     * will be returned through the WaypointListener-interface. Throws a
     * FeatureNotSupportException if it isn't possible to do this on the GPS.
     *
     * @param t DOCUMENT ME!
     */
    /**
     * Asks the GPS to either start or stop transmitting data periodically. <br/
     * > The data will be the that which is accessible through the
     * IGPSlistener-interface. Throws a FeatureNotSupportException if it isn't
     * possible to do this on the GPS.
     *
     * @param t DOCUMENT ME!
     */
    public abstract void setAutoTransmit(boolean t);

    /**
     * Stops communication with GPS.<br/
     * > Most likely, your program won't be able to shut down before you've
     * called this method. If b is set to true, the GPS will be asked to turn
     * off. If b is set to false, the GPS will remain turned on. Throws a
     * FeatureNotSupportException if b is true, but the GPS-unit doesn't
     * support that function.
     *
     * @param b DOCUMENT ME!
     */
    public abstract void shutdown(boolean b);
}
