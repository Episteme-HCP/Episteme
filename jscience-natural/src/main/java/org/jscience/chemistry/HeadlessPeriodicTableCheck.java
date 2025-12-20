package org.jscience.chemistry;

public class HeadlessPeriodicTableCheck {
    public static void main(String[] args) {
        System.out.println("Starting PeriodicTable Check...");
        int count = PeriodicTable.getElementCount();
        System.out.println("Total Elements Loaded: " + count);

        Element e103 = PeriodicTable.getElement("Lawrencium");
        System.out.println("Element 103 (Lr): " + (e103 != null ? "Found" : "Missing"));

        Element e104 = PeriodicTable.getElement("Rutherfordium");
        System.out.println("Element 104 (Rf): " + (e104 != null ? "Found" : "Missing"));

        Element e118 = PeriodicTable.getElement("Oganesson");
        System.out.println("Element 118 (Og): " + (e118 != null ? "Found" : "Missing"));

        if (count >= 118 && e104 != null && e118 != null) {
            System.out.println("SUCCESS: Periodic Table limit fixed.");
        } else {
            System.out.println("FAILURE: Elements missing.");
        }
    }
}
