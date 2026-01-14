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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * This class se encarga of the lectura of las petitiones a nivel of I/O
 * basico, ajustando los caracteres of forma adecuada.
 * <p/>
 * <p/>
 * Es importante que todas las lecturas se realicen a traves of
 * <code>readLine()</code> ya que this es the unico method protegido contra
 * DOS. El method getPetition() emplea the class PeticionHTTP.
 * </p>
 */
class HTTPInputStream extends FilterInputStream {
    /**
     * maxima entrada. Proteccion contra DOS
     */
    private final static int MAX_BUF_DOS = 16000;

    /**
     * DOCUMENT ME!
     */
    private ServerHTTP padre;

    /**
     * Crea a canal HTTPInputStream from un InputStream conectado al socket.
     *
     * @param in    canal of entrada desde the socket
     * @param padre server HTTP padre
     */
    public HTTPInputStream(InputStream in, ServerHTTP padre) {
        super(in);
        this.padre = padre;
    }

    /**
     * Lee a linea desde the InputStream
     *
     * @return returns the cadena of caracteres leida.
     * @throws IOException Cuando ocurra alguna anomalia in the proceso I/O
     */
    public String readLine() throws IOException {
        StringBuffer result = new StringBuffer();
        boolean finished = false;
        boolean cr = false;
        int lenDOS = 0;

        do {
            if ((lenDOS++) > MAX_BUF_DOS) {
                padre.printLog("ATAQUE DOS!");

                return null;
            }

            int ch = -1;
            ch = read();

            if (ch == -1) {
                return result.toString();
            }

            result.append((char) ch);

            if (cr && (ch == 10)) {
                result.setLength(result.length() - 2);

                return result.toString();
            }

            if (ch == 13) {
                cr = true;
            } else {
                cr = false;
            }
        } while (!finished);

        return result.toString();
    }

    /**
     * Lee a PetitionHTTP desde this canal
     *
     * @return the PetitionHTTP leida y analizada
     * @throws Exception if ocurre a error of I/O
     */
    public PetitionHTTP getPetition() throws Exception {
        PetitionHTTP petition = new PetitionHTTP(padre);
        String linea;

        do {
            linea = readLine();

            if (linea.length() > 0) {
                petition.newLine(linea);
            } else {
                break;
            }
        } while (true);

        petition.analize();

        return petition;
    }
}
