/*
 * $Id: Phrasebook.java,v 1.2 2007-10-21 17:46:59 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl/
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which 
 *  case the provisions of the LGPL license are applicable instead of those 
 *  above. A copy of the LGPL license is available at http://www.gnu.org/
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
 */
package org.jscience.ml.openmath.phrasebook;

import java.util.Vector;

/**
 * Defines what a minimal phrasebook should implement. <p>
 * <p/>
 * Note that this class does NOT assume anything about the protocol
 * used between client and server. This is the responsibility of the
 * implementing subclass. Because of this, problems with the communication
 * should be thrown as a subclass of PhrasebookException. <p>
 *
 * @author Manfred Riem (mriem@win.tue.nl)
 * @version $Version$
 */
public abstract class Phrasebook {
    /**
     * Constructor. <p>
     */
    public Phrasebook() {
        super();
    }

    /**
     * A perform method. <p>
     *
     * @param fMethod    the type of perform.
     * @param fArguments the arguments to the perform.
     * @return the result.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public String perform(String fMethod, Vector fArguments)
            throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }

    /**
     * An execute method. <p>
     * <p/>
     * <p/>
     * <i>Note:</i> the previous version of the library (1.2) didn't allow
     * you to return any object as a result of a perform. To make it possible
     * this method is the right one to implement.
     * </p>
     *
     * @param fMethod    the type of execution.
     * @param fArguments the arguments to the execute.
     * @return the result.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public Object execute(String fMethod, Vector fArguments)
            throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }

    /**
     * Adds a CD to the phrasebook. <p>
     *
     * @param fName      the name of the CD.
     * @param fLocation  the URL of the CD, if none is given it is
     *                   assumed to be on the classpath
     * @param fClassName the Codec of the CD.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public void addCD(String fName, String fLocation, String fClassName)
            throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }

    /**
     * Removes a CD from the phrasebook. <p>
     *
     * @param fName the name of the CD.
     * @throws PhrasebookException thrown when a major error occurs.
     */
    public void removeCD(String fName) throws PhrasebookException {
        throw new PhrasebookException("Subclasses should implement this");
    }
}
