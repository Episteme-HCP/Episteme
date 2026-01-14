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

// Los HTTPInput(Output)Stream deben implementar a inner class
// que periodicamente compruebe the diferencia entre
// lastReadTime y lastWriteTime respectivamente, con the time actual,
// y cierre the Stream if es mayor que a value dado.
// los methods of lectura y escritura deberan actualizar dichos values
// convenientemente!
package org.jscience.physics.fluids.dynamics.http;

import org.jscience.physics.fluids.dynamics.KernelADFC;

import java.io.InterruptedIOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ServerHTTP implements Runnable //, InterfazRMI
                                            // UNCOMMENT ALSO IN CONSTRUCTOR!!
 {
    /** Packet Version */
    static final String DESCRIPTION = "Legolas v1.2.1java";

    /** Timeout for the server socket */
    static final int SO_TIMEOUT = 5000;

    /**
     * number maximum of servers actives. It is a hard limit conections
     * will be closed in a rough manner.
     */
    static int MAX_SERVERS = 8;

    //  MIME types
    /** DOCUMENT ME! */
    static final String[] EXTENSIONS = {
            "class", "gif", "htm", "html", "jpeg", "jpg", "mid", "mp3"
        };

    /** DOCUMENT ME! */
    static final String[] MIME_TYPES = {
            "application/octet-stream", "image/gif", "text/html", "text/html",
            "image/jpeg", "image/jpeg", "audio/x-midi", "audio/x-mpeg"
        };

    /** File that will be shown by default */
    String defaultFile = "index.html";

    /** Ruta by defecto del directorio raiz del server */
    String rootPath = ".";

    /** Port to listen */
    int portHTTP = 8080;

    /** number of servers activos */
    int servers;

    /** last petition */
    String lastPetition;

    /** last host that did a petition */
    String lastHost;

    /** total petitions */
    int totalConnections;

    /** total time of all conextions, used to find the average */
    long totalConectionTime;

    /** total flux of bytes, used to find the average */
    long bytesTotalConexion;

    /** flag that tells run() if it should be ending */
    boolean isRunning;

    /**
     * flag that tells that run() knows about the change of the flag
     * 'isRunning' and it is completely closed.
     */
    boolean death;

    /** flag to be active if using RMI */
    private boolean rmi = false;

    /** Server Socket that receives external conextions */
    ServerSocket server;

    /**
     * InterfazServFiles, allows to increase the functionality of our
     * server. Here servers are entered for different tasks as the local file
     * system, to zip, jar, whatever...
     */
    Vector servArch;

    /**
     * Vector that contains the differents administrators that are
     * registered via RMI, and that should be inform.
     */
    Vector admin;

    /** DOCUMENT ME! */
    String htmlDoc;

    // Only accept local conextions
    /** DOCUMENT ME! */
    private boolean localOnly;

    /** DOCUMENT ME! */
    private KernelADFC kernel;

/**
     * Creates a new ServerHTTP object.
     *
     * @param kadfc DOCUMENT ME!
     */
    public ServerHTTP(KernelADFC kadfc) {
        kernel = kadfc;
        death = false;
        isRunning = false;
        htmlDoc = new String();
    }

    /**
     * The invocation of this method should stop the web server in, at
     * most, SO_TIMEOUT milliseconds. Belongs to InterfazRMI.
     */
    public void terminate() {
        if (!isRunning) {
            return;
        }

        isRunning = false;

        // This es the needed time to shut down the former
        try {
            int times = 1;

            do {
                System.out.println("Waiting for the thread to finish..." +
                    " (x" + (times++) + ")");
                Thread.sleep(SO_TIMEOUT / 8);
            } while (!death);

            admin = null;
            servArch = null;
        } catch (Exception ex) {
            System.out.println("terminate(): " + ex);
            ex.printStackTrace();
        }

        System.out.println("Ended");
        death = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHTMLDocument() {
        return htmlDoc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setHTMLString(String str) {
        htmlDoc = str;
    }

    /**
     * DOCUMENT ME!
     *
     * @param loc DOCUMENT ME!
     */
    public void setSoloLocal(boolean loc) {
        localOnly = loc;
    }

    /**
     * Initiates the server. Belongs to the InterfazRMI
     */
    public void inicia() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        death = false;

        Thread hilo = new Thread(this);
        hilo.start();

        kernel.out("<B>ServerHTTP:</B> initiated at port " + portHTTP +
            ((localOnly) ? ", only localhost" : ", accepts any client"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    void out(String html) {
        kernel.out(html);
    }

    /**
     * Send a string to the log
     *
     * @param log DOCUMENT ME!
     */
    void printLog(String log) {
        System.out.println("- " + log);
    }

    /**
     * Purge paths searching repeteated //
     *
     * @param route DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String purgeRoute(String route) {
        String ret = new String();

        for (int p = 0; p < route.length(); p++) {
            char c = route.charAt(p);
            ret = ret + c;

            // Skip all exceeding '/' .
            if (c == '/') {
                for (;
                        ((p + 1) < route.length()) &&
                        (route.charAt(p + 1) == '/'); p++)
                    ;
            }
        }

        // System.out.println("purgaRuta: '"+ruta+"' -> '"+ret+"'");
        return ret;
    }

    /**
     * substitute in the string
     *
     * @param string DOCUMENT ME!
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String substitute(String string, String a, String b) {
        int pos;

        while ((pos = string.indexOf(a)) > 0) {
            StringBuffer sb = new StringBuffer(string);
            System.out.println(
                "NO substituting IN ServerHTTP.substitute !!! correct");

            // sb.replace(pos, pos+a.length(), b);
            string = sb.toString();
        }

        return string;
    }

    /**
     * This method reinicia the server web. Forma part of the
     * InterfazRMI.
     */
    public void restart() {
        terminate();

        // The former is now finish. Start again
        inicia();
    }

    /**
     * Method run() of the Thread
     */
    public void run() {
        servers = 0;
        lastPetition = "No petitions";
        lastHost = "No conextions";
        totalConnections = 0;
        totalConectionTime = 0;
        bytesTotalConexion = 0;

        System.out.println("Started: Web Server " + DESCRIPTION +
            " (jscience.fluids)");

        try {
            server = new ServerSocket(portHTTP);
            server.setSoTimeout(SO_TIMEOUT);
        } catch (Exception ex) {
            System.out.println("ServerSocket: " + ex);
            ex.printStackTrace();

            return;
        }

        // Received and waiting for petitions
        do {
            try {
                Socket cliente = server.accept();

                // If too many conections, close.
                if (servers < MAX_SERVERS) {
                    (new Server(cliente, this)).start();
                } else {
                    cliente.close();
                }
            } catch (InterruptedIOException ex) {
                // System.out.println("reinit");
                // The waiting time has expired. If
                // finalizaFlag is active, the method terminate runs,
                // if not, a new server socket is created.
                // This is the unique form of having some control in
                // the accept(). And to be able to end the method
                // run() without using stop().
                continue;
            } catch (Exception ex) {
                System.out.println("Accept(): " + ex);
                ex.printStackTrace();

                return;
            }
        } while (isRunning);

        try {
            server.close();
        } catch (Exception ex) {
            System.out.println("run(): closing ServerSocket...");
            ex.printStackTrace();
        }

        // Todo ok.
        death = true;
    }

    /**
     * Especify the port TCP/IP where to listen. Generally is port 80.
     * this method forms part of the InterfazRMI.
     *
     * @param puerto DOCUMENT ME!
     */
    public void setPuertoHTTP(int puerto) {
        portHTTP = puerto;
        System.out.println("Listening at port: " + portHTTP);

        // if(enEjecucion) restart();
    }

    /**
     * Identify the filename type via its extension.
     *
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    String fileType(String file) throws Exception {
        String extension = file.substring(file.lastIndexOf(".") + 1);

        for (int i = 0; i < EXTENSIONS.length; i++)
            if (extension.equalsIgnoreCase(EXTENSIONS[i])) {
                return MIME_TYPES[i];
            }

        System.out.println("     Unknown Extension: " + extension);

        return ("text/html");
    }
}
