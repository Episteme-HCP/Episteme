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

import java.io.IOException;
import java.io.Serializable;

import java.net.URL;

import java.util.List;


/**
 * SampleCrawlerSetting is what it's named: a sample CrawlerSetting. It is
 * currently used by JSpider as the default CrawlerSetting.
 *
 * @author Holger Antelmann
 *
 * @see JSpider
 */
public class SampleCrawlerSetting implements CrawlerSetting, Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2050256965668017590L;

    /** DOCUMENT ME! */
    public static final String[] defaultRestrictURLPattern = new String[] {
            ".zip", ".ZIP", ".tar", ".TAR", ".gif", ".GIF", ".jpg", ".JPG",
            ".jpeg", ".JPEG", ".gz", ".GZ", ".pdf", ".PDF", ".rm", //".asp",
            ".mpeg", ".mp3", ".mpg", ".jar", ".ppt", ".exe"
        };

    /** DOCUMENT ME! */
    public int depth;

    /** DOCUMENT ME! */
    public boolean currentSiteOnly;

    /** DOCUMENT ME! */
    public String[] restrictURLPattern = null;

    /** DOCUMENT ME! */
    public String[] includeTextPattern = null;

    /** DOCUMENT ME! */
    public boolean includeHTMLCode = false;

    /** DOCUMENT ME! */
    boolean active = true;

/**
     * searches all files 3 levels deep in current site only
     */
    public SampleCrawlerSetting() {
        this(3, null);
    }

/**
     * Creates a new SampleCrawlerSetting object.
     *
     * @param depth              DOCUMENT ME!
     * @param includeTextPattern DOCUMENT ME!
     */
    public SampleCrawlerSetting(int depth, String includeTextPattern) {
        this(depth, true, defaultRestrictURLPattern,
            (includeTextPattern == null) ? null
                                         : new String[] { includeTextPattern },
            false);
    }

/**
     * Creates a new SampleCrawlerSetting object.
     *
     * @param depth              DOCUMENT ME!
     * @param currentSiteOnly    DOCUMENT ME!
     * @param restrictURLPattern DOCUMENT ME!
     * @param includeTextPattern DOCUMENT ME!
     * @param includeHTMLCode    DOCUMENT ME!
     */
    public SampleCrawlerSetting(int depth, boolean currentSiteOnly,
        String[] restrictURLPattern, String[] includeTextPattern,
        boolean includeHTMLCode) {
        this.depth = depth;
        this.currentSiteOnly = currentSiteOnly;
        this.restrictURLPattern = restrictURLPattern;
        this.includeTextPattern = includeTextPattern;
    }

    /**
     * DOCUMENT ME!
     *
     * @param flag DOCUMENT ME!
     */
    public void setActive(boolean flag) {
        active = flag;
    }

    /**
     * if inactive, followLinks() always returns false
     *
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        return active;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @param referer DOCUMENT ME!
     * @param depth DOCUMENT ME!
     * @param resultURLList DOCUMENT ME!
     * @param closedURLList DOCUMENT ME!
     * @param searchURLWrapperList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean followLinks(URL url, URL referer, int depth,
        List resultURLList, List closedURLList, List searchURLWrapperList) {
        if (!active) {
            return false;
        }

        if (this.depth <= depth) {
            return false;
        }

        if (url.getProtocol().equals("mailto")) {
            return false;
        }

        if (currentSiteOnly && (referer != null) &&
                (!url.getHost().equals(referer.getHost()))) {
            return false;
        }

        if (restrictURLPattern != null) {
            for (int i = 0; i < restrictURLPattern.length; i++) {
                if (url.getPath().indexOf(restrictURLPattern[i]) > -1) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @param referer DOCUMENT ME!
     * @param depth DOCUMENT ME!
     * @param resultURLList DOCUMENT ME!
     * @param closedURLList DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean matchesCriteria(URL url, URL referer, int depth,
        List resultURLList, List closedURLList) {
        if (currentSiteOnly && (referer != null) &&
                (!url.getHost().equals(referer.getHost()))) {
            return false;
        }

        if (url.getProtocol().equals("mailto")) {
            return false;
        }

        if (restrictURLPattern != null) {
            for (int i = 0; i < restrictURLPattern.length; i++) {
                if (url.getPath().indexOf(restrictURLPattern[i]) > -1) {
                    return false;
                }
            }
        }

        try {
            Spider sp = new Spider(url);

            if ((includeTextPattern != null) &&
                    !sp.includesPattern(includeTextPattern, includeHTMLCode)) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += (" (depth: " + depth);
        s += ("; currentSiteOnly: " + currentSiteOnly);
        s += "; restrictURLPattern: ";

        if (restrictURLPattern == null) {
            s += "null";
        } else {
            for (int i = 0; i < restrictURLPattern.length; i++) {
                s += ("\"" + restrictURLPattern[i] + "\"");

                if (i < (restrictURLPattern.length - 1)) {
                    s += ", ";
                }
            }
        }

        s += "; includeTextPattern: ";

        if (includeTextPattern == null) {
            s += "null";
        } else {
            for (int i = 0; i < includeTextPattern.length; i++) {
                s += ("\"" + includeTextPattern[i] + "\"");

                if (i < (includeTextPattern.length - 1)) {
                    s += ", ";
                }
            }
        }

        s += ("; includeHTMLCode: " + includeHTMLCode + ")");

        return s;
    }
}
