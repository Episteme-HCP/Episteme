/*
 * AbstractTemplate.java
 *
 * Created on December 16, 2005, 9:52 PM
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
 * @author Default
 */
public abstract class AbstractTemplate {

    public static ArrayList<AbstractTemplate> getAvailableTemplates() {

        ArrayList<AbstractTemplate> templates = new ArrayList();

        templates.add(new TrussTemplate());

        return templates;

    }

    public abstract AtlasModel constructModel() throws InvalidTemplateException;

    public abstract String getName();

    /**
     * Returns a list of names for double parameters.
     */
    public abstract ArrayList<String> getDoubleNames();
}
