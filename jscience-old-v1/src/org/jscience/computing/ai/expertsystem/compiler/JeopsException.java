package org.jscience.computing.ai.expertsystem.compiler;


/*
 * JEOPS - The Java Embedded Object Production System
 * Copyright (c) 2000   Carlos Figueira Filho
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Contact: Carlos Figueira Filho (csff@cin.ufpe.br)
 */
/**
 * An exception thrown by JEOPS.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  09.03.2000
 */
public class JeopsException extends Exception {
    /**
     * The line number in the rule file where there is the problem that
     * caused this exception.
     */
    private int line;

    /**
     * The column number in the rule file where there is the problem
     * that caused this exception.
     */
    private int column;

/**
     * Constructs a JeopsException with no detail message.
     *
     * @param line   the line of the problem.
     * @param column the column of the problem.
     */
    public JeopsException(int line, int column) {
        super();
        this.line = line;
        this.column = column;
    }

/**
     * Constructs a JeopsException with the specified detail message.
     *
     * @param s      the detail message.
     * @param line   the line of the problem.
     * @param column the column of the problem.
     */
    public JeopsException(String s, int line, int column) {
        super(s);
        this.line = line;
        this.column = column;
    }

    /**
     * Returns the column number in the rule file where there is the
     * problem that caused this exception.
     *
     * @return the column number in the rule file where there is the problem
     *         that caused this exception.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the line number in the rule file where there is the
     * problem that caused this exception.
     *
     * @return the line number in the rule file where there is the problem that
     *         caused this exception.
     */
    public int getLine() {
        return line;
    }
}
