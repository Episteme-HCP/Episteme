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

package org.jscience.devices.gps.garmin;

import org.jscience.devices.gps.IDate;
import org.jscience.devices.gps.ITime;


/**
 * This class encapsulates the information of a Garmin-Date-Time-packet.
 */
public class TimeDataPacket extends GarminPacket implements ITime, IDate {
    /** Month (1-12) */
    protected short month;

    /** Day (1-31) */
    protected short day;

    /** Year. */
    protected int year;

    /** Hour of the day. */
    protected int hour;

    /** Minute of the hour. */
    protected short minute;

    /** Second of the minute. */
    protected short second;

/**
     * Treats the packet p as a packet containing Time-data. Throws
     * PacketNotRecognizedException if p is not a Time-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     *
     * @param p DOCUMENT ME!
     */
    public TimeDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Date_Time_Data) {
            throw (new PacketNotRecognizedException(Pid_Date_Time_Data, getID()));
        }

        if (getDataLength() != 8) {
            throw (new InvalidPacketException(packet, 2));
        }

        month = readByte(3);
        day = readByte(4);
        year = readWord(5);
        hour = readWord(7);
        minute = readByte(9);
        second = readByte(10);
    }

/**
     * Treats the packet p as a packet containing Time-data. Throws
     * PacketNotRecognizedException if p is not a Time-packet. Throws
     * InvalidPacketException if the packet contains too little data.
     *
     * @param p DOCUMENT ME!
     */
    public TimeDataPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * Returns the day of the month.
     *
     * @return DOCUMENT ME!
     */
    public short getDay() {
        return day;
    }

    /**
     * Returns the month.
     *
     * @return DOCUMENT ME!
     */
    public short getMonth() {
        return month;
    }

    /**
     * returns the year.
     *
     * @return DOCUMENT ME!
     */
    public int getYear() {
        return year;
    }

    /**
     * Returns the hour of the day.
     *
     * @return DOCUMENT ME!
     */
    public int getHours() {
        return hour;
    }

    /**
     * Returns the minute of the hour.
     *
     * @return DOCUMENT ME!
     */
    public short getMinutes() {
        return minute;
    }

    /**
     * Returns the second of the minute.
     *
     * @return DOCUMENT ME!
     */
    public short getSeconds() {
        return second;
    }

    /**
     * Returns the value of this packet in a human-readable format.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer res = new StringBuffer();

        if (hour < 10) {
            res.append("0");
        }

        res.append(hour + ":");

        if (minute < 10) {
            res.append("0");
        }

        res.append(minute + ":");

        if (second < 10) {
            res.append("0");
        }

        res.append(second + " on ");
        res.append(day + "/");
        res.append(month + "-" + year);

        return res.toString();
    }
}
