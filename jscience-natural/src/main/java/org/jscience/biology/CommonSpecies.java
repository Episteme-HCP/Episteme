/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 */
package org.jscience.biology;

import java.util.*;
import org.jscience.biology.taxonomy.Species;

/**
 * Common biological species with predefined data.
 * <p>
 * Provides factory methods for frequently used species in biology.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
public final class CommonSpecies {

    private CommonSpecies() {
    } // Utility class

    // ========== Model Organisms ==========

    public static Species escherichiaColiK12() {
        Species s = new Species("E. coli K-12", "Escherichia coli");
        s.setKingdom("Bacteria");
        s.setPhylum("Pseudomonadota");
        s.setTaxonomicClass("Gammaproteobacteria");
        s.setOrder("Enterobacterales");
        s.setFamily("Enterobacteriaceae");
        s.setGenus("Escherichia");
        s.setConservationStatus(Species.ConservationStatus.NOT_EVALUATED);
        s.addAttribute("genome_size_bp", "4600000");
        return s;
    }

    public static Species saccharomycesCerevisiae() {
        Species s = new Species("Baker's Yeast", "Saccharomyces cerevisiae");
        s.setKingdom("Fungi");
        s.setPhylum("Ascomycota");
        s.setTaxonomicClass("Saccharomycetes");
        s.setOrder("Saccharomycetales");
        s.setFamily("Saccharomycetaceae");
        s.setGenus("Saccharomyces");
        s.addAttribute("chromosomes", "16");
        return s;
    }

    public static Species drosophilaMelanogaster() {
        Species s = new Species("Fruit Fly", "Drosophila melanogaster");
        s.setKingdom("Animalia");
        s.setPhylum("Arthropoda");
        s.setTaxonomicClass("Insecta");
        s.setOrder("Diptera");
        s.setFamily("Drosophilidae");
        s.setGenus("Drosophila");
        s.addAttribute("chromosomes", "8");
        s.addAttribute("genome_size_bp", "180000000");
        return s;
    }

    public static Species musMusculus() {
        Species s = new Species("House Mouse", "Mus musculus");
        s.setKingdom("Animalia");
        s.setPhylum("Chordata");
        s.setTaxonomicClass("Mammalia");
        s.setOrder("Rodentia");
        s.setFamily("Muridae");
        s.setGenus("Mus");
        s.addAttribute("chromosomes", "40");
        s.setConservationStatus(Species.ConservationStatus.LEAST_CONCERN);
        return s;
    }

    public static Species arabidopsisThaliana() {
        Species s = new Species("Thale Cress", "Arabidopsis thaliana");
        s.setKingdom("Plantae");
        s.setPhylum("Tracheophyta");
        s.setTaxonomicClass("Magnoliopsida");
        s.setOrder("Brassicales");
        s.setFamily("Brassicaceae");
        s.setGenus("Arabidopsis");
        s.addAttribute("chromosomes", "10");
        return s;
    }

    public static Species caenorhabditisElegans() {
        Species s = new Species("Roundworm", "Caenorhabditis elegans");
        s.setKingdom("Animalia");
        s.setPhylum("Nematoda");
        s.setTaxonomicClass("Chromadorea");
        s.setOrder("Rhabditida");
        s.setFamily("Rhabditidae");
        s.setGenus("Caenorhabditis");
        s.addAttribute("chromosomes", "12");
        s.addAttribute("neurons", "302");
        return s;
    }

    public static Species danioRerio() {
        Species s = new Species("Zebrafish", "Danio rerio");
        s.setKingdom("Animalia");
        s.setPhylum("Chordata");
        s.setTaxonomicClass("Actinopterygii");
        s.setOrder("Cypriniformes");
        s.setFamily("Cyprinidae");
        s.setGenus("Danio");
        s.addAttribute("chromosomes", "50");
        s.setConservationStatus(Species.ConservationStatus.LEAST_CONCERN);
        return s;
    }

    // ========== Common Animals ==========

    public static Species panTroglodytes() {
        Species s = new Species("Chimpanzee", "Pan troglodytes");
        s.setKingdom("Animalia");
        s.setPhylum("Chordata");
        s.setTaxonomicClass("Mammalia");
        s.setOrder("Primates");
        s.setFamily("Hominidae");
        s.setGenus("Pan");
        s.addAttribute("chromosomes", "48");
        s.setConservationStatus(Species.ConservationStatus.ENDANGERED);
        return s;
    }

    public static Species canisLupusFamiliaris() {
        Species s = new Species("Domestic Dog", "Canis lupus familiaris");
        s.setKingdom("Animalia");
        s.setPhylum("Chordata");
        s.setTaxonomicClass("Mammalia");
        s.setOrder("Carnivora");
        s.setFamily("Canidae");
        s.setGenus("Canis");
        s.addAttribute("chromosomes", "78");
        return s;
    }

    public static Species felisCatus() {
        Species s = new Species("Domestic Cat", "Felis catus");
        s.setKingdom("Animalia");
        s.setPhylum("Chordata");
        s.setTaxonomicClass("Mammalia");
        s.setOrder("Carnivora");
        s.setFamily("Felidae");
        s.setGenus("Felis");
        s.addAttribute("chromosomes", "38");
        return s;
    }

    /**
     * Returns list of all common model organisms.
     */
    public static List<Species> modelOrganisms() {
        return List.of(
                escherichiaColiK12(),
                saccharomycesCerevisiae(),
                drosophilaMelanogaster(),
                musMusculus(),
                arabidopsisThaliana(),
                caenorhabditisElegans(),
                danioRerio());
    }
}
