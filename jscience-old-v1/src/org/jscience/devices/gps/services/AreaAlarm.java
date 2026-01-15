package org.jscience.devices.gps.services;

import org.jscience.devices.gps.*;

import java.util.Vector;


/**
 * This class implements an AreaAlarm-service. The class allows the user to
 * specify two positions, which will be used as opposite corners in a
 * rectangular area. Whenever the GPS enters or exits the area all listeners
 * are notified through the IAlarmListener-interface.
 */
public class AreaAlarm implements IGPSlistener {
    /** DOCUMENT ME! */
    private GPS gps;

    // The positions defining the rectangle to be used.
    /** DOCUMENT ME! */
    private PositionRadians bottom_longitude;

    // The positions defining the rectangle to be used.
    /** DOCUMENT ME! */
    private PositionRadians top_longitude;

    // The positions defining the rectangle to be used.
    /** DOCUMENT ME! */
    private PositionRadians left_latitude;

    // The positions defining the rectangle to be used.
    /** DOCUMENT ME! */
    private PositionRadians right_latitude;

    /** DOCUMENT ME! */
    private Vector alarmListeners;

    /** DOCUMENT ME! */
    private boolean inside;

/**
     * Creates a new AreaAlarm object.
     *
     * @param g  DOCUMENT ME!
     * @param p1 DOCUMENT ME!
     * @param p2 DOCUMENT ME!
     */
    public AreaAlarm(GPS g, Position p1, Position p2) {
        gps = g;
        gps.setAutoTransmit(true);
        gps.addGPSlistener(this);

        alarmListeners = new Vector();

        PositionRadians l1;
        PositionRadians l2;
        l1 = p1.getLatitude();
        l2 = p2.getLatitude();

        if (l1.equals(l2)) {
            throw (new RuntimeException(
                "No area. Latitude of point 1 equals latitude of point 2."));
        }

        if (l1.greaterThan(l2)) {
            right_latitude = l1;
            left_latitude = l2;
        } else {
            right_latitude = l2;
            left_latitude = l1;
        }

        l1 = p1.getLongitude();
        l2 = p2.getLongitude();

        if (l1.equals(l2)) {
            throw (new RuntimeException(
                "No area. Longitude of point 1 equals longitude of point 2."));
        }

        if (l1.greaterThan(l2)) {
            top_longitude = l1;
            bottom_longitude = l2;
        } else {
            top_longitude = l2;
            bottom_longitude = l1;
        }
    }

    /**
     * Adds l to the list of listeners interested in receiving
     * notification when the GPS enters or exits the area.
     *
     * @param l DOCUMENT ME!
     */
    public void addAlarmListener(IAlarmListener l) {
        // Only allow a listener to be registered once.
        if (alarmListeners.contains(l)) {
            return;
        }

        alarmListeners.add(l);

        return;
    }

    /**
     * Removes the the Alarm-listener l from the list of
     * Waypoint-listeners.
     *
     * @param l DOCUMENT ME!
     */
    public void removeAlarmListener(IAlarmListener l) {
        while (alarmListeners.removeElement(l)) {
        }
    }

    /**
     * This method propagates the information that the gps has exited
     * the area to all listeners.
     */
    protected void fireOutside() {
        for (int i = 0; i < alarmListeners.size(); i++) {
            ((IAlarmListener) alarmListeners.elementAt(i)).exitedAlarm();
        }
    }

    /**
     * This method propagates the information that the gps has entered
     * the area to all listeners.
     */
    protected void fireInside() {
        for (int i = 0; i < alarmListeners.size(); i++) {
            ((IAlarmListener) alarmListeners.elementAt(i)).enteredAlarm();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     */
    public void timeReceived(ITime t) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     */
    public void dateReceived(IDate d) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     */
    public void positionReceived(IPosition pos) {
        System.out.println("Areaalarm: Received pos. Analyzing!");

        if (inside == false) {
            if ((pos.getLatitude().greaterThan(left_latitude)) &&
                    (pos.getLatitude().smallerThan(right_latitude)) &&
                    (pos.getLongitude().greaterThan(bottom_longitude)) &&
                    (pos.getLongitude().smallerThan(top_longitude))) {
                inside = true;
                fireInside();
            }
        } else {
            if (!((pos.getLatitude().greaterThan(left_latitude)) &&
                    (pos.getLatitude().smallerThan(right_latitude)) &&
                    (pos.getLongitude().greaterThan(bottom_longitude)) &&
                    (pos.getLongitude().smallerThan(top_longitude)))) {
                inside = false;
                fireOutside();
            }
        }
    }
}
