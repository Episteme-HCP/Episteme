/*
 * HeaderElement.java
 *
 * Created on December 31, 2003, 8:54 PM
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

/**
 * Represents a .
 *
 * @author <a href="mailto:oeze@coli.uni-sb.de">Oezguer Demir</a>
 * @version 1.84 $Id: HeaderElement.java,v 1.1 2006-09-07 21:51:37 virtualcall Exp $
 */
public interface HeaderElement {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toXMLString();
}
