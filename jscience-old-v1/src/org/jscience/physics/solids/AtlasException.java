/*
 * Created on Feb 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.jscience.physics.solids;

/**
 * @author Herbie
 *         <p/>
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class AtlasException extends Throwable {

    private String msg = "";

    public AtlasException(String err) {
        this.msg = err;
    }

    public String getMessage() {
        return msg;
    }

}
