/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.net;

import java.io.IOException;


/**
 * A HandshakeException is thrown to indicate that a handshake of a
 * SocketConnection was not successful due to signatures that do not match.
 *
 * @author Holger Antelmann
 *
 * @see SocketConnection
 */
public class HandshakeException extends IOException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6718445048018685292L;

/**
     * Creates a new HandshakeException object.
     */
    public HandshakeException() {
        super("signatures did not match during NetConnection handshake");
    }
}
