/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.util.Filter;
import org.jscience.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;


/**
 * provides several methods to analyse a ServerLog
 *
 * @author Holger Antelmann
 */
public class ServerLogAnalyser {
    /**
     * DOCUMENT ME!
     */
    static String lb = System.getProperty("line.separator", "\n");

    /**
     * DOCUMENT ME!
     */
    ServerLog log;

    /**
     * Creates a new ServerLogAnalyser object.
     *
     * @param log DOCUMENT ME!
     */
    public ServerLogAnalyser(ServerLog log) {
        this.log = log;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    ServerLog getLog() {
        return log;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getGoogleSearchStrings() {
        Filter<ServerLogEntry> filter = new Filter<ServerLogEntry>() {
            public boolean accept(ServerLogEntry entry) {
                if (entry.getReferer() == null) {
                    return false;
                }

                return (entry.getReferer().indexOf("google") > -1) ? true
                        : false;
            }
        };

        Enumeration e = log.getServerLogEntryEnumerator(filter);
        Vector<String> v = new Vector<String>();

        while (e.hasMoreElements()) {
            try {
                String s = ((ServerLogEntry) e.nextElement()).getReferer();
                URL url = new URL(s);
                s = url.getQuery();

                if (s == null) {
                    continue;
                }

                s = URLDecoder.decode(s, "UTF-8");

                int p = s.indexOf("q=");

                if (p >= 0) {
                    int end = s.indexOf("&", p);

                    if (end < 1) {
                        end = s.length();
                    }

                    String q = s.substring(p + 2, end);
                    v.add(q);
                } else {
                }
            } catch (UnsupportedEncodingException ignore) {
            } catch (MalformedURLException ignore) {
            } catch (IllegalArgumentException ignore) {
            }
        }

        return v.toArray(new String[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void writeRefererHostCount(File file) throws IOException {
        FileWriter writer = new FileWriter(file, false);
        writer.write("refererHost\tcount" + lb);
        writer.write(StringUtils.mapAsString(refererHostCount(null), "\t", lb));
        writer.flush();
        writer.close();
    }

    /**
     * returns a map with the key elements being Strings denoting the
     * referer host name and the mapped value (an Integer) denoting the count.
     *
     * @param filter DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public Map<String, Integer> refererHostCount(Filter<ServerLogEntry> filter)
            throws IOException {
        Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
        Enumeration<ServerLogEntry> e = log.getServerLogEntryEnumerator(filter);

        while (e.hasMoreElements()) {
            ServerLogEntry entry = e.nextElement();

            try {
                String host = new URL(entry.getReferer()).getHost();

                if (hash.containsKey(host)) {
                    hash.put(host, hash.get(host) + 1);
                } else {
                    hash.put(host, 1);
                }
            } catch (MalformedURLException ignore) {
            } catch (NullPointerException ignore) {
            }
        }

        return hash;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void writeRefererDomainCount(File file) throws IOException {
        FileWriter writer = new FileWriter(file, false);
        writer.write("refererDomain\tcount" + lb);
        writer.write(StringUtils.mapAsString(refererDomainCount(null), "\t", lb));
        writer.flush();
        writer.close();
    }

    /**
     * returns a map with the key elements being Strings denoting the
     * referer domain name and the mapped value (an Integer) denoting the
     * count.
     *
     * @param filter DOCUMENT ME!
     * @return DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public Map<String, Integer> refererDomainCount(
            Filter<ServerLogEntry> filter) throws IOException {
        Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
        Enumeration<ServerLogEntry> e = log.getServerLogEntryEnumerator(filter);

        while (e.hasMoreElements()) {
            ServerLogEntry entry = e.nextElement();

            try {
                String domain = new Spider(new URL(entry.getReferer())).getDomainName();

                if (hash.containsKey(domain)) {
                    hash.put(domain, hash.get(domain) + 1);
                } else {
                    hash.put(domain, 1);
                }
            } catch (MalformedURLException ignore) {
            } catch (NullPointerException ignore) {
            }
        }

        return hash;
    }
}
