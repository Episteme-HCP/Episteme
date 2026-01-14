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

package org.jscience.physics.fluids.dynamics.http;

import java.io.DataInputStream;
import java.io.InterruptedIOException;

import java.net.Socket;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class Server extends Thread {
    /** DOCUMENT ME! */
    final static int SO_TIMEOUT = 5000;

    /** DOCUMENT ME! */
    Socket socket;

    /** DOCUMENT ME! */
    ServerHTTP serverHttp;

    /** DOCUMENT ME! */
    String rootPath;

    /** DOCUMENT ME! */
    Thread thread;

/**
     * Creates a new Server object.
     *
     * @param s      DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @throws Exception DOCUMENT ME!
     */
    Server(Socket s, ServerHTTP parent) throws Exception {
        socket = s;
        socket.setSoTimeout(SO_TIMEOUT);
        socket.setSoLinger(true, 1);
        this.serverHttp = parent;
        this.rootPath = parent.rootPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param petition DOCUMENT ME!
     * @param out DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    void processGET(PetitionHTTP petition, HTTPOutputStream out)
        throws Exception {
        long startTime = System.currentTimeMillis();
        int len = 0;
        String name = petition.filename;

        // String nameWeb = petition.nameWeb;
        String host = socket.getInetAddress().getHostAddress();

        /////////////////////////////////////////////////////////////////////////////
        // get
        try {
            String message = serverHttp.getHTMLDocument();
            out.println("HTTP/1.1 200 OK");
            out.println("Date: " + new Date());
            out.println("Server: " + ServerHTTP.DESCRIPTION);

            // out.println("Location: "+location);
            out.println("Last-Modified: " + new Date());
            out.println("Accept-Ranges: bytes");

            // length can be omited
            out.println("Content-Length: " + message.length());
            out.println("Connection: close");
            out.println("Content-Type: " + serverHttp.fileType("index.html"));
            out.println();

            // out.println(mensaje);
            out.println(message);

            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Estadistics
        long duration = System.currentTimeMillis() - startTime;
        serverHttp.out("<B>ServerHTTP:</B> query of <U>" + host +
            "</U> " // +" "+padre.serveres+"/"+ServidorHTTP.MAX_SERVIDORES
                    //+" ("+tiempo/1000.0+" s) "
             +new Date());

        // Store data for estatistic
        synchronized (serverHttp) {
            serverHttp.lastPetition = "GET " + name;
            serverHttp.lastHost = host;
            serverHttp.totalConectionTime += duration;
            serverHttp.bytesTotalConexion += len;
            serverHttp.totalConnections++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     * @param path DOCUMENT ME!
     * @param modified DOCUMENT ME!
     * @param length DOCUMENT ME!
     * @param mime DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    void sendResponse(HTTPOutputStream out, String path, Date modified,
        long length, String mime) throws Exception {
        out.println("HTTP/1.1 200 OK");
        out.println("Date: " + new Date());
        out.println("Server: " + ServerHTTP.DESCRIPTION);

        //String location = "http://127.0.0.1:8000"+padre.purgaRuta("/" + ruta);
        //System.out.println("LOCATION : "+location);
        // out.println("Location: "+location);
        out.println("Last-Modified: " + modified);
        out.println("Accept-Ranges: bytes");

        // longitud se puede omitir
        if (length > -1) {
            out.println("Content-Length: " + length);
        }

        out.println("Connection: close");
        out.println("Content-Type: " + mime);
        out.println();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     * @param file DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    boolean sendResource(HTTPOutputStream out, String file)
        throws Exception {
        DataInputStream in;
        int numberRead;
        byte[] buffer = new byte[64 * 1024];

        // System.out.println("enviarRecurso: "+file);
        in = new DataInputStream(getClass().getResourceAsStream(file));

        if (in == null) {
            return false;
        }

        sendResponse(out, file, new Date(), -1, serverHttp.fileType(file));

        while ((numberRead = in.read(buffer, 0, buffer.length)) > -1)
            out.write(buffer, 0, numberRead);

        in.close();
        out.flush();
        out.close();

        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        try {
            // Protection against Denial of Service in Legolas (ServerSocket)

            /*
              while(padre.servers > padre.MAX_servers)
              {
              socket.close();
              System.out.println("Se ha excedido the limite of servers");
              return;
            
              // This codigo es vulnerable a ataques
              try {
              System.out.println("Sleeeping...");
              sleep(100);
              } catch (Exception ex)
              {
              System.out.println(ex);
              }
            
              }
             */
            serverHttp.servers++;

            HTTPInputStream in = new HTTPInputStream(socket.getInputStream(),
                    serverHttp);
            HTTPOutputStream out = new HTTPOutputStream(socket.getOutputStream());

            PetitionHTTP petition = in.getPetition();

            if (petition.isGET()) {
                processGET(petition, out);
            } else {
                System.out.println("   No GET! (PortScan!?)");
            }
        } catch (InterruptedIOException exi) {
            System.out.println("SO_TIMEOUT!");
            exi.printStackTrace();
        } catch (Exception ex) {
            System.out.println("Server.run(): " + ex);
            ex.printStackTrace();
        }

        try {
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // As this is the sole function of the class with a try-catch,
        // to pass by here (and adjust the counter) is assured.
        // �And it should be! or Legolas will run little by little out of servers.
        serverHttp.servers--;
    }
}
