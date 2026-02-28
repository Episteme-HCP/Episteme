package org.episteme.devices.gps;

/**
 *
 */
public interface IWaypointListener extends ITransferListener {
    /**
     * This method is called whenever a waypoint is received from the
     * GPS.
     *
     * @param wp DOCUMENT ME!
     */
    public void waypointReceived(IWaypoint wp);
}
