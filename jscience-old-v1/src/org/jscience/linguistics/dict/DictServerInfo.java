package org.jscience.linguistics.dict;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * Uchovava informace o dict serveru - seznam databazi a zpusobu hledani
 *
 * @author Stepan Bechynsky
 * @author bechynsky@bdflow.com
 * @author BD Flow, a. s.
 * @version 0.1
 */
public class DictServerInfo {
    /**
     * DOCUMENT ME!
     */
    private String serverName = new String();

    /**
     * DOCUMENT ME!
     */
    private int serverPort = 0;

    /**
     * DOCUMENT ME!
     */
    private DictClient dict = null;

    /**
     * DOCUMENT ME!
     */
    private Vector database = null;

    /**
     * DOCUMENT ME!
     */
    private Vector strategy = null;

/**
     * Creates a new DictServerInfo object.
     *
     * @param name DOCUMENT ME!
     * @param port DOCUMENT ME!
     */
    public DictServerInfo(String name, int port) {
        serverName = name;
        serverPort = port;
        dict = new DictClient(serverName, serverPort);
    } // END: DictServerInfo(String name, int port)

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        DictServerInfo dci = new DictServerInfo("dione.zcu.cz", 2628);

        try {
            System.out.println(dci.getDatabase());
            System.out.println(dci.getStrategy());
            System.out.println(dci.getDatabase());
        } catch (DictException e) {
            System.out.println(e.toString());
        }
    } // END: main(String args[])

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getServerName() {
        return serverName;
    } // END: getServerName()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getServerPort() {
        return serverPort;
    } // END: getServerPort()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return serverName + ":" + serverPort;
    } // END: toString()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DictException DOCUMENT ME!
     */
    public Vector getDatabase() throws DictException {
        if (database == null) {
            fillDatabase();
        }

        return database;
    } // END: getDatabase()

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DictException DOCUMENT ME!
     */
    public Vector getStrategy() throws DictException {
        if (strategy == null) {
            fillStrategy();
        }

        return strategy;
    } // END: getDatabase()

    /**
     * DOCUMENT ME!
     *
     * @param db DOCUMENT ME!
     * @param strat DOCUMENT ME!
     * @param word DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DictException DOCUMENT ME!
     */
    public Vector getWords(String db, String strat, String word)
        throws DictException {
        return fillVector(dict.getWords(db, strat, word));
    } // END: getWords(String db, String strat, String word)

    /**
     * DOCUMENT ME!
     *
     * @param db DOCUMENT ME!
     * @param word DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DictException DOCUMENT ME!
     */
    public String getDefinition(String db, String word)
        throws DictException {
        return dict.getDefinition(db, word);
    } // END: getDefinition(String db, String word)

    /**
     * DOCUMENT ME!
     *
     * @throws DictException DOCUMENT ME!
     */
    private void fillDatabase() throws DictException {
        System.out.print("Reading database list from " + toString() + "...");
        database = fillVector(dict.getDatabaseList());
        database.add(0, new DSItem("*", "all databases"));
        System.out.println("\b\b\b done");
    } // END: fillDatabase()

    /**
     * DOCUMENT ME!
     *
     * @throws DictException DOCUMENT ME!
     */
    private void fillStrategy() throws DictException {
        System.out.print("Reading strategy list from " + toString() + "...");
        strategy = fillVector(dict.getStrategyList());
        System.out.println("\b\b\b done");
    } // END: fillStrategy()

    /**
     * DOCUMENT ME!
     *
     * @param ans DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Vector fillVector(String ans) {
        Vector ret = new Vector();
        StringTokenizer st = new StringTokenizer(ans, "\r\n");

        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            int i = line.indexOf("\"");

            if (i < 0) {
                continue;
            }

            String name = line.substring(0, i - 1).trim();
            String info = line.substring(i + 1, line.length() - 1).trim();
            ret.addElement(new DSItem(name, info));
        }

        return ret;
    } // END: fillVector(String ans)
}
