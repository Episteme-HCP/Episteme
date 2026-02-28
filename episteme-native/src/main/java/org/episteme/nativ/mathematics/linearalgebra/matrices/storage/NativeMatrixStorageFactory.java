/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.episteme.nativ.mathematics.linearalgebra.matrices.storage;

import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorage;
import org.episteme.core.mathematics.linearalgebra.matrices.storage.MatrixStorageFactory;
import org.episteme.core.mathematics.structures.rings.Ring;
import com.google.auto.service.AutoService;

/**
 * Native implementation of MatrixStorageFactory.
 * Creates {@link NativeDoubleMatrixStorage} instances for Real numbers.
 */
@AutoService(MatrixStorageFactory.class)
public class NativeMatrixStorageFactory implements MatrixStorageFactory {

    @Override
    public <E> MatrixStorage<E> createDense(int rows, int cols, Ring<E> ring) {
        // NativeMatrix only supports Real (double)
        E zero = ring.zero();
        if (zero instanceof org.episteme.core.mathematics.numbers.real.Real) {
            @SuppressWarnings("unchecked")
            MatrixStorage<E> storage = (MatrixStorage<E>) new NativeDoubleMatrixStorage(rows, cols);
            return storage;
        }
        
        // Fallback for non-Real types? 
        // Or return null to indicate "cannot handle"?
        // For now, let's return null if we can't handle it, and let MatrixFactory fallback.
        return null;
    }

    @Override
    public int getPriority() {
        return 100; // High priority for Native
    }
}
