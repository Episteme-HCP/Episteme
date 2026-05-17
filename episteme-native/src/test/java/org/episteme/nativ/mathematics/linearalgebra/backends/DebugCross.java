package org.episteme.nativ.mathematics.linearalgebra.backends;

import org.episteme.core.mathematics.numbers.complex.Complex;
import org.episteme.core.mathematics.linearalgebra.Vector;
import org.episteme.core.mathematics.sets.Complexes;
import java.util.Arrays;

public class DebugCross {
    public static void main(String[] args) {
        try (NativeSIMDComplexBackend backend = new NativeSIMDComplexBackend()) {
            Complex[] va = {Complex.of(1, 0), Complex.of(0, 1), Complex.of(0, 0)};
            Complex[] vb = {Complex.of(0, 0), Complex.of(1, 0), Complex.of(0, 1)};
            
            Vector<Complex> a = Vector.of(Arrays.asList(va), Complexes.getInstance());
            Vector<Complex> b = Vector.of(Arrays.asList(vb), Complexes.getInstance());
            
            System.out.println("Testing cross product...");
            Vector<Complex> res = backend.cross(a, b);
            System.out.println("Result: " + res);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
