/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
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
