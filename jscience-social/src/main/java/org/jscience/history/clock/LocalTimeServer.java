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

package org.jscience.history.clock;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A local time server that broadcasts modern wall-clock time updates at regular intervals.
 * Uses a daemon {@link Timer} to periodically sample system time and notify listeners.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class LocalTimeServer extends TimeServer {

    private Timer timer;
    private final long intervalMs;

    /**
     * Creates a new LocalTimeServer with a default update interval of 1 second.
     */
    public LocalTimeServer() {
        this(1000);
    }

    /**
     * Creates a new LocalTimeServer with a custom update interval.
     *
     * @param intervalMs update frequency in milliseconds
     */
    public LocalTimeServer(long intervalMs) {
        this.intervalMs = intervalMs;
    }

    @Override
    public synchronized void start() {
        if (timer != null) return;
        
        timer = new Timer("LocalTimeServer-Thread", true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ModernTime currentTime = new ModernTime();
                dispatchTimeEvent(new TimeEvent(LocalTimeServer.this, currentTime, TimeEvent.TIME_CHANGED));
            }
        }, 0, intervalMs);
    }

    @Override
    public synchronized void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
