/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.util.StringUtils;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * A wrapper around java.net.URL that caches a copy of the content
 * and adds some additional functionality designed for HTML pages. <p>
 * The URLCache cache can be made up-to-date by calling <code>refresh()</code>.<br>
 * The URLCache object also maintains the time when it was last updated successfully.
 * The method <code>lastUpdated()</code> will return that time. <p>
 * Most methods operate on the cached data to avoid the need to reconnect
 * over the web for each operation. This allows to call several methods on a
 * URLCache object sequentially with reasonable performance. <p>
 * If a call to a content accessing method is first made and the data has not
 * been cached yet, the method will enforce a refresh and wait for completion.
 * If the content could not be refreshed due to an IOException, that
 * exception is then immediately thrown as well as in all subsequent method
 * calls accessing the content. <p>
 * <p/>
 * If <code>refresh()</code> is then called at later times, either the content
 * is successfully cached then (and the initial exception is irrelevant) or
 * the exception is refreshed.<p>
 * <p/>
 * If - once the content is initially cached - subsequent attempts to
 * <code>refresh()</code> the the cache fail, all content accessing methods will
 * still revert to the cached data. <br>
 * If you then want to find out the cause for the failure of subsequent calls to
 * <code>refresh()</code>, you will have to register a RefreshListener, as
 * the callback method defined there will include the IOException in case of an
 * unsuccessful refresh attempt.<p>
 * <p/>
 * A special case is if the content data of the embedded URL is too large
 * to currently fit into memory; i.e. caching is impossible, although the URL
 * is accessible. In that case, methods like <code>getContentAsString()</code>
 * will return an IOException with the message that the data content is too
 * large to fit into memory, while other methods (like
 * <code>saveContentToFile()</code> or <code>getInputStream()</code>)
 * will then directly work off the online content.
 * Even though many operations may probably be quite difficult with content
 * that cannot be cached due to its size, the method
 * <code>saveContentToFile()</code> can still be used savely regardless of
 * the size of the content (of course it may take quite a bit of time).
 * The method <code>tooLargeForCaching()</code> will indicate whether this
 * is the case. Note that a later call to <code>refresh()</code>  - after
 * some memory has been freed up - may be able to cache the same object
 * successfully. <p>
 * <p/>
 * In addition, this class maintains a static map serving as an application-wide
 * cache for URLCache objects, which can be accessed using the put() and get()
 * methods. <p>
 * <p/>
 * Currently, this implementation starts a new thread whenever refresh() is
 * called. A future revision may want to revise the performance overhead
 * associated with this. The implementation is suited for large content
 * on slow networks, so that it makes sense to load the data for each
 * URL in a separate thread simultaneously.<p>
 * <p/>
 * Note that many methods assume that the underlying content is HTML data;
 * if that is untrue for a specific object, these methods may return empty
 * objects.<p>
 * <p/>
 * Note that the only data that is actually cached is a byte array that
 * represents the content fetched from the URL and a header map;
 * all other information (title, links, images, etc.) will be calculated each
 * time based on the cached byte array.
 *
 * @author Holger Antelmann
 * @see Spider
 * @since 4/2/2002
 */
public class URLCache implements Serializable {
    static final long serialVersionUID = 5452986503371927044L;

    class RefreshThread extends Thread {
        /**
         * accessed by clearCache() to ensure prompt return
         */
        InputStream in = null;
        /**
         * accessed by URLCache to enable progress monitoring
         */
        int bytesReadSoFar = 0;

        public void run() {
            boolean result = false;
            IOException cause = null;
            try {
                tooLargeForCaching = false;
                long tmp = System.currentTimeMillis();
                URLConnection con = url.openConnection();
                con.setUseCaches(false);
                header = con.getHeaderFields();
                content = readContent(con);
                lastRefresh = System.currentTimeMillis();
                updated = lastRefresh;
                refreshTime = lastRefresh - tmp;
                result = true;
            } catch (IOException e) {
                lastRefresh = System.currentTimeMillis();
                cause = e;
                if (content == null) {
                    problem = cause;
                } else {
                    problem = null;
                }
            } finally {
                notifyListeners(result, cause);
                refresher = null;
            }
        }

        /**
         * redundant to Spider.getBytes(URL) to reuse the same connection
         */
        byte[] readContent(URLConnection con) throws IOException {
            InputStream in = null;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                byte[] bytes = new byte[0];
                byte[] buffer = new byte[bufferSize];
                in = con.getInputStream();
                int n = 0;
                int pos;
                while (n > -1) {
                    n = in.read(buffer);
                    bytesReadSoFar += n;
                    if (n < 0) break;
                    out.write(buffer, 0, n);
                    if (interrupted()) {
                        throw new IOException("refresh was interrupted");
                    }
                }
                return out.toByteArray();
            } catch (OutOfMemoryError e) {
                tooLargeForCaching = true;
                return null;
            } finally {
                if (in != null) in.close();
            }
        }
    }

    /**
     * RefreshListener objects can register with URLCache objects to be
     * notified when the URLCache object is refreshed
     *
     * @see URLCache#addRefreshListener(URLCache.RefreshListener)
     * @see URLCache#refresh()
     */
    public interface RefreshListener extends EventListener {
        /**
         * This method is called when a call to <code>URLCache.refresh()</code>
         * completes. This method is to return quickly as a hold-up here
         * delays the completion of a call to <code>URLCache.refresh()</code>!
         * The object monitor of the given URLCache instance is held while
         * this method executes, which blocks further refresh() calls;
         * it does not block access to previously cached data, however.
         *
         * @param uc                The URLCache object that performed the refresh
         * @param refreshSuccessful indicating whether the cache was successfully updated;
         * @param reasonForFailure  contains the exception that caused the refresh to fail;
         *                          is null if update was successful
         * @see URLCache#refresh()
         */
        void refreshedURLCache(URLCache uc, boolean refreshSuccessful, IOException reasonForFailure);
    }

    static int bufferSize = 2048;
    //static HashMap<URL, URLCache> cacheMap = new HashMap<URL, URLCache>();

    URL url = null;
    long updated = 0;
    long lastRefresh = 0;
    long refreshTime = -1;
    Vector<RefreshListener> listeners = new Vector<RefreshListener>(1, 1);
    ;
    boolean tooLargeForCaching = false;

    volatile Map header = null;
    volatile byte[] content = null;
    volatile IOException problem = null;
    volatile RefreshThread refresher = null;

    /**
     * constructs the URLCache object based on the spec denoting
     * the absolute path of the URL and without refresh
     *
     * @see #URLCache(URL,boolean)
     */
    public URLCache(String spec) throws MalformedURLException {
        this(new URL(spec), false);
    }

    /**
     * calls <code>URLCache(url, false)</code>
     *
     * @see #URLCache(URL,boolean)
     */
    public URLCache(URL url) {
        this(url, false);
    }

    /**
     * constructs the URLCache object based on the given URL.
     * If refreshNow is true, an immediate call to
     * <code>refresh()</code> is made in order to obtain a cached version;
     * the refresh is performed asynchronously, i.e. the constructor
     * returns immediately. A delay may then only be experienced when
     * the first content accessing method is called.
     *
     * @see #refresh()
     */
    public URLCache(URL url, boolean refreshNow) {
        this.url = url;
        if (refreshNow) refresh();
    }

    /**
     * puts the given URLCache object into the static cache maintained by the class.
     * The method complies to the contract of the java.util.Map interface.
     */
    /*
    public static URLCache put (URLCache uc) {
        return cacheMap.put(uc.getURL(), uc);
    }
    */

    /**
     * retrieves a URLCache object from the given URL object.
     * If the URL is not present in the class cache, the URLCache object
     * is created and put into the class cache.
     * The method complies to the contract of the java.util.Map interface, otherwise.
     */
    /*
    public static URLCache get (URL url) {
        URLCache uc = cacheMap.get(url);
        if (uc == null) {
            uc = new URLCache(url);
            cacheMap.put(uc.getURL(), uc);
        }
        return uc;
    }
    */


    /**
     * returns only after the refresh finished
     *
     * @see #refresh()
     */
    public void refreshAndWait() {
        refresh();
        if (refresher != null) {
            try {
                refresher.join();
            } catch (InterruptedException e) {
            } catch (NullPointerException e) {
            }
        }
    }

    /**
     * updates the cached content asynchronously with a fresh copy
     * directly from the web.
     * <br>This method returns immediately;
     * the refresh is performed in a separate thread. <p>
     * If you want to be notified when a URLCache object has completed a refresh call,
     * (whether or not the refresh was successful) you can register a RefreshListener.
     * The call to the RefreshListener will also contain the information whether the
     * refresh was successfully performed or not; if unsuccessful, the IOException that
     * caused this latest <code>refresh()</code> to fail will be included there, too. <p>
     * As only one refresh thread per instance is allowed at a time, a call
     * to <code>refresh()</code> may cause listeners to be notified of a failure
     * due to concurrent <code>refresh()</code> calls; the IOException included
     * in the callback will reflect that.
     * In fact, this method doesn't allow a subsequent refresh in less than a second
     * after the last refresh finished (in those cases, the method call is simply ignored).
     * If a refresh attempt is unsuccessful, all previously cached data
     * is maintained from the time that <code>lastUdated()</code> indicates.
     * The time of the last refresh attempt can be retrieved by calling
     * <code>lastRefreshAttempt()</code>; if the last call was successfull,
     * <code>lastUdated()</code> and <code>lastRefreshAttempt()</code> will
     * return the same value.
     *
     * @see #addRefreshListener(URLCache.RefreshListener)
     * @see #lastRefreshed()
     * @see #lastUpdated()
     * @see #isRefreshing()
     * @see #refreshAndWait()
     * @see URLCache.RefreshListener
     */
    public void refresh() {
        // exit immediately if refresh in progress
        if (refresher != null) {
            notifyListenerOfConcurrentAccess();
            return;
        }
        synchronized (this) {
            // prevent continuous refresh
            if ((System.currentTimeMillis() - lastRefresh) < 1000) {
                notifyListenerOfConcurrentAccess();
                return;
            }
            refresher = new RefreshThread();
            refresher.start();
        }
    }

    /**
     * called only by refresh()
     */
    private void notifyListenerOfConcurrentAccess() {
        String s = "Concurrent call to URLCache.refresh(): another call to refresh()";
        s += " is currently either in process or completed within the last second";
        IOException e = new IOException(s);
        Iterator<RefreshListener> i = listeners.iterator();
        while (i.hasNext()) {
            i.next().refreshedURLCache(this, false, e);
        }
    }

    /**
     * interrupts any currently ongoing refresh process (if any)
     * and then returns; previously cached data and subsequent
     * calls are uneffected
     *
     * @see #refresh()
     */
    public void stopCurrentRefresh() {
        if (refresher != null) {
            try {
                refresher.interrupt();
                try {
                    refresher.in.close();
                } catch (IOException e) {
                } catch (NullPointerException e) {
                }
                refresher.join();
            } catch (InterruptedException e) {
            } catch (NullPointerException e) {
            }
        }
    }

    /**
     * interrupts any ongoing refresh process and clears the cache;
     * subsequent calls to any content will force a new refresh
     *
     * @see #refresh()
     */
    public void clearCache() {
        stopCurrentRefresh();
        content = null;
        header = null;
        problem = null;
        updated = 0;
        lastRefresh = 0;
        refreshTime = -1;
        tooLargeForCaching = false;
    }

    /**
     * This method only returns after ensuring that a cached
     * a result from a previous refresh() is available
     * (either the cached data or the cached IOException).
     *
     * @see #refresh()
     * @see #isCached()
     */
    public void waitForRefresh() {
        if (content != null) return;
        if (problem != null) return;
        if (tooLargeForCaching) return;
        if (refresher == null) {
            refresh();
        }
        try {
            refresher.join();
        } catch (InterruptedException e) {
        } catch (NullPointerException e) {
        }
    }

    /**
     * returns a Map to the cached header fields
     *
     * @throws IOException if the headers could not have been cached
     * @see java.net.URLConnection#getHeaderFields()
     */
    public Map getHeaderFields() throws IOException {
        waitForRefresh();
        if (header == null) {
            throw problem;
        } else {
            return header;
        }
    }

    /**
     * retrieves the first field value matching the fieldKey based on
     * case-insensitive key search
     *
     * @param fieldKey if null, it returns the HTTP response if available
     */
    public String getHeaderField(String fieldKey) throws IOException {
        Iterator i = getHeaderFields().keySet().iterator();
        String key = null;
        while (i.hasNext()) {
            key = (String) i.next();
            if (key == null) {
                if (fieldKey == null) {
                    return (String) ((List) (getHeaderFields().get(null))).get(0);
                }
            } else {
                if (key.equalsIgnoreCase(fieldKey)) {
                    return (String) ((List) (getHeaderFields().get(key))).get(0);
                }
            }
        }
        return null;
    }

    /**
     * returns the header value from the cached content
     */
    public String getContentType() throws IOException {
        return (String) getHeaderField("content-type");
    }

    /**
     * returns the header value from the cached content
     */
    public String getContentEncoding() throws IOException {
        return (String) getHeaderField("content-encoding");
    }

    /**
     * returns the header value from the cached content
     */
    public long getLastModified() throws IOException {
        String s = (String) getHeaderField("last-modified");
        try {
            return StringUtils.ietfDateFormat.parse(s).getTime();
        } catch (Exception e) {
        }
        return -1;
    }

    /**
     * returns the raw cached content.
     * null is returned only if content cannot be cached
     * (due to memory limitations) <p>
     * WARNING: Altering the returned array means altering the internal cache,
     * which is in effect until the next successfull refresh.
     */
    public byte[] getContent() throws IOException {
        waitForRefresh();
        if (content == null) {
            if (tooLargeForCaching) {
                return null;
            }
            throw problem;
        } else {
            return content;
        }
    }

    public String getContentAsString() throws IOException {
        if (tooLargeForCaching) {
            throw new IOException("data content is too large to fit into memory");
        }
        return new String(getContent());
    }

    public String getContentAsString(String charsetName) throws IOException {
        if (tooLargeForCaching) {
            throw new IOException("data content is too large to fit into memory");
        }
        return new String(getContent(), charsetName);
    }

    /**
     * returns a reader from the cached content (suitable for non-binary data).
     * If the data is too large to be cached, this method returns a reader
     * to the online content.
     */
    public Reader getReader() throws IOException {
        if (tooLargeForCaching) {
            return new Spider(url).getReader();
        } else {
            return new StringReader(getContentAsString());
        }
    }

    /**
     * returns a reader from the cached content by using the specified charset for decoding.
     * If the data is too large to be cached, this method returns a reader
     * to the online content.
     */
    public Reader getReader(String charsetName) throws IOException {
        if (tooLargeForCaching) {
            return new Spider(url).getReader(charsetName);
        } else {
            return new StringReader(getContentAsString(charsetName));
        }
    }

    /**
     * returns an input stream from the cached content (suitable for binary data).
     * Only if the data is too large to be cached, this method returns a stream
     * to the online content.
     */
    public InputStream getInputStream() throws IOException {
        if (tooLargeForCaching) {
            return url.openStream();
        } else {
            return new ByteArrayInputStream(getContent());
        }
    }

    /**
     * returns the HTML title of the cached document
     */
    public String getTitle() throws IOException {
        return getTagText(HTML.Tag.TITLE, null);
    }

    /**
     * returns URLs of all links from the cached HTML document.
     * Note that duplicate links will only be included once
     */
    public URL[] getLinks() throws IOException {
        return new Spider(url).getLinks(getReader(), false);
    }

    /**
     * returns URLs to all unique images embedded in the cached HTML document
     */
    public URL[] getImages() throws IOException {
        return new Spider(url).getImages(getReader(), false);
    }

    /**
     * returns a new HTMLDocument initialized with the cached content of this URLCache object.
     * If - for some reason - a BadLocationException is caught, this method returns null.
     */
    public HTMLDocument getHTMLDocument() throws IOException {
        return new Spider().getHTMLDocument(getReader());
    }

    /**
     * returns all text from the HTML cache data that is found in the given tag. <br>
     * The separate text sequences found are delimited by the given delimiter.
     */
    public String getTagText(final HTML.Tag desiredTag, final String delimiter) throws IOException {
        return new Spider().getTagText(getReader(), desiredTag, delimiter);
    }

    /**
     * calls the other stripText() method with a line break as delimiter
     */
    public String stripText() throws IOException {
        return stripText(StringUtils.lb);
    }

    /**
     * returns a String containing the text of all HTML tag types,
     * separated by the given delimiter
     */
    public String stripText(final String delimiter) throws IOException {
        return new Spider().stripText(getReader(), delimiter);
    }

    /**
     * returns true only if the cache is currently being refreshed
     */
    public boolean isRefreshing() {
        return (refresher != null);
    }

    /**
     * returns the time when the last refresh() attempt was performed
     * - whether or not successful.
     * If the last attempt was successful, the returned value is identical
     * to the return value of <code>lastUdated()</code>
     *
     * @see #lastUpdated()
     */
    public long lastRefreshed() {
        return lastRefresh;
    }

    /**
     * returns the time when this object was last refreshed successfully;
     * 0 is returned if no refresh has been performed, yet
     *
     * @see #lastRefreshed()
     */
    public long lastUpdated() {
        return updated;
    }

    /**
     * returns the time taken by the last successfull refresh;
     * -1 is returned if content was never successfully refreshed.
     *
     * @see #refresh()
     */
    public long getLastRefreshTime() {
        return refreshTime;
    }

    /**
     * checks whether the cached content equals the current live online content.
     * This method will connect to the actual URL and only return once
     * the cached data has been fully compared to the live content. <p>
     * It is a <b>bad</b> idea to call this method to see whether a
     * <code>refresh()</code> is needed, as a call to this method is just
     * as expensive as <code>refresh()</code> itself, only that the latter
     * returns immediately.
     * If you just want to check the provided timestamp of the online content
     * to see whether it is no later than your last successful refresh,
     * use <code>isUpToDate()</code> instead.
     *
     * @return true if the cached content is equal to the live online content
     *         and false if the content is different
     * @throws IOException if the connection to the live online
     *                     content failed
     * @see #isUpToDate()
     */
    public boolean verifyContent() throws IOException {
        if (tooLargeForCaching) return false;
        if (Arrays.equals(getContent(), new Spider(url).getBytes())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * checks whether the timestamp provided by the online content
     * is no later than your last successfull refresh. <p>
     * Note that the result may not be accurrate if the header timestamp
     * of the online content is incorrect or missing. <p>
     * If you need to  verify that the exact online content is in fact
     * identical to the cached content, use  <code>verifyContent()</code>
     * instead.
     *
     * @see #verifyContent()
     */
    public boolean isUpToDate() throws IOException {
        URLConnection con = url.openConnection();
        long mod = con.getLastModified();
        if (con instanceof java.net.HttpURLConnection) {
            ((java.net.HttpURLConnection) con).disconnect();
        }
        return (lastUpdated() >= mod);
    }

    /**
     * returns the underlying URL object. <br>
     * Note that any operation on the URL directly cannot take advantage
     * of the caching in URLCache.
     */
    public URL getURL() {
        return url;
    }

    /**
     * @see #removeRefreshListener(URLCache.RefreshListener)
     * @see #refresh()
     */
    public void addRefreshListener(RefreshListener listener) {
        listeners.add(listener);
    }

    /**
     * @see #addRefreshListener(URLCache.RefreshListener)
     */
    public void removeRefreshListener(RefreshListener listener) {
        listeners.remove(listener);
    }

    public boolean containsRefreshListener(RefreshListener listener) {
        return listeners.contains(listener);
    }

    public RefreshListener[] getRefreshListener() {
        return listeners.toArray(new RefreshListener[listeners.size()]);
    }

    /**
     * returns true only if the content has ever been successfully refreshed before
     */
    public boolean isCached() {
        return (updated > 0);
    }

    /**
     * returns the number of bytes currently read by the refresh thread;
     * returns -1 if no refresh in progress
     */
    public int bytesReadByCurrentRefresh() {
        try {
            return refresher.bytesReadSoFar;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    /**
     * returns the actual length of the already cached data or -1
     * if the data is too large to fit into memory. <p>
     * Note that before this method can return, this object will have
     * already attempted to load the entire content into memory.
     * If you try to avoid that and just want to peek at what the
     * online content provides for its content length, use
     * <code>peekContentLength()</code> instead.
     *
     * @throws IOException if the data cannot be accessed
     * @see #peekContentLength()
     */
    public int getRealContentLength() throws IOException {
        if (tooLargeForCaching) return -1;
        return getContent().length;
    }

    /**
     * returns the <code>content-length</code> header field
     * directly from the online data; the cache is neither
     * affected nor used. <p>
     * You can use this method if you want to get information
     * about the content length before you attempt to download
     * or cache the content. If you need an always accurate
     * content length, use <code>getContentLengh()</code>,
     * which will return the exact length of the cached content
     * (after the entire content has been loaded, though).
     *
     * @see #getRealContentLength()
     * @see Spider#getContentLength()
     */
    public int peekContentLength() throws IOException {
        return new Spider(url).getContentLength();
    }

    /**
     * return value of true indicates that even though the content to the
     * URL is accessible, the data is too large to be cached given the
     * current memory. <br>
     * This method can only return true after a refresh attempt has failed
     * due to the size of the content.<p>
     * If this method returns true, you can still obtain an input stream
     * or a reader to the data; these methods will then simply read from
     * the online content. Also, you can still save the data to a file.
     * Methods like <code>getContentAsString()</code>, however, will then
     * return an IOException stating that there is no memory available. <p>
     * This method may also return true if your memory is simply exhausted
     * in a particular point in time and the URL content is acutually not
     * that large; in that case, you may try refreshing again after freeing
     * up some memory or you can check the online content lenght - if
     * provided for the URL in question - with <code>peekContentLength()</code>
     *
     * @see #peekContentLength()
     */
    public boolean tooLargeForCaching() {
        return tooLargeForCaching;
    }

    /**
     * returns the file type denoted by the path of the URL.
     * The extension is the String of those characters that
     * follow the last 'dot' (".") in the file name in lowercase.
     * If no extension is present, null is returned.
     */
    public String getFileExtension() {
        String fname = url.getPath();
        int i = fname.lastIndexOf('.');
        if ((i > 0) && (i < (fname.length() - 1))) {
            return fname.substring(i + 1).toLowerCase();
        }
        return null;
    }

    /**
     * calls <code>saveContentToFile(file, false)</code>
     *
     * @see #saveContentToFile (File, boolean)
     */
    public void saveContentToFile(File file) throws IOException {
        saveContentToFile(file, false);
    }

    /**
     * If the file might not fit into memory, <code>streamDirectlyFromURL</code>
     * should be true to stream directly from the URL; otherwise this method
     * simply writes the cache to the file.
     */
    public void saveContentToFile(File file, boolean streamDirectlyFromURL) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        if (tooLargeForCaching || streamDirectlyFromURL) {
            byte[] bytes = new byte[bufferSize];
            InputStream in = url.openStream();
            int n = 0;
            while (n > -1) {
                n = in.read(bytes);
                if (n < 0) break;
                out.write(bytes, 0, n);
            }
            in.close();
        } else {
            out.write(getContent());
        }
        out.flush();
        out.close();
    }

    /**
     * tests equality on whether the embedded URL is the same file
     */
    public boolean equals(Object obj) {
        if (obj instanceof URLCache) {
            return ((URLCache) obj).getURL().sameFile(url);
        }
        return false;
    }

    /**
     * hashes based on the embedded URL
     */
    public int hashCode() {
        return url.hashCode();
    }

    public String toString() {
        String s = url.toString();
        s += " (cache updated:";
        //s += ((updated == 0)? " never" : (" " + updated)) + ")";
        s += ((updated == 0) ? " never" : (" " + new java.util.Date(updated).toString())) + ")";
        return s;
    }

    /**
     * called by the RefreshThread
     */
    void notifyListeners(boolean result, IOException cause) {
        try {
            Iterator<RefreshListener> i = listeners.iterator();
            while (i.hasNext()) {
                i.next().refreshedURLCache(this, result, cause);
            }
        } catch (Throwable t) {
        }
    }
}
