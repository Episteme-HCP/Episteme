package org.jscience.benchmark;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Generates benchmark result graphs.
 */
public class BenchmarkGraphGenerator {

    public static void generateBarChart(String title, String xAxis, String yAxis,
            Map<String, Double> data, File outputFile) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            dataset.addValue(entry.getValue(), "Time (ms)", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                title,
                xAxis,
                yAxis,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        int width = 800;
        int height = 600;
        ChartUtils.saveChartAsPNG(outputFile, barChart, width, height);
    }
}
