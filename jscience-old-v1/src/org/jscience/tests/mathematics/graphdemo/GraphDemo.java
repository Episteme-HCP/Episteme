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

import org.jscience.awt.DefaultCategoryGraph2DModel;
import org.jscience.awt.DefaultGraph2DModel;
import org.jscience.awt.Graph2D;
import org.jscience.awt.GraphLayout;

import org.jscience.swing.JBarGraph;
import org.jscience.swing.JGraphLayout;
import org.jscience.swing.JLineGraph;
import org.jscience.swing.JPieChart;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


/**
 * Sample program demonstrating use of the Swing/AWT graph components.
 *
 * @author Mark Hale
 * @version 1.1
 */
public class GraphDemo extends Frame {
    /**
     * DOCUMENT ME!
     */
    private DefaultCategoryGraph2DModel categoryModel;

    /**
     * DOCUMENT ME!
     */
    private DefaultGraph2DModel valueModel;

    /**
     * Creates a new GraphDemo object.
     */
    public GraphDemo() {
        super("JScience Graph Demo");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        setSize(700, 600);

        final Font titleFont = new Font("Default", Font.BOLD, 14);
        Label title;
        // category graphs
        categoryModel = createCategoryData();

        // bar graph
        JBarGraph barGraph = new JBarGraph(categoryModel);
        final Panel barGraphPanel = new Panel(new JGraphLayout());
        title = new Label("Bar graph", Label.CENTER);
        title.setFont(titleFont);
        barGraphPanel.add(title, "Title");
        barGraphPanel.add(barGraph, "Graph");
        barGraphPanel.add(new Label("y-axis", Label.RIGHT), "Y-axis");
        barGraphPanel.add(new Label("Category axis", Label.CENTER), "X-axis");

        // pie chart
        JPieChart pieChart = new JPieChart(categoryModel);
        final Panel pieChartPanel = new Panel(new GraphLayout());
        title = new Label("Pie chart", Label.CENTER);
        title.setFont(titleFont);
        pieChartPanel.add(title, "Title");
        pieChartPanel.add(pieChart, "Graph");
        // value graphs
        valueModel = createValueData();

        // line graph
        JLineGraph lineGraph = new JLineGraph(valueModel);
        lineGraph.setGridLines(true);
        lineGraph.setMarker(new Graph2D.DataMarker.Circle(3));

        final Panel lineGraphPanel = new Panel(new JGraphLayout());
        title = new Label("Line graph", Label.CENTER);
        title.setFont(titleFont);
        lineGraphPanel.add(title, "Title");
        lineGraphPanel.add(lineGraph, "Graph");

        Choice choice = new Choice();
        choice.add("Temp.");
        choice.add("Rain fall");
        choice.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent evt) {
                    if (evt.getStateChange() == ItemEvent.SELECTED) {
                        if (evt.getItem().toString().equals("Temp.")) {
                            valueModel.setSeriesVisible(0, true);
                            valueModel.setSeriesVisible(1, false);
                        } else if (evt.getItem().toString().equals("Rain fall")) {
                            valueModel.setSeriesVisible(0, false);
                            valueModel.setSeriesVisible(1, true);
                        }
                    }
                }
            });
        lineGraphPanel.add(choice, "Y-axis");
        lineGraphPanel.add(new Label("x-axis", Label.CENTER), "X-axis");

        // data series tables
        final Box tablePanel = new Box(BoxLayout.Y_AXIS);
        tablePanel.add(new JLabel("Data series 0", JLabel.CENTER));
        tablePanel.add(new JTable(valueModel.getSeries(0)));
        tablePanel.add(new JLabel("Data series 1", JLabel.CENTER));
        tablePanel.add(new JTable(valueModel.getSeries(1)));

        // layout
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gb);
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gb.setConstraints(barGraphPanel, gbc);
        add(barGraphPanel);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gb.setConstraints(pieChartPanel, gbc);
        add(pieChartPanel);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        Button updateButton = new Button("Update");
        updateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    float[] newData = { 3.4f, 5.6f, 6.2f, 3.9f, 1.8f };
                    categoryModel.changeSeries(0, newData);
                }
            });
        gb.setConstraints(updateButton, gbc);
        add(updateButton);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gb.setConstraints(lineGraphPanel, gbc);
        add(lineGraphPanel);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gb.setConstraints(tablePanel, gbc);
        add(tablePanel);
        setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        new GraphDemo();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static DefaultCategoryGraph2DModel createCategoryData() {
        String[] labels = { "Alpha1", "Beta2", "Gamma3", "Delta4", "Epsilon5" };
        float[] values1 = { 2.4f, 7.3f, 3.2f, 0.5f, 2.2f };
        float[] values2 = { 0.9f, 3.4f, 2.1f, 6.5f, 8.2f };
        DefaultCategoryGraph2DModel model = new DefaultCategoryGraph2DModel();
        model.setCategories(labels);
        model.addSeries(values1);
        model.addSeries(values2);

        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static DefaultGraph2DModel createValueData() {
        float[] values1 = { 3.0f, 2.8f, 3.5f, 3.6f, 3.1f, 2.6f };
        float[] values2 = { 7.8f, 4.1f, 0.9f, 0.2f, 1.3f, 2.5f };
        DefaultGraph2DModel model = new DefaultGraph2DModel();
        model.setXAxis(0.0f, 5.0f, values1.length);
        model.addSeries(values1);
        model.addSeries(values2);
        model.setSeriesVisible(1, false);

        return model;
    }
}
