package org.jscience.computing.ai.expertsystem;


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
/**
 * The listener interface for receiving conflict set events. The class
 * that is interested in acting in response to some element being added
 * to or removed from the conflict set should implement this interface,
 * and the object created with this class is registered with a rule base,
 * using the <code>addConflictSetListener</code> method. When an element
 * is inserted into the conflict set, that object's
 * <code>elementAdded</code> method is invoked; when an element is
 * removed from the conflict set, that object's
 * <code>elementRemoved</code> method is invoked.
 *
 * @author Carlos Figueira Filho (<a href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  08 Jun 2000
 */
public interface ConflictSetListener {
    /**
     * Invoked when an element is added to the conflict set.
     *
     * @param e the conflict set event.
     */
    public void elementAdded(ConflictSetEvent e);

    /**
     * Invoked when an element is removed from the conflict set.
     *
     * @param e the conflict set event.
     */
    public void elementRemoved(ConflictSetEvent e);
}
