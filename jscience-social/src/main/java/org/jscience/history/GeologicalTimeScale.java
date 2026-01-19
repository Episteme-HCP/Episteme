package org.jscience.history;


/**
 * Geological Time Scale (ICS).
 * Official divisions of geological time.
 * Dates are approximate (Ma = Million years ago).
 */
public enum GeologicalTimeScale {
    
    // Eons
    HADEAN("Hadean", 4600, 4000),
    ARCHEAN("Archean", 4000, 2500),
    PROTEROZOIC("Proterozoic", 2500, 541),
    PHANEROZOIC("Phanerozoic", 541, 0),

    // Eras (Phanerozoic)
    PALEOZOIC("Paleozoic", 541, 252),
    MESOZOIC("Mesozoic", 252, 66),
    CENOZOIC("Cenozoic", 66, 0),

    // Periods (Mesozoic/Cenozoic subset for brevity/example, can expand)
    TRIASSIC("Triassic", 252, 201),
    JURASSIC("Jurassic", 201, 145),
    CRETACEOUS("Cretaceous", 145, 66),
    PALEOGENE("Paleogene", 66, 23),
    NEOGENE("Neogene", 23, 2.58),
    QUATERNARY("Quaternary", 2.58, 0), // Includes Pleistocene/Holocene

    // Epochs
    PLEISTOCENE("Pleistocene", 2.58, 0.0117),
    HOLOCENE("Holocene", 0.0117, 0);

    private final String name;
    private final double startMa; // Million Years Ago
    private final double endMa; // Million Years Ago

    GeologicalTimeScale(String name, double startMa, double endMa) {
        this.name = name;
        this.startMa = startMa;
        this.endMa = endMa;
    }

    public String getName() {
        return name;
    }

    public double getStartMa() {
        return startMa;
    }

    public double getEndMa() {
        return endMa;
    }
    
    public double getStartYear() {
        return startMa * 1_000_000;
    }
    
    public double getEndYear() {
        return endMa * 1_000_000;
    }

    /**
     * Checks if a date (in years ago) falls within this period.
     * @param yearsAgo Years ago (positive).
     */
    public boolean contains(double yearsAgo) {
        return yearsAgo <= (startMa * 1_000_000) && yearsAgo >= (endMa * 1_000_000);
    }
}
