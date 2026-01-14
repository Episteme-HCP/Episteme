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

package org.jscience.device.loaders.nmea;

/**
 * NMEA 2000 (CAN-based marine protocol) message parser and representation.
 * <p>
 * NMEA 2000 uses PGNs (Parameter Group Numbers) to identify message types.
 * This parser handles the binary CAN frame format used by NMEA 2000.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NMEA2000Message {

    /** NMEA 2000 PGN definitions */
    public enum PGN {
        SYSTEM_TIME(126992, "System Time"),
        VESSEL_HEADING(127250, "Vessel Heading"),
        RATE_OF_TURN(127251, "Rate of Turn"),
        ATTITUDE(127257, "Attitude"),
        POSITION_RAPID(129025, "Position, Rapid Update"),
        COG_SOG_RAPID(129026, "COG & SOG, Rapid Update"),
        GNSS_POSITION(129029, "GNSS Position Data"),
        WIND_DATA(130306, "Wind Data"),
        ENVIRONMENTAL_PARAMS(130311, "Environmental Parameters"),
        TEMPERATURE(130312, "Temperature"),
        HUMIDITY(130313, "Humidity"),
        PRESSURE(130314, "Actual Pressure"),
        DEPTH(128267, "Water Depth"),
        SPEED(128259, "Speed"),
        ENGINE_PARAMETERS(127489, "Engine Parameters, Rapid Update"),
        BATTERY_STATUS(127508, "Battery Status"),
        AIS_CLASS_A(129038, "AIS Class A Position Report"),
        AIS_CLASS_B(129039, "AIS Class B Position Report");

        private final int pgn;
        private final String description;

        PGN(int pgn, String description) {
            this.pgn = pgn;
            this.description = description;
        }

        public int getValue() {
            return pgn;
        }

        public String getDescription() {
            return description;
        }

        public static PGN fromValue(int value) {
            for (PGN p : values()) {
                if (p.pgn == value)
                    return p;
            }
            return null;
        }
    }

    private final int pgn;
    private final int priority;
    private final int source;
    private final int destination;
    private final byte[] data;
    private final long timestamp;

    public NMEA2000Message(int pgn, int priority, int source, int destination, byte[] data) {
        this.pgn = pgn;
        this.priority = priority;
        this.source = source;
        this.destination = destination;
        this.data = data.clone();
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * Parses a CAN frame into an NMEA 2000 message.
     * 
     * @param canId the 29-bit CAN extended identifier
     * @param data  the CAN frame data (up to 8 bytes)
     */
    public static NMEA2000Message fromCANFrame(int canId, byte[] data) {
        int priority = (canId >> 26) & 0x07;
        int pgnRaw = (canId >> 8) & 0x3FFFF;
        int source = canId & 0xFF;
        int destination = 255; // Broadcast by default

        // PDU1 format (PF < 240): includes destination
        int pf = (pgnRaw >> 8) & 0xFF;
        if (pf < 240) {
            destination = pgnRaw & 0xFF;
            pgnRaw = pgnRaw & 0xFF00;
        }

        return new NMEA2000Message(pgnRaw, priority, source, destination, data);
    }

    public int getPGN() {
        return pgn;
    }

    public PGN getPGNType() {
        return PGN.fromValue(pgn);
    }

    public int getPriority() {
        return priority;
    }

    public int getSource() {
        return source;
    }

    public int getDestination() {
        return destination;
    }

    public byte[] getData() {
        return data.clone();
    }

    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Gets a signed 16-bit value from the data at the specified byte offset.
     */
    public int getInt16(int offset) {
        if (offset + 1 >= data.length)
            return 0;
        return (data[offset] & 0xFF) | ((data[offset + 1] & 0xFF) << 8);
    }

    /**
     * Gets an unsigned 16-bit value from the data at the specified byte offset.
     */
    public int getUInt16(int offset) {
        return getInt16(offset) & 0xFFFF;
    }

    /**
     * Gets a 32-bit value from the data at the specified byte offset.
     */
    public long getInt32(int offset) {
        if (offset + 3 >= data.length)
            return 0;
        return (data[offset] & 0xFFL) |
                ((data[offset + 1] & 0xFFL) << 8) |
                ((data[offset + 2] & 0xFFL) << 16) |
                ((data[offset + 3] & 0xFFL) << 24);
    }

    /**
     * Parses latitude from GNSS position PGN (129029).
     * 
     * @return latitude in degrees or NaN if not applicable
     */
    public double parseLatitude() {
        if (pgn != 129029 && pgn != 129025)
            return Double.NaN;
        long raw = getInt32(0);
        return raw * 1e-7; // 0.0000001 degree resolution
    }

    /**
     * Parses longitude from GNSS position PGN (129029).
     * 
     * @return longitude in degrees or NaN if not applicable
     */
    public double parseLongitude() {
        if (pgn != 129029 && pgn != 129025)
            return Double.NaN;
        long raw = getInt32(4);
        return raw * 1e-7; // 0.0000001 degree resolution
    }

    /**
     * Parses heading from Vessel Heading PGN (127250).
     * 
     * @return heading in radians or NaN if not applicable
     */
    public double parseHeading() {
        if (pgn != 127250)
            return Double.NaN;
        int raw = getUInt16(1);
        return raw * 0.0001; // 0.0001 rad resolution
    }

    /**
     * Parses depth from Water Depth PGN (128267).
     * 
     * @return depth in meters or NaN if not applicable
     */
    public double parseDepth() {
        if (pgn != 128267)
            return Double.NaN;
        long raw = getInt32(1);
        return raw * 0.01; // 0.01 meter resolution
    }

    /**
     * Parses wind speed from Wind Data PGN (130306).
     * 
     * @return wind speed in m/s or NaN if not applicable
     */
    public double parseWindSpeed() {
        if (pgn != 130306)
            return Double.NaN;
        int raw = getUInt16(1);
        return raw * 0.01; // 0.01 m/s resolution
    }

    /**
     * Parses wind angle from Wind Data PGN (130306).
     * 
     * @return wind angle in radians or NaN if not applicable
     */
    public double parseWindAngle() {
        if (pgn != 130306)
            return Double.NaN;
        int raw = getUInt16(3);
        return raw * 0.0001; // 0.0001 rad resolution
    }

    @Override
    public String toString() {
        PGN pgnType = getPGNType();
        String pgnName = pgnType != null ? pgnType.getDescription() : "Unknown";
        return String.format("NMEA2000[PGN=%d (%s), src=%d, dst=%d, data=%s]",
                pgn, pgnName, source, destination, bytesToHex(data));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}


