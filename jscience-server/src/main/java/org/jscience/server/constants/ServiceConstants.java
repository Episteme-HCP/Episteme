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

package org.jscience.server.constants;

/**
 * Service-related constants for JScience Server.
 * 
 * This class contains constants for:
 * - mDNS service discovery
 * - Service identifiers
 * - Protocol definitions
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Mohr, P. J., et al. (2016). CODATA Recommended Values of the Fundamental Physical Constants. <i>Reviews of Modern Physics</i>.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public final class ServiceConstants {

    private ServiceConstants() {
        throw new AssertionError("Cannot instantiate constants class");
    }

    // === mDNS Service Discovery ===

    /**
     * mDNS service type for JScience servers.
     * Format: _service._protocol.domain
     */
    public static final String MDNS_SERVICE_TYPE = "_jscience._tcp.local.";

    /**
     * Default service name for mDNS advertisement.
     */
    public static final String MDNS_SERVICE_NAME = "JScience Server";

    // === Service Identifiers ===

    /**
     * Unique service ID for the compute service.
     */
    public static final String SERVICE_ID_COMPUTE = "jscience.compute";

    /**
     * Unique service ID for the authentication service.
     */
    public static final String SERVICE_ID_AUTH = "jscience.auth";

    /**
     * Unique service ID for the device management service.
     */
    public static final String SERVICE_ID_DEVICE = "jscience.device";

    /**
     * Unique service ID for the data service.
     */
    public static final String SERVICE_ID_DATA = "jscience.data";

    /**
     * Unique service ID for the collaboration service.
     */
    public static final String SERVICE_ID_COLLABORATION = "jscience.collaboration";

    // === Protocol Versions ===

    /**
     * Current gRPC API version.
     */
    public static final String API_VERSION = "v1";

    /**
     * Current protocol version for task serialization.
     */
    public static final int PROTOCOL_VERSION = 1;

    // === Service Metadata ===

    /**
     * Default service priority for load balancing.
     */
    public static final int DEFAULT_PRIORITY = 0;

    /**
     * Default service weight for load balancing.
     */
    public static final int DEFAULT_WEIGHT = 100;

    /**
     * Service heartbeat interval in seconds.
     */
    public static final int HEARTBEAT_INTERVAL_SECONDS = 30;

    /**
     * Service timeout threshold in seconds.
     */
    public static final int SERVICE_TIMEOUT_SECONDS = 90;
}
