package org.jscience.history.archeology;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Models archaeological stratigraphy and Harris matrix relationships.
 */
public final class StratigraphyModel {

    private StratigraphyModel() {}

    public enum Relationship {
        ABOVE, BELOW, EQUAL, CUT_BY, COVERS
    }

    public record Stratum(
        String id,
        String description,
        double depthMeters,
        Map<String, Relationship> relations
    ) {}

    /**
     * Checks if a stratigraphic sequence is logically consistent.
     */
    public static boolean isSequenceConsistent(List<Stratum> sequence) {
        for (Stratum s : sequence) {
            for (Map.Entry<String, Relationship> rel : s.relations().entrySet()) {
                Relationship type = rel.getValue();
                String targetId = rel.getKey();
                
                // Find target
                Stratum target = sequence.stream().filter(st -> st.id().equals(targetId)).findFirst().orElse(null);
                if (target == null) continue;

                // Check symmetry
                if (type == Relationship.ABOVE && target.relations().get(s.id()) != Relationship.BELOW) return false;
                if (type == Relationship.BELOW && target.relations().get(s.id()) != Relationship.ABOVE) return false;
            }
        }
        return true;
    }

    /**
     * Estimates age of a stratum based on depth if no direct dating is available (simplified).
     */
    public static Real estimateAgeFromDepth(double depth, double sedimentationRateCmPerCentury) {
        double years = (depth * 100 / sedimentationRateCmPerCentury) * 100;
        return Real.of(years);
    }
}
