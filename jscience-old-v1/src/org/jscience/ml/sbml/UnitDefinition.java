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

package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.List;

/**
 * A combination of {@link Unit}s to create a user-defined unit of measurement.
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Marc Vass
 * @author Nicholas Allen
 */

public final class UnitDefinition extends SBaseId {

    private final List units;
    private final SBase listElement;

    public UnitDefinition(String id) {
        this(id, null);
    }

    public UnitDefinition(String id, String name) {
        super(id, name);
        listElement = new SBase();
        units = new ArrayList();
    }

    /**
     * Root element for the unit list.
     */

    public SBase getListOfUnitsElement() {
        return listElement;
    }

    /**
     * List[Unit] of units combined in this definition.
     */

    public List getUnits() {
        return units;
    }

    public String toString() {
        StringBuffer s = new StringBuffer("<unitDefinition id=\"" + id + "\"");
        if (name != null)
            s.append(" name=\"" + name + "\" ");
        s.append(">\n");
        printList(s, units, "<listOfUnits>", "</listOfUnits>");
        s.append(super.toString());
        s.append("</unitDefinition>\n");
        return s.toString();
    }
}
