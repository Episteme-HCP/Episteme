/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * MediaCrawler is a single thread that searches the web for files that are of
 * a given type.
 *
 * @author Holger Antelmann
 * @see Spider
 * @since 10/29/2002
 */
public class MediaCrawler extends Thread implements CrawlerSetting {
    /**
     * used to handle the media files found during the search of the MediaCrawler
     */
    public static interface Handler {
        /**
         * called when the given URL is searched for further links
         */
        void currentURL(URL url);

        /**
         * called when a media file is found, so that it can be saved or handled otherwise
         *
         * @see URLCache#saveContentToFile(File,boolean)
         * @see Downloader
         * @see JDownloader
         */
        void foundMedia(URLCache uc);
    }

    static String[] restrictURLPattern = new String[]{
            ".zip", ".ZIP", ".tar", ".TAR", ".gif", ".GIF", ".jpg", ".JPG",
            ".jpeg", ".JPEG", ".gz", ".GZ", ".pdf", ".PDF", ".rm", //".asp",
            ".mpeg", ".mp3", ".mpg", ".jar", ".ppt", ".exe"
    };

    URL rootURL;
    String mediaExtension;
    String[] pattern;
    int depth;
    boolean currentSiteOnly;
    Vector<URLCache> list;
    Vector<Handler> listener;

    public MediaCrawler(URL rootURL, int depth, String mediaExtension, boolean currentSiteOnly, String[] pattern) {
        this(rootURL, depth, mediaExtension, currentSiteOnly, null, pattern);
    }

    public MediaCrawler(URL rootURL, int depth, String mediaExtension, boolean currentSiteOnly, Handler handler, String[] pattern) {
        this.rootURL = rootURL;
        this.pattern = pattern;
        this.mediaExtension = mediaExtension.toLowerCase();
        this.depth = depth;
        this.currentSiteOnly = currentSiteOnly;
        list = new Vector<URLCache>();
        listener = new Vector<Handler>();
        if (handler != null) listener.add(handler);
    }

    public void addHandler(Handler handler) {
        listener.add(handler);
    }

    public void run() {
        new Spider(rootURL).crawlWeb(this, 0, null);
    }

    public URLCache[] getFilesFound() {
        return list.toArray(new URLCache[list.size()]);
    }

    public boolean followLinks(URL url, URL referer, int depth, List<URL> resultURLList, List<URL> closedURLList, List<Spider.URLWrapper> searchURLWrapperList) {
        if (currentSiteOnly && (referer != null) && (!url.getHost().equals(referer.getHost())))
            return false;
        if (this.depth <= depth) return false;
        if (url.getProtocol().equals("mailto")) return false;
        for (int i = 0; i < restrictURLPattern.length; i++) {
            if (url.getPath().indexOf(restrictURLPattern[i]) > -1) return false;
        }
        if (pattern != null) {
            try {
                String content = new Spider(url).getContentAsString();
                boolean contained = false;
                for (int i = 0; i < pattern.length; i++) {
                    if (content.indexOf(pattern[i]) > -1) {
                        contained = true;
                        break;
                    }
                }
                if (!contained) return false;
            } catch (IOException ex) {
                return false;
            }
        }
        for (Handler h : listener) h.currentURL(url);
        return true;
    }

    public boolean matchesCriteria(URL url, URL referer, int depth, List<URL> resultURLList, List<URL> closedURLList) {
        if (url.getProtocol().equals("mailto")) return false;
        if (url.getProtocol().equals("file")) return false;
        URLCache uc = new URLCache(url);
        if (mediaExtension.equals(uc.getFileExtension())) {
            if (!list.contains(uc)) {
                list.add(uc);
                Iterator i = listener.iterator();
                for (Handler h : listener) h.foundMedia(uc);
            }
            return true;
        }
        return false;
    }
}
