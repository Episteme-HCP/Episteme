/*
 * Program : ADFC�
 * Class : org.jscience.fluids.http.PetitionHTTP
 *         Analiza petitiones of los clientes HTTP
 * Director : Leo Gonzalez Gutierrez <leo.gonzalez@iit.upco.es>
 * Programacion : Alejandro Rodriguez Gallego <balrog@amena.com>
 * Date  : 16/11/2001
 *
 * This program se distribuye bajo LICENCIA PUBLICA GNU (GPL)
 */
package org.jscience.physics.fluids.dynamics.http;

import java.util.StringTokenizer;
import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
class PetitionHTTP {
    /** DOCUMENT ME! */
    private String rootPath;

    /** DOCUMENT ME! */
    private ServerHTTP parent;

    /** DOCUMENT ME! */
    private Vector lines = new Vector();

    /** name of the file inthe petition GET */
    public String filename;

    /** protocole of the petition GET */
    public String protocole;

/**
     * Create a PetitionHTTP.
     *
     * @param parent server parent of the process
     */
    public PetitionHTTP(ServerHTTP parent) {
        this.parent = parent;
        rootPath = parent.rootPath;
    }

    /**
     * add new text lin building little by little the petition GET.
     * Once all lines are added the content could be consulted su contenido
     * after invocation of <CODE>analize()</CODE>.
     *
     * @param line string of characters
     */
    public void newLine(String line) {
        lines.addElement(line);

        // System.out.println("*LINE: "+line);
    }

    /**
     * Returns true if it is a petition GET.
     *
     * @return Returns true if it is a petition GET. False otherwise.
     */
    public boolean isGET() {
        if (lines.size() > 0) {
            String firstLine = (String) lines.elementAt(0);
            firstLine = firstLine.trim();

            if (firstLine.length() > 0) {
                if (firstLine.substring(0, 3).equalsIgnoreCase("GET")) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void analize() {
        String order = null;
        String name = null;
        String protocole = null;

        try {
            // No petition
            if (lines.size() == 0) {
                return;
            }

            // StringTokenizer... I am more relaxed with this.
            try {
                StringTokenizer tok = new StringTokenizer((String) lines.elementAt(
                            0), " ");

                int totalTokens = tok.countTokens();
                order = tok.nextToken();

                if (!order.equalsIgnoreCase("GET")) {
                    parent.printLog("Incorrect petition : no GET.");

                    return;
                }

                name = tok.nextToken();

                for (int c = 1; c < (totalTokens - 2); c++)
                    name += (" " + tok.nextToken());

                name = name.trim();

                protocole = tok.nextToken();

                // System.out.println(name+"->"+protocole);
            } catch (Exception ex) {
                System.out.println("PetitionHTTP: " + ex);
                name = "/";
                ex.printStackTrace();
            }

            // Sustitute %20 by spaces
            // There are still many other characters which are not yet
            // recognized!
            name = ServerHTTP.substitute(name, "%20", " ");

            filename = name;

            return;
        } catch (Exception ex) {
            return;
        }
    }
}
