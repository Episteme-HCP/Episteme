package org.jscience.devices.gps.garmin;

/**
 * This packet is transmitted between devices before a large transfer of
 * data-units, ie. a transfer of waypoints.
 */
public class RecordsPacket extends GarminPacket {
    /** The number of records to come, that this packet announces. */
    protected int number;

/**
     * Creates a new RecordsPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public RecordsPacket(int[] p) {
        super(p);

        if (getID() != Pid_Records) {
            throw (new PacketNotRecognizedException(Pid_Records, getID()));
        }

        if (getDataLength() != 2) {
            throw (new InvalidPacketException(packet, 2));
        }

        number = readWord(3);
    }

/**
     * Creates a new RecordsPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public RecordsPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * Returns the number of records that this packet announces.
     *
     * @return DOCUMENT ME!
     */
    public int getNumber() {
        return number;
    }
}
