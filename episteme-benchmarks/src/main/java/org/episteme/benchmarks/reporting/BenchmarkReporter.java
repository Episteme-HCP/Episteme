package org.episteme.benchmarks.reporting;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import org.episteme.benchmarks.cli.BenchmarkResult;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a PDF report containing benchmark results and comparative charts.
 */
public class BenchmarkReporter {

    public static void generateReport(List<BenchmarkResult> results, String pdfPath) {
        // Switch to Landscape for better chart visibility
        Document document = new Document(PageSize.A4.rotate());
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Font Styles - More professional sizes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new Color(0, 51, 102));
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new Color(0, 102, 204));

            // Title
            Paragraph title = new Paragraph("Episteme Benchmark Executive Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(10);
            document.add(title);

            Paragraph subtitle = new Paragraph("Precision Performance Analytics | Generated: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(30);
            document.add(subtitle);

            // System Info Table for cleaner look
            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingAfter(20);
            
            table.addCell("Operating System");
            table.addCell(System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
            table.addCell("Java Runtime");
            table.addCell(System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
            table.addCell("Logical Processors");
            table.addCell(String.valueOf(Runtime.getRuntime().availableProcessors()));
            
            document.add(table);

            // Group by Domain
            Map<String, List<BenchmarkResult>> grouped = results.stream()
                .filter(r -> "SUCCESS".equals(r.status))
                .collect(Collectors.groupingBy(r -> r.item.getDomain()));

            // Sort domains for consistent reporting
            List<String> sortedDomains = grouped.keySet().stream().sorted().collect(Collectors.toList());

            for (String domain : sortedDomains) {
                document.add(new Paragraph("Domain Analysis: " + domain, sectionFont));
                document.add(new Paragraph(" "));

                JFreeChart chart = createChart(domain, grouped.get(domain));
                
                // Optimized dimensions for Landscape A4
                int width = 750;
                int height = 400;
                
                java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(width, height);
                Image pdfImage = Image.getInstance(writer, bufferedImage, 1.0f);
                pdfImage.setAlignment(Element.ALIGN_CENTER);
                pdfImage.scaleToFit(700, 400);
                document.add(pdfImage);
                
                document.add(new Paragraph(" "));
            }

            document.close();
        } catch (Exception e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static JFreeChart createChart(String domain, List<BenchmarkResult> results) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Sort results by score descending for better visualization
        results.sort(Comparator.comparingDouble((BenchmarkResult r) -> r.score).reversed());

        for (BenchmarkResult r : results) {
            String lib = r.item.libraryProperty().get();
            // Shorten common library names for better labels
            if (lib.contains("Commons Math")) lib = "Commons";
            else if (lib.contains("DistributedContext")) lib = "Distributed";
            
            String label = r.item.getName() + " [" + lib + "]";
            dataset.addValue(r.score, "Throughput", label);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                domain + " | Scaling Performance",
                "Implementation Architecture",
                "Throughput (Operations per Second)",
                dataset,
                PlotOrientation.HORIZONTAL,
                false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 16));
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(250, 250, 250));
        plot.setRangeGridlinePaint(new Color(220, 220, 220));
        plot.setDomainGridlinePaint(new Color(220, 220, 220));
        plot.setOutlineVisible(false);

        // Customize Bar Appearance - Use a premium deep blue
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 86, 179)); 
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.10);

        // Domain Axis (Labels)
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        domainAxis.setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);
        domainAxis.setCategoryMargin(0.15); // Add space between bars

        // Range Axis (Values)
        plot.getRangeAxis().setTickLabelFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10));

        return chart;
    }
}
