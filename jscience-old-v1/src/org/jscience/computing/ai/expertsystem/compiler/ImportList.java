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
import java.util.Vector;


/**
 * A list of import statements.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  09.03.2000
 */
public class ImportList {
    /**
     * The set of generic import statements. A generic import statement
     * is one such as <br>
     * <code>import java.util.;</code>
     */
    private Vector genericImportStatements;

    /**
     * The set of specific import statements. A specific import
     * statement is one such as <br>
     * <code>import java.util.Vector;</code>
     */
    private Vector specificImportStatements;

/**
     * Class constructor for rule bases in the default package.
     */
    public ImportList() {
        this("");
    }

/**
     * Class constructor.
     *
     * @param packageName the package of the rule base.
     */
    public ImportList(String packageName) {
        specificImportStatements = new Vector();
        genericImportStatements = new Vector();
        genericImportStatements.addElement("java.lang.");
        genericImportStatements.addElement(""); // The default package.

        if (packageName.length() != 0) {
            genericImportStatements.addElement(packageName + '.');
        }
    }

    /**
     * Returns the possible file names an identifier can represent,
     * based on this import list.
     *
     * @param ident the name of the identifier.
     *
     * @return the possible file names an identifier can represent, based on
     *         this import list.
     */
    public String[] possibleFileNames(String ident) {
        String[] possibleClassNames = null;

        if (ident.indexOf('.') != -1) {
            possibleClassNames = new String[] { ident };
        } else {
            for (int i = 0; i < specificImportStatements.size(); i++) {
                String statement = (String) specificImportStatements.elementAt(i);

                if (statement.equals(ident) || statement.endsWith("." + ident)) {
                    possibleClassNames = new String[] { statement };
                }
            }

            if (possibleClassNames == null) {
                possibleClassNames = new String[genericImportStatements.size() -
                    1];

                for (int i = 1; i < genericImportStatements.size(); i++) {
                    String statement = (String) genericImportStatements.elementAt(i);
                    statement = statement.concat(ident);
                    possibleClassNames[i - 1] = statement;
                }
            }
        }

        return possibleClassNames;
    }

    /**
     * Adds an import statement to this list.
     *
     * @param statement the statement to be added.
     */
    public void addImport(String statement) {
        int len = statement.length();

        if (statement.charAt(len - 1) == '*') {
            genericImportStatements.addElement(statement.substring(0, len - 1));
        } else {
            specificImportStatements.addElement(statement);
        }
    }

    /**
     * Returns the class that is represented by the given identifier
     * for this import list.
     *
     * @param ident the identifier that represents a class.
     *
     * @return a set of classes that can be represented by the given
     *         identifier.
     *
     * @throws ClassNotFoundException if the ident doesn't represent any class,
     *         given the import statements of this list.
     * @throws ImportException if the ident represents more than one class,
     *         given the import statements of this list.
     */
    public Class getRepresentingClass(String ident)
        throws ClassNotFoundException, ImportException {
        Class result = null;

        if (ident.indexOf('.') != -1) { // It's a fully qualified name
            result = Class.forName(ident);
        } else {
            for (int i = 0; i < specificImportStatements.size(); i++) {
                String statement = (String) specificImportStatements.elementAt(i);

                if (statement.equals(ident) || statement.endsWith("." + ident)) {
                    try {
                        Class c = Class.forName(statement);

                        if ((result == null) || result.equals(c)) {
                            result = c;
                        } else {
                            throw new ImportException("Ambiguous class: " +
                                result + " and " + c);
                        }
                    } catch (ClassNotFoundException e) {
                    }
                }
            }

            if (result == null) {
                for (int i = 0; i < genericImportStatements.size(); i++) {
                    String statement = (String) genericImportStatements.elementAt(i);
                    statement = statement.concat(ident);

                    try {
                        Class c = Class.forName(statement);

                        if ((result == null) || result.equals(c)) {
                            result = c;
                        } else {
                            throw new ImportException("Ambiguous class: " +
                                result + " and " + c);
                        }
                    } catch (ClassNotFoundException e) {
                    } catch (NoClassDefFoundError e) {
                    }
                }
            }
        }

        if (result == null) {
            throw new ClassNotFoundException(ident);
        } else {
            return result;
        }
    }
}
