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
import org.jscience.computing.ai.expertsystem.conflict.ConflictSetElement;

import java.util.EventObject;


/**
 * A semantic event that indicates that an element was either added to or
 * removed from a conflict set.
 *
 * @author Carlos Figueira Filho (<a
 *         href="mailto:csff@cin.ufpe.br">csff@cin.ufpe.br</a>)
 * @version 0.01  08 Jun 2000
 */
public class ConflictSetEvent extends EventObject {
    /** The element that was added/removed. */
    private ConflictSetElement element;

/**
     * Class constructor.
     *
     * @param source  the knowledge base that added or removed the element.
     * @param element the element that was added or removed.
     */
    public ConflictSetEvent(AbstractKnowledgeBase source,
        ConflictSetElement element) {
        super(source);
        this.element = element;
    }

    /**
     * Returns the element that was added/removed.
     *
     * @return the element that was added/removed.
     */
    public ConflictSetElement getElement() {
        return element;
    }

    /**
     * Returns the knowledge base that added or removed the element.
     *
     * @return the knowledge base that added or removed the element.
     */
    public AbstractKnowledgeBase getKnowledgeBase() {
        return (AbstractKnowledgeBase) super.getSource();
    }
}
