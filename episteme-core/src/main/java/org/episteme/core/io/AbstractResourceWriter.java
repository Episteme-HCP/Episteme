package org.episteme.core.io;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import org.episteme.core.ui.i18n.I18N;

/**
 * Abstract base for resource writers.
 * Provides basic buffering support and I18N metadata.
 */
public abstract class AbstractResourceWriter<T> implements ResourceWriter<T> {

    @Override
    public String getName() {
        return I18N.getInstance().get("io.generic.writer.name");
    }

    @Override
    public String getDescription() {
        return I18N.getInstance().get("io.generic.writer.description");
    }

    @Override
    public String getCategory() {
        return I18N.getInstance().get("io.generic.writer.category");
    }

    @Override
    public String getLongDescription() {
        return getDescription();
    }

    /**
     * Prepares an OutputStream with buffering.
     */
    protected OutputStream createBufferedStream(OutputStream out) {
        if (out instanceof BufferedOutputStream) {
            return out;
        }
        return new BufferedOutputStream(out);
    }

    // Subclasses implement save(T, String)
}

