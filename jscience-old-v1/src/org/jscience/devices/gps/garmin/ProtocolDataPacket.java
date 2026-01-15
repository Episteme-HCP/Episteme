package org.jscience.devices.gps.garmin;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProtocolDataPacket extends GarminPacket {
    /** DOCUMENT ME! */
    protected char[] tags;

    /** DOCUMENT ME! */
    protected int[] data;

/**
     * Treats the packet p as a packet containing data about which protocols
     * the GPS support. Throws PacketNotRecognizedException if p is not a
     * product-data-packet.
     *
     * @param p DOCUMENT ME!
     */
    public ProtocolDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Protocol_Array) {
            throw (new PacketNotRecognizedException(Pid_Protocol_Array, getID()));
        }

        if ((getDataLength() % 3) != 0) {
            throw (new InvalidPacketException(packet, 2));
        }

        tags = new char[getDataLength() / 3];
        data = new int[getDataLength() / 3];

        int packet_index = 3;
        int array_index = 0;

        while (packet_index != (getDataLength() + 3)) {
            tags[array_index] = (char) readByte(packet_index++);
            data[array_index] = readWord(packet_index);
            packet_index += 2;
            array_index++;
        }
    }

/**
     * Creates a new ProtocolDataPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public ProtocolDataPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * This method will return the exact version of a protocol. If the
     * protocol is not supported by the GPS, the method returns -1.
     *
     * @param tag DOCUMENT ME!
     * @param protocol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVersion(char tag, int protocol) {
        for (int i = 0; i < tags.length; i++) {
            if (tags[i] == tag) {
                if ((data[i] / protocol) == 1) {
                    return data[i];
                }
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append("Tag:\tData:\n");

        for (int i = 0; i < tags.length; i++) {
            res.append(tags[i] + "\t" + data[i] + '\n');
        }

        return res.toString();
    }
}
