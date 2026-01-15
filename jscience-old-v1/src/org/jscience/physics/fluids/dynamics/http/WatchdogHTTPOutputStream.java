/*
 * Program : ADFC�
 * Class : org.jscience.fluids.http.WatchdogHTTPOutputStream
 *         close conectiones "muertas"
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 16/11/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.http;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class WatchdogHTTPOutputStream extends Thread {
    /** DOCUMENT ME! */
    HTTPOutputStream stream;

/**
     * Creates a new WatchdogHTTPOutputStream object.
     *
     * @param s DOCUMENT ME!
     */
    WatchdogHTTPOutputStream(HTTPOutputStream s) {
        stream = s;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        while (stream.alive) {
            try {
                if (stream.enviado) {
                    stream.enviado = false;
                } else {
                    stream.out.close();
                    stream.alive = false;
                    System.out.println("HTTPOutputStream.Watchdog()");
                }

                sleep(HTTPOutputStream.WATCHDOG_TIMEOUT);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
