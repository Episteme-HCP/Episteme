/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */

package org.jscience.nativ.physics.loaders.fits;

import java.io.IOException;
import java.nio.file.Path;
import org.jscience.natural.physics.loaders.fits.ImageHDU;
import org.jscience.natural.physics.loaders.fits.FITSConstants;
import org.jscience.natural.physics.loaders.fits.Header;
import org.jscience.nativ.mathematics.linearalgebra.matrices.storage.NativeDoubleMatrixStorage;
import java.lang.foreign.Arena;

/**
 * Optimized version of ImageHDU that uses Panama-based native reader for zero-copy access.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.1
 */
public class OptimizedImageHDU extends ImageHDU {

    public OptimizedImageHDU(Header header) {
        super(header);
    }

    /**
     * Reads image data directly into a NativeDoubleMatrixStorage using cfitsio.
     * 
     * @param path the path to the FITS file
     * @param arena the arena for the result matrix
     * @return the native matrix containing image data
     * @throws IOException if an error occurs
     */
    public NativeDoubleMatrixStorage asNativeDoubleMatrixStorage(Path path, Arena arena) throws IOException {
        int width = header.getIntValue(FITSConstants.KEY_NAXIS + "1", 0);
        int height = header.getIntValue(FITSConstants.KEY_NAXIS + "2", 0);
        
        NativeDoubleMatrixStorage matrix = new NativeDoubleMatrixStorage(height, width, arena);
        
        try (NativeFITSReader reader = new NativeFITSReader(path)) {
            // Read first plane (assuming 2D for now)
            reader.readImage(1, (long) width * height, matrix);
        }
        
        return matrix;
    }
}





