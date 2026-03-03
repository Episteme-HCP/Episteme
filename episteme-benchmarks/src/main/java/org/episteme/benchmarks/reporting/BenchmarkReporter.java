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
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();

            // Font Styles
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, Color.BLACK);
            Font subTitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.DARK_GRAY);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(0, 51, 102));

            // Title
            Paragraph title = new Paragraph("Episteme Benchmark Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Paragraph subtitle = new Paragraph("Generated on: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), subTitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(20);
            document.add(subtitle);

            // System Info
            document.add(new Paragraph("System Context:", sectionFont));
            document.add(new Paragraph("OS: " + System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")"));
            document.add(new Paragraph("Java: " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")"));
            document.add(new Paragraph("Processors: " + Runtime.getRuntime().availableProcessors()));
            document.add(new Paragraph(" "));

            // Group by Domain
            Map<String, List<BenchmarkResult>> grouped = results.stream()
                .filter(r -> "SUCCESS".equals(r.status))
                .collect(Collectors.groupingBy(r -> r.item.getDomain()));

            for (String domain : grouped.keySet()) {
                document.add(new Paragraph("Domain: " + domain, sectionFont));
                document.add(new Paragraph(" "));

                JFreeChart chart = createChart(domain, grouped.get(domain));
                
                // Convert chart to PDF-friendly image context
                int width = 500;
                int height = 300;
                
                // Using a simpler approach: Render to Image then add to PDF
                java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(width, height);
                Image pdfImage = Image.getInstance(writer, bufferedImage, 1.0f);
                pdfImage.setAlignment(Element.ALIGN_CENTER);
                pdfImage.scaleToFit(500, 300);
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

        for (BenchmarkResult r : results) {
            String label = r.item.getName() + " (" + r.item.libraryProperty().get() + ")";
            dataset.addValue(r.score, "Ops/s", label);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                domain + " Performance",
                "Implementation",
                "Operations per Second",
                dataset,
                PlotOrientation.HORIZONTAL,
                false, true, false);

        chart.setBackgroundPaint(Color.WHITE);
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(245, 245, 245));
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        // Customize appearance
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(0, 102, 204));
        renderer.setBarPainter(new org.jfree.chart.renderer.category.StandardBarPainter());
        renderer.setShadowVisible(false);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        return chart;
    }
}
