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

import org.jscience.devices.gps.ILap;
import org.jscience.devices.gps.Position;


/**
 * This class encapsulates a lap-packet.
 */
public class LapDataPacket extends GarminPacket implements ILap {
    /**
     * Holds information about which lap-format this Garmin-unit uses. The
     * default is 108.
     */
    protected static int datatypeversion = 906;

    /**
     * DOCUMENT ME!
     */
    protected boolean bound = false;

    /**
     * start time
     */
    protected long start_time;

    /**
     * total time
     */
    protected long total_time;

    /**
     * total_distance
     */
    protected float total_distance;

    /**
     * begin point
     */
    Position start_position;

    /**
     * end point
     */
    Position end_position;

    /**
     * calories
     */
    protected int calories;

    /**
     * track index
     */
    protected short track_index;

    /**
     * unused
     */
    protected short unused;

    /**
     * Throws a PacketNotRecognizedException if the lap-dataformat is not
     * implemented.
     *
     * @param p DOCUMENT ME!
     */
    public LapDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Lap) {
            throw (new PacketNotRecognizedException(Pid_Lap, getID()));
        }

        switch (datatypeversion) {
            case 906:
                initD906();

                break;

            default:
                System.out.println("lap-type " + datatypeversion +
                        " not supported.");
                throw (new PacketNotRecognizedException(Pid_Lap, getID()));
        }
    }

    /**
     * Creates a new LapDataPacket object.
     *
     * @param p DOCUMENT ME!
     */
    public LapDataPacket(GarminPacket p) {
        this((int[]) p.packet.clone());
    }

    /**
     * Configures and binds the data as a D108 (lap).
     */
    private void initD906() {
        start_time = readLong();
        total_time = readLong();
        total_distance = readFloat();
        start_position = readPosition();
        end_position = readPosition();
        calories = readWord();
        track_index = readByte();
        unused = readByte();
        bound = true;
    }

    /**
     * Sets which version of the packet that this class should treat. <br/
     * ><b> Note: </b>Setting this value will affect all instances of the
     * class.
     *
     * @param v DOCUMENT ME!
     */
    public static void setDatatypeVersion(int v) {
        datatypeversion = v;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "lap:[" + "start_time=" + start_time + ", total_time=" +
                total_time + ", total_distance=" + total_distance +
                ", start_position=" + start_position.toString() + ", end_position=" +
                end_position.toString() + ", calories=" + calories + ", track_index=" +
                track_index + "]";
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getStartTime()
     */
    public long getStartTime() {
        return start_time;
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getTotalTime()
     */
    public long getTotalTime() {
        return total_time;
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getTotalDistance()
     */
    public float getTotalDistance() {
        return total_distance;
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getStartPosition()
     */
    public Position getStartPosition() {
        return start_position;
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getEndPosition()
     */
    public Position getEndPosition() {
        return end_position;
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getCalaries()
     */
    public int getCalories() {
        return calories;
    }

    /* (non-Javadoc)
     * @see org.jscience.devices.gps.ILap#getTrackIndex()
     */
    public short getTrackIndex() {
        return track_index;
    }
}
