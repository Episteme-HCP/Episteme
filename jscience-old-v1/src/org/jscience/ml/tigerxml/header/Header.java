/*
 * Header.java
 *
 * Created on December 30, 2003, 8:57 PM
 *
 * Copyright (C) 2003 Oezguer Demir <oeze@coli.uni-sb.de>,
 *                    Vaclav Nemcik <vicky@coli.uni-sb.de>,
 *                    Hajo Keffer <hajokeffer@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jscience.ml.tigerxml.header;

import java.util.ArrayList;


/**
 * Represents a .
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84 $Id: Header.java,v 1.3 2007-10-23 18:21:42 virtualcall Exp $
 */
public class Header {
    /** DOCUMENT ME! */
    private Meta meta;

    /** DOCUMENT ME! */
    private ArrayList features;

    /** DOCUMENT ME! */
    private ArrayList edgeLabels;

    /** DOCUMENT ME! */
    private ArrayList secEdgeLabels;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXMLString() {
        return "";
    }
}
