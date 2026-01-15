/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

/**
 * Thrown to indicate that a specific resource (an image, a sound, etc.)
 * needed to perform the requested operation was not found (while the resource
 * was expected to be present with the distribution of this Antelmann.com
 * framework).
 *
 * @author Holger Antelmann
 *
 * @see Settings#getResource(String)
 * @since 4/6/2002
 */
public class ResourceNotFoundException extends RuntimeException {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -4741573872221219506L;

    /** DOCUMENT ME! */
    Object resource;

/**
     * Creates a new ResourceNotFoundException object.
     */
    public ResourceNotFoundException() {
        super();
    }

/**
     * Creates a new ResourceNotFoundException object.
     *
     * @param message DOCUMENT ME!
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

/**
     * Creates a new ResourceNotFoundException object.
     *
     * @param message  DOCUMENT ME!
     * @param resource DOCUMENT ME!
     */
    public ResourceNotFoundException(String message, Object resource) {
        super(message);
        this.resource = resource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getResourceNotFound() {
        return resource;
    }
}
