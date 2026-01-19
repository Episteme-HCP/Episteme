package org.jscience.law.loaders;

import org.jscience.law.Statute;

/**
 * Common interface for legal document loaders.
 */
public interface LegalDocumentLoader {
    Statute load(String content);
}
