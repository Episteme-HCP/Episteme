/*
 * DomTools.java
 *
 * Created on October 06, 2003, 0:08 AM
 *
 * Copyright (C) 2003 Hajo Keffer <hajokeffer@coli.uni-sb.de>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.jscience.ml.tigerxml.tools;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * Provides methods useful when utilizing
 * the Document Object Model API as specified by the w3c.
 * This class is for static use.
 *
 * @author <a href="mailto:hajokeffer@coli.uni-sb.de">Hajo Keffer</a>
 * @version 1.84
 *          $Id: DomTools.java,v 1.2 2007-10-21 17:47:09 virtualcall Exp $
 */
public class DomTools {

    /**
     * Checks if the given <code>Element</code> has a child named
     * <code>name</code>.
     *
     * @param ele  The <code>Element</code> to be checked.
     * @param name The name of the child to be checked.
     * @return <code>true</code> if <code>ele</code> has a child <code>name<code>,
     *         else <code>false</code>.
     */
    public static final boolean hasElement(Element ele, String name) {
        boolean return_value = false;
        NodeList list_of_nodes = ele.getElementsByTagName(name);
        if (list_of_nodes.getLength() > 0) {
            return_value = true;
        }
        return return_value;
    }

    /**
     * Returns the first element named <code>name</code> in
     * <code>Element ele</code>.
     *
     * @param ele  The <code>Element</code> holding the element to be retrieved.
     * @param name The name of the <code>Element</code> to be retrieved.
     * @return The element to be retrieved.
     */
    public static final Element getElement(Element ele, String name) {
        Element return_ele = null;
        NodeList list_of_nodes = ele.getElementsByTagName(name);
        if (list_of_nodes.getLength() != 1) {
            System.err.println("org.jscience.ml.tigerxml.tools.DomTools: "
                    + "WARNING: Expected one instead of"
                    + list_of_nodes.getLength()
                    + " " + name
                    + " nodes");
        }
        if (list_of_nodes.getLength() >= 1) {
            Node my_node = list_of_nodes.item(0);
            return_ele = (Element) my_node;
        }
        return return_ele;
    } // get_element()

    /**
     * Prints information about a given node to stdout.
     *
     * @param node The node to print information about.
     */
    public static final void printNodeInfo(Node node) {
        int ELEMENT_TYPE = 1;
        int ATTR_TYPE = 2;
        int TEXT_TYPE = 3;
        int CDATA_TYPE = 4;
        int ENTITYREF_TYPE = 5;
        int ENTITY_TYPE = 6;
        int PROCINSTR_TYPE = 7;
        int COMMENT_TYPE = 8;
        int DOCUMENT_TYPE = 9;
        int DOCTYPE_TYPE = 10;
        int DOCFRAG_TYPE = 11;
        int NOTATION_TYPE = 12;

        String name = node.getNodeName();
        int type = node.getNodeType();

        System.out.println("Node name " + name);
        if (type == ELEMENT_TYPE) {
            System.out.println("is Element");
        } else if (type == TEXT_TYPE) {
            System.out.println("is Text");
        } else if (type == ENTITYREF_TYPE) {
            System.out.println("is Entityref");
        } else if (type == CDATA_TYPE) {
            System.out.println("is cddata");
        } else if (type == ATTR_TYPE) {
            System.out.println("is attr");
        }
    }

    /**
     * Checks if the name of <code>ele</code> is <code>name</code>. If not,
     * a warning message is printed to stderr.
     *
     * @param ele  The <code>Element</code> to be checked.
     * @param name The name of the <code>Element</code> to be checked against.
     */
    public static final void checkElementName(Element ele, String name) {
        if (!((ele.getNodeName()).equals(name))) {
            System.err.println("org.jscience.ml.tigerxml.tools.DomTools: WARNING: Expected "
                    + name + " node");
        }
    } // check element name

    /**
     * Returns the text embeded in <code>ele</code>.
     *
     * @param ele The <code>Element</code> to be read out.
     * @return The text embeded in <code>ele</code>.
     */
    public static final String getText(Element ele) {
        String return_string = null;
        NodeList list_of_nodes = ele.getChildNodes();
        if (list_of_nodes.getLength() != 1) {
            System.err.println("org.jscience.ml.tigerxml.tools.DomTools: "
                    + "WARNING: Expected one instead of"
                    + list_of_nodes.getLength()
                    + " nodes");
        }
        if (list_of_nodes.getLength() >= 1) {
            Node my_node =
                    list_of_nodes.item(0);
            int type = my_node.getNodeType();
            if (type == Node.TEXT_NODE) {
                Text text_node =
                        (Text) my_node;
                return_string = text_node.getData();
            } else {
                System.err.println("org.jscience.ml.tigerxml.tools.DomTools: WARNING: Expected text node");
            }
        }
        return return_string;
    } // get_text

// see StringTools.cleanXMLString(String)
//  /**
//   * Handles special characters
//   * (particularly German umlauts) by translating
//   * them into XML entity references. This method
//   * is useful when one prints data to an XML file.
//   */
//  public static String convert(String string) {
//    String output_string = new String(string);
//    output_string = output_string.replaceAll
//        ("&", "&amp;");
//    output_string = output_string.replaceAll
//        ("<", "&lt;");
//    output_string = output_string.replaceAll
//        (">", "&gt;");
//    output_string = output_string.replaceAll
//        ("\"", "&quot;");
//    output_string = output_string.replaceAll
//        ("\'", "&apos;");
//    // replacing ae
//    output_string = output_string.replaceAll
//        ("�", "&#x00e4;");
//    // replacing Ae
//    output_string = output_string.replaceAll
//        ("�", "&#x00c4;");
//    // replacing oe
//    output_string = output_string.replaceAll
//        ("�", "&#x00f6;");
//    // replacing Oe
//    output_string = output_string.replaceAll
//        ("�", "&#x00d6;");
//    // replacing ue
//    output_string = output_string.replaceAll
//        ("�", "&#x00fc;");
//    // replacing Ue
//    output_string = output_string.replaceAll
//        ("�", "&#x00dc;");
//    // replacing sz
//    output_string = output_string.replaceAll
//        ("�", "&#x00df;");
//    // replace all unknown characters by "?"
//    output_string = output_string.replaceAll
//        ("[^_a-zA-Z0-9\\r\\t\\n\\f ,;\\&$.!?{}()\\[\\]\\-\\+*\\/�%=``�~:|#]",
//         "?");
//    return output_string;
//  }
} // class
