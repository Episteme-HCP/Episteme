/*
 * Program : ADFC�
 * Class : org.jscience.fluids.http.HTTPInputStream
 *         Canal of entrada of data HTTP.
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 16/11/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
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
