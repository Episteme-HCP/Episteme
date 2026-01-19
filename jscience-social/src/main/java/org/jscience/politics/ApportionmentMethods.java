package org.jscience.politics;

import org.jscience.mathematics.numbers.real.Real;
import java.util.*;

/**
 * Electoral seat apportionment methods.
 */
public final class ApportionmentMethods {

    private ApportionmentMethods() {}

    public record Party(String name, long votes) {}
    
    public record ApportionmentResult(
        Map<String, Integer> seats,
        String method,
        double gallagherIndex  // Disproportionality measure
    ) {}

    /**
     * D'Hondt (Jefferson) method - favors larger parties.
     */
    public static ApportionmentResult dHondt(List<Party> parties, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        parties.forEach(p -> seats.put(p.name(), 0));
        
        for (int i = 0; i < totalSeats; i++) {
            String winner = null;
            double maxQuotient = -1;
            
            for (Party party : parties) {
                double quotient = (double) party.votes() / (seats.get(party.name()) + 1);
                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party.name();
                }
            }
            
            if (winner != null) {
                seats.merge(winner, 1, (a, b) -> a + b);
            }
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "D'Hondt", gallagher);
    }

    /**
     * Sainte-Laguë (Webster) method - more proportional.
     */
    public static ApportionmentResult sainteLague(List<Party> parties, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        parties.forEach(p -> seats.put(p.name(), 0));
        
        for (int i = 0; i < totalSeats; i++) {
            String winner = null;
            double maxQuotient = -1;
            
            for (Party party : parties) {
                // Divisor is 2n+1: 1, 3, 5, 7, ...
                double divisor = 2 * seats.get(party.name()) + 1;
                double quotient = (double) party.votes() / divisor;
                if (quotient > maxQuotient) {
                    maxQuotient = quotient;
                    winner = party.name();
                }
            }
            
            if (winner != null) {
                seats.merge(winner, 1, (a, b) -> a + b);
            }
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "Sainte-Laguë", gallagher);
    }

    /**
     * Hamilton (Largest Remainder) method with Hare quota.
     */
    public static ApportionmentResult hamilton(List<Party> parties, int totalSeats) {
        Map<String, Integer> seats = new HashMap<>();
        long totalVotes = parties.stream().mapToLong(Party::votes).sum();
        double quota = (double) totalVotes / totalSeats;
        
        Map<String, Double> remainders = new HashMap<>();
        int seatsAssigned = 0;
        
        // First allocation: floor of quota
        for (Party party : parties) {
            double exactSeats = party.votes() / quota;
            int floorSeats = (int) exactSeats;
            seats.put(party.name(), floorSeats);
            remainders.put(party.name(), exactSeats - floorSeats);
            seatsAssigned += floorSeats;
        }
        
        // Distribute remaining seats by largest remainder
        List<Party> sortedByRemainder = new ArrayList<>(parties);
        sortedByRemainder.sort((a, b) -> 
            Double.compare(remainders.get(b.name()), remainders.get(a.name())));
        
        for (int i = 0; i < totalSeats - seatsAssigned && i < sortedByRemainder.size(); i++) {
            seats.merge(sortedByRemainder.get(i).name(), 1, (a, b) -> a + b);
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "Hamilton", gallagher);
    }

    /**
     * Huntington-Hill method (used in US House apportionment).
     */
    public static ApportionmentResult huntingtonHill(List<Party> parties, int totalSeats,
            int minimumPerParty) {
        
        Map<String, Integer> seats = new HashMap<>();
        
        // Give each party the minimum
        int remaining = totalSeats;
        for (Party party : parties) {
            seats.put(party.name(), minimumPerParty);
            remaining -= minimumPerParty;
        }
        
        // Distribute remaining seats
        for (int i = 0; i < remaining; i++) {
            String winner = null;
            double maxPriority = -1;
            
            for (Party party : parties) {
                int n = seats.get(party.name());
                // Geometric mean divisor
                double divisor = Math.sqrt((double) n * (n + 1));
                double priority = party.votes() / divisor;
                
                if (priority > maxPriority) {
                    maxPriority = priority;
                    winner = party.name();
                }
            }
            
            if (winner != null) {
                seats.merge(winner, 1, (a, b) -> a + b);
            }
        }
        
        double gallagher = calculateGallagher(parties, seats, totalSeats);
        return new ApportionmentResult(seats, "Huntington-Hill", gallagher);
    }

    /**
     * Compares all methods for given vote data.
     */
    public static Map<String, ApportionmentResult> compareAllMethods(List<Party> parties, 
            int totalSeats) {
        
        Map<String, ApportionmentResult> results = new LinkedHashMap<>();
        results.put("D'Hondt", dHondt(parties, totalSeats));
        results.put("Sainte-Laguë", sainteLague(parties, totalSeats));
        results.put("Hamilton", hamilton(parties, totalSeats));
        results.put("Huntington-Hill", huntingtonHill(parties, totalSeats, 0));
        
        return results;
    }

    /**
     * Calculates Gallagher index of disproportionality.
     * Lower is more proportional; typical range 0-15.
     */
    public static double calculateGallagher(List<Party> parties, Map<String, Integer> seats,
            int totalSeats) {
        
        long totalVotes = parties.stream().mapToLong(Party::votes).sum();
        double sumSquares = 0;
        
        for (Party party : parties) {
            double voteShare = (double) party.votes() / totalVotes * 100;
            double seatShare = (double) seats.getOrDefault(party.name(), 0) / totalSeats * 100;
            sumSquares += Math.pow(voteShare - seatShare, 2);
        }
        
        return Math.sqrt(sumSquares / 2);
    }

    /**
     * Checks for threshold effect (minimum vote % to win seat).
     */
    public static Real effectiveThreshold(int totalSeats) {
        // Approximate: 0.5 / (totalSeats + 1) for D'Hondt
        return Real.of(50.0 / (totalSeats + 1));
    }
}
