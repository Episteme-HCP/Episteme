/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.biology;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A class representing a cell.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//represents only the very basic structure, that is the kernel
//no functional systems like mitochondries or processes like breathing
//A bit of information for newcomers:
//The Nucleus: Information Center for the Cell. The nucleus of a eukaryotic cell isolates the cell's DNA.
//The Endoplasmic Reticulum: Compartmentalizing the Cell. An extensive system of membranes subdivides the cell interior.
//The Golgi Apparatus: Delivery System of the Cell. A system of membrane channels collects, modifies, packages,
//and distributes molecules within the cell.
//Vesicles: Enzyme Storehouses. Sacs that contain enzymes digest or modify particles in the cell, while other vesicles
//transport substances in and out of cells.
//Ribosomes: Sites of Protein Synthesis. An RNA-protein complex directs the production of proteins.
//Organelles contain DNA. Some organelles with very different functions contain their own DNA.
//The Cytoskeleton: Interior Framework of the Cell. A network of protein fibers supports the shape of the cell and anchors organelles.
//Cell Movement: Eukaryotic cell movement utilizes cytoskeletal elements.
//we could add support for neighbors (as cells are part of tissues)
public class Cell extends Object implements Cloneable {
    /** The prokaryote constant. */
    public static final int PROKARYOTE = 0;

    /** The animal constant. */
    public static final int ANIMAL = 1; //1<<0;//EURKARYOTE cell

    /** The vegetal constant. */
    public static final int VEGETAL = 2; //1<<1;//EURKARYOTE cell

    //mutually exclusive
    /** DOCUMENT ME! */
    public static final int GROWTH_0 = 0; //standard state

    /** DOCUMENT ME! */
    public static final int GROWTH_1 = 1; //interphase, growth phase 1

    /** DOCUMENT ME! */
    public static final int SYNTHESIS = 2; //interphase, synthesis

    /** DOCUMENT ME! */
    public static final int GROWTH_2 = 3; //interphase, growth phase 1

    /** DOCUMENT ME! */
    public static final int PROPHASE = 4; //mitosis

    /** DOCUMENT ME! */
    public static final int METAPHASE = 5; //mitosis

    /** DOCUMENT ME! */
    public static final int ANAPHASE = 6; //mitosis

    /** DOCUMENT ME! */
    public static final int TELOPHASE = 7; //mitosis

    /** DOCUMENT ME! */
    public static final int CYTOKINESIS = 8;

    /** DOCUMENT ME! */
    private static final int LAST_STAGE = CYTOKINESIS;

    //cell kind
    /** DOCUMENT ME! */
    public static final int UNDIFFERENCIATED_CELL = 0; //normal

    /** DOCUMENT ME! */
    public static final int ABNORMAL_CELL = 1; //cancerous

    /** DOCUMENT ME! */
    public static final int BLOOD_CELL = 2;

    /** DOCUMENT ME! */
    public static final int NERVE_CELL = 3;

    /** DOCUMENT ME! */
    public static final int LIVER_CELL = 4;

    /** DOCUMENT ME! */
    public static final int SEX_CELL = 5;

    /** DOCUMENT ME! */
    public static final int ANTIBODY_CELL = 6;

    /** DOCUMENT ME! */
    private int type;

    /** DOCUMENT ME! */
    private int stage;

    /** DOCUMENT ME! */
    private Set neighbors;

    /** The Genome. */
    private Genome genome;

    /** The kind. */
    private int kind;

    /** DOCUMENT ME! */
    private Alphabet coding;

/**
     * Constructs a an undifferenciated animal Cell at interphase.
     *
     * @param genome DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Cell(Genome genome) {
        if (genome != null) {
            this.genome = genome;
            this.type = ANIMAL;
            this.kind = UNDIFFERENCIATED_CELL;
            this.coding = Codons.STANDARD;
            this.stage = GROWTH_0;
            this.neighbors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Cell constructor can't have null arguments.");
        }
    }

/**
     * Creates a new Cell object.
     *
     * @param genome DOCUMENT ME!
     * @param type   DOCUMENT ME!
     * @param kind   DOCUMENT ME!
     * @param coding DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Cell(Genome genome, int type, int kind, Alphabet coding) {
        if ((genome != null) && (coding != null)) {
            this.genome = genome;
            this.type = type;
            this.kind = kind;
            this.coding = coding;
            this.stage = GROWTH_0;
            this.neighbors = Collections.EMPTY_SET;
        } else {
            throw new IllegalArgumentException(
                "The Cell constructor can't have null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Genome getGenome() {
        return genome;
    }

    /**
     * DOCUMENT ME!
     *
     * @param genome DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    private void setGenome(Genome genome) {
        if (genome != null) {
            this.genome = genome;
        } else {
            throw new IllegalArgumentException("You can't set a null genome.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getKind() {
        return kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @param kind DOCUMENT ME!
     */
    public void setKind(int kind) {
        this.kind = kind;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Alphabet getCoding() {
        return coding;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coding DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setCoding(Alphabet coding) {
        if (coding != null) {
            this.coding = coding;
        } else {
            throw new IllegalArgumentException("You can't set a null coding.");
        }
    }

    //the current stage of the cell
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStage() {
        return stage;
    }

    /**
     * DOCUMENT ME!
     */
    public void setStage() {
        this.stage = stage;
    }

    //goes to the next stage
    /**
     * DOCUMENT ME!
     */
    public void nextStage() {
        if (stage < LAST_STAGE) {
            stage += 1;
        } else {
            stage = GROWTH_0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getNeighbors() {
        return neighbors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void addNeighbor(Cell neighbor) {
        if (neighbor != null) {
            neighbors.add(neighbor);
            neighbor.addRemoteNeighbor(this);
        } else {
            throw new IllegalArgumentException("You can't add a null neighbor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     */
    private void addRemoteNeighbor(Cell neighbor) {
        neighbors.add(neighbor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void removeNeighbor(Cell neighbor) {
        if (neighbor != null) {
            neighbors.remove(neighbor);
            neighbor.removeRemoteNeighbor(this);
        } else {
            throw new IllegalArgumentException(
                "You can't remove a null neighbor.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbor DOCUMENT ME!
     */
    private void removeRemoteNeighbor(Cell neighbor) {
        neighbors.remove(neighbor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param neighbors ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public void setNeighbors(Set neighbors) {
        Iterator iterator;
        boolean valid;

        if (neighbors != null) {
            iterator = neighbors.iterator();
            valid = true;

            while (iterator.hasNext() && valid) {
                valid = iterator.next() instanceof Cell;
            }

            if (valid) {
                this.neighbors = neighbors;
                iterator = neighbors.iterator();

                while (iterator.hasNext()) {
                    ((Cell) iterator.next()).addRemoteNeighbor(this);
                }
            } else {
                throw new IllegalArgumentException(
                    "The neighbors Set must contain only Cells.");
            }
        } else {
            throw new IllegalArgumentException(
                "You can't set a null neighbors set.");
        }
    }

    //return the array of elements to be found in a cell depending on its kind
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getElements() {
        String[] result;

        switch (kind) {
        case PROKARYOTE:
            result = new String[5];
            result[0] = "Cell wall (protein-polysaccharide)";
            result[1] = "Cell membrane";
            result[2] = "Flagella (single strand)";
            result[3] = "Ribosomes";
            result[4] = "Chromosomes (circle DNA)";

            break;

        case ANIMAL:
            result = new String[15];
            result[0] = "Nucleus";
            result[1] = "Smooth Endoplasmic Reticulum";
            result[2] = "Rough Endoplasmic Reticulum";
            result[3] = "Ribosomes";
            result[4] = "Cytoskeleton";
            result[5] = "Golgi Apparatus";
            result[6] = "Plasma Membrane";
            result[7] = "Lysosomes";
            result[8] = "Centrioles";
            result[9] = "Nuclear envelope";
            result[10] = "Nucleolus";
            result[11] = "Mitochondrion";
            result[12] = "Cytoplasm";
            result[13] = "Nucleoplasm";
            result[14] = "Chromosomes (Multiple; DNA-protein complex)";

            break;

        case VEGETAL:
            result = new String[16];
            result[0] = "Cell wall";
            result[1] = "Plasma membrane";
            result[2] = "Central vacuole";
            result[3] = "Mitochondrion";
            result[4] = "Ribosomes";
            result[5] = "Golgi apparatus";
            result[6] = "Nucleus";
            result[7] = "Nucleolus";
            result[8] = "Chloroplasts";
            result[9] = "Nuclear envelope";
            result[10] = "Nucleoplasm";
            result[11] = "Rough endoplasmic reticulum";
            result[12] = "Cytoplasm";
            result[13] = "Smooth endoplasmic reticulum";
            result[14] = "Lysosomes";
            result[15] = "Chromosomes (Multiple; DNA-protein complex)";

            break;

        default:
            result = new String[0];

            break;
        }

        return result;
    }

    //splits the chromosomes into arrays of genes, by cutting the whole chromosome depending on stop codons
    //there is no edition or splicing but raw transcription
    //not implemented because of memory consumption
    //public mRNA[] getAllGenes() {
    //}
    //separate the DNA into complementary strains
    //transcription one of the strains in a given direction into a a precusor RNA (pre-mRNA) with introns and exons (the only coding ones) and a 5" (starting) and 3" cap (ending with Adenines)
    //??????? How to extract the correct mRNA sequence ????
    //mRNA can be further edited (individual changes)
    //translates the mRNA into a protein using tRNA and folded using ribosomes (rRNA made)
    //not implemented
    //translation is done by the user using selected sequences from the Genome
    //public Protein getProtein(Base[] dna) {
    //}
    /**
     * returns a boolean indicating is this cell is an eukarytoe cell
     * or a prokaryote one.
     *
     * @return DOCUMENT ME!
     */
    public boolean isEukaryote() {
        return ((kind == ANIMAL) || (kind == VEGETAL));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Cell cell;

        //is this the way to use it ?
        try {
            cell = (Cell) super.clone();
            cell.genome = getGenome();
            cell.type = getType();
            cell.kind = getKind();
            cell.coding = getCoding();
        } catch (java.lang.CloneNotSupportedException e) {
            //manual clone
            cell = new Cell(getGenome(), getType(), getKind(), getCoding());
        }

        cell.stage = getStage();
        cell.neighbors = getNeighbors();

        return cell;
    }

    /**
     * returns a set of cells resulting from mitosis of this cell. This
     * cell is returned in the Set.
     *
     * @return DOCUMENT ME!
     */
    public Set provokeMitosis() {
        HashSet result;

        result = new HashSet();
        result.add(clone());
        result.add(this);

        return result;
    }

    /**
     * returns a set of cell resulting from meiosis of this cell,
     * WITHOUT any crossover or other mutation, so the process is a bit
     * idealized. This modified cell is returned in the Set. Kind is set to
     * Sex. Please note that meiosis is suitable only for diploid organisms
     * with sexual reproduction (genome length is dividable by two), otherwise
     * an error is thrown. Meiosis produces gametes. These gametes should be
     * in turn used in individual reproduction, see Specie.
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */

    //evolution is the result of mutation, (natural) selection, and genetic shift
    public Set provokeMeiosis() {
        HashSet result;
        Genome genome1;
        Genome genome2;
        Chain[] dnas1;
        Chain[] dnas2;
        Cell currentCell;

        if (Math.floor(getGenome().getChains().length / 2) == (getGenome()
                                                                       .getChains().length / 2)) {
            result = new HashSet();
            dnas1 = new DNA[getGenome().getChains().length / 2];
            dnas2 = new DNA[getGenome().getChains().length / 2];

            for (int i = 0; i < (getGenome().getChains().length / 2); i++) {
                if (Math.random() < 0.5) {
                    dnas1[i] = getGenome().getChains()[i];
                    dnas2[i] = getGenome().getChains()[i +
                        (getGenome().getChains().length / 2)];
                } else {
                    dnas1[i] = getGenome().getChains()[i +
                        (getGenome().getChains().length / 2)];
                    dnas2[i] = getGenome().getChains()[i];
                }
            }

            genome1 = new Genome(dnas1);
            genome2 = new Genome(dnas2);
            currentCell = new Cell(genome1, getType(), SEX_CELL, getCoding());
            result.add(currentCell);
            currentCell = new Cell(genome2, getType(), SEX_CELL, getCoding());
            result.add(currentCell);
            currentCell = new Cell(genome1, getType(), SEX_CELL, getCoding());
            result.add(currentCell);
            setGenome(genome2);
            setKind(SEX_CELL);
            result.add(this);
        } else {
            throw new IllegalArgumentException(
                "Meiosis cannot occur in haploid organism.");
        }

        return result;
    }
}
