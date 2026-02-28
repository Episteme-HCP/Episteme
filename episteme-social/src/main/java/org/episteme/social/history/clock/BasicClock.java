/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.social.history.clock;

import java.util.Objects;

/**
 * A basic clock implementation using standard {@link ModernTime}.
 * Automatically synchronizes with the provided {@link TimeServer}.
 * * @version 1.1
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class BasicClock extends Clock {

    /** Current time state. */
    private ModernTime time;

    /**
     * Creates a new BasicClock associated with a time server.
     *
     * @param timeServer the time server to monitor
     */
    public BasicClock(TimeServer timeServer) {
        super(timeServer);
        this.time = new ModernTime();
    }

    @Override
    public ModernTime getTime() {
        return time;
    }

    /**
     * Updates the internal time state.
     *
     * @param time the new time state
     * @throws NullPointerException if time is null
     */
    public void setTime(ModernTime time) {
        this.time = Objects.requireNonNull(time, "Time cannot be null");
    }

    @Override
    public void timeChanged(TimeEvent event) {
        Objects.requireNonNull(event, "TimeEvent cannot be null");
        if (event.getTime() instanceof ModernTime modernTime) {
            setTime(modernTime);
        }
    }
}

