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

import org.jscience.io.ExtendedFile;
import org.jscience.io.ExtensionFileFilter;
import org.jscience.util.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * LocalWeb represents a website based on the local file system.
 *
 * @author Holger Antelmann
 * @since 4/11/03
 */
public class LocalWeb implements Serializable {
    static final long serialVersionUID = -7574551923905523267L;

    static class HTMLFileFilter extends ExtensionFileFilter {
        static final long serialVersionUID = -1006817920117925836L;

        HTMLFileFilter() {
            super("html", "html files");
            for (int i = 0; i < htmlFileExtentions.length; i++)
                addType(htmlFileExtentions[i]);
        }

        public boolean accept(File file) {
            if (file == null) return false;
            if (file.isDirectory()) return false;
            return super.accept(file);
        }
    }

    static class ImageFileFilter extends ExtensionFileFilter {
        static final long serialVersionUID = 2500470798589537874L;

        ImageFileFilter() {
            super("jpg", "image files");
            for (int i = 0; i < imageFileExtentions.length; i++)
                addType(imageFileExtentions[i]);
        }

        public boolean accept(File file) {
            if (file == null) return false;
            if (file.isDirectory()) return false;
            return super.accept(file);
        }
    }

    /**
     * an array containing the file extensions that mark HTML files to be parsed for links
     */
    static String[] htmlFileExtentions = new String[]{
            "htm", "html", "shtml", "shtm", "jsp", "asp"
    };
    /**
     * an array containing the file extensions that mark image files
     */
    static String[] imageFileExtentions = new String[]{
            "jpg", "jpeg", "gif", "png", "bmp"
    };
    public final static HTMLFileFilter htmlFilter = new HTMLFileFilter();
    public final static ImageFileFilter imageFilter = new ImageFileFilter();

    ExtendedFile webDir;
    ExtendedFile webRoot;

    /**
     * the webRoot must exist and be within the webDir
     */
    public LocalWeb(File webDir, File webRoot) throws IllegalArgumentException {
        this.webDir = new ExtendedFile(webDir);
        this.webRoot = new ExtendedFile(webRoot);
        if (!webRoot.exists())
            throw new IllegalArgumentException("webRoot doesn't exist: " + webRoot);
        if (!this.webRoot.hasParent(webDir))
            throw new IllegalArgumentException("the webRoot ("
                    + webRoot + ") must be within the webDir (" + webDir + ")");
    }

    public ExtendedFile getWebRoot() {
        return webRoot;
    }

    public ExtendedFile getWebDir() {
        return webDir;
    }

    /**
     * returns a set of all files all files within this web
     * that are accessible from the root
     */
    public File[] getLinkedFiles() throws IOException {
        return getLinkedFiles(webRoot);
    }

    public long calculateAccessibleWebSize() throws IOException {
        long size = 0;
        File[] list = getLinkedFiles();
        for (int i = 0; i < list.length; i++) size += list[i].length();
        return size;
    }

    /**
     * checks whether any linked local files are outside the web directory
     */
    public File[] findLocalLinksOutsideDir() throws IOException {
        File[] list = getLinkedFiles();
        ArrayList<File> outside = new ArrayList<File>(list.length);
        for (int i = 0; i < list.length; i++) {
            if (!new ExtendedFile(list[i]).hasParent(webDir)) outside.add(list[i]);
        }
        return outside.toArray(new File[outside.size()]);
    }

    /**
     * returns a set of all files within this web that are accessible
     * from the given file.
     * The file must be from within the web.
     */
    public File[] getLinkedFiles(File file) throws IOException {
        if (!new ExtendedFile(file).hasParent(webDir))
            throw new IllegalArgumentException("the given file (" + file
                    + ") is not within the webDir (" + webDir + ")");
        HashSet<File> set = new HashSet<File>();
        set.add(file);
        LinkedHashSet<File> found = new LinkedHashSet<File>();
        found.add(file);
        while (!found.isEmpty()) {
            File current = found.iterator().next();
            found.remove(current);
            Spider spider = new Spider();
            try {
                spider.setURL(current.toURI().toURL());
                URL[] images = spider.getImages(false);
                for (int i = 0; i < images.length; i++) {
                    File f = toFile(images[i]);
                    if (f != null) set.add(f);
                }
                URL[] links = spider.getLinks(false);
                for (int i = 0; i < links.length; i++) {
                    File f = toFile(links[i]);
                    if (f != null) {
                        if (set.add(f) && (htmlFilter.accept(f)))
                            found.add(f);
                    }
                }
            } catch (IOException ignore) {
            }
        }
        return set.toArray(new File[set.size()]);
    }

    public File[] getHTMLFiles() {
        return webDir.listFilesInTree(htmlFilter);
    }

    public File[] getImageFiles() {
        return webDir.listFilesInTree(imageFilter);
    }

    public File[] getDataFiles() {
        FileFilter filter = new FileFilter() {
            public boolean accept(File file) {
                if (htmlFilter.accept(file)) return false;
                if (imageFilter.accept(file)) return false;
                return true;
            }
        };
        return webDir.listFilesInTree(filter);
    }

    public String[] getFileTypes() {
        return ExtendedFile.getFileTypes(webDir.listFilesInTree());
    }

    public File[] findOrphans() throws IOException {
        File[] fileList = webDir.listFilesInTree();
        HashSet<File> orphans = new HashSet<File>(fileList.length);
        for (int i = 0; i < fileList.length; i++) {
            orphans.add(fileList[i]);
        }
        File[] linked = getLinkedFiles();
        for (int i = 0; i < linked.length; i++) {
            orphans.remove(linked[i]);
        }
        return orphans.toArray(new File[orphans.size()]);
    }

    public File[] filesContainingLink(URL link) {
        return filesContainingLinks(new URL[]{link});
    }

    public File[] filesContainingLinks(URL[] link) {
        HashSet<File> set = new HashSet<File>();
        File[] files = getHTMLFiles();
        for (int i = 0; i < files.length; i++) {
            try {
                URL url = files[i].toURI().toURL();
                Spider spider = new Spider(url);
                URL[] links = spider.getLinks(false);
                loop1:
                for (int l = 0; l < links.length; l++) {
                    for (int n = 0; n < link.length; n++) {
                        if (link[n] == null) continue;
                        if (links[l].sameFile(link[n])) {
                            set.add(files[i]);
                            break loop1;
                        }
                    }
                }
                links = spider.getImages(false);
                loop2:
                for (int l = 0; l < links.length; l++) {
                    for (int n = 0; n < link.length; n++) {
                        if (link[n] == null) continue;
                        if (links[l].sameFile(link[n])) {
                            set.add(files[i]);
                            break loop2;
                        }
                    }
                }
            } catch (IOException ignore) {
            }
        }
        return set.toArray(new File[set.size()]);
    }

    /**
     * finds links to URLs that are not accessible
     */
    public URL[] findBrokenLinks() {
        File[] files = getHTMLFiles();
        HashSet<URL> set = new HashSet<URL>();
        HashSet<URL> closed = new HashSet<URL>();
        for (int i = 0; i < files.length; i++) {
            try {
                URL url = files[i].toURI().toURL();
                Spider spider = new Spider(url);
                URL[] links = spider.getLinks(false);
                for (int l = 0; l < links.length; l++) {
                    if (closed.contains(links[l])) continue;
                    closed.add(links[l]);
                    if (!(new Spider(links[l]).isAccessible())) {
                        set.add(links[l]);
                    }
                }
                links = spider.getImages(false);
                for (int l = 0; l < links.length; l++) {
                    if (closed.contains(links[l])) continue;
                    closed.add(links[l]);
                    if (!(new Spider(links[l]).isAccessible())) {
                        set.add(links[l]);
                    }
                }
            } catch (IOException ignore) {
            }
        }
        return set.toArray(new URL[set.size()]);
    }

    /**
     * finds links to local files that do not exist
     */
    public File[] findBrokenLocalLinks() {
        File[] files = getHTMLFiles();
        HashSet<File> set = new HashSet<File>();
        for (int i = 0; i < files.length; i++) {
            try {
                URL url = files[i].toURI().toURL();
                Spider spider = new Spider(url);
                URL[] links = spider.getLinks(false);
                for (int l = 0; l < links.length; l++) {
                    File f = toFile(links[l]);
                    if ((f != null) && (!f.exists())) set.add(f);
                }
                links = spider.getImages(false);
                for (int l = 0; l < links.length; l++) {
                    File f = toFile(links[l]);
                    if ((f != null) && (!f.exists())) set.add(f);
                }
            } catch (IOException ignore) {
            }
        }
        return set.toArray(new File[set.size()]);
    }

    public File[] findLinksTo(File file) throws IOException {
        File[] fileList = getHTMLFiles();
        HashSet<File> linkList = new HashSet<File>(fileList.length);
        loop:
        for (int n = 0; n < fileList.length; n++) {
            URLCache uc = new URLCache(fileList[n].toURI().toURL(), true);
            URL[] links = uc.getLinks();
            for (int i = 0; i < links.length; i++) {
                File f = toFile(links[i]);
                if (f == null) continue;
                if (f.equals(file)) {
                    linkList.add(fileList[n]);
                    continue loop;
                }
            }
            links = uc.getImages();
            for (int i = 0; i < links.length; i++) {
                File f = toFile(links[i]);
                if (f == null) continue;
                if (f.equals(file)) {
                    linkList.add(fileList[n]);
                    continue loop;
                }
            }
        }
        return linkList.toArray(new File[linkList.size()]);
    }

    /**
     * returns the canonical file or null if the URL doesn't point to a valid file
     */
    public static File toFile(URL url) {
        if (!"file".equals(url.getProtocol())) return null;
        try {
            String t = url.getPath();
            // handling some common escape characters
            t = StringUtils.replaceAll(t, "%20", " ");
            t = StringUtils.replaceAll(t, "%2B", "+");
            File f = new File(t).getCanonicalFile(); // may throw IOException
            return f;
        } catch (IOException ex) {
            return null;
        }
    }
}
