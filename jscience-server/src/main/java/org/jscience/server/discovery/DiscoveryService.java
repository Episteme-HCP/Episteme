/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.server.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;

/**
 * mDNS/Zeroconf Service for auto-discovery of the JScience Grid.
 * Server broadcasts its presence; Workers can discover it.
 */
public class DiscoveryService {
    private static final Logger LOG = LoggerFactory.getLogger(DiscoveryService.class);

    public static final String SERVICE_TYPE = "_jscience-grid._tcp.local.";
    public static final String SERVICE_NAME = "JScience-Grid-Server";

    private JmDNS jmdns;
    private ServiceInfo serviceInfo;

    /**
     * Start broadcasting the server on mDNS (for server use).
     */
    public void startBroadcast(int port) {
        try {
            jmdns = JmDNS.create(InetAddress.getLocalHost());
            serviceInfo = ServiceInfo.create(SERVICE_TYPE, SERVICE_NAME, port, "JScience Distributed Grid Server");
            jmdns.registerService(serviceInfo);
            LOG.info("mDNS: Broadcasting {} on port {}", SERVICE_NAME, port);
        } catch (IOException e) {
            LOG.error("Failed to start mDNS broadcast", e);
        }
    }

    /**
     * Discover the server on the network (for worker use).
     * 
     * @return ServiceInfo or null if not found
     */
    public static ServiceInfo discoverServer(int timeoutMs) {
        try (JmDNS jmdns = JmDNS.create(InetAddress.getLocalHost())) {
            LOG.info("mDNS: Searching for {} ...", SERVICE_NAME);
            ServiceInfo[] services = jmdns.list(SERVICE_TYPE, timeoutMs);
            if (services.length > 0) {
                ServiceInfo found = services[0];
                LOG.info("mDNS: Found server at {}:{}",
                        found.getHostAddresses()[0], found.getPort());
                return found;
            }
        } catch (IOException e) {
            LOG.error("mDNS discovery failed", e);
        }
        return null;
    }

    /**
     * Stop broadcasting (call on server shutdown).
     */
    public void stop() {
        if (jmdns != null) {
            jmdns.unregisterAllServices();
            try {
                jmdns.close();
            } catch (IOException e) {
                LOG.warn("Error closing mDNS", e);
            }
        }
    }
}


