package org.jscience.devices.gps;

/**
 *
 */
public interface ITrackpointListener extends ITransferListener {
    /**
     * This method is called whenever a waypoint is received from the
     * GPS.
     *
     * @param tp DOCUMENT ME!
     */
    public void trackpointReceived(ITrackpoint tp);
}
