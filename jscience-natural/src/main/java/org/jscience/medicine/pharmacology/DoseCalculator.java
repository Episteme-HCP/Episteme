package org.jscience.medicine.pharmacology;

import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Mass;

/**
 * Weight-based medication dose calculator.
 */
public class DoseCalculator {

    private DoseCalculator() {
    }

    /**
     * Calculates dose based on weight.
     * dose = dosePerKg * weight
     * 
     * @param dosePerKg Dose per kg (mg/kg)
     * @param weight    Patient weight
     * @return Total dose in mg
     */
    public static double weightBasedDose(double dosePerKg, Quantity<Mass> weight) {
        double kg = weight.to(Units.KILOGRAM).getValue().doubleValue();
        return dosePerKg * kg;
    }

    /**
     * Calculates dose based on BSA.
     * dose = dosePerM2 * BSA
     * 
     * @param dosePerM2 Dose per m² (mg/m²)
     * @param bsa       Body surface area in m²
     * @return Total dose in mg
     */
    public static double bsaBasedDose(double dosePerM2, double bsa) {
        return dosePerM2 * bsa;
    }

    /**
     * Calculates IV drip rate.
     * rate = (dose_mg * volume_ml) / (concentration_mg_per_ml * time_hours)
     * 
     * @return ml/hour
     */
    public static double ivDripRate(double totalDoseMg, double concentrationMgPerMl, double infusionHours) {
        double volumeNeeded = totalDoseMg / concentrationMgPerMl;
        return volumeNeeded / infusionHours;
    }

    /**
     * Pediatric dose using Clark's rule.
     * Child dose = (weight_lbs / 150) * adult_dose
     */
    public static double clarksRule(double adultDoseMg, Quantity<Mass> childWeight) {
        double lbs = childWeight.to(Units.KILOGRAM).getValue().doubleValue() * 2.205;
        return (lbs / 150.0) * adultDoseMg;
    }

    /**
     * Pediatric dose using Young's rule.
     * Child dose = (age / (age + 12)) * adult_dose
     */
    public static double youngsRule(double adultDoseMg, int ageYears) {
        return (ageYears / (double) (ageYears + 12)) * adultDoseMg;
    }

    /**
     * Creatinine clearance (Cockcroft-Gault).
     * CrCl = ((140 - age) * weight * [0.85 if female]) / (72 * serum_creatinine)
     */
    public static double creatinineClearance(int ageYears, Quantity<Mass> weight,
            double serumCreatinineMgDl, boolean isFemale) {
        double kg = weight.to(Units.KILOGRAM).getValue().doubleValue();
        double result = ((140 - ageYears) * kg) / (72 * serumCreatinineMgDl);
        return isFemale ? result * 0.85 : result;
    }
}
