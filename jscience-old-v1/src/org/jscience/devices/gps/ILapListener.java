package org.jscience.devices.gps;

/**
 *
 */
public interface ILapListener extends ITransferListener {
    /**
     * This method is called whenever a waypoint is received from the
     * GPS.
     *
     * @param tp DOCUMENT ME!
     */
    public void lapReceived(ILap tp);
}
