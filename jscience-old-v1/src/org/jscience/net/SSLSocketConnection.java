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

import org.jscience.util.SecurityNames;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

/**
 * An SSLSocketConnection provides simplified access to a remote process
 * through an SSLSocket connection.
 * <p/>
 * not done yet
 *
 * @author Holger Antelmann
 * @see NetConnectionServer
 * @see NetConnectionHandler
 * @see ConnectionDispatcher
 * @see NetConnection
 */
public class SSLSocketConnection implements NetConnection {
    static KeyStore keyStore = null;
    static char[] password = null;

    ObjectInputStream in;
    ObjectOutputStream out;
    SSLSocket socket;
    InetAddress host;
    int port;
    Object signature;

    /**
     * called by createConnection(), createServerConnection(), or a subclass
     */
    protected SSLSocketConnection(String hostname, int port, Object signature) throws IOException {
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.signature = signature;
    }


    // used by NetConnectionServer.run()
    /**
     * called by objects that create the connection themselves - including the handshake
     */
    protected SSLSocketConnection(SSLSocket socket, ObjectInputStream in, ObjectOutputStream out, Object signature)
            throws IOException {
        this.socket = socket;
        this.host = socket.getInetAddress();
        this.port = socket.getPort();
        this.in = in;
        this.out = out;
        this.signature = signature;
    }

    public static void init(KeyStore keyStore, char[] password) {
        SSLSocketConnection.keyStore = keyStore;
        SSLSocketConnection.password = password;
    }

    public static SSLSocketConnection createServerConnection(int port, Object signature)
            throws IOException, HandshakeException, IllegalStateException, GeneralSecurityException {
        if (keyStore == null) throw new IllegalStateException("not initialized, yet");
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, password);
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(kmf.getKeyManagers(), null, null);
        SSLServerSocket ss = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(port);
        return createServerConnection(ss, signature);
    }

    /**
     * waits and listens for a single connection to be made and returns an
     * SSLSocketConnection if the connection made matches the signature
     *
     * @throws HandshakeException if the signatures did not match
     */
    public static SSLSocketConnection createServerConnection(SSLServerSocket serverSocket, Object signature)
            throws IOException, HandshakeException, IllegalStateException {
        if (keyStore == null) throw new IllegalStateException("not initialized, yet");
        try {
            Socket socket = serverSocket.accept();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Object remoteSignature = in.readObject();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(signature);
            out.flush();
            SSLSocketConnection con = new SSLSocketConnection((SSLSocket) socket, in, out, signature);
            if (signature == null) {
                if (remoteSignature == null) return con;
            } else if (signature.equals(remoteSignature)) return con;
        } catch (ClassNotFoundException e) {
        }
        throw new HandshakeException();
    }

    /**
     * returns a SocketConnection provided there is a Server listening at the
     * specified location with the secified signature; note that this
     * method is blocking.
     *
     * @param signature - an object that identifies the desired type of connection
     * @throws HandshakeException if the signatures did not match
     * @see NetConnectionServer
     */
    public static SSLSocketConnection createConnection(String hostname, int port, Object signature)
            throws IOException, HandshakeException {
        SSLSocketConnection con = new SSLSocketConnection(hostname, port, signature);
        if (con.handshake()) {
            return con;
        }
        con.close();
        throw new HandshakeException();
    }

    boolean handshake() throws IOException {
        try {
            Socket s = new Socket(host, port);
            socket = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault()).createSocket(s, host.getHostName(), port, true);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(signature);
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());
            Object remoteSignature = in.readObject();
            if (signature == null) {
                if (remoteSignature == null) return true;
            } else if (signature.equals(remoteSignature)) {
                return true;
            }

        } catch (ClassNotFoundException e) {
        }
        return false;
    }

    public boolean isActive() {
        return (socket.isConnected() && !socket.isClosed());
    }

    public synchronized void sendMessage(Object message) throws IOException {
        if (out == null) throw new IOException("connection is closed");
        out.writeObject(message);
        out.flush();
    }

    public Object readMessage() throws IOException, ClassNotFoundException {
        if (in == null) throw new IOException("connection is closed");
        return in.readObject();
    }

    /**
     * propagates to the embedded socket
     */
    public void setSoTimeout(int timeout) throws IOException {
        if (socket == null) throw new IOException("connection is closed");
        socket.setSoTimeout(timeout);
    }

    public Object getSignature() {
        return signature;
    }

    /**
     * closes the SocketConnection
     */
    public synchronized void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
        } finally {
            out = null;
            in = null;
            socket = null;
        }
    }

    //protected void finalize() { close(); }


    /**
     * returns an SSLSocket
     */
    public Socket getSocket() {
        return socket;
    }

    public InetAddress getRemoteHost() {
        return host;
    }

    public int getRemotePort() {
        return port;
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public String toString() {
        String s = super.toString() + " connected to host " + host.getHostName();
        s += " at port " + port;
        s += " (signature: " + signature + ")";
        return s;
    }
}
