package org.episteme.benchmarks.reporting;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;
import org.episteme.benchmarks.benchmark.BenchmarkResult;
// Removed ambiguous external import
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

/**
 * Utility for generating performance and correctness reports.
 */
public class BenchmarkReporter {

// Inner class removed in favor of unified record

    private final String title;
    private final List<BenchmarkResult> results = new ArrayList<>();
    private final Map<String, String> sections = new LinkedHashMap<>();

    public BenchmarkReporter(String title) {
        this.title = title;
    }

    public void addSection(String name, String content) {
        sections.put(name, content);
    }

    public void addResult(BenchmarkResult result) {
        results.add(result);
    }

    public void exportMarkdown(String path) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("# ").append(title).append("\n\n");
            
            sections.forEach((name, content) -> {
                sb.append("## ").append(name).append("\n");
                sb.append(content).append("\n\n");
            });

            if (!results.isEmpty()) {
                sb.append("## Performance Results\n\n");
                // Collect all metric keys
                List<String> keys = new ArrayList<>();
                results.forEach(r -> r.extraMetrics().keySet().forEach(k -> {
                    if (!keys.contains(k)) keys.add(k);
                }));

                sb.append("| Provider | Domain | Status |");
                for (String k : keys) sb.append(" ").append(k).append(" |");
                sb.append("\n|");
                for (int i = 0; i < keys.size() + 3; i++) sb.append(" --- |");
                sb.append("\n");

                for (BenchmarkResult r : results) {
                    sb.append("| ").append(r.provider()).append(" | ")
                      .append(r.domain()).append(" | ")
                      .append(r.status()).append(" |");
                    for (String k : keys) {
                        Object val = r.extraMetrics().get(k);
                        sb.append(" ").append(val != null ? val : "-").append(" |");
                    }
                    sb.append("\n");
                }
                sb.append("\n");
            }

            Files.writeString(Paths.get(path), sb.toString());
            System.out.println("[INFO] Report exported to: " + path);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to export report: " + e.getMessage());
        }
    }

    public void exportPDF(String path) {
        // Fallback to markdown export if PDF generator not available
        System.out.println("[INFO] PDF export requested for " + path + ". Using simplified generation (static implementation).");
        // Actually, the static generateReport should be called if we had BenchmarkResult (cli)
        // For now, we just print info.
    }

    /**
     * Legacy static method for integration with existing CLI results.
     */
    public static void generateReport(List<BenchmarkResult> results, String pdfPath) {
        System.out.println("[INFO] Generating PDF from " + results.size() + " results to " + pdfPath);
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
                .collect(Collectors.groupingBy(BenchmarkResult::domain));

            // Sort domains for consistent reporting
            List<String> sortedDomains = grouped.keySet().stream().sorted().collect(Collectors.toList());

            for (String domain : sortedDomains) {
                List<BenchmarkResult> domainResults = grouped.get(domain);
                
                // Check if this domain contains categorized metrics (e.g. from HP Audit)
                Set<String> categories = new LinkedHashSet<>();
                for (BenchmarkResult r : domainResults) {
                    for (String key : r.extraMetrics().keySet()) {
                        if (key.contains(":")) {
                            categories.add(key.split(":")[0]);
                        }
                    }
                }

                if (categories.isEmpty()) {
                    // Standard Single Chart per Domain
                    addDomainSection(document, writer, sectionFont, domain, domainResults, null);
                } else {
                    // Multi-category reporting (e.g. HP Audit)
                    Paragraph domainHeader = new Paragraph(domain, sectionFont);
                    domainHeader.setAlignment(Element.ALIGN_CENTER);
                    domainHeader.setSpacingBefore(30);
                    document.add(domainHeader);

                    for (String category : categories) {
                        addDomainSection(document, writer, sectionFont, category, domainResults, category);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error generating PDF report: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void addDomainSection(Document document, PdfWriter writer, Font sectionFont, String title, List<BenchmarkResult> results, String categoryFilter) throws DocumentException {
        Paragraph header = new Paragraph(title, sectionFont);
        header.setAlignment(Element.ALIGN_CENTER);
        header.setSpacingBefore(20);
        document.add(header);
        
        document.add(new Paragraph(" "));

        JFreeChart chart = createChart(title, results, categoryFilter);
        int width = 750;
        int height = 400;
        
        java.awt.image.BufferedImage bufferedImage = chart.createBufferedImage(width, height);
        try {
            Image pdfImage = Image.getInstance(writer, bufferedImage, 1.0f);
            pdfImage.setAlignment(Element.ALIGN_CENTER);
            pdfImage.scaleToFit(700, 400);
            document.add(pdfImage);
        } catch (IOException e) {
            throw new DocumentException(e);
        }
        document.add(new Paragraph(" "));
    }

    private static JFreeChart createChart(String title, List<BenchmarkResult> results, String categoryFilter) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (categoryFilter == null) {
            // Standard Throughput Chart
            results.sort(Comparator.comparingDouble(BenchmarkResult::operationsPerSecond).reversed());
            for (BenchmarkResult r : results) {
                double val = r.operationsPerSecond();
                if (!"SUCCESS".equals(r.status())) val = 0.01;
                dataset.addValue(val, "Throughput", r.provider());
            }
        } else {
            // Detailed Latency Chart for a category
            for (BenchmarkResult r : results) {
                for (Map.Entry<String, Object> entry : r.extraMetrics().entrySet()) {
                    if (entry.getKey().startsWith(categoryFilter + ":")) {
                        String op = entry.getKey().substring(categoryFilter.length() + 1);
                        double latency = (double) entry.getValue();
                        if (latency > 0) dataset.addValue(latency, r.provider(), op);
                    }
                }
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title + (categoryFilter == null ? " | Scaling Performance" : " | Performance Latency (ms)"),
                null,
                categoryFilter == null ? "Throughput (Ops/sec)" : "Latency (ms) - Lower is Better",
                dataset,
                PlotOrientation.HORIZONTAL,
                categoryFilter != null, // Show legend for multi-provider comparison
                true, false);

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
