package org.jscience.medicine.anthropometry;

import org.jscience.measure.Quantities;
import org.jscience.measure.Quantity;
import org.jscience.measure.Units;
import org.jscience.measure.quantity.Length;
import org.jscience.measure.quantity.Mass;

/**
 * Body Mass Index and Body Surface Area calculators.
 */
public class BodyMetrics {

    private BodyMetrics() {
    }

    /**
     * Calculates BMI.
     * BMI = mass (kg) / height² (m²)
     */
    public static double bmi(Quantity<Mass> mass, Quantity<Length> height) {
        double m = mass.to(Units.KILOGRAM).getValue().doubleValue();
        double h = height.to(Units.METER).getValue().doubleValue();
        return m / (h * h);
    }

    /**
     * BMI category.
     */
    public static String bmiCategory(double bmi) {
        if (bmi < 18.5)
            return "Underweight";
        if (bmi < 25)
            return "Normal";
        if (bmi < 30)
            return "Overweight";
        return "Obese";
    }

    /**
     * Du Bois formula for Body Surface Area.
     * BSA = 0.007184 * weight^0.425 * height^0.725
     * 
     * @return BSA in m²
     */
    public static double bsaDuBois(Quantity<Mass> mass, Quantity<Length> height) {
        double w = mass.to(Units.KILOGRAM).getValue().doubleValue();
        double h = height.to(Units.CENTIMETER).getValue().doubleValue();
        return 0.007184 * Math.pow(w, 0.425) * Math.pow(h, 0.725);
    }

    /**
     * Mosteller formula for BSA (simpler).
     * BSA = sqrt(weight * height / 3600)
     */
    public static double bsaMosteller(Quantity<Mass> mass, Quantity<Length> height) {
        double w = mass.to(Units.KILOGRAM).getValue().doubleValue();
        double h = height.to(Units.CENTIMETER).getValue().doubleValue();
        return Math.sqrt(w * h / 3600);
    }

    /**
     * Ideal Body Weight (Devine formula, male).
     * IBW = 50 + 2.3 * (height_inches - 60)
     */
    public static Quantity<Mass> idealBodyWeightMale(Quantity<Length> height) {
        double cm = height.to(Units.CENTIMETER).getValue().doubleValue();
        double inches = cm / 2.54;
        double ibw = 50 + 2.3 * (inches - 60);
        return Quantities.create(ibw, Units.KILOGRAM);
    }

    /**
     * Ideal Body Weight (Devine formula, female).
     * IBW = 45.5 + 2.3 * (height_inches - 60)
     */
    public static Quantity<Mass> idealBodyWeightFemale(Quantity<Length> height) {
        double cm = height.to(Units.CENTIMETER).getValue().doubleValue();
        double inches = cm / 2.54;
        double ibw = 45.5 + 2.3 * (inches - 60);
        return Quantities.create(ibw, Units.KILOGRAM);
    }
}
