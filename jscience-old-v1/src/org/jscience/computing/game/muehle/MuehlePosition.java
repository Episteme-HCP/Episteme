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

package org.jscience.computing.game.muehle;

import org.jscience.computing.game.GameBoardPosition;


/**
 * The class MuehlePosition represents a Muehle game position. The
 * representation is an integer based on the following little ASCII
 * graphic:<br><pre> 00----------01----------02  |           |           |
 *   |  08------09------10   |  |   |       |       |   |
 *   |   |  16--17--18   |   |  |   |   |       |   |   |
 *  07--15--23      19--11--03  |   |   |       |   |   |
 *   |   |  22--21--20   |   |  |   |       |       |   |
 *   |  14------13------12   |  |           |           |
 *  06----------05----------04</pre>
 *
 * @author Holger Antelmann
 */
public class MuehlePosition extends GameBoardPosition {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 225801582955323756L;

    /** DOCUMENT ME! */
    public static int NUMBER_OF_FIELDS = 24;

/**
     * Creates a new MuehlePosition object.
     *
     * @param pos DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public MuehlePosition(int pos) throws IllegalArgumentException {
        super(pos);

        // NUMBER_OF_FIELDS must be 24 to finction correctly with
        // other functions within this class
        if (!isValidPosition(pos)) {
            String s = "org.jscience.computing.game.MuehlePosition cannot be instanciated with ";
            s += ("the given integer: " + pos);
            throw (new IllegalArgumentException(s));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isValidPosition(int p) {
        return ((p >= 0) && (p < NUMBER_OF_FIELDS));
    }

    /**
     * returns getMuehlePairs(asInteger())
     *
     * @return DOCUMENT ME!
     */
    public int[] getMuehlePairs() {
        return getMuehlePairs(asInteger());
    }

    /**
     * As every potential muehle is closed by two pairs of 2 other
     * positions having a piece of the same color, getMuehlePairs() returns an
     * array of integers that represent the two pairs of Positions that
     * fullfill this condition.
     *
     * @param pos DOCUMENT ME!
     *
     * @return an int[] with 4 elememts; the first two array elements represent
     *         the first position pair to potentially close the muehle, the
     *         next two elements represent the second position pair - or null
     *         if the given pos is not valid.
     */
    public static int[] getMuehlePairs(int pos) {
        switch (pos) {
        case 0:
            return new int[] { 1, 2, 6, 7 }; //  00----------01----------02

        case 1:
            return new int[] { 0, 2, 9, 17 }; //   |           |           |

        case 2:
            return new int[] { 0, 1, 3, 4 }; //   |  08------09------10   |

        case 3:
            return new int[] { 2, 4, 11, 19 }; //   |   |       |       |   |

        case 4:
            return new int[] { 2, 3, 5, 6 }; //   |   |  16--17--18   |   |

        case 5:
            return new int[] { 4, 6, 13, 21 }; //   |   |   |       |   |   |

        case 6:
            return new int[] { 4, 5, 0, 7 }; //  07--15--23      19--11--03

        case 7:
            return new int[] { 0, 6, 15, 23 }; //   |   |   |       |   |   |

        case 8:
            return new int[] { 9, 10, 14, 15 }; //   |   |  22--21--20   |   |

        case 9:
            return new int[] { 1, 17, 8, 10 }; //   |   |       |       |   |

        case 10:
            return new int[] { 8, 9, 11, 12 }; //   |  14------13------12   |

        case 11:
            return new int[] { 3, 19, 10, 12 }; //   |           |           |

        case 12:
            return new int[] { 10, 11, 13, 14 }; //  06----------05----------04

        case 13:
            return new int[] { 12, 14, 5, 21 };

        case 14:
            return new int[] { 12, 13, 8, 15 };

        case 15:
            return new int[] { 7, 23, 8, 14 };

        case 16:
            return new int[] { 17, 18, 22, 23 };

        case 17:
            return new int[] { 1, 9, 16, 18 };

        case 18:
            return new int[] { 16, 17, 19, 20 };

        case 19:
            return new int[] { 3, 11, 18, 20 };

        case 20:
            return new int[] { 18, 19, 21, 22 };

        case 21:
            return new int[] { 5, 13, 20, 22 };

        case 22:
            return new int[] { 20, 21, 16, 23 };

        case 23:
            return new int[] { 7, 15, 16, 22 };

        default:
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean connectsTo(MuehlePosition pos) {
        int[] set = getConnections();

        for (int i = 0; i < set.length; i++) {
            if (pos.asInteger() == set[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean connectsTo(int pos) {
        int[] set = getConnections();

        for (int i = 0; i < set.length; i++) {
            if (pos == set[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * returns getMuehlePairs(asInteger())
     *
     * @return DOCUMENT ME!
     */
    public int[] getConnections() {
        return getConnections(asInteger());
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] getConnections(int pos) {
        switch (pos) {
        case 0:
            return new int[] { 1, 7 }; //  00----------01----------02

        case 1:
            return new int[] { 0, 2, 9 }; //   |           |           |

        case 2:
            return new int[] { 1, 3 }; //   |  08------09------10   |

        case 3:
            return new int[] { 2, 4, 11 }; //   |   |       |       |   |

        case 4:
            return new int[] { 3, 5 }; //   |   |  16--17--18   |   |

        case 5:
            return new int[] { 4, 6, 13 }; //   |   |   |       |   |   |

        case 6:
            return new int[] { 5, 7 }; //  07--15--23      19--11--03

        case 7:
            return new int[] { 0, 6, 15 }; //   |   |   |       |   |   |

        case 8:
            return new int[] { 9, 15 }; //   |   |  22--21--20   |   |

        case 9:
            return new int[] { 1, 8, 10, 17 }; //   |   |       |       |   |

        case 10:
            return new int[] { 9, 11 }; //   |  14------13------12   |

        case 11:
            return new int[] { 3, 10, 12, 19 }; //   |           |           |

        case 12:
            return new int[] { 11, 13 }; //  06----------05----------04

        case 13:
            return new int[] { 5, 12, 14, 21 };

        case 14:
            return new int[] { 13, 15 };

        case 15:
            return new int[] { 7, 8, 14, 23 };

        case 16:
            return new int[] { 17, 23 };

        case 17:
            return new int[] { 9, 16, 18 };

        case 18:
            return new int[] { 17, 19 };

        case 19:
            return new int[] { 11, 18, 20 };

        case 20:
            return new int[] { 19, 21 };

        case 21:
            return new int[] { 13, 20, 22 };

        case 22:
            return new int[] { 21, 23 };

        case 23:
            return new int[] { 15, 16, 22 };

        default:
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "MuehlePosition: " + asInteger();

        return s;
    }
}
