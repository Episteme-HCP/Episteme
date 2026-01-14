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
