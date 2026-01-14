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

package org.jscience.linguistics.dict;

import java.io.*;

import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Zakladni klient pro DICT dle RFC2229 implementovano SHOW DB, SHOW STRAT,
 * DEFINE, MATCH, QUIT
 *
 * @author Stepan Bechynsky
 * @author bechynsky@bdflow.com
 * @author BD Flow, a. s.
 * @version 0.1
 */
public class DictClient {
    /**
     * DOCUMENT ME!
     */
    private final static int DEFAULT_DICT_PORT = 2628; //vychozi port dict serveru

    /* START: prikazy */
    private final static String CMD_SHOW_DB = "SHOW DB"; // seznam databazi dict serveru

    /**
     * DOCUMENT ME!
     */
    private final static String CMD_SHOW_STRAT = "SHOW STRAT"; // seznam moznosti vyhledavani

    /**
     * DOCUMENT ME!
     */
    private final static String CMD_DEFINE = "DEFINE"; // hledat slovo

    /**
     * DOCUMENT ME!
     */
    private final static String CMD_MATCH = "MATCH"; // hledat slovo

    /**
     * DOCUMENT ME!
     */
    private final static String CMD_QUIT = "QUIT"; // odpojeni od serveru

    /* END: prikazy */
    /* START: navratove kody */
    private final int CODE_110 = 110;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_111 = 111;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_112 = 112;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_113 = 113;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_114 = 114;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_130 = 130;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_150 = 150;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_151 = 151;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_152 = 152;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_210 = 210;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_220 = 220;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_221 = 221;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_230 = 230;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_250 = 250;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_330 = 330;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_420 = 420;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_421 = 421;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_500 = 500;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_501 = 501;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_502 = 502;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_503 = 503;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_530 = 530;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_531 = 531;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_532 = 532;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_550 = 550;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_551 = 551;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_552 = 552;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_554 = 554;

    /**
     * DOCUMENT ME!
     */
    private final int CODE_555 = 555;

    /* END: navratove kody */
    private String serverName = new String(); // jmeno dict serveru

    /**
     * DOCUMENT ME!
     */
    private int serverPort = 0; // port dict serveru

/**
     * Creates a new DictClient object.
     *
     * @param serverName DOCUMENT ME!
     * @param serverPort DOCUMENT ME!
     */
    public DictClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    } // END: DictClient(String serverName, int serverPort)

/**
     * Creates a new DictClient object.
     *
     * @param serverName DOCUMENT ME!
     */
    public DictClient(String serverName) {
        this(serverName, DEFAULT_DICT_PORT);
    } // END: DictClient(String serverName, int serverPort)

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        DictClient dc = new DictClient("dione.zcu.cz");

        try {
            //System.out.println(dc.getDatabaseList());
            //System.out.println(dc.getStrategyList());
            System.out.println(dc.getWords("*", "prefix", "fast "));

            //System.out.println(dc.getDefinition("!", "wheel"));
            //System.out.println(dc.getDefinition("!", "adasefqwer"));
        } catch (DictException e) {
            System.out.println("ERROR: " + e.toString());
        }

        dc = null;
    }

    /**
     * Provede dotaz na serveru
     *
     * @param cmd prikaz pro dict server
     * @param returnCode navratova hodnota pro dany prikaz, pokud je vykonan
     *        uspesne
     *
     * @return String vysledek prikazu
     *
     * @throws DictException DOCUMENT ME!
     */
    private String getResponse(String cmd, int returnCode)
        throws DictException {
        StringBuffer response = new StringBuffer();
        Socket dictSocket = null; // spojeni s dict serverem

        try {
            dictSocket = new Socket(serverName, serverPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                        dictSocket.getInputStream(), "UTF-8"));
            String line = new String();
            line = in.readLine();

            /* START: test platneho spojeni */
            int resultCode = Integer.parseInt(line.substring(0, 3));

            switch (resultCode) {
            case 220: // v poradku
                break;

            case 420:
                throw new DictException(CODE_420);

            case 421:
                throw new DictException(CODE_421);

            case 530:
                throw new DictException(CODE_530);
            }

            /* END: test platneho spojeni */
            /* START: poslani prikazu */
            Writer out = new OutputStreamWriter(dictSocket.getOutputStream(),
                    "UTF-8");
            out.write(cmd);
            out.write("\r\n");
            out.flush();

            /* END: poslani prikazu */
            /* START: test odpovedi na prikaz */
            line = in.readLine(); // radek s odpovedi
            resultCode = Integer.parseInt(line.substring(0, 3));

            if (resultCode != returnCode) {
                switch (resultCode) {
                case 500:
                    throw new DictException(CODE_500);

                case 501:
                    throw new DictException(CODE_501);

                case 502:
                    throw new DictException(CODE_502);

                case 503:
                    throw new DictException(CODE_503);

                case 550:
                    throw new DictException(CODE_550);

                case 552:
                    throw new DictException(CODE_552);

                case 554:
                    throw new DictException(CODE_554);

                default:
                    throw new DictException(resultCode);
                }
            }

            /* END: test odpovedi na prikaz */
            while (true) {
                line = in.readLine();

                if (line.startsWith(".") && !line.startsWith("..")) {
                    line = in.readLine();
                    resultCode = Integer.parseInt(line.substring(0, 3));

                    if (resultCode != CODE_151) {
                        break;
                    }
                }

                response.append(line + "\r\n");
            }

            if (resultCode != CODE_250) {
                throw new DictException(CODE_250);
            }

            out.write(CMD_QUIT);
            out.write("\r\n");
            out.flush();
            line = in.readLine();
            resultCode = Integer.parseInt(line.substring(0, 3));

            if (resultCode != CODE_221) {
                throw new DictException(CODE_221);
            }
        } catch (UnknownHostException e) {
            response.append("ERROR: Unknown host " + serverName + ":" +
                serverPort);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        } finally {
            try {
                dictSocket.close();
            } catch (IOException ee) {
            } catch (NullPointerException ee) {
            }
        }

        return response.toString();
    } // END: getResponse(String cmd)

    /**
     * 
    DOCUMENT ME!
     *
     * @return String seznam databazi na dict serveru
     *
     * @throws DictException
     */
    public String getDatabaseList() throws DictException {
        return getResponse(CMD_SHOW_DB, CODE_110);
    } // END: getDatabaseList()

    /**
     * 
    DOCUMENT ME!
     *
     * @return String seznam moznosti vyhledavani pro MATCH
     *
     * @throws DictException
     */
    public String getStrategyList() throws DictException {
        return getResponse(CMD_SHOW_STRAT, CODE_111);
    } // END: getDatabaseList()

    /**
     * 
    DOCUMENT ME!
     *
     * @param db prohledavana databaze
     * @param word hledane slovo
     *
     * @return String vysledek vyjledavani DEFINE
     *
     * @throws DictException
     */
    public String getDefinition(String db, String word)
        throws DictException {
        return getResponse(CMD_DEFINE + " " + db + " \"" + word + "\"", CODE_150);
    } // END: getDatabaseList()

    /**
     * 
    DOCUMENT ME!
     *
     * @param db prohledavana databaze
     * @param strat DOCUMENT ME!
     * @param word hledane slovo
     *
     * @return String vysledek vyjledavani DEFINE
     *
     * @throws DictException
     */
    public String getWords(String db, String strat, String word)
        throws DictException {
        return getResponse(CMD_MATCH + " " + db + " " + strat + " \"" + word +
            "\"", CODE_152);
    } // END: getDatabaseList()
}
