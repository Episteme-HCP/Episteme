package org.jscience.biology.taxonomy;

import java.util.Set;
import java.util.Vector;

/**
 * A class representing a taxon within a classification.
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

//reproduced from: David E. Joyce
//http://aleph0.clarku.edu/~djoyce/java/Phyltree/intro.html
//Validity of the tree model. A tree isn't always the best model. Here are some times when it isn't best. 
//For individuals within a species. The genetic material of an individual doesn't derive from a single earlier existing individual. Animals and plants that multiply by sexual reproduction receive half their genetic material from each of two parents, so a tree like this is inappropriate. For species that multiply asexually, a tree is appropriate. Even for species that usually multiply asexually�such as many one-celled creatures�the occassional exchange of genetic material through conjugation is so important that trees are inappropriate. 
//For closely related species. Individuals do occasionaly mate between closely related species, and their progeny survive to contribute to the gene pool of one or both of the parent species. As the species diverge, such intermixing of genetic material becomes more rare. One solution is to treat closely related species as one larger variable species. Another is simply not to consider closely related species. 
//Hybrid species. In the plant world it occassionally happens that a new tetraploid species arises from two diploid species. The two parent species need to be somewhat related for this to happen. 
//Distant interaction. There are a couple of ways that genetic material from one species can find its way into a distantly related or unrelated species. Among bacteria, sometimes a bacterium of one species can injest the genetic material of a bacterium of another species and incorporate part of it into its own genetic material. Rare as this may be, the effects are significant. Sometimes viruses can inadvertantly transport genetic material from one species to another. When some viruses break out of cells of one species, they may infect other species and carry that material to them. 
//In spite of these exceptions, a tree model is usually a pretty good model to show the relations among species. 

/**
 * <p>A taxon within a classification.</p>
 * <p/>
 * <p>Taxa may be 'leaf' nodes specifying species, or 'internal' nodes
 * specifying kingdoms and the like.</p>
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
public interface Taxon {
    /**
     * <p>The common name of the Taxon.</p>
     * <p/>
     * <p>This is the normal name used in common speech, such as
     * 'human'.</p>
     *
     * @return a String representing this Taxon's common name
     */
    public String getCommonName();

    /**
     * <p>The scientific name of this taxon.</p>
     * <p/>
     * <p>This will be the portion of the scientific classification
     * pertaining to just this node within the classifictaion. It will
     * be something like 'homo sapiens' or 'archaeal group 2', rather
     * than the full classification list.</p>
     */
    public String getScientificName();

    /**
     * <p>The parent of this Taxon.</p>
     * <p/>
     * <p>Taxa live within a tree data-structure, so every taxon has a
     * single parent except for the root type. This has the null
     * parent.</p>
     *
     * @return the parent Taxon, or null if this is the root type.
     */
    public Taxon getParent();

    /**
     * <p>The root of this Tree.</p>
     * <p/>
     * <p>Taxa live within a tree data-structure, so every taxon has a
     * single parent except for the root type. This has the null
     * parent.</p>
     *
     * @return a boolean indicating whether or not this is the root of the classification.
     */
    public boolean isRoot();

    /**
     * <p>The leafs of this Taxon.</p>
     * <p/>
     * <p>A taxa with no children should be considered as a real specie.</p>
     *
     * @return true if the Taxon has no children
     */
    public boolean isLeaf();

    /**
     * <p>The children of this Taxon.</p>
     * <p/>
     * <p>Taxa live within a tree data-structure, so every taxon has
     * zero or more children. In the case of zero children, the empty
     * set is returned.</p>
     *
     * @return the Set (possibly empty) of all child Taxa
     */
    public Set getChildren();

    /**
     * <p>The complete name of this Taxon.</p>
     * <p/>
     * <p>A complete name using the root and going down the hierarchy to this Taxon.</p>
     *
     * @return the full ordered taxonomic name
     */
    public Vector getExtendedName();

    /**
     * <p>The date at which this taxon appeared in earth history in seconds before or after unix time (1/1/1970). A value of O stands for unknown. Defaults to 0.</p>
     * <p/>
     * <p></p>
     *
     * @return the date at which the taxon first differenciated or appeared
     */
    public double getAppearance();

    /**
     * <p>The date at which this taxon disappeared in earth history in seconds before or after unix time (1/1/1970). A value of O stands for unknown. Defaults to 0. Use the value Double.MAX_VALUE for species still existing.</p>
     * <p/>
     * <p></p>
     *
     * @return the date at which the taxon became extinct if any
     */
    public double getExtinction();

    /**
     * <p>Two taxa are equal if they have equivalent children, common
     * and scientific names.</p>
     * <p/>
     * <p>Two different implementations of Taxon should be able to
     * appropriately trans-class equality. The parent of a Taxon is not
     * considered in testing equality as this potentially leads to
     * combinatorial problems checking whole taxa hierachies against one
     * another.</p>
     *
     * @param o the object to check
     * @return true if o is a Taxon instance and has the same properties
     *         as this
     */
    public boolean equals(Object o);
}
