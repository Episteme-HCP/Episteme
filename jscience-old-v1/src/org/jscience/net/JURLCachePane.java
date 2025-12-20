/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * A JEditorPane that can take advantage of a URLCache.
 * Unfortunately, this doesn't work quite yet, as the @#$&*^#@$#@
 * JEditorPane still reads directly from the URL.
 *
 * @author Holger Antelmann
 */
public class JURLCachePane extends JEditorPane {
    static final long serialVersionUID = 2164716976772868807L;

    Map<URL, URLCache> cache;

    public JURLCachePane() {
        this(null);
    }

    /**
     * the cache must map URL objects to URLCache objects
     */
    public JURLCachePane(Map<URL, URLCache> cache) {
        super();
        setCache(cache);
    }

    /**
     * the cache must map URL objects to URLCache objects
     */
    public void setCache(Map<URL, URLCache> cache) {
        this.cache = (cache == null) ? new HashMap<URL, URLCache>() : cache;
    }

    /**
     * returns a map of URL objects to URLCache objects if not null
     */
    public Map getCache() {
        return cache;
    }

    URLCache getURLCache(URL url) {
        synchronized (cache) {
            URLCache uc = cache.get(url);
            if (uc != null) {
                return uc;
            }
            return new URLCache(url);
        }
    }

    /**
     * ensures that the given parameter is available in the cache
     * and then calls setPage(URL)
     *
     * @see javax.swing.JEditorPane#setPage(java.net.URL)
     */
    void setPage(URLCache uc) throws IOException {
        synchronized (cache) {
            if (!cache.containsKey(uc.getURL())) {
                cache.put(uc.getURL(), uc);
            }
        }
        setPage(uc.getURL());
    }

    /**
     * the ability to forward the request to a redirect
     * is lost in this implementation
     */
    protected InputStream getStream(URL page) throws IOException {
        URLCache uc = getURLCache(page);
        String type = uc.getContentType();
        if (type != null) {
            setContentType(type);
        }
        InputStream in = uc.getInputStream();
        return in;
    }
}
