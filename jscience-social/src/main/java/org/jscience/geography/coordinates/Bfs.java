package org.jscience.geography.coordinates;

import java.util.Vector;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Bfs {
    /** DOCUMENT ME! */
    private static Bfs _bfs = new Bfs();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Bfs instance() {
        return _bfs;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     * @param destination DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector GetConversionPath(SRM_SRFT_Code source,
        SRM_SRFT_Code destination) {
        boolean[] visited_node = new boolean[SRM_SRFT_Code._totalEnum];
        SRM_SRFT_Code[] parent_node = new SRM_SRFT_Code[SRM_SRFT_Code._totalEnum];
        int[] distance = new int[SRM_SRFT_Code._totalEnum];
        Vector q = new Vector();
        Vector path = new Vector();
        Vector returnPath = new Vector();
        SRM_SRFT_Code current_node;
        SRM_SRFT_Code node;
        Conversions conv;

        for (int i = 0; i < SRM_SRFT_Code._totalEnum; i++) {
            visited_node[i] = false;
            parent_node[i] = SRM_SRFT_Code.SRFT_UNDEFINED;
            distance[i] = -1;
        }

        if (source != destination) {
            visited_node[source.toInt()] = true;
            distance[source.toInt()] = 0;
            queue(q, source);

            while (!q.isEmpty()) {
                current_node = dequeue(q);

                if (current_node == destination) {
                    node = current_node;

                    do {
                        queue(path, node);
                        node = parent_node[node.toInt()];
                    } while (node != source);

                    queue(path, node);

                    for (int i = 0; i < path.size(); i++)
                        returnPath.insertElementAt(path.elementAt(i), 0);

                    // 		    System.out.println("PATH=> " + returnPath + ", distance=> " + distance[destination.toInt()]);
                    return returnPath;
                }

                int index = 0;

                if ((conv = (Conversions) FunctionMap.instance()
                                                         .get(current_node)) != null) {
                    node = (SRM_SRFT_Code) conv.getDest()[index];
                } else {
                    node = SRM_SRFT_Code.SRFT_UNDEFINED;
                }

                while (node != SRM_SRFT_Code.SRFT_UNDEFINED) {
                    if (!visited_node[node.toInt()]) {
                        queue(q, node);
                        visited_node[node.toInt()] = true;
                        distance[node.toInt()] = distance[current_node.toInt()] +
                            1;
                        parent_node[node.toInt()] = current_node;
                    }

                    visited_node[current_node.toInt()] = true;

                    if ((conv = (Conversions) FunctionMap.instance()
                                                             .get(current_node)) != null) {
                        node = (SRM_SRFT_Code) conv.getDest()[index++];
                    } else {
                        node = SRM_SRFT_Code.SRFT_UNDEFINED;
                    }
                }
            }
        }
        // if source == destination
        else {
            boolean firstRound = true;
            distance[source.toInt()] = 0;
            queue(q, source);

            while (!q.isEmpty()) {
                current_node = dequeue(q);

                // to check against when the source node and the target node are from the same SRF
                if ((current_node == destination) && !firstRound) {
                    node = current_node;

                    //		    System.out.print("=> Parent Array=> " );
                    // 		    for (int i=0; i<parent_node.length; i++)
                    // 			System.out.print(parent_node[i] + ", ");
                    // 		    System.out.println("");
                    do {
                        queue(path, node);

                        //			System.out.println("PATH=> " + path);
                        node = parent_node[node.toInt()];
                    } while (node != source);

                    queue(path, node);

                    // reverse the order of the vector elements from source to target
                    for (int i = 0; i < path.size(); i++)
                        returnPath.insertElementAt(path.elementAt(i), 0);

                    // 		    System.out.println("PATH=> " + returnPath + ", distance=> " + distance[destination.toInt()]);
                    return returnPath;
                }

                firstRound = false;

                int index = 0;

                if ((conv = (Conversions) FunctionMap.instance()
                                                         .get(current_node)) != null) {
                    node = (SRM_SRFT_Code) conv.getDest()[index];
                } else {
                    node = SRM_SRFT_Code.SRFT_UNDEFINED;
                }

                while (node != SRM_SRFT_Code.SRFT_UNDEFINED) {
                    if (!visited_node[node.toInt()]) {
                        queue(q, node);

                        //			System.out.println("q=> " + q);
                        visited_node[node.toInt()] = true;
                        distance[node.toInt()] = distance[current_node.toInt()] +
                            1;
                        parent_node[node.toInt()] = current_node;

                        // 		    System.out.print("=> Parent Array=> " );
                        // 		    for (int i=0; i<parent_node.length; i++)
                        // 			System.out.print(parent_node[i] + ", ");
                        // 		    System.out.println("");
                    }

                    if (current_node != source) {
                        visited_node[current_node.toInt()] = true;
                    }

                    if ((conv = (Conversions) FunctionMap.instance()
                                                             .get(current_node)) != null) {
                        node = (SRM_SRFT_Code) conv.getDest()[index++];
                    } else {
                        node = SRM_SRFT_Code.SRFT_UNDEFINED;
                    }
                }
            }
        }

        // no path found from source to target
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param q DOCUMENT ME!
     * @param node DOCUMENT ME!
     */
    private void queue(Vector q, SRM_SRFT_Code node) {
        q.add(node);
    }

    /**
     * DOCUMENT ME!
     *
     * @param q DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SRM_SRFT_Code dequeue(Vector q) {
        return ((SRM_SRFT_Code) q.remove(0));
    }

    //     public void main(String[] arg) {
    //  	Vector v;
    // 	for (int i=0; i<SRM_SRFT_Code._totalEnum; i++)
    // 	    for (int j=0; j<SRM_SRFT_Code._totalEnum; j++) {
    // 		System.out.println("*** From " + SRM_SRFT_Code._enumList[i].toString() +
    // 				 " to " + SRM_SRFT_Code._enumList[j].toString());
    // 		v= GetConversionPath(SRM_SRFT_Code._enumList[i], SRM_SRFT_Code._enumList[j]);
    // 	    }
    // 	System.out.println("Path from " + SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL + " to " +
    // 			   SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL );
    // 	v = GetConversionPath(SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL, SRM_SRFT_Code.SRFT_LOCAL_TANGENT_SPACE_CYLINDRICAL);
    // 	for ( int i=0; i<v.size(); i++ )
    // 	    System.out.println("Node " + i + " => " + v.elementAt(i) );
    //     }
}
