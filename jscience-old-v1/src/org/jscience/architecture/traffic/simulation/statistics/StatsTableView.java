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

package org.jscience.architecture.traffic.simulation.statistics;

import org.jscience.architecture.traffic.infrastructure.Node.NodeStatistics;
import org.jscience.architecture.traffic.infrastructure.RoaduserFactory;

import java.awt.*;


/**
 * Extension of StatisticsView showing the statistics in a table.
 *
 * @author Group GUI
 * @version 1.0
 */
public class StatsTableView extends StatisticsView {
    /** DOCUMENT ME! */
    public final static int ROW_HEIGHT = 20;

    /** DOCUMENT ME! */
    public final static int COL_WIDTH = 100;

    /** DOCUMENT ME! */
    public final static int SPACING = 5;

    /** DOCUMENT ME! */
    protected final static String[] colDescs = {
            "node", "ru type", "# roadusers", "avg waiting time"
        };

    /** DOCUMENT ME! */
    protected int numRows;

    /** DOCUMENT ME! */
    protected int numCols;

/**
     * Creates a new StatsTableView object.
     *
     * @param parent DOCUMENT ME!
     * @param stats  DOCUMENT ME!
     */
    public StatsTableView(StatisticsController parent, StatisticsModel stats) {
        super(parent, stats);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintStats(Graphics g) {
        NodeStatistics[][] nodeStats = stats.getNodeStatistics();

        g.setColor(Color.black);

        //Draw the table itself
        for (int i = 0; i <= numCols; i++)
            g.drawLine(paintArea.x + (i * COL_WIDTH), paintArea.y,
                paintArea.x + (i * COL_WIDTH),
                paintArea.y + (numRows * ROW_HEIGHT));

        for (int i = 0; i <= numRows; i++)
            g.drawLine(paintArea.x, paintArea.y + (i * ROW_HEIGHT),
                paintArea.x + (numCols * COL_WIDTH),
                paintArea.y + (i * ROW_HEIGHT));

        for (int i = 0; i < numCols; i++)
            g.drawString(colDescs[i + horScroll],
                paintArea.x + (i * COL_WIDTH) + SPACING,
                (paintArea.y + ROW_HEIGHT) - SPACING);

        //Now draw the data in the table
        for (int row = 1; row < numRows; row++) {
            String[] data = getTableData(nodeStats, row - 1);

            for (int col = 0; col < numCols; col++)
                g.drawString(data[col],
                    paintArea.x + (col * COL_WIDTH) + SPACING,
                    (paintArea.y + ((row + 1) * ROW_HEIGHT)) - SPACING);
        }
    }

    /**
     * Returns the text to be shown in a table row.
     *
     * @param nodeStats Where the data should come from.
     * @param row The row where the data goes.
     *
     * @return DOCUMENT ME!
     */
    protected String[] getTableData(NodeStatistics[][] nodeStats, int row) {
        int id = (verScroll + row) / nodeStats[0].length;
        int statIndex = (verScroll + row) % nodeStats[0].length;
        NodeStatistics ns = nodeStats[id][statIndex];

        String[] data = new String[numCols];

        for (int i = 0; i < numCols; i++)
            data[i] = getCellData(horScroll + i, id, statIndex, ns);

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param id DOCUMENT ME!
     * @param statIndex DOCUMENT ME!
     * @param ns DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected String getCellData(int col, int id, int statIndex,
        NodeStatistics ns) {
        switch (col) {
        case 0:
            return ((id < stats.getNumSpecialNodes()) ? "Special node "
                                                      : "Junction ") + id;

        case 1:
            return RoaduserFactory.getDescByStatIndex(statIndex);

        case 2:
            return ns.getTotalRoadusers() + "";

        case 3:
            return ns.getAvgWaitingTime(stats.getAllTimeAvg()) + "";
        }

        return "";
    }

    /**
     * DOCUMENT ME!
     */
    protected void paintAreaChanged() {
        int rowsPossible = Math.max(paintArea.height / ROW_HEIGHT, 1);
        int colsPossible = Math.max(paintArea.width / COL_WIDTH, 1);
        NodeStatistics[][] ns = stats.getNodeStatistics();
        int rowsNeeded = (ns.length * ns[0].length) + 1;
        parent.setScrollMax(5 - colsPossible, (1 + rowsNeeded) - rowsPossible);
        numCols = Math.min(colsPossible, 4 - horScroll);
        numRows = Math.min(rowsPossible, rowsNeeded - verScroll);
    }
}
