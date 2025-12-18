package org.jscience.physics;

import org.jscience.bibliography.Citation;
import org.jscience.bibliography.SimpleCitation;
import org.jscience.bibliography.Standard;

public class CODATA implements Standard {

    public static final CODATA V2022 = new CODATA("2022");

    private final String version;
    private final Citation citation;

    public CODATA(String version) {
        this.version = version;
        this.citation = new SimpleCitation(
                "CODATA" + version,
                "CODATA Recommended Values of the Fundamental Physical Constants: " + version,
                "Tiesinga et al.",
                "2022", // Approximate year of publication
                "10.1103/RevModPhys.93.025010" // Example DOI (check specific version)
        );
    }

    @Override
    public String getName() {
        return "CODATA";
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public Citation getCitation() {
        return citation;
    }
}
