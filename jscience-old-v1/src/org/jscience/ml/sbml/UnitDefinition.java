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
