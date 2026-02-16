package org.jscience.core.ui.viewers.mathematics.analysis.plotting.backends;

import org.jscience.core.technical.backend.Backend;
import org.jscience.core.ui.viewers.mathematics.analysis.plotting.PlottingBackend;

/**
 * Service provider interface for plotting backend providers.
 * <p>
 * Extends {@link Backend} to integrate with the standard backend discovery system.
 * </p>
 */
public interface PlottingBackendProvider extends Backend {

    @Override
    default String getType() {
        return "plotting";
    }

    /**
     * Creates the backing {@link PlottingBackend} instance.
     */
    @Override
    PlottingBackend createBackend();
}
