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

package org.jscience.physics.solids.gui;

import org.jscience.physics.solids.*;

import java.util.Arrays;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class AtlasTree extends JTree {
    //Tree backbone
    /**
     * DOCUMENT ME!
     */
    DefaultTreeModel dtm;

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(
            "Model Summary");

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode nodeNode = new DefaultMutableTreeNode("Nodes");

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode elemNode = new DefaultMutableTreeNode("Elements");

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode loadNode = new DefaultMutableTreeNode("Loads");

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode spcNode = new DefaultMutableTreeNode("Constraints");

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode matNode = new DefaultMutableTreeNode("Materials");

    /**
     * DOCUMENT ME!
     */
    DefaultMutableTreeNode analNode = new DefaultMutableTreeNode("Solutions");

/**
     * Creates a new instance of AtlasTree
     */
    public AtlasTree() {
        super();
        dtm = new DefaultTreeModel(rootNode);
        this.setModel(dtm);

        rootNode.add(nodeNode);
        rootNode.add(elemNode);
        rootNode.add(loadNode);
        rootNode.add(spcNode);
        rootNode.add(matNode);
        rootNode.add(analNode);
    }

    /**
     * DOCUMENT ME!
     *
     * @param model DOCUMENT ME!
     */
    public void setAtlasModel(AtlasModel model) {
        this.emptyTree();

        //Gte all of the objects in the model and stick them in the correct location
        String[] types = model.getAllObjectTypes();
        Arrays.sort(types);

        for (int i = 0; i < types.length; i++) {
            AtlasObject[] subobjs = model.getAllObjects(types[i]);

            //CHeck for bad shit- this shouldn't happen...
            if (subobjs == null) {
                continue;
            }

            if (subobjs.length < 1) {
                continue;
            }

            DefaultMutableTreeNode typeNode = new DefaultMutableTreeNode(types[i]);

            Arrays.sort(subobjs);

            for (int j = 0; j < subobjs.length; j++) {
                DefaultMutableTreeNode tn = new DefaultMutableTreeNode(subobjs[j].getId());
                //tn.setUserObject( subobjs[j] );
                typeNode.add(tn);
            }

            //Add the typeNode into the correct location
            if (subobjs[0] instanceof AtlasNode) {
                nodeNode.add(typeNode);
            }

            if (subobjs[0] instanceof AtlasElement) {
                elemNode.add(typeNode);
            }

            if (subobjs[0] instanceof AtlasLoad) {
                loadNode.add(typeNode);
            }

            if (subobjs[0] instanceof AtlasConstraint) {
                spcNode.add(typeNode);
            }

            if (subobjs[0] instanceof AtlasSolution) {
                analNode.add(typeNode);
            }

            if (subobjs[0] instanceof AtlasMaterial) {
                matNode.add(typeNode);
            }
        }

        dtm.reload();
    }

    /**
     * DOCUMENT ME!
     */
    public void emptyTree() {
        nodeNode.removeAllChildren();
        elemNode.removeAllChildren();
        loadNode.removeAllChildren();
        spcNode.removeAllChildren();
        matNode.removeAllChildren();
        analNode.removeAllChildren();

        dtm.reload();
    }
}
