package org.jscience.architecture;

import org.jscience.mathematics.numbers.real.Real;
import org.jscience.data.UniversalDataModel;
import java.util.*;

/**
 * Universal data model for architectural constraints and simulations.
 */
public final class ArchitecturalModel implements UniversalDataModel {

    @Override
    public String getModelType() { return "ARCHITECTURAL_CONSTRAINTS"; }

    public record Ray(Real x1, Real y1, Real x2, Real y2, String type) {}
    public record VectorField(Real x, Real y, Real dx, Real dy, Real magnitude) {}

    private final List<Ray> rays = new ArrayList<>();
    private final List<VectorField> loadPaths = new ArrayList<>();
    
    public void addRay(Real x1, Real y1, Real x2, Real y2, String type) {
        rays.add(new Ray(x1, y1, x2, y2, type));
    }

    public void addLoadPath(Real x, Real y, Real dx, Real dy, Real magnitude) {
        loadPaths.add(new VectorField(x, y, dx, dy, magnitude));
    }

    public List<Ray> getRays() { return Collections.unmodifiableList(rays); }
    public List<VectorField> getLoadPaths() { return Collections.unmodifiableList(loadPaths); }
}
