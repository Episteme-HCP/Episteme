/*
 * TrussTemplate.java
 *
 * Created on December 16, 2005, 10:01 PM
 *
    Copyright (C) 2005  Brandon Wegge and Herb Smith

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */
package org.jscience.physics.solids.gui.templates;

import org.jscience.physics.solids.AtlasModel;

import java.util.ArrayList;


/**
 * 
DOCUMENT ME!
 *
 * @author Default
 */
public class TrussTemplate extends AbstractTemplate {
/**
     * Creates a new instance of TrussTemplate
     */
    public TrussTemplate() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return "Truss";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws InvalidTemplateException DOCUMENT ME!
     */
    public AtlasModel constructModel() throws InvalidTemplateException {
        AtlasModel model = new AtlasModel(getName());

        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList<String> getDoubleNames() {
        return null;
    }
}
