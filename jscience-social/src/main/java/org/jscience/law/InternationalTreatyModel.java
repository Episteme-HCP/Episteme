package org.jscience.law;

import java.util.*;

/**
 * Models international treaties and their ratification status.
 */
public final class InternationalTreatyModel {

    private InternationalTreatyModel() {}

    public enum RatificationStatus {
        SIGNED, RATIFIED, ACCEDED, NOT_A_PARTY
    }

    public record Treaty(
        String name,
        String abbreviation,
        int yearAdopted,
        Map<String, RatificationStatus> partyStatus
    ) {}

    /**
     * Checks if a country is bound by a treaty.
     */
    public static boolean isBound(Treaty treaty, String countryCode) {
        RatificationStatus status = treaty.partyStatus().getOrDefault(countryCode, RatificationStatus.NOT_A_PARTY);
        return status == RatificationStatus.RATIFIED || status == RatificationStatus.ACCEDED;
    }

    /**
     * Calculates the "Global Adoption Rate" of a treaty.
     */
    public static double adoptionRate(Treaty treaty, int totalCountries) {
        long count = treaty.partyStatus().values().stream()
            .filter(s -> s == RatificationStatus.RATIFIED || s == RatificationStatus.ACCEDED)
            .count();
        return (double) count / totalCountries;
    }

    /**
     * Simulates the "Universal Declaration of Human Rights" (as a model).
     */
    public static Treaty udhr() {
        return new Treaty("Universal Declaration of Human Rights", "UDHR", 1948, new HashMap<>());
    }
}
