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

import java.io.IOException;
import java.io.OutputStream;

// Antes extendia FilterOutputStream... AHORA ES MUCHO MAS RAPIDO
// aunque parezca poco ortodoxo.

/**
 * The salida of data hacia the socket es tratado mediante este
 * HTTPOutputStream. Lo logico seria que extendiese the class
 * FilterOutputStream, pero resulta demasiado lento. Asi va
 * muy (pero muy) rapido. <p>
 * <p/>
 * Los methods, excepto println(), simplemente redireccionan a
 * OutputStream. <p>
 * <p/>
 * <b>Proteccion DOS</b> <p>
 * This class ha sido blindada contra ataques <em>Denial Of Service</em>,
 * o al menos eso espero. The inner class Watchdog se encarga of comprobar
 * que the escritura no se ha detenido durante mas of
 * <code>WATCHDOG_TIMEOUT</code> miliseconds. Si es asi, close the puerto,
 * lo que causa a exception inmediata (en teoria) que the cogera
 * <code>Servidor</code>. <p>
 * En the momento que se lleve a cabo a escritura, se debe poner
 * <code>enviado</code> igual a true. Si the puerto se close,
 * <code>alive</code> a false. Si in dos iteraciones del bucle del
 * watchdog, enviado no se actualiza, se cerrara the puerto.
 */
class HTTPOutputStream {
    protected OutputStream out;
    // This value debe estar in concordancia con los buffers!!
    protected final static int WATCHDOG_TIMEOUT = 6000;
    protected boolean alive, enviado;

    /**
     * Crea a canal HTTPOutputStream a partir del canal of salida hacia
     * the socket.
     *
     * @param out canal of salida
     */
    public HTTPOutputStream(OutputStream out) {
        this.out = out;
        alive = true;
        enviado = true;
        new WatchdogHTTPOutputStream(this).start();
    }

    /**
     * close the canal.
     *
     * @throws IOException in caso of error I/O
     */
    public void close() throws IOException {
        out.close();
        alive = false;
    }

    /**
     * vuelca las memorias intermedias
     *
     * @throws IOException if hay error I/O
     */
    public void flush() throws IOException {
        out.flush();
    }

    /**
     * envia a retorno of carro.
     *
     * @throws IOException in caso of error I/O
     */
    public void println() throws IOException {
        write(13);
        write(10);
    }

    /**
     * envia a cadena of caracteres
     *
     * @param s the cadena of caracteres a enviar
     * @throws IOException in caso of error I/O
     */
    public void println(String s) throws IOException {
        byte[] b = s.getBytes();
        out.write(b, 0, b.length);
        println();
    }

    /**
     * envia a entero by the canal
     *
     * @param value entero a enviar
     * @throws IOException in caso of error I/O
     */
    public void write(int valor) throws IOException {
        enviado = true;
        out.write(valor);
    }

    /**
     * escribe desde a array a ristra of bytes
     *
     * @param data   array of data
     * @param inicio indice del primer byte a enviar
     * @param f      longitud
     * @throws IOException
     */
    public void write(byte[] data, int inicio, int f) throws IOException {
        enviado = true;
        out.write(data, inicio, f);
    }

}
    

