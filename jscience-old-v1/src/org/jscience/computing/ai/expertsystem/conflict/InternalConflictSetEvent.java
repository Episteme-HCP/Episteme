package org.jscience.computing.ai.expertsystem.conflict;


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
import java.util.EventObject;


/**
 * A semantic event that indicates that an element was added to or removed
 * from a conflict set. This class is used only internally to JEOPS for
 * control purposes.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  08 Jun 2000
 */
public class InternalConflictSetEvent extends EventObject {
    /** The element that was added or removed. */
    private ConflictSetElement element;

/**
     * Class constructor.
     *
     * @param source  the conflict set to/from where the element was
     *                added/removed.
     * @param element the element that was added or removed.
     */
    public InternalConflictSetEvent(ConflictSet source,
        ConflictSetElement element) {
        super(source);
        this.element = element;
    }

    /**
     * Returns the element that was added or removed.
     *
     * @return the element that was added or removed.
     */
    public ConflictSetElement getElement() {
        return element;
    }
}
