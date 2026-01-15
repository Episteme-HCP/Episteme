/*-----------------------------------------------------------------------
 * Copyright (C) 2001 Green Light District Team, Utrecht University
 *
 * This program (Green Light District) is free software.
 * You may redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by
 * the Free Software Foundation (version 2 or later).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * See the documentation of Green Light District for further information.
 *------------------------------------------------------------------------*/
package org.jscience.architecture.traffic.xml;

import org.jscience.architecture.traffic.TrafficException;


// You're in a maze of obscure exceptions, all alike...
/**
 * Thrown by a XMLSerializable when it receives invalid input from the
 * parser. (e.g. a negative int when it should be positive)
 */
public class XMLInvalidInputException extends TrafficException {
/**
     * oops..
     *
     * @param message A description of the problem.
     */
    public XMLInvalidInputException(String message) {
        super(message);
    }
}
