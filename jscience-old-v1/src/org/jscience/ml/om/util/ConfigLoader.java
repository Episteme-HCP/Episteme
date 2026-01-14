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

/* ====================================================================
 * /util/ConfigLoader.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */

package org.jscience.ml.om.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;


/**
 * The ConfigLoader is used to find config files inside the classpath (and the extension directory),
 * and if config files are found, it can provide easy access to the
 * config information.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public class ConfigLoader {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------
    // Extension of config files
    private static final String MANIFEST_FILENAME = "META-INF/SCHEMATYPE";
    // Config file XSI relation entry: XSI_Type ending
    private static final String CONFIG_FILE_ENTRY_TYPE_ENDING = "XSI_Relation_Type";
    // Config file XSI relation entry: XSI_Type ending
    private static final String CONFIG_FILE_ENTRY_CLASSNAME_ENDING = "XSI_Relation_Class";

    // ------------------
    // Instance variables ------------------------------------------------
    // ------------------
    // All properties from config with xsi:type as key and Java classname
    // as value
    private static Properties properties = new Properties();

    // --------------
    // Public methods ----------------------------------------------------
    // --------------
    // -------------------------------------------------------------------
    /**
     * Returns the java classname that matches the given
     * xsi:type attribute, which can be found at additional schema
     * elements<br>
     * E.g.:<br>
     * <target id="someID" <b>xsi:type="fgca:deepSkyGX"</b>><br>
     * // More Target data goes here<br>
     * </target><br>
     * If for example the type "fgca:deepSkyGX" would be passed to
     * this method, it would return the classname:
     * "org.jscience.ml.om.deepSky.DeepSkyTarget".
     * The classname may then be used to load the corresponding java class
     * via java reflection API for a given schema element.
     *
     * @param type The xsi:type value which can be found at additional
     *             schema elements
     * @return The corresponding java classname for the given type, or
     *         <code>null</code> if the type could not be resolved.
     * @throws ConfigException if problems occured during load of config
     */
    public static String getClassnameFromType(String type) throws ConfigException {
        if (type == null) {
            return null;
        }
        synchronized (properties) {
            if (properties.isEmpty()) {
                loadConfig();
            }
        }
        String classname = properties.getProperty(type);

        if ((classname == null)
                || ("".equals(classname.trim()))
                ) {
            throw new ConfigException("No class defined for type: " + type + ". Please check plugin Manifest files, or download new extension. ");
        }
        return classname;
    }

    // -------------------------------------------------------------------
    /**
     * Scans the java classpath again for valid configfile.
     *
     * @throws ConfigException if problems occured during load of config
     */
    public static void reloadConfig() throws ConfigException {
        loadConfig();
    }


    // ---------------
    // Private methods ---------------------------------------------------
    // ---------------
    // -------------------------------------------------------------------
    private static void loadConfig() throws ConfigException {

        // Get JARs from classpath
        String sep = System.getProperty("path.separator");
        String path = System.getProperty("java.class.path");

        StringTokenizer tokenizer = new StringTokenizer(path, sep);
        File token = null;
        while (tokenizer.hasMoreTokens()) {
            token = new File(tokenizer.nextToken());
            if ((token.isFile())
                    && (token.getName().endsWith(".jar"))
                    ) {
                scanJarFile(token);
            }
        }

        // Get JARs under extension path
        String extPath = System.getProperty("java.ext.dirs");
        File ext = new File(extPath);
        if (ext.exists()) {
            File[] jars = ext.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {

                    if (name.toLowerCase().endsWith(".jar"))
                        return true;

                    return false;
                }
            });

            if (jars != null) {
                for (int i = 0; i < jars.length; i++) {
                    scanJarFile(jars[i]);
                }
            }
        }

    }

    // -------------------------------------------------------------------
    private static void scanJarFile(File jar) throws ConfigException {
        ZipFile archive = null;
        try {
            archive = new ZipFile(jar);
        } catch (ZipException zipEx) {
            throw new ConfigException("Error while accessing JAR file. ", zipEx);
        } catch (IOException ioe) {
            throw new ConfigException("Error while accessing JAR file. ", ioe);
        }
        Enumeration enumeration = archive.entries();
        while (enumeration.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) enumeration.nextElement();
            String name = entry.getName();
            if (name.toUpperCase().equals(MANIFEST_FILENAME)) {
                InputStream in = null;
                try {

                    in = archive.getInputStream(entry);
                    Properties prop = new Properties();
                    prop.load(in);
                    addConfig(prop);
                } catch (IOException ioe) {
                    throw new ConfigException("Error while accessing entry from JAR file. ", ioe);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ioe) {
                            // we can't do anything here
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------
    private static void addConfig(Properties newProperties) throws ConfigException {
        Iterator keys = newProperties.keySet().iterator();

        String currentKey = null;
        String prefix = null;
        String classname = null;
        String type = null;
        while (keys.hasNext()) {
            currentKey = (String) keys.next();
            // Check if key ends with TYPE ending
            if (currentKey.endsWith(ConfigLoader.CONFIG_FILE_ENTRY_TYPE_ENDING)) {
                // Get TYPE value
                type = newProperties.getProperty(currentKey);

                // Get prefix (everything that is berfore our TYPE ending) 
                prefix = currentKey.substring(0, currentKey.lastIndexOf(ConfigLoader.CONFIG_FILE_ENTRY_TYPE_ENDING));
                // Use prefix and CLASS ending to get class property
                classname = newProperties.getProperty(prefix + ConfigLoader.CONFIG_FILE_ENTRY_CLASSNAME_ENDING);

                // Check if we found TYPE and matching CLASS entry
                if ((classname != null)
                        && (type != null)
                        && (!"".equals(classname.trim()))
                        && (!"".equals(type.trim()))
                        ) {
                    // Add type and classname to our list of known types
                    synchronized (properties) {
                        properties.put(type, classname);
                    }
                }
            }
        }

    }

}
