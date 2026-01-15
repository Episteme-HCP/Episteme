/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/

/*
 * TargteDefinitionTable.java
 *
 * Created on December 17, 2001, 2:30 PM
 */
package org.jscience.physics.nuclear.kinematics.math.analysis.spanc.tables;

import org.jscience.physics.nuclear.kinematics.math.analysis.spanc.Target;
import org.jscience.physics.nuclear.kinematics.nuclear.NuclearException;


/**
 * 
DOCUMENT ME!
 *
 * @author Dale
 */
public class TargetDefinitionTable extends javax.swing.JTable {
/**
     * Creates new TargteDefinitionTable
     */
    public TargetDefinitionTable(Target target) {
        super(new TargetDefinitionTableModel(target));
    }

    /**
     * Creates a new TargetDefinitionTable object.
     */
    public TargetDefinitionTable() {
        super(new TargetDefinitionTableModel());
    }

    /**
     * Returns a Target object constructed from entries in the table.
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public Target makeTarget(String name) throws NuclearException {
        return ((TargetDefinitionTableModel) getModel()).makeTarget(name);
    }

    /**
     * DOCUMENT ME!
     */
    public void addRow() {
        ((TargetDefinitionTableModel) getModel()).addRow();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void removeRow(int row) {
        ((TargetDefinitionTableModel) getModel()).removeLayer(row);
    }
}
