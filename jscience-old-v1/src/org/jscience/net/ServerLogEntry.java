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

package org.jscience.net;

import java.io.UnsupportedEncodingException;

import java.net.*;

import java.util.Date;


/**
 * a log entry from a web-server log
 *
 * @author Holger Antelmann
 *
 * @see ServerLog
 */
public class ServerLogEntry {
    /** DOCUMENT ME! */
    String host;

    /** DOCUMENT ME! */
    String method;

    /** DOCUMENT ME! */
    String url;

    /** DOCUMENT ME! */
    String referer;

    /** DOCUMENT ME! */
    Date time;

    /** DOCUMENT ME! */
    String agent;

    /** DOCUMENT ME! */
    String user;

    /** DOCUMENT ME! */
    short code;

    /** DOCUMENT ME! */
    String protocol;

/**
     * Creates a new ServerLogEntry object.
     */
    ServerLogEntry() {
    }

/**
     * Creates a new ServerLogEntry object.
     *
     * @param host     DOCUMENT ME!
     * @param method   DOCUMENT ME!
     * @param url      DOCUMENT ME!
     * @param referer  DOCUMENT ME!
     * @param time     DOCUMENT ME!
     * @param agent    DOCUMENT ME!
     * @param user     DOCUMENT ME!
     * @param code     DOCUMENT ME!
     * @param protocol DOCUMENT ME!
     */
    public ServerLogEntry(String host, String method, String url,
        String referer, Date time, String agent, String user, short code,
        String protocol) {
        this.host = host;
        this.method = method;
        this.url = url;
        this.referer = referer;
        this.time = time;
        this.agent = agent;
        this.user = user;
        this.code = code;
        this.protocol = protocol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHost() {
        return host;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnknownHostException DOCUMENT ME!
     */
    public InetAddress getHostAddress() throws UnknownHostException {
        return InetAddress.getByName(host);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMethod() {
        return method;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getURL() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getReferer() {
        return referer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Date getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAgent() {
        return agent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUser() {
        return user;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public short getCode() {
        return code;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * extracts the query parameter portion of a URL that usually
     * contains the search string that was used to query a search engine
     *
     * @return null if no query parameter was present or the UTF-8 decoded
     *         query parameter value
     */
    public String getQueryString() {
        try {
            URL url = new URL(referer);
            String s = url.getQuery();

            if (s == null) {
                return null;
            }

            try {
                s = URLDecoder.decode(s, "UTF-8");
            } catch (IllegalArgumentException ignore) {
            }

            int p = s.indexOf("q=");

            if (p >= 0) {
                int end = s.indexOf("&", p);

                if (end < 1) {
                    end = s.length();
                }

                return s.substring(p + 2, end);
            } else {
                return "";
            }
        } catch (UnsupportedEncodingException ignore) {
            return null;
        } catch (MalformedURLException ignore) {
            return null;
        }
    }
}
