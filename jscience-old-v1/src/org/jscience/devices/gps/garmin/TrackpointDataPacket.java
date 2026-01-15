package org.jscience.devices.gps.garmin;

import org.jscience.devices.gps.ITrackpoint;
import org.jscience.devices.gps.Position;
import org.jscience.devices.gps.PositionDegrees;
import org.jscience.devices.gps.PositionRadians;


/**
 * This class encapsulates a trackpoint-packet. The Garmin-protocol contains a
 * huge amount of different trackpoint-Packet specifications.
 */
public class TrackpointDataPacket extends GarminPacket implements ITrackpoint {
    /**
     * Holds information about which trackpoint-format this Garmin-unit uses.
     * The default is 301.
     */
    protected int datatypeversion = 301;

    /**
     * DOCUMENT ME!
     */
    protected int index = -1;

    /**
     * Latitude of trackpoint.
     */
    protected PositionDegrees lat;

    /**
     * Longitude of trackpoint.
     */
    protected PositionDegrees lon;

    /**
     * Time of track point sample
     */
    protected long time;

    /**
     * Altitude.
     */
    protected float alt;

    /**
     * Depth.
     */
    protected float depth;

    /**
     * Temperature.
     */
    protected float temp;

    /**
     * new track segment
     */
    protected boolean new_trk;

    /**
     * Throws a PacketNotRecognizedException if the Trackpoint-dataformat is
     * not implemented.
     *
     * @param p DOCUMENT ME!
     */
    public TrackpointDataPacket(int[] p) {
        super(p);

        if (getID() == Pid_Trk_data) {
            datatypeversion = 301;
        } else if (getID() == Pid_Trk_Hdr) {
            datatypeversion = 312;
        } else {
            throw (new PacketNotRecognizedException(Pid_Trk_data, getID()));
        }

        switch (datatypeversion) {
            case 300:
                initD300();

                break;

            case 301:
                initD301();

                break;

            case 302:
                initD302();

                break;

            case 312:
                initD312();

                break;

            default:
                System.out.println("Trackpoint-type " + datatypeversion +
                        " not supported.");
                throw (new PacketNotRecognizedException(Pid_Trk_data, getID()));
        }
    }

    /**
     * Creates a new TrackpointDataPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public TrackpointDataPacket(GarminPacket p) {
        this((int[]) p.packet.clone());
    }

    /**
     * Configures this packet as a D301 Trackpoint (Default).
     */
    private void initD300() {
        long l;
        l = readLong(3);

        // Calculate from semicircles to degrees.
        lat = new PositionDegrees((l * 180) / Math.pow(2.0d, 31.0d));
        l = readLong(7);
        lon = new PositionDegrees((l * 180) / Math.pow(2.0d, 31.0d));
        time = readLong(11);
        new_trk = readBoolean(15);
    }

    /**
     * Configures this packet as a D301 Trackpoint.
     */
    private void initD301() {
        long l;
        l = readLong(3);

        // Calculate from semicircles to degrees.
        lat = new PositionDegrees((l * 180) / Math.pow(2.0d, 31.0d));
        l = readLong(7);
        lon = new PositionDegrees((l * 180) / Math.pow(2.0d, 31.0d));
        time = readLong(11);
        alt = readFloat(15);
        depth = readFloat(19);
        new_trk = readBoolean(23);
    }

    /**
     * Configures this packet as a D301 Trackpoint.
     */
    private void initD302() {
        long l;
        l = readLong(3);

        // Calculate from semicircles to degrees.
        lat = new PositionDegrees((l * 180) / Math.pow(2.0d, 31.0d));
        l = readLong(7);
        lon = new PositionDegrees((l * 180) / Math.pow(2.0d, 31.0d));
        time = readLong(11);
        alt = readFloat(15);
        depth = readFloat(19);
        temp = readFloat(23);
        new_trk = readBoolean(27);
    }

    /**
     * Configures this packet as a D312 Trackpoint Header.
     */
    private void initD312() {
        datatypeversion = 312;
        index = readWord(3);
    }

    /**
     * Sets which version of the packet that this class should treat. <br/
     * ><b> Note: </b>Setting this value will affect all instances of the
     * class.
     *
     * @return DOCUMENT ME!
     */

    //public static void setDatatypeVersion(int v) {
    //	datatypeversion = v;
    //}
    public boolean isHeader() {
        if (index >= 0) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIndex() {
        return index;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Position getPosition() {
        return new Position(lat.convertToRadians(), lon.convertToRadians());
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ITrackpoint#getTime()
     */
    public long getTime() {
        // TODO Auto-generated method stub
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getAltitude() {
        return alt;
    }

    /**
     * This method returns the latitude of the trackpoint.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLatitude() {
        return lat.convertToRadians();
    }

    /**
     * This method returns the longitude of the trackpoint.
     *
     * @return DOCUMENT ME!
     */
    public PositionRadians getLongitude() {
        return lon.convertToRadians();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        if (isHeader()) {
            return "trackpoint header:[" + index + "]";
        }

        return "trackpoint:" + "[latitude=" + lat.toString() + ", longitude=" +
                lon.toString() + ", time=" + time + ", depth=" + depth + ", altitude=" +
                alt + ", new_track=" + new_trk + "]";
    }
}
