/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.episteme.natural.physics.loaders.fits;

import org.episteme.core.io.AbstractResourceReader;
import org.episteme.core.mathematics.linearalgebra.matrices.RealDoubleMatrix;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resource Reader for FITS (Flexible Image Transport System) files.
 * <p>
 * This reader provides integration with the Episteme Resource I/O framework,
 * allowing FITS images to be loaded directly as Matrix objects.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class FITSReader extends AbstractResourceReader<RealDoubleMatrix> {

    @Override
    public String getResourcePath() {
        return null;
    }

    @Override
    public Class<RealDoubleMatrix> getResourceType() {
        return RealDoubleMatrix.class;
    }

    @Override
    public String getName() {
        return "FITS Reader";
    }

    @Override
    public String getDescription() {
        return "Reads FITS scientific images as numerical matrices.";
    }

    @Override
    public String getLongDescription() {
        return "Standard FITS (Flexible Image Transport System) reader for astronomical and scientific data. " +
               "Automatically extracts the primary image HDU and converts it to a RealDoubleMatrix.";
    }

    @Override
    public String getCategory() {
        return "Physics/Astronomy";
    }

    @Override
    public String[] getSupportedVersions() {
        return new String[] { "4.0" };
    }

    @Override
    protected RealDoubleMatrix loadFromSource(String resourceId) throws Exception {
        Path path = Paths.get(resourceId);
        if (java.nio.file.Files.exists(path)) {
            try (FITSFile fitsFile = new FITSFile(path)) {
                fitsFile.open();
                return fitsFile.asMatrix();
            }
        }
        throw new IOException("FITS file not found: " + resourceId);
    }

    @Override
    protected RealDoubleMatrix loadFromInputStream(InputStream is, String id) throws Exception {
        File temp = File.createTempFile("episteme_fits_", ".fits");
        try {
            try (FileOutputStream fos = new FileOutputStream(temp)) {
                is.transferTo(fos);
            }
            try (FITSFile fitsFile = new FITSFile(temp.toPath())) {
                fitsFile.open();
                return fitsFile.asMatrix();
            }
        } finally {
            if (temp.exists()) {
                temp.delete();
            }
        }
    }
}

