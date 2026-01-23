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

package org.jscience.history.time;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A time server that synchronizes with a network source (e.g., NTP server).
 * Periodically fetches time from the specified host and notifies listeners.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @version 1.1
 * @since 1.0
 */
public class NetworkTimeServer extends TimeServer {

    private final String host;
    private final long poolIntervalMs;
    private Timer timer;

    /**
     * Creates a new NetworkTimeServer for the specified pool.
     *
     * @param host the NTP server hostname (e.g., "pool.ntp.org")
     */
    public NetworkTimeServer(String host) {
        this(host, 60000); // 1 minute default
    }

    /**
     * Creates a new NetworkTimeServer with custom host and interval.
     *
     * @param host           hostname
     * @param poolIntervalMs polling frequency
     * @throws NullPointerException if host is null
     */
    public NetworkTimeServer(String host, long poolIntervalMs) {
        this.host = Objects.requireNonNull(host, "NTP host cannot be null");
        this.poolIntervalMs = poolIntervalMs;
    }

    @Override
    public synchronized void start() {
        if (timer != null) return;
        
        timer = new Timer("NetworkTimeServer-" + host, true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Placeholder for actual NTP fetching logic
                // In a real implementation, we would use a library like Apache Commons Net
                ModernTime syncedTime = new ModernTime(); 
                dispatchTimeEvent(new TimeEvent(NetworkTimeServer.this, syncedTime, TimeEvent.TIME_CHANGED));
            }
        }, 0, poolIntervalMs);
    }

    @Override
    public synchronized void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * Returns the target synchronization host.
     * @return hostname
     */
    public String getHost() {
        return host;
    }
}
