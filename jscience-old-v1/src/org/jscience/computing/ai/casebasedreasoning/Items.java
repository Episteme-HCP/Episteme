package org.jscience.computing.ai.casebasedreasoning;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * 
DOCUMENT ME!
 *
 * @author <small>baylor</small>
 */
public class Items {
    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     */
    ArrayList data = new ArrayList();

    /**
     * DOCUMENT ME!
     */
    TraitDescriptors traitDescriptors;

    //-----------------------------------------------------------------------------
    // Constructors
    //-----------------------------------------------------------------------------
/**
     * This is the only constructor and it requires TraitDescriptors
     * <p/>
     * Technically, Items doesn't need to know anything about TraitDescriptors,
     * <p/>
     * but a lot of the people who use Items (actually, Item) do.
     * <p/>
     * They could create their own copy, but that would be slow, what with
     * <p/>
     * all the loading and what not. Ditto for storing it with each Item.
     * <p/>
     * So storing TraitDescriptors here is just a minor performance tuning thingy
     */
    protected Items(TraitDescriptors traitDescriptors) {
        this.traitDescriptors = traitDescriptors;
    } //--- constructor

    //-----------------------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @param item DOCUMENT ME!
     */
    protected void add(Item item) {
        data.add(item);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @param traitDescriptors DOCUMENT ME!
     * @param encodedData DOCUMENT ME!
     */
    protected void add(TraitDescriptors traitDescriptors, String encodedData) {
        Item item = new Item(traitDescriptors);

        item.loadFromDelimitedString(traitDescriptors, encodedData);

        this.add(item);
    } //--- add

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Items newItems = new Items(traitDescriptors);

        newItems.setValues(data);

        return (Object) newItems;
    } //--- clone

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TraitDescriptors getTraitDescriptors() {
        return traitDescriptors;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return data.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param newValues DOCUMENT ME!
     */
    private void setValues(ArrayList newValues) {
        data = (ArrayList) newValues.clone();
    }

    /**
     * Number of items in the collection
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return data.size();
    }
} //--- Items
