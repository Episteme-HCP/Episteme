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

package org.jscience.linguistics.kif;

import java.io.*;

import java.text.ParseException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


/**
 * This is a class that manages a group of knowledge bases.  It should only
 * have one instance, contained in its own static member variable.
 */
public class KBmanager {
    /** DOCUMENT ME! */
    private static KBmanager manager = new KBmanager();

    /** DOCUMENT ME! */
    private HashMap preferences = new HashMap();

    /** DOCUMENT ME! */
    private HashMap kbs = new HashMap();

    /** DOCUMENT ME! */
    private String configuration = null;

    /** DOCUMENT ME! */
    private boolean initialized = false;

/**
     * Constructor which reads in a configuration from a file.
     */
    public KBmanager() {
        String sep = File.separator;

        if (KBmanager.manager == null) {
            try {
                preferences.put("kbDir",
                    System.getProperty("user.dir") + sep + "KBs");
                preferences.put("testOutputDir",
                    System.getProperty("user.dir") + sep + "webapps" + sep +
                    "sigma" + sep + "tests");
                readConfiguration();
            } catch (IOException ioe) {
                System.out.println(
                    "Error in KBmanager: Configuration file not read.");
                System.out.println(ioe.getMessage());
            } finally {
                NLformatter.readKeywordMap((String) preferences.get("kbDir"));

                if (!preferences.containsKey("inferenceTestDir")) {
                    preferences.put("inferenceTestDir",
                        "C:\\Program Files\\Apache Tomcat 4.0\\tests");
                }

                if (!preferences.containsKey("inferenceEngine")) {
                    preferences.put("inferenceEngine",
                        "C:\\Artic\\vampire\\Vampire_VSWorkspace\\vampire\\Release\\kif.exe");
                }

                if (!preferences.containsKey("cache")) {
                    preferences.put("cache", "no");
                }

                if (!preferences.containsKey("showcached")) {
                    preferences.put("showcached", "yes");
                }

                if (!preferences.containsKey("loadCELT")) {
                    preferences.put("loadCELT", "no");
                }
            }
        }
    }

    /**
     * Read in any KBs defined in the configuration.
     *
     * @throws IOException DOCUMENT ME!
     * @throws ParseException DOCUMENT ME!
     */
    public void initializeOnce() throws IOException, ParseException {
        if (!initialized) {
            BasicXMLparser config = new BasicXMLparser(configuration);
            System.out.println(
                "INFO in KBmanager.initializeOnce(): Initializing.");
            System.out.print(
                "INFO in KBmanager.initializeOnce(): Number of preferences:");
            System.out.println(config.elements.size());

            for (int i = 0; i < config.elements.size(); i++) {
                BasicXMLelement element = (BasicXMLelement) config.elements.get(i);

                if (element.tagname.equalsIgnoreCase("preference")) {
                    String name = (String) element.attributes.get("key");
                    String value = (String) element.attributes.get("value");
                    preferences.put(name, value);
                    System.out.println(
                        "INFO in KBmanager.initializeOnce(): Storing preferences: " +
                        name + " " + value);
                }

                if (element.tagname.equalsIgnoreCase("kb")) {
                    String kbName = (String) element.attributes.get("name");
                    addKB(kbName);

                    KB kb = getKB(kbName);

                    for (int j = 0; j < element.subelements.size(); j++) {
                        BasicXMLelement kbConst = (BasicXMLelement) element.subelements.get(j);

                        if (!kbConst.tagname.equalsIgnoreCase("constituent")) {
                            System.out.println(
                                "Error in KBmanager.initialize(): Bad element: " +
                                kbConst.tagname);
                        }

                        String filename = (String) kbConst.attributes.get(
                                "filename");
                        kb.addConstituent(filename);
                    }

                    System.out.println(
                        "INFO in KBmanager.initializeOnce(): value of cache: " +
                        KBmanager.getMgr().getPref("cache"));

                    if ((KBmanager.getMgr().getPref("cache") != null) &&
                            KBmanager.getMgr().getPref("cache")
                                         .equalsIgnoreCase("yes")) {
                        kb.cache();
                    }
                }
            }

            initialized = true;
        }

        System.out.println("INFO in KBmanager.initializeOnce(): celtdir: " +
            (String) preferences.get("celtdir"));
    }

    /**
     * Double the backslash in a filename so that it can be saved to a
     * text file and read back properly.
     *
     * @param fname DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String escapeFilename(String fname) {
        StringBuffer newstring = new StringBuffer("");

        for (int i = 0; i < fname.length(); i++) {
            if ((fname.charAt(i) == 92) && (fname.charAt(i + 1) != 92)) {
                newstring = newstring.append("\\\\");
            }

            if ((fname.charAt(i) == 92) && (fname.charAt(i + 1) == 92)) {
                newstring = newstring.append("\\\\");
                i++;
            }

            if (fname.charAt(i) != 92) {
                newstring = newstring.append(fname.charAt(i));
            }
        }

        return newstring.toString();
    }

    /**
     * Create a new empty KB with a name.
     *
     * @param name - the name of the KB
     */
    public void addKB(String name) {
        KB kb = new KB(name, (String) preferences.get("kbDir"));
        kbs.put(name.intern(), kb);
        System.out.println("INFO in KBmanager.addKB: Adding KB: " + name);
    }

    /**
     * Remove a knowledge base.
     *
     * @param name - the name of the KB
     */
    public void removeKB(String name) {
        KB kb = (KB) kbs.get(name);

        try {
            if (kb.inferenceEngine != null) {
                kb.inferenceEngine.terminate();
            }
        } catch (IOException ioe) {
            System.out.println(
                "Error in KBmanager.removeKB(): Error terminating inference engine: " +
                ioe.getMessage());
        }

        kbs.remove(name);

        try {
            writeConfiguration();
        } catch (IOException ioe) {
            System.out.println(
                "Error in KBmanager.removeKB(): Error writing configuration file. " +
                ioe.getMessage());
        }

        System.out.println("INFO in KBmanager.removeKB: Removing KB: " + name);
    }

    /**
     * Write the current configuration of the system.  Call
     * writeConfiguration() on each KB object to write its manifest.
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeConfiguration() throws IOException {
        FileWriter fw = null;
        PrintWriter pw = null;
        Iterator it;
        String dir = (String) preferences.get("kbDir");
        String fname = "config.txt";
        String key;
        String value;
        KB kb = null;
        File f;

        System.out.println(
            "INFO in KBmanager.writeConfiguration: Writing configuration.");

        try {
            fw = new FileWriter(dir + File.separator + fname);
            pw = new PrintWriter(fw);
            it = preferences.keySet().iterator();

            while (it.hasNext()) {
                key = (String) it.next();
                value = (String) preferences.get(key);

                if ((key.compareTo("kbDir") == 0) ||
                        (key.compareTo("celtdir") == 0) ||
                        (key.compareTo("inferenceEngine") == 0) ||
                        (key.compareTo("inferenceTestDir") == 0)) {
                    value = escapeFilename(value);
                }

                if (key.compareTo("userName") != 0) {
                    pw.println("<preference key=\"" + key + "\" value=\"" +
                        value + "\"/>");
                }
            }

            //System.out.print("INFO in KBmanager.writeConfiguration(): number of KBs: ");
            //System.out.println(kbs.keySet().size());
            it = kbs.keySet().iterator();

            while (it.hasNext()) {
                key = (String) it.next();
                kb = (KB) kbs.get(key);
                kb.writeConfiguration(pw);

                System.out.print(
                    "INFO in KBmanager.writeConfiguration: Number of constituents in kb: " +
                    kb.name + " is: ");
                System.out.println(kb.constituents.size());
            }
        } catch (java.io.IOException e) {
            throw new IOException("Error writing file " + dir + File.separator +
                fname);
        } finally {
            System.out.println(
                "INFO in KBmanager.writeConfiguration: Completed writing configuration");

            if (pw != null) {
                pw.close();
            }

            if (fw != null) {
                fw.close();
            }
        }
    }

    /**
     * Read an XML-formatted configuration file. The method
     * initializeOnce() sets the preferences based on the contents of the
     * configuration file.
     *
     * @throws IOException DOCUMENT ME!
     */
    private void readConfiguration() throws IOException {
        String fname = "config.txt";
        StringBuffer xml = new StringBuffer();
        String dir = System.getProperty("user.dir") + File.separator + "KBs";
        File f = new File(dir);

        if (!f.exists()) {
            f.mkdir();
        }

        f = new File(dir + File.separator + fname);

        if (!f.exists()) {
            writeConfiguration();
        }

        System.out.println("INFO in KBmanager.readConfiguration(): Reading: " +
            dir);

        BufferedReader br = new BufferedReader(new FileReader(dir +
                    File.separator + fname));

        try {
            do {
                String line = br.readLine();
                xml.append(line + "\n");
            } while (br.ready());
        } catch (java.io.IOException e) {
            System.out.println(
                "Error in KBmanager.readConfiguration(): IO exception parsing file " +
                fname);
        } finally {
            if (br != null) {
                br.close();
            }
        }

        System.out.println(xml.toString());
        configuration = xml.toString();
    }

    /**
     * Get the KB that has the given name.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KB getKB(String name) {
        if (!kbs.containsKey(name)) {
            System.out.println("Error in KBmanager.getKB(): KB " + name +
                " not found.");
        }

        return (KB) kbs.get(name.intern());
    }

    /**
     * Returns true if a KB with the given name exists.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean existsKB(String name) {
        return kbs.containsKey(name);
    }

    /**
     * Remove the KB that has the given name.
     *
     * @param name DOCUMENT ME!
     */
    public void remove(String name) {
        kbs.remove(name);
    }

    /**
     * Get the one instance of KBmanager from its class variable.
     *
     * @return DOCUMENT ME!
     */
    public static KBmanager getMgr() {
        if (manager == null) {
            manager = new KBmanager();
        }

        return manager;
    }

    /**
     * Get the Set of KB names in this manager.
     *
     * @return DOCUMENT ME!
     */
    public Set getKBnames() {
        return kbs.keySet();
    }

    /**
     * Get the preference corresponding to the given kef.
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPref(String key) {
        return (String) preferences.get(key);
    }

    /**
     * Set the preference to the given value.
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setPref(String key, String value) {
        preferences.put(key, value);
    }

    /**
     * A test method.
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        KB kb = KBmanager.getMgr().getKB("SUMO");
        System.out.println(KBmanager.getMgr().getKBnames());
        System.out.println(kb.name);
        System.out.println(NLformatter.htmlParaphrase("",
                "(or (instance ?X0 Relation) (not (instance ?X0 TotalValuedRelation)))",
                kb.getFormatMap("en"), kb.getTermFormatMap("en"), "en"));
    }
}
