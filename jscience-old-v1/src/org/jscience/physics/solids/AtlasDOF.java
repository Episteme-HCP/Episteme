/*
 * Created on Jan 12, 2005
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
public class AtlasDOF extends AtlasObject {

    protected static String TYPE = "DOF";

    public AtlasNode node;
    public int dof;

    public AtlasDOF(AtlasNode node, int dof) {
        this.node = node;
        this.dof = dof;
    }

    public String toString() {
        return "N" + node.getId() + ":" + dof;
    }

    public boolean equals(Object rhs) {
        return this.toString().equals(rhs.toString());
    }

    public String getType() {
        return TYPE;
    }
}
