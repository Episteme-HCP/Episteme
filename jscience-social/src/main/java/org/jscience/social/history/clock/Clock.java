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

package org.jscience.social.history.clock;

import java.util.Objects;
import org.jscience.social.history.time.Time;

import java.io.Serializable;

/**
 * An abstract base class for clock implementations that observe a {@link TimeServer}.
 * A clock maintains a reference to a time server and reacts to its chronological updates.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public abstract class Clock implements TimeListener, Serializable {

    private static final long serialVersionUID = 1L;

    /** The time server instance this clock is synchronized with. */
    private final TimeServer timeServer;

    /**
     * Creates a new Clock synchronized with the specified time server.
     *
     * @param timeServer the source of chronological updates
     * @throws NullPointerException if timeServer is null
     */
    protected Clock(TimeServer timeServer) {
        this.timeServer = Objects.requireNonNull(timeServer, "TimeServer cannot be null");
        this.timeServer.addTimeListener(this);
    }

    /**
     * Returns the time server associated with this clock.
     *
     * @return the time server
     */
    public final TimeServer getTimeServer() {
        return timeServer;
    }

    /**
     * Returns the current time representation held by this clock.
     *
     * @return the current time
     */
    public abstract Time getTime();

    @Override
    public String toString() {
        return getTime().toString();
    }
}

