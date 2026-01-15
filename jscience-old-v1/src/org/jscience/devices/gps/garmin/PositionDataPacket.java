package org.jscience.devices.gps.garmin;

import org.jscience.devices.gps.IPosition;
import org.jscience.devices.gps.PositionRadians;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class PositionDataPacket extends GarminPacket implements IPosition {
    /** DOCUMENT ME! */
    private PositionRadians lat;

    /** DOCUMENT ME! */
    private PositionRadians lon;

/**
     * Treats the packet p as a packet containing position-data. Throws
     * PacketNotRecognizedException if p is not a position-data-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     *
     * @param p DOCUMENT ME!
     */
    public PositionDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Position_Data) {
            throw (new PacketNotRecognizedException(Pid_Position_Data, getID()));
        }

        if (getDataLength() != 16) {
            throw (new InvalidPacketException(packet, 2));
        }

        lat = new PositionRadians(readDouble(3));
        lon = new PositionRadians(readDouble(11));
    }

/**
     * This method is a copy-constructor allowing to "upgrade" a GarminPacket
     * to a PositionPacket. Throws PacketNotRecognizedException if p is not a
     * position-data-packet.
     *
     * @param p DOCUMENT ME!
     */
    public PositionDataPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * This method returns the latitude of the position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLatitude() {
        return lat;
    }

    /**
     * This method returns the longitude of the position.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLongitude() {
        return lon;
    }

    /**
     * Returns a String containing the position in a human-readable
     * format.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("\nPosition:");
        res.append("\nLatitude: " + lat.toString());
        res.append("\nLongitude: " + lon.toString());

        return res.toString();
    }
}
