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

import org.jscience.util.Monitor;
import org.jscience.util.StringUtils;
import org.jscience.util.logging.Logger;

import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.DTD;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * Spider provides several useful static functions for accessing
 * web content and parsing HTML most based on a simple URL.
 * Note that because this class uses functionality from the javax.swing
 * package (although no GUIs are used in this class), there are non-terminating
 * javax.swing threads that get created when using this class.
 * I.e. an application using this class without using any other
 * javax.swing GUI components may end up with unwanted non-terminated
 * threads possible forcing calls to e.g. <code>System.exit(0)</code>
 * to terminate a simple program.
 * Most methods are synchronized, so don't expect to have longer running methods
 * (such as getting links from a URL) run simultaneously on the same Spider object.
 *
 * @author Holger Antelmann
 * @see CrawlerSetting
 * @see URLCache
 */
public final class Spider {
    /**
     * wrappes a java.net.URL and keeps a reference to its referer
     */
    public static class URLWrapper {
        public URLWrapper(URL url, URL referer) {
            this.url = url;
            this.referer = referer;
        }

        public URL url, referer;
    }

    private static class Wrapper {
        boolean found = false;
        boolean first = true;
    }

    private static String lineBreak = System.getProperty("line.separator");
    private static int bufferSize = 2048;
    private static String whoisURLLocation = "http://www.netsol.com/cgi-bin/whois/whois?STRING=${domain}&SearchType=do";

    private Wrapper wrapper = new Wrapper();
    private String betterWhoisURLLocation = "http://www.betterwhois.com/bwhois.cgi?domain=${domain}";
    private DTD dtd;
    private ParserDelegator parser = new ParserDelegator();
    private HTMLEditorKit docKit = new HTMLEditorKit();
    private final HashSet<URL> links = new HashSet<URL>(); // also used for synchronization
    private URL currentURL;
    private boolean allowDuplicates;
    private HTMLEditorKit.ParserCallback linkSniffer = new HTMLEditorKit.ParserCallback() {
        public void handleStartTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
            if (tag == HTML.Tag.A) {
                try {
                    URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.HREF), currentURL);
                    if (!allowDuplicates && (links.contains(lurl))) return;
                    links.add(lurl);
                } catch (MalformedURLException ignore) {
                }
            }
        }

        public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
            if (tag == HTML.Tag.FRAME) {
                try {
                    URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.SRC), currentURL);
                    if (!allowDuplicates && (links.contains(lurl))) return;
                    links.add(lurl);
                } catch (MalformedURLException ignore) {
                }
            }
        }
    };
    private final HashSet<URL> images = new HashSet<URL>();
    private HTMLEditorKit.ParserCallback imageSniffer = new HTMLEditorKit.ParserCallback() {
        public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
            if (tag == HTML.Tag.IMG) {
                try {
                    URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.SRC), currentURL);
                    if (!allowDuplicates && images.contains(lurl)) return;
                    images.add(lurl);
                } catch (MalformedURLException ignore) {
                }
            }
        }

        public void handleStartTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
            if ((tag == HTML.Tag.BODY) ||
                    (tag == HTML.Tag.TABLE) ||
                    (tag == HTML.Tag.TR) ||
                    (tag == HTML.Tag.TD)) {
                try {
                    URL lurl = getURLFromLink((String) attrSet.getAttribute(HTML.Attribute.BACKGROUND), currentURL);
                    if (!allowDuplicates && images.contains(lurl)) return;
                    images.add(lurl);
                } catch (MalformedURLException ignore) {
                }
            }
        }
    };
    private String text = null;
    private HTML.Tag desiredTag;
    private String delimiter;
    private HTMLEditorKit.ParserCallback tagSniffer = new HTMLEditorKit.ParserCallback() {
        public void handleStartTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
            if (tag == desiredTag) wrapper.found = true;
        }

        public void handleEndTag(HTML.Tag tag, MutableAttributeSet attrSet, int pos) {
            if (tag == desiredTag) wrapper.found = false;
        }

        public void handleText(char[] data, int pos) {
            if (wrapper.found) {
                if ((!wrapper.first) && (delimiter != null)) text += delimiter;
                text += new String(data);
                wrapper.found = false;
                wrapper.first = false;
            }
        }
    };
    private HTMLEditorKit editorKit = new HTMLEditorKit();
    private HTMLEditorKit.ParserCallback textSniffer = new HTMLEditorKit.ParserCallback() {
        public void handleText(char[] data, int pos) {
            if (wrapper.first) text += delimiter;
            text += new String(data);
            wrapper.first = true;
        }
    };

    private URL url;

    /**
     * convenience constructor, that initializes the Spider with a null value as URL
     */
    public Spider() {
        this(null);
    }

    /**
     * constructs a Spider object based on the given URL
     */
    public Spider(URL url) {
        this.url = url;
        try {
            dtd = DTD.getDTD("html32"); // that's the ugly way as it's implemented in ParserDelegator
        } catch (IOException ignore) {
        }
    }

    /**
     * returns the embedded URL
     */
    public URL getURL() {
        return url;
    }

    /**
     * sets the embedded URL
     */
    public synchronized void setURL(URL url) {
        this.url = url;
    }

    public String getDomainName() {
        String domain = url.getHost();
        int n = StringUtils.count(domain, ".");
        while (n > 1) {
            domain = domain.substring(domain.indexOf(".") + 1, domain.length());
            n = StringUtils.count(domain, ".");
        }
        return domain;
    }

    public synchronized String fullHeaderAsString() throws IOException {
        String s = null;
        URLConnection con = url.openConnection();
        Map map = con.getHeaderFields();
        Iterator i = map.keySet().iterator();
        if (i.hasNext()) s = "";
        while (i.hasNext()) {
            String key = (String) i.next();
            s += ((key == null) ? "" : key + ": ");
            s += ((List) map.get(key)).get(0) + lineBreak;
        }
        return s;
    }

    /**
     * returns the time it takes to establish a live connection to the embedded URL
     * and returns -1 only if the URL is unreachable.
     */
    public long ping() {
        try {
            URLConnection con = url.openConnection();
            long start = System.currentTimeMillis();
            con.connect();
            long time = (System.currentTimeMillis() - start);
            if (con instanceof java.net.HttpURLConnection) {
                ((java.net.HttpURLConnection) con).disconnect();
            }
            return time;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * saves the content of the embedded URL to the given file
     */
    public void saveURLtoFile(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        byte[] bytes = new byte[bufferSize];
        InputStream in = url.openStream();
        int n = 0;
        while (n > -1) {
            n = in.read(bytes);
            if (n < 0) break;
            out.write(bytes, 0, n);
        }
        out.flush();
        out.close();
    }

    /**
     * returns links filtered by the given protocol
     *
     * @see #getLinks(boolean)
     */
    public URL[] getLinks(boolean allowDuplicates, String protocol)
            throws IOException {
        URL[] links = getLinks(allowDuplicates);
        if (links == null) return null;
        ArrayList<URL> v = new ArrayList<URL>(links.length);
        for (int i = 0; i < links.length; i++) {
            if (links[i].getProtocol().equals(protocol)) v.add(links[i]);
        }
        return v.toArray(new URL[v.size()]);
    }

    /**
     * returns an array containing URLs that the embedded URL links to;
     * if the page is a frameset, the frame sources are returned.
     * If no links are present within the given URL, an empty array
     * is returned
     */
    public URL[] getLinks(boolean allowDuplicates) throws IOException {
        return getLinks(getReader(), allowDuplicates);
    }

    /**
     * allows to read the content from another location but the url itself
     *
     * @see #getLinks(boolean)
     */
    public synchronized URL[] getLinks(Reader reader, boolean allowDuplicates)
            throws IOException {
        links.clear();
        currentURL = url;
        this.allowDuplicates = allowDuplicates;
        // not complete, but handles at least a common case
        if (url.getProtocol().equals("mailto")) return null;
        parser.parse(reader, linkSniffer, true);
        return (URL[]) links.toArray(new URL[links.size()]);
    }

    /**
     * Assuming the URL points to a HTML page, only links that are not accessible
     * are returned. If all links are valid (or the page didn't contain links),
     * an empty array is returned.
     * Only links with 'http', 'ftp' or 'file' protocol are checked.
     */
    public URL[] getBrokenLinks() throws IOException {
        synchronized (links) {
            URL[] links = getLinks(false);
            ArrayList<URL> broken = new ArrayList<URL>();
            for (int i = 0; i < links.length; i++) {
                Spider s = new Spider(links[i]);
                if (!s.isAccessible()) broken.add(s.getURL());
            }
            return (URL[]) broken.toArray(new URL[broken.size()]);
        }
    }

    /**
     * actually connects to the embedded URL while executing
     */
    public boolean isAccessible() {
        String p = url.getProtocol();
        if ("mailto".equals(p)) return true;
        if ("http".equals(p) || "ftp".equals(p) || "file".equals(p)) {
            try {
                URLConnection con = url.openConnection();
                con.connect();
                if (con instanceof java.net.HttpURLConnection) {
                    ((java.net.HttpURLConnection) con).disconnect();
                }
                return true;
            } catch (IOException ex) {
                return false;
            }
        } else {
            return false; // unknown protocol
        }
    }

    /**
     * returns an array of images that are contained in the embedded URL
     */
    public URL[] getImages(boolean allowDuplicates) throws IOException {
        return getImages(getReader(), allowDuplicates);
    }

    /**
     * allows to read the content from another location but the url itself
     *
     * @see #getImages(boolean)
     */
    public synchronized URL[] getImages(Reader reader, boolean allowDuplicates)
            throws IOException {
        images.clear();
        currentURL = url;
        this.allowDuplicates = allowDuplicates;
        parser.parse(reader, imageSniffer, true);
        return (URL[]) images.toArray(new URL[images.size()]);
    }

    /**
     * translates a relative URL to an absolute URL
     */
    static URL getURLFromLink(String link, URL context) throws MalformedURLException {
        try {
            return new URL(link);
        } catch (MalformedURLException e) {
            // See if it is a relative URL that can be translated
            // into an absolute URL
            // Note that this translation will be incorrect if the context
            // is e.g. "http://localhost/myweb" vs. "http://localhost/myweb/"
            return new URL(context, link);
        }
    }

    /**
     * searches the content of the embedded URL for the presence of one of the
     * searchPatterns given; returns true if one of the patterns was found
     *
     * @param searchPattern   array of search patterns this function will look for
     * @param includeHTMLCode if true, this function will search through all content of
     *                        the URL, including HTML code; if false, it will only search
     *                        through text found
     */
    public boolean includesPattern(String[] searchPattern, boolean includeHTMLCode)
            throws IOException {
        String text = null;
        if (includeHTMLCode) {
            text = getContentAsString();
        } else {
            text = stripText();
        }
        if (text == null) throw new IOException("The given URL's text is not accessible");
        for (int i = 0; i < searchPattern.length; i++) {
            if (text.indexOf(searchPattern[i]) > -1) return true;
        }
        return false;
    }

    /**
     * returns the title of the document
     */
    public String getTitle() throws IOException {
        return getTagText(HTML.Tag.TITLE, null);
    }

    /**
     * returns all text found in the given desiredTag delimited by
     * the given delimiter
     *
     * @see #getTagText(Reader,HTML.Tag,String)
     */
    public String getTagText(HTML.Tag desiredTag, String delimiter)
            throws IOException {
        return getTagText(getReader(), desiredTag, delimiter);
    }

    /**
     * allows to read the content from another location but the url itself
     *
     * @see #getTagText(HTML.Tag,String)
     */
    public synchronized String getTagText(Reader reader, HTML.Tag desiredTag, String delimiter)
            throws IOException {
        text = "";
        this.desiredTag = desiredTag;
        this.delimiter = delimiter;
        wrapper.found = false;
        wrapper.first = false;
        parser.parse(reader, tagSniffer, true);
        return text.toString();
    }

    /**
     * a line break is put after each separate text occurrence
     */
    public String stripText() throws IOException {
        return stripText(lineBreak);
    }

    /**
     * returns a String containing the text of all HTML tag types from
     * the embedded URL
     */
    public String stripText(String delimiter)
            throws IOException {
        return stripText(getReader(), delimiter);
    }

    /**
     * allows to read the content from another location but the url itself
     *
     * @see #stripText(String)
     */
    public synchronized String stripText(Reader reader, String delimiter)
            throws IOException {
        text = "";
        wrapper.first = false;
        this.delimiter = delimiter;
        if (reader == null) return null;
        parser.parse(reader, textSniffer, true);
        return text;
    }

    /**
     * returns an HTMLDocument object with the parsed content of the embedded
     * URL for further examination
     */
    public HTMLDocument getHTMLDocument() throws IOException {
        return getHTMLDocument(getReader());
    }

    /**
     * returns an HTMLDocument object with the parsed content from the given
     * reader for further examination
     */
    public synchronized HTMLDocument getHTMLDocument(Reader reader) throws IOException {
        HTMLDocument doc = null;
        synchronized (editorKit) {
            doc = (HTMLDocument) editorKit.createDefaultDocument();
        }
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        try {
            synchronized (docKit) {
                docKit.read(reader, doc, 0);
                return doc;
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * This function constructs a reader appropriate for reading the content
     * from the embedded URL.
     * Currently, this function only supports HTTP, FTP and FILE protocol.
     *
     * @throws IOException
     * @throws UnsupportedOperationException if the given URL is of another
     *                                       protocol than HTTP or FILE
     */
    public Reader getReader() throws IOException {
        return getReader(null);
    }

    /**
     * @see #getReader()
     */
    public Reader getReader(String charsetName) throws IOException {
        Reader reader;
        if (url.getProtocol().equals("http")) {
            if (charsetName == null) {
                reader = new InputStreamReader(url.openConnection().getInputStream());
            } else {
                reader = new InputStreamReader(url.openConnection().getInputStream(), charsetName);
            }
        } else if (url.getProtocol().equals("ftp")) {
            if (charsetName == null) {
                reader = new InputStreamReader(url.openConnection().getInputStream());
            } else {
                reader = new InputStreamReader(url.openConnection().getInputStream(), charsetName);
            }
        } else if (url.getProtocol().equals("file")) {
            reader = new FileReader(url.getPath());
        } else {
            String s = "Protocol " + url.getProtocol();
            s += " not supported for obtaining a Reader";
            throw new IOException(s);
        }
        return reader;
    }

    /**
     * retrieves the raw content from the embedded URL. <p>
     */
    public byte[] getBytes() throws IOException {
        byte[] bytes = new byte[0];
        byte[] buffer = new byte[bufferSize];
        URLConnection con = url.openConnection();
        con.setUseCaches(false);
        //con.connect();
        InputStream in = con.getInputStream();
        int n = 0;
        int pos;
        while (n > -1) {
            n = in.read(buffer);
            if (n < 0) break;
            pos = bytes.length;
            byte[] tmp = new byte[pos + n];
            System.arraycopy(bytes, 0, tmp, 0, pos);
            System.arraycopy(buffer, 0, tmp, pos, n);
            bytes = tmp;
        }
        return bytes;
    }

    /**
     * retrieves the entire content accessible through the embedded URL as a String.
     * If the URL points to an HTML page, the full HTML code is returned.
     * This method is not suitable for retrieving binary data as it uses a
     * BufferedReader and also places platform specific line breaks between the
     * lines read with readLine().
     * If the URL could not be accessed and an IOException was caught, null is returned.
     */
    public String getContentAsString() throws IOException {
        String s = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inline = null;
        while ((inline = in.readLine()) != null) {
            if (!s.equals("")) s += lineBreak;
            s += inline;
        }
        in.close();
        return s;
    }

    /**
     * retrieves the content length from an URLConnection
     */
    public int getContentLength() throws IOException {
        URLConnection con = url.openConnection();
        int len = con.getContentLength();
        if (con instanceof java.net.HttpURLConnection) {
            ((java.net.HttpURLConnection) con).disconnect();
        }
        return len;
    }

    /**
     * returns the page weigt in bytes
     * (= content length of URL plus sum of embedded images)
     */
    public int calculatePageWeight() throws IOException {
        int size = getContentLength();
        URL[] embImg = getImages(false);
        for (int i = 0; i < embImg.length; i++) {
            size += new Spider(embImg[i]).getContentLength();
        }
        return size;
    }

    /**
     * returns the registrant information from the Internic database;
     * the embedded URL must use the host name and not the IP address
     */
    public String whois() throws IOException {
        String host = url.getHost();
        if (host != null) {
            int dot = host.lastIndexOf(".");
            int begin = host.lastIndexOf(".", dot - 1);
            return whois(host.substring(begin + 1));
        }
        return null;
    }

    /**
     * returns the registrant information from the Internic database
     */
    public static String whois(String domainName) throws IOException {
        if ((domainName == null) || (domainName.length() < 1)) return null;
        String urlLoc = StringUtils.replaceAll(whoisURLLocation, "${domain}", domainName);
        //return Spider.getTagText(new URL(url), Tag.PRE, "\n");
        String all = new Spider(new URL(urlLoc)).getContentAsString();
        int begin = StringUtils.indexOfIgnoreCase(all, "<pre>");
        if (begin < 0) return null;
        int end = StringUtils.indexOfIgnoreCase(all, "</pre>");
        if (end < 0) return null;
        return all.substring(begin + 5, end);
    }

    /**
     * searches the web from the embedded URL (used as root) for URLs based on the criteria
     * given in the  crawler; search is performed breadth-first
     *
     * @param crawler            criteria for crawling
     * @param numberOfURLsToFind if >0 the search is stopped when the given number
     *                           of URLs are found to match the crawler's criteria
     * @param logger             to log IOExceptions occuring while processing links
     * @return an array containing URLs found that satisfy the crawler's criteria
     *         as defined by the crawler
     */
    public URL[] crawlWeb(CrawlerSetting crawler, int numberOfURLsToFind, Logger logger) {
        ArrayList<URLWrapper> searchList = new ArrayList<URLWrapper>();
        searchList.add(new URLWrapper(url, null));
        return crawlWeb(searchList, new ArrayList<URL>(), new ArrayList<URL>(), crawler, 0, numberOfURLsToFind, logger);
    }

    /**
     * usually called by crawlWeb(URL root, CrawlerSetting crawler, Logger)
     *
     * @param searchList         List of Spider.URLWrapper objects containing nodes to be examined
     * @param resultList         List of URL objects
     * @param closedList         List of URL objects
     * @param crawler            criteria for crawling
     * @param depth              link distance from the root of the search
     * @param numberOfURLsToFind if >0 the search is stopped when the given number
     *                           of URLs are found to match the crawler's criteria
     * @param logger             to log IOExceptions occuring while processing links
     * @return an array containing URLs found that satisfy the criteria
     *         as defined by the crawler
     * @see #crawlWeb(CrawlerSetting,int,Logger)
     */
    public static URL[] crawlWeb(List<URLWrapper> searchList, List<URL> resultList, List<URL> closedList, CrawlerSetting crawler, int depth, int numberOfURLsToFind, Logger logger) {
        ArrayList<URLWrapper> newLinks = new ArrayList<URLWrapper>();
        URL url, referer;
        // first, working through the list just to search for matches,
        // not for gathering new links
        // (to avoid out-of-memory-errors before the list has been worked through)
        for (int current = 0; current < searchList.size(); current++) {
            url = ((URLWrapper) searchList.get(current)).url;
            referer = ((URLWrapper) searchList.get(current)).referer;
            // ask the crawler whether it matches its criteria
            if (crawler.matchesCriteria(url, referer, depth, resultList, closedList)) {
                resultList.add(url);
            } else {
                closedList.add(url);
            }
            // stop the search if the number of reqired URLs is found
            if ((numberOfURLsToFind > 0) && (numberOfURLsToFind <= resultList.size()))
                return (URL[]) resultList.toArray(new URL[resultList.size()]);
        }
        // another walk through the list; this time for generating links
        // for the next level
        for (int current = 0; current < searchList.size(); current++) {
            url = ((URLWrapper) searchList.get(current)).url;
            referer = ((URLWrapper) searchList.get(current)).referer;
            // ask the crawler whether or not to follow the links of this url
            if (crawler.followLinks(url, referer, depth, resultList, closedList, newLinks)) {
                try {
                    URL[] link = new Spider(url).getLinks(false);
                    linkLoop:
                    for (int i = 0; i < link.length; i++) {
                        // checking whether the page was encountered before
                        for (int x = 0; x < closedList.size(); x++) {
                            if (link[i].sameFile((URL) closedList.get(x))) continue linkLoop;
                        }
                        for (int x = 0; x < resultList.size(); x++) {
                            if (link[i].sameFile((URL) resultList.get(x))) continue linkLoop;
                        }
                        for (int x = 0; x < newLinks.size(); x++) {
                            if (link[i].sameFile(((URLWrapper) newLinks.get(x)).url)) continue linkLoop;
                        }
                        // adding the new link to the list of links for the next level
                        newLinks.add(new URLWrapper(link[i], url));
                    }
                } catch (IOException e) {
                    if (logger != null)
                        logger.log("IOException while processing links for: " + url, e);
                }
            }
        }
        if (newLinks.isEmpty()) return (URL[]) resultList.toArray(new URL[resultList.size()]);
        return crawlWeb(newLinks, resultList, closedList, crawler, (depth + 1), numberOfURLsToFind, logger);
    }

    /**
     * This special web search function returns all URLs found that contain
     * the desired searchString given the constrains of the other parameter.
     * The search starts at the entryPoint and goes recursively through the
     * tree derived from the links from that URL as deep as suggested by the
     * level parameter; the search is conducted in a bredth-first-search manner.
     * For more flexible web searches, consider the use of a org.jscience.net.CrawlerSetting.
     * <br>Use of Monitor - if present (i.e. monitor may be null, in which case
     * no feedback is provided while the function is executing):
     * <ul>
     * <li>search is stopped when monitor is disabled (enabling e.g. to stop
     * searching after 10 pages were found etc.)</li>
     * <li>for each examined node, monitor.increment() is called</li>
     * <li>for each examined node, monitor.runTask() is called</li>
     * <li>monitor.getObject() contains the current URL being examined</li>
     * <li>monitor.getObject(0) contains the current searchList</li>
     * <li>monitor.getObject(1) contains the current excludeList</li>
     * <li>monitor.getObject(2) contains the current resultSet</li>
     * <li>monitor.getNumber() contains the number of total pages examined so far</li>
     * <li>monitor.getNumber(0) contains the number of result pages found so far</li>
     * <li>monitor.getNumber(1) contains the current level (counting down to 0)</li>
     * <li>monitor.getNumber(2) contains the number of URLs to be searched in the current level
     * (giving an indication on how many URLs are ahead in a new level)</li>
     * <li>thus, monitor.getSize() is required to be at least 3 if present</li>
     * </ul>
     *
     * @param searchPattern              an array containing String patterns to search for;
     *                                   wildcards are not supported
     * @param entryPoint                 the URL from where to start the search
     * @param includeHTMLCode            if true, the search will include not only the text,
     *                                   but also the HTML code of a page
     * @param level                      limits the depth of the search; only pages that are reachable
     *                                   with less or equal than the given number of recursive links will be included
     * @param currentSiteOnly            if true, the search is limited to the host of the entryPoint
     * @param searchURLExclusionPatterns if not null it contains an array of String patterns
     *                                   which will be used to filter out unwanted URLs, i.e. if any of the patterns
     *                                   are present in the URL's path, that URL will be disregarded;
     *                                   wildcards are not supported
     * @param monitor                    see above for usage; may be null
     * @see #crawlWeb(CrawlerSetting,int,Logger)
     * @deprecated
     */
    @Deprecated
    public static URL[] searchWebFor(String[] searchPattern,
                                     URL entryPoint,
                                     boolean includeHTMLCode,
                                     int level,
                                     boolean currentSiteOnly,
                                     String[] searchURLExclusionPatterns,
                                     Monitor monitor) {
        ArrayList<URL> searchList = new ArrayList<URL>();
        searchList.add(entryPoint);
        List<URL> resultSet = searchWebFor(searchPattern,
                searchList,
                includeHTMLCode,
                level,
                currentSiteOnly,
                new ArrayList<URL>(), // excludeList
                new ArrayList<URL>(), // resultSet
                searchURLExclusionPatterns,
                monitor);
        return (URL[]) resultSet.toArray(new URL[resultSet.size()]);
    }

    /**
     * usually called by the other searchWebFor() function; all Lists contain URL objects
     *
     * @see #searchWebFor(String[],URL,boolean,int,boolean,String[],Monitor)
     * @deprecated
     */
    @Deprecated
    public static List<URL> searchWebFor(String[] searchPattern,
                                         ArrayList<URL> searchList,
                                         boolean includeHTMLCode,
                                         int level,
                                         boolean currentSiteOnly,
                                         List<URL> excludeList,
                                         List<URL> resultList,
                                         String[] searchURLExclusionPatterns,
                                         Monitor monitor) {
        if ((monitor != null) && (monitor.getSize() < 3)) {
            String s = "The given monitor is expected to have at least a length of 3";
            throw new IllegalArgumentException(s);
        }
        if (searchList.isEmpty()) return resultList;
        ArrayList<URL> newLinks = new ArrayList<URL>();
        if (monitor != null) monitor.setNumber(1, level);
        if (monitor != null) monitor.setNumber(2, searchList.size());
        searchListLoop:
        for (int current = 0; current < searchList.size(); current++) {
            if (monitor != null) {
                monitor.setObject(0, searchList);
                monitor.setObject(1, excludeList);
                monitor.setObject(2, resultList);
                if (monitor.disabled()) return resultList;
            }
            URL entryPoint = (URL) searchList.get(current);
            if (monitor != null) monitor.setObject(entryPoint);
            if (monitor != null) monitor.increment();
            if (monitor != null) monitor.runTask();
            // filtering out unwanted URLs
            if (searchURLExclusionPatterns != null) {
                for (int i = 0; i < searchURLExclusionPatterns.length; i++) {
                    if (searchURLExclusionPatterns[i] == null) continue;
                    if (entryPoint.getPath().indexOf(searchURLExclusionPatterns[i]) > -1) continue searchListLoop;
                }
            }
            // stripping the text (with or without HTML code) from the given URL
            String text = null;
            try {
                if (includeHTMLCode) {
                    text = new Spider(entryPoint).getContentAsString();
                } else {
                    text = new Spider(entryPoint).stripText();
                }
            } catch (IOException e) {
                continue;
            }
            // test whether the current URL inclues any of the searchPattern
            for (int i = 0; i < searchPattern.length; i++) {
                if (text.indexOf(searchPattern[i]) > -1) {
                    resultList.add(entryPoint);
                    if (monitor != null) monitor.increment(0);
                    break;
                } else {
                    excludeList.add(entryPoint);
                    break;
                }
            }
            if (level <= 0) continue;
            // there's another level to go, so collect all links
            try {
                URL[] linksArray = new Spider(entryPoint).getLinks(false, "http");
                smallLoop:
                for (int i = 0; i < linksArray.length; i++) {
                    if (currentSiteOnly && (!linksArray[i].getHost().equals(entryPoint.getHost()))) continue smallLoop;
                    for (int x = 0; x < excludeList.size(); x++) {
                        if (linksArray[i].sameFile((URL) excludeList.get(x))) continue smallLoop;
                    }
                    for (int x = 0; x < searchList.size(); x++) {
                        if (linksArray[i].sameFile((URL) searchList.get(x))) continue smallLoop;
                    }
                    for (int x = 0; x < resultList.size(); x++) {
                        if (linksArray[i].sameFile((URL) resultList.get(x))) continue smallLoop;
                    }
                    for (int x = 0; x < newLinks.size(); x++) {
                        if (linksArray[i].sameFile((URL) newLinks.get(x))) continue smallLoop;
                    }
                    newLinks.add(linksArray[i]);
                }
            } catch (IOException e) {
            }
        }
        return searchWebFor(searchPattern,
                newLinks,
                includeHTMLCode,
                (level - 1),
                currentSiteOnly,
                excludeList,
                resultList,
                searchURLExclusionPatterns,
                monitor);
    }

    /**
     * a specialized Monitor that can be used with a Spider;
     * it simply provides some useful progress information during execution
     * of the searchWebFor() function
     *
     * @see #searchWebFor(String[],URL,boolean,int,boolean,String[],Monitor)
     * @deprecated
     */
    @Deprecated
    public static class SMonitor extends Monitor implements Runnable {
        int found = 0;
        int level = 0;
        int maxFound = 0;
        Logger logger = null;

        /**
         * @param logger   if not null, logger will be used to log all messages there
         * @param maxFound if set to a number >0, run() will disable the
         *                 given monitor after the given number of pages were reported
         *                 found by the monitor
         * @see Spider#searchWebFor(String[],URL,boolean,int,boolean,String[],Monitor)
         */
        public SMonitor(Logger logger, int maxFound) {
            super(3);
            setTask(this);
            this.maxFound = maxFound;
            this.level = getNumber(1);
            this.logger = logger;
        }

        /**
         * prints progress information from the given monitor to either
         * the console or a specified log file
         */
        public void run() {
            if ((level != 0) && (getNumber(1) != level)) {
                say("levels to go: " + level);
                say("pages ahead in current level: " + getNumber(2));
            }
            level = getNumber(1);
            if (getNumber(0) > found) {
                say("** entry found (#" + ++found + ")");
                if ((maxFound > 0) && (found >= maxFound)) disable();
            }
            say("examining #" + getNumber() + ": " + getObject());
        }

        public int getNumberOfPagesSearched() {
            return getNumber();
        }

        public int getCurrentLevel() {
            return getNumber(1);
        }

        public int getNumberOfPagesFound() {
            return getNumber(2);
        }

        public URL getCurrentURL() {
            return (URL) getObject();
        }

        public List getSearchList() {
            return (List) getObject(0);
        }

        public List getExcludeList() {
            return (List) getObject(1);
        }

        public List getResultList() {
            return (List) getObject(2);
        }

        void say(String s) {
            if (logger == null) {
                System.out.println("[MonitorTaskMessage]: " + s);
            } else {
                logger.log(s);
            }
        }

        public void setMaxFound(int maxFound) {
            this.maxFound = maxFound;
        }
    }
}
